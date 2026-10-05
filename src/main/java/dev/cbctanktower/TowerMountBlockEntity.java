package dev.cbctanktower;

import com.simibubi.create.content.contraptions.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.*;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import java.util.*;

public final class TowerMountBlockEntity extends CannonMountBlockEntity implements IControlContraption {
    private ControlledContraptionEntity tower;
    private UUID towerId;
    private ListTag glueJoints=new ListTag();
    private float initialYaw;
    private float persistedYaw=Float.NaN,persistedPitch=Float.NaN;

    public TowerMountBlockEntity(BlockPos pos,BlockState state){super(TankTower.MOUNT_ENTITY.get(),pos,state);}
    public ControlledContraptionEntity getTower(){return tower;}
    @Override protected void assemble()throws AssemblyException{
        if(isRunning())return;
        Direction vertical=getBlockState().getValue(CannonMountBlock.VERTICAL_DIRECTION);
        BlockPos pivot=worldPosition.relative(vertical,-2);
        if(level.isOutsideBuildHeight(pivot))throw cannonBlockOutsideOfWorld(pivot);
        if(!(level.getBlockState(pivot).getBlock() instanceof CannonContraptionProviderBlock provider))return;
        var preview=provider.getCannonContraption();
        if(!preview.assemble(level,pivot))return;
        var facing=getBlockState().getValue(CannonMountBlock.HORIZONTAL_FACING);
        if(preview.initialOrientation().getAxis().isHorizontal()&&preview.initialOrientation().getAxis()!=facing.getAxis())return;
        Set<BlockPos> cannon=new LinkedHashSet<>();preview.getBlocks().keySet().forEach(p->cannon.add(p.offset(preview.anchor)));
        GlueLinks glue=new GlueLinks(level,pivot);
        TurretContraption decoration=TurretContraption.collect(level,pivot,worldPosition,cannon,glue);
        // All glue, movement and size checks finish before either mechanism removes blocks.
        super.assemble();
        if(!isRunning()||mountedContraption==null)return;
        if(decoration.getBlocks().isEmpty())return;
        initialYaw=mountedContraption.getInitialYaw();
        ControlledContraptionEntity created=ControlledContraptionEntity.create(level,this,decoration);
        created.setPos(Vec3.atLowerCornerOf(pivot));created.setRotationAxis(Direction.Axis.Y);created.setAngle(0);
        if(!level.addFreshEntity(created)){created.discard();super.disassemble();return;}
        tower=created;towerId=created.getUUID();glueJoints=glue.snapshot();
        decoration.removeBlocksFromWorld(level,BlockPos.ZERO);glue.remove();setChanged();sendData();
    }
    @Override public void tick(){
        super.tick();
        // CBC updates its angles without marking the controller's chunk dirty.
        // Save the controller together with the two entity angles, including
        // stationary mounts whose chunk would otherwise have nothing to save.
        if(!level.isClientSide&&isRunning()&&(Float.compare(persistedYaw,cannonYaw)!=0||Float.compare(persistedPitch,cannonPitch)!=0)){
            persistedYaw=cannonYaw;persistedPitch=cannonPitch;setChanged();
        }
        if(tower==null&&towerId!=null&&level instanceof ServerLevel server&&server.getEntity(towerId) instanceof ControlledContraptionEntity entity)tower=entity;
        if(tower!=null&&tower.isAlive()){
            // Use the actual gun angle. getYawOffset(1) extrapolates another kinetic tick.
            // CBC applies initial yaw followed by minus current yaw; pitch stays independent.
            if(mountedContraption!=null)tower.setAngle(initialYaw-mountedContraption.yaw);
            BlockPos pivot=worldPosition.relative(getBlockState().getValue(CannonMountBlock.VERTICAL_DIRECTION),-2);
            tower.setPos(Vec3.atLowerCornerOf(pivot));
            if(!level.isClientSide&&!isRunning())disassembleTower();
        }
    }
    @Override public void disassemble(){super.disassemble();disassembleTower();}
    private void disassembleTower(){
        if(level==null||level.isClientSide)return;
        if(tower==null&&towerId!=null&&level instanceof ServerLevel server&&server.getEntity(towerId) instanceof ControlledContraptionEntity entity)tower=entity;
        if(tower!=null&&tower.isAlive()){
            // CBC also restores its gun at assembly orientation; both mechanisms return together.
            tower.setAngle(0);tower.save(new CompoundTag());tower.disassemble();
            BlockPos pivot=worldPosition.relative(getBlockState().getValue(CannonMountBlock.VERTICAL_DIRECTION),-2);
            GlueLinks.restore(level,pivot,glueJoints);
            tower=null;towerId=null;glueJoints=new ListTag();setChanged();sendData();
        }
    }
    @Override public boolean isAttachedTo(AbstractContraptionEntity entity){return entity==tower||super.isAttachedTo(entity);}
    @Override public void attach(ControlledContraptionEntity entity){
        if(towerId!=null&&!towerId.equals(entity.getUUID()))return;
        tower=entity;towerId=entity.getUUID();
        if(!level.isClientSide){setChanged();sendData();}
    }
    @Override public boolean isValid(){return !isRemoved();}
    @Override public BlockPos getBlockPosition(){return worldPosition;}
    @Override protected void write(CompoundTag tag,HolderLookup.Provider registry,boolean clientPacket){
        super.write(tag,registry,clientPacket);
        if(towerId!=null)tag.putUUID("TowerEntity",towerId);
        tag.putFloat("TowerInitialYaw",initialYaw);
        // Glue is only needed for server-side restoration, not repeated aim packets.
        if(!clientPacket)tag.put("TowerGlue",glueJoints.copy());
    }
    @Override protected void read(CompoundTag tag,HolderLookup.Provider registry,boolean clientPacket){
        super.read(tag,registry,clientPacket);
        towerId=tag.hasUUID("TowerEntity")?tag.getUUID("TowerEntity"):null;
        initialYaw=tag.getFloat("TowerInitialYaw");
        if(!clientPacket)glueJoints=tag.getList("TowerGlue",Tag.TAG_COMPOUND).copy();
        if(tower!=null&&!tower.getUUID().equals(towerId))tower=null;
    }
}
