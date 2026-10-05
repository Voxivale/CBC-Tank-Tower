package dev.cbctanktower.checks;

import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import dev.cbctanktower.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.*;
import rbasamoyai.createbigcannons.cannon_control.contraption.*;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.index.*;
import rbasamoyai.createbigcannons.munitions.autocannon.*;
import java.nio.file.*;
import java.util.*;

public final class TowerChecks {
    private static boolean done;
    private record Trial(boolean big,Direction direction,Direction vertical) {}
    private static final List<Trial> TRIALS=new ArrayList<>();
    static {for(boolean big:new boolean[]{false,true})for(Direction direction:Direction.Plane.HORIZONTAL)for(Direction vertical:new Direction[]{Direction.DOWN,Direction.UP})TRIALS.add(new Trial(big,direction,vertical));}
    private static int cases,phase,honeyCases;
    private static long phaseAt;
    private static TowerMountBlockEntity turret,invalid;
    private static CannonMountBlockEntity original;
    private static int projectilesBefore;
    private static int projectileSpawns;
    private static float kineticYaw;
    @SubscribeEvent public static void projectile(EntityJoinLevelEvent event){
        if(Boolean.getBoolean("cbctanktower.smoke")&&!event.loadedFromDisk()&&!event.getLevel().isClientSide&&event.getEntity() instanceof AbstractAutocannonProjectile)projectileSpawns++;
    }
    private static final BlockPos SAVE_POS=new BlockPos(0,200,0);
    private static final Path MARKER=Path.of("tower-reload.nbt");
    @SubscribeEvent public static void tick(ServerTickEvent.Post event){
        if(!Boolean.getBoolean("cbctanktower.smoke")||done||event.getServer().getTickCount()<20)return;
        MinecraftServer server=event.getServer();
        try{
            if(Files.exists(MARKER)){
                if(server.getTickCount()<60)return;
                var tag=NbtIo.readCompressed(MARKER,NbtAccounter.create(1024*1024));
                TowerMountBlockEntity mount=(TowerMountBlockEntity)server.overworld().getBlockEntity(SAVE_POS);
                check(mount!=null&&mount.isRunning()&&mount.getTower()!=null&&mount.getContraption()!=null,"Running turret did not restore after native restart");
                check(mount.getTower().getUUID().equals(tag.getUUID("Tower"))&&mount.getContraption().getUUID().equals(tag.getUUID("Gun")),"Native restart changed mechanism UUIDs");
                check(Math.abs(mount.getDisplayPitch()-15)<1e-3&&Math.abs(mount.getYawOffset(1)-35)<1e-3,"Native restart lost aim: pitch="+mount.getDisplayPitch()+", yaw="+mount.getYawOffset(1)+", gunPitch="+mount.getContraption().pitch+", gunYaw="+mount.getContraption().yaw+", tower="+mount.getTower().getAngle(1)+", speeds="+mount.getYawSpeed()+"/"+mount.getPitchSpeed());
                check(Math.abs(mount.getTower().getAngle(1)+35)<1e-3,"Native restart lost horizontal turret rotation");
                verifySeparation(mount);mount.disassemble();verifyRestored(server.overworld(),SAVE_POS,Direction.SOUTH,Direction.DOWN);
                Files.writeString(Path.of("tower-result.txt"),"PASS: native CBC big cannons and autocannons, all four horizontal orientations and upright/inverted mounts, "+tag.getInt("Cases")+" glue cases, "+tag.getInt("HoneyCases")+" using Aeronautics honey glue; decoration yaw independent of pitch; real Create motors drive both native CBC axes with aligned turret yaw; unchanged original cannon blocks, stress, rotation coefficient and elevation limits; native ammunition capability, mechanical-arm recognition and AP firing; chest cargo/glue/disassembly; "+(tag.getInt("HoneyCases")>0?"native moving and rotated Sable plot with both glue types; ":"")+"real saved-world restart retains both mechanism UUIDs, aim, turret and cargo");
            }else{
                if(!firstStage(server))return;
                var mount=(TowerMountBlockEntity)server.overworld().getBlockEntity(SAVE_POS);
                verifySeparation(mount);
                var tag=new CompoundTag();tag.putUUID("Tower",mount.getTower().getUUID());tag.putUUID("Gun",mount.getContraption().getUUID());tag.putInt("Cases",cases);tag.putInt("HoneyCases",honeyCases);
                server.saveEverything(true,true,false);NbtIo.writeCompressed(tag,MARKER);
                Files.writeString(Path.of("tower-first-stage.txt"),"PASS: "+cases+" native cannon/glue/orientation cases, "+honeyCases+" using Aeronautics honey glue; safe rejection; "+(ModList.get().isLoaded("sable")?"native moving Sable plot, normal and honey glue; ":"")+"assembled turret checkpoint saved for restart");
            }
            done=true;server.halt(false);
        }catch(Throwable error){done=true;TankTower.LOG.error("TOWER_CHECK_FAILED",error);try{Files.writeString(Path.of("tower-result.txt"),"FAIL: "+error);}catch(Exception ignored){}server.halt(false);}
    }
    private static boolean firstStage(MinecraftServer server)throws Exception{
        ServerLevel level=server.overworld();long now=server.getTickCount();
        if(cases<TRIALS.size()){
            Trial trial=TRIALS.get(cases);BlockPos pos=new BlockPos(96+(cases%4)*64,200,(cases/4)*64);
            if(phase==0){
                boolean honey=ModList.get().isLoaded("simulated")&&cases%2==1;
                turret=(TowerMountBlockEntity)build(level,pos,trial.direction,trial.vertical,trial.big,true,honey);
                original=build(level,pos.east(24),trial.direction,trial.vertical,trial.big,false,false);
                if(honey)honeyCases++;
                phase=1;phaseAt=now;return false;
            }
            // Sable defers native entity insertion to the following frame. Observe actual ticks.
            if(now-phaseAt<4)return false;
            if(phase==1){
                if(!glueReady(level,pos,trial.direction,trial.vertical,ModList.get().isLoaded("simulated")&&cases%2==1)){
                    check(now-phaseAt<200,"Native glue entity did not become accessible in forced fixture chunks");return false;
                }
                activate(turret);activate(original);
                check(turret.isRunning()&&turret.getTower()!=null,"Tower did not assemble, case="+cases+", running="+turret.isRunning()+", body="+turret.getTower()+", gun="+turret.getContraption()+", pivot="+level.getBlockState(pivot(pos,trial.vertical))+", error="+turret.getLastAssemblyException());
                check(original.isRunning(),"Original CBC comparison did not assemble: "+original.getLastAssemblyException());
                check(turret.getTower().getContraption().getBlocks().size()==3,"Glue gathered unexpected blocks");
                check(!level.getBlockState(pivot(pos,trial.vertical).east(5)).isAir(),"Unglued neighbor was moved");
                check(turret.getContraption().getContraption().getBlocks().size()==original.getContraption().getContraption().getBlocks().size(),"Decoration changed cannon length");
                check(turret.calculateCannonStressApplied()==original.calculateCannonStressApplied(),"Decoration changed cannon stress");
                check(turret.getContraption().getRotationCoefficient()==original.getContraption().getRotationCoefficient(),"Decoration changed original rotation coefficient");
                check(turret.getContraption().maximumElevation()==original.getContraption().maximumElevation()&&turret.getContraption().maximumDepression()==original.getContraption().maximumDepression(),"Original CBC limits changed: "+turret.getContraption().maximumElevation()+" / "+original.getContraption().maximumElevation());
                check(new CBCArmInteractionPointTypes.CannonMountType().canCreatePoint(level,pos,turret.getBlockState()),"Mechanical arm cannot recognize turret mount");
                verifySeparation(turret);
                if(!trial.big){projectilesBefore=projectileSpawns;fire(level,turret);}
                phase=2;phaseAt=now;return false;
            }
            if(phase==2){
                if(!trial.big){
                    // Sable joins and ticks entities through its asynchronous frame pipeline.
                    // Wait for the real shot event instead of assuming a four-tick deadline.
                    if(projectileSpawns==projectilesBefore){check(now-phaseAt<100,"Original AP autocannon did not fire, case="+cases+", gunAlive="+turret.getContraption().isAlive()+", state="+turret.getBlockState());return false;}
                    check(projectileSpawns==projectilesBefore+1,"One AP cartridge produced more than one projectile");
                }
                kineticYaw=turret.getContraption().yaw;
                level.setBlock(pos.relative(trial.vertical),AllBlocks.CREATIVE_MOTOR.getDefaultState().setValue(CreativeMotorBlock.FACING,trial.vertical.getOpposite()),3);
                level.setBlock(pos.relative(trial.direction.getClockWise()),AllBlocks.CREATIVE_MOTOR.getDefaultState().setValue(CreativeMotorBlock.FACING,trial.direction.getCounterClockWise()),3);
                phase=3;phaseAt=now;return false;
            }
            if(phase==3){
                if(now-phaseAt<12)return false;
                check(turret.getYawSpeed()!=0&&turret.getPitchSpeed()!=0&&Math.abs(turret.getContraption().yaw-kineticYaw)>.1,"Real Create motors did not drive native CBC axes");
                var yawAxisSample=trial.direction.getAxis()==Direction.Axis.X?new Vec3(.5,.5,1.5):new Vec3(1.5,.5,.5);
                check(turret.getTower().toGlobalVector(yawAxisSample,1).distanceTo(turret.getContraption().toGlobalVector(yawAxisSample,1))<1e-3,"Turret body leads or lags the gun while real motors rotate and elevate it");
                level.setBlock(pos.relative(trial.vertical),Blocks.AIR.defaultBlockState(),3);level.setBlock(pos.relative(trial.direction.getClockWise()),Blocks.AIR.defaultBlockState(),3);
                turret.disassemble();original.disassemble();phase=4;phaseAt=now;return false;
            }
            verifyRestored(level,pos,trial.direction,trial.vertical);cases++;phase=0;return false;
        }
        if(ModList.get().isLoaded("sable")&&!SableChecks.tick(server))return false;
        if(phase==0){
            invalid=(TowerMountBlockEntity)build(level,new BlockPos(0,200,64),Direction.SOUTH,Direction.DOWN,false,true,false);
            BlockPos anchor=pivot(invalid.getBlockPos(),Direction.DOWN);level.setBlock(anchor.west(2),Blocks.BEDROCK.defaultBlockState(),3);
            level.addFreshEntity(new SuperGlueEntity(level,SuperGlueEntity.span(anchor,anchor.west(2))));
            phase=1;phaseAt=now;return false;
        }
        if(now-phaseAt<4)return false;
        if(phase==1){
            BlockPos invalidAnchor=pivot(invalid.getBlockPos(),Direction.DOWN), bedrock=invalidAnchor.west(2);
            boolean blockingGlueReady=level.getEntitiesOfClass(SuperGlueEntity.class,new AABB(invalidAnchor).inflate(4)).stream().anyMatch(e->e.contains(invalidAnchor)&&e.contains(bedrock));
            if(!glueReady(level,invalid.getBlockPos(),Direction.SOUTH,Direction.DOWN,false)||!blockingGlueReady){
                check(now-phaseAt<200,"Native invalid fixture glue did not become accessible");return false;
            }
            activate(invalid);
            check(!invalid.isRunning()&&invalid.getLastAssemblyException()!=null&&!level.getBlockState(invalidAnchor).isAir(),"Invalid glued block rejection failed: running="+invalid.isRunning()+", error="+invalid.getLastAssemblyException()+", cannon="+level.getBlockState(invalidAnchor)+", bedrock="+level.getBlockState(bedrock)+", movement="+com.simibubi.create.api.contraption.BlockMovementChecks.isMovementAllowed(level.getBlockState(bedrock),level,bedrock)+", necessary="+com.simibubi.create.api.contraption.BlockMovementChecks.isMovementNecessary(level.getBlockState(bedrock),level,bedrock)+", glue="+level.getEntitiesOfClass(SuperGlueEntity.class,new AABB(invalidAnchor).inflate(4)).stream().map(e->e.getBoundingBox().toString()).toList());
            build(level,SAVE_POS,Direction.SOUTH,Direction.DOWN,false,true,false);phase=2;phaseAt=now;return false;
        }
        if(phase==2){
            if(!glueReady(level,SAVE_POS,Direction.SOUTH,Direction.DOWN,false)){
                check(now-phaseAt<200,"Saved fixture glue did not become accessible");return false;
            }
            activate((TowerMountBlockEntity)level.getBlockEntity(SAVE_POS));phase=3;phaseAt=now;return false;
        }
        check(((TowerMountBlockEntity)level.getBlockEntity(SAVE_POS)).getTower()!=null,"Saved turret did not assemble");return true;
    }
    private static BlockPos pivot(BlockPos pos,Direction vertical){return pos.relative(vertical,-2);}
    static CannonMountBlockEntity build(ServerLevel level,BlockPos pos,Direction dir,Direction vertical,boolean big,boolean tower,boolean honey){
        if(!ModList.get().isLoaded("sable")||!SableChecks.isPlot(level,pos))for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++)level.setChunkForced((pos.getX()>>4)+x,(pos.getZ()>>4)+z,true);
        BlockPos anchor=pivot(pos,vertical);
        level.setBlock(pos,(tower?TankTower.MOUNT.get():CBCBlocks.CANNON_MOUNT.get()).defaultBlockState().setValue(CannonMountBlock.HORIZONTAL_FACING,dir).setValue(CannonMountBlock.VERTICAL_DIRECTION,vertical),3);
        for(int i=-1;i<=2;i++){
            Block block=big?(i==-1?CBCBlocks.CAST_IRON_CANNON_END.get():i==0?CBCBlocks.CAST_IRON_CANNON_CHAMBER.get():CBCBlocks.CAST_IRON_CANNON_BARREL.get()):(i==-1?CBCBlocks.CAST_IRON_AUTOCANNON_BREECH.get():CBCBlocks.CAST_IRON_AUTOCANNON_BARREL.get());
            var state=block.defaultBlockState().setValue(BlockStateProperties.FACING,dir);
            if(state.hasProperty(rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlock.HANDLE))state=state.setValue(rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlock.HANDLE,false);
            level.setBlock(anchor.relative(dir,i),state,3);
        }
        for(int i=-1;i<2;i++){
            ((ICannonBlockEntity<?>)level.getBlockEntity(anchor.relative(dir,i))).cannonBehavior().setConnectedFace(dir,true);
            ((ICannonBlockEntity<?>)level.getBlockEntity(anchor.relative(dir,i+1))).cannonBehavior().setConnectedFace(dir.getOpposite(),true);
        }
        if(tower){
            // Choose a side perpendicular to the gun for every horizontal facing.
            Direction side=dir.getClockWise();BlockPos wall=anchor.relative(side);
            level.setBlock(wall,Blocks.STONE.defaultBlockState(),3);level.setBlock(wall.relative(dir),Blocks.GLASS.defaultBlockState(),3);level.setBlock(wall.above(),Blocks.CHEST.defaultBlockState(),3);
            ((ChestBlockEntity)level.getBlockEntity(wall.above())).setItem(0,new ItemStack(Items.DIAMOND,7));
            AABB bounds=SuperGlueEntity.span(anchor,wall.relative(dir).above());
            if(honey)HoneyChecks.place(level,bounds);else level.addFreshEntity(new SuperGlueEntity(level,bounds));
            level.setBlock(anchor.east(5),Blocks.GOLD_BLOCK.defaultBlockState(),3);
        }
        return (CannonMountBlockEntity)level.getBlockEntity(pos);
    }
    static void activate(CannonMountBlockEntity mount){
        var facing=mount.getBlockState().getValue(CannonMountBlock.HORIZONTAL_FACING);
        mount.getLevel().setBlock(mount.getBlockPos().relative(facing.getOpposite()),Blocks.REDSTONE_BLOCK.defaultBlockState(),3);
        if(!mount.isRunning())mount.onRedstoneUpdate(true,false,false,false,0);
    }
    static boolean glueReady(ServerLevel level,BlockPos pos,Direction dir,Direction vertical,boolean honey){
        BlockPos anchor=pivot(pos,vertical),wall=anchor.relative(dir.getClockWise());
        if(honey)return HoneyChecks.connected(level,anchor,wall);
        return level.getEntitiesOfClass(SuperGlueEntity.class,new AABB(anchor).inflate(3)).stream().anyMatch(e->e.contains(anchor)&&e.contains(wall));
    }
    static void verifySeparation(TowerMountBlockEntity mount){
        var body=mount.getTower();check(body!=null,"Turret body missing");
        mount.setYaw(35);mount.setPitch(0);mount.tick();
        Vec3 sample=new Vec3(1.5,1.5,1.5);
        Vec3 before=body.toGlobalVector(sample,1);
        var gunSample=mount.getContraption().toGlobalVector(sample,1);
        // CBC composes two lookup-table rotations; Create's bearing applies one summed angle.
        check(before.distanceTo(gunSample)<1e-3,"Gun and turret yaw do not align: body="+before+", gun="+gunSample+", bodyAngle="+body.getAngle(1)+", initial="+mount.getContraption().getInitialYaw()+", yaw="+mount.getContraption().yaw);
        Vec3 barrel=Vec3.atCenterOf(BlockPos.ZERO.relative(mount.getContraptionDirection(),2));
        Vec3 gunBefore=mount.getContraption().toGlobalVector(barrel,1);
        mount.setPitch(15);mount.tick();
        check(before.distanceTo(body.toGlobalVector(sample,1))<1e-5,"Gun pitch moved turret body");
        check(Math.abs(gunBefore.y-mount.getContraption().toGlobalVector(barrel,1).y)>.1,"Original gun no longer elevates");
    }
    private static void fire(ServerLevel level,TowerMountBlockEntity mount){
        ItemStack ammo=CBCItems.AUTOCANNON_CARTRIDGE.asStack();AutocannonCartridgeItem.writeProjectile(CBCItems.AP_AUTOCANNON_ROUND.asStack(),ammo);
        var handler=mount.getItemHandler(Direction.UP);check(handler!=null,"Native cannon inventory capability missing");
        boolean inserted=false;for(int slot=0;slot<handler.getSlots();slot++)if(handler.insertItem(slot,ammo.copy(),false).isEmpty()){inserted=true;break;}check(inserted,"Native ammunition insertion failed");
        var cannon=(AbstractMountedCannonContraption)mount.getContraption().getContraption();
        var breech=(AbstractAutocannonBreechBlockEntity)cannon.presentBlockEntities.get(cannon.getStartPos());breech.setFireRate(15);
        level.setBlock(mount.getBlockPos().relative(mount.getBlockState().getValue(CannonMountBlock.HORIZONTAL_FACING)),Blocks.REDSTONE_BLOCK.defaultBlockState(),3);
        mount.onRedstoneUpdate(true,true,true,false,15);
    }
    static void verifyRestored(ServerLevel level,BlockPos pos,Direction dir,Direction vertical){
        BlockPos anchor=pivot(pos,vertical),wall=anchor.relative(dir.getClockWise());
        check(level.getBlockState(wall).is(Blocks.STONE)&&level.getBlockState(wall.relative(dir)).is(Blocks.GLASS),"Turret blocks did not return to assembly position");
        var chest=(ChestBlockEntity)level.getBlockEntity(wall.above());check(chest!=null&&chest.getItem(0).is(Items.DIAMOND)&&chest.getItem(0).getCount()==7,"Chest cargo lost at disassembly");
        check(!level.getBlockState(anchor).isAir(),"Original cannon did not disassemble");
        check(!level.getEntitiesOfClass(SuperGlueEntity.class,new AABB(anchor).inflate(3)).isEmpty()||ModList.get().isLoaded("simulated")&&HoneyChecks.present(level,new AABB(anchor).inflate(3)),"Glue joints lost at disassembly");
    }
    private static void check(boolean condition,String message){if(!condition)throw new AssertionError(message);}
}
