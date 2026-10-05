package dev.cbctanktower.checks;

import dev.cbctanktower.TowerMountBlockEntity;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Blocks;
import rbasamoyai.createbigcannons.compat.sable.CBCSableConnectivityUtils;
import org.joml.Quaterniond;

/** Real plot storage and moving, rotated parent body, not a simulated Level stub. */
final class SableChecks {
    static boolean isPlot(net.minecraft.server.level.ServerLevel level,BlockPos pos){return dev.ryanhcode.sable.Sable.HELPER.getContaining(level,net.minecraft.world.phys.Vec3.atCenterOf(pos))!=null;}
    private static int stage,cases;
    private static long began;
    private static BlockPos mountPos;
    private static ServerSubLevel ship;
    static boolean tick(MinecraftServer server){
        if(cases==2)return true;
        var level=server.overworld();var container=SubLevelContainer.getContainer(level);
        if(stage==0){
            // A dedicated server has no nearby player. Keep the physical holding
            // chunks ticking, as well as the remote plot chunks Sable allocates.
            for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++)level.setChunkForced(x,8+cases*2+z,true);
            ship=(ServerSubLevel)container.allocateNewSubLevel(new Pose3d());
            var plot=ship.getPlot();plot.newEmptyChunk(plot.getCenterChunk());mountPos=plot.getCenterBlock();
            level.setBlock(mountPos.below(),Blocks.STONE.defaultBlockState(),3);
            TowerChecks.build(level,mountPos,Direction.SOUTH,Direction.DOWN,false,true,cases==1);
            ship.forceUpdateGlobalBounds();ship.updateMergedMassData(1);
            Pose3d pose=new Pose3d(ship.logicalPose());pose.rotationPoint().set(ship.getSelfMassTracker().getCenterOfMass());pose.position().set(0,230,128+cases*32);pose.orientation().set(new Quaterniond().rotationXYZ(.1,.35,-.05));
            ship.logicalPose().set(pose);container.physicsSystem().getPipeline().teleport(ship,pose.position(),pose.orientation());ship.updateBoundingBox();ship.forceUpdateGlobalBounds();
            began=server.getTickCount();stage=1;return false;
        }
        if(server.getTickCount()-began<8)return false;
        if(stage==1){
            var mount=(TowerMountBlockEntity)level.getBlockEntity(mountPos);
            if(!TowerChecks.glueReady(level,mountPos,Direction.SOUTH,Direction.DOWN,cases==1)){
                if(server.getTickCount()-began>200)throw new AssertionError("Native Sable glue did not become accessible");return false;
            }
            TowerChecks.activate(mount);
            if(mount==null||!mount.isRunning()||mount.getTower()==null||mount.getTower().getContraption().getBlocks().size()!=3)throw new AssertionError("Turret did not assemble on native Sable plot, honey="+(cases==1));
            stage=2;began=server.getTickCount();return false;
        }
        var mount=(TowerMountBlockEntity)level.getBlockEntity(mountPos);
        if(stage==2){
            if(mount.getTower()==null||level.getEntity(mount.getTower().getUUID())!=mount.getTower()||level.getEntity(mount.getContraption().getUUID())!=mount.getContraption())throw new AssertionError("Sable turret entities did not join actual world storage");
            if(!CBCSableConnectivityUtils.isCannonMountAirGap(mountPos.above(),level)||!CBCSableConnectivityUtils.getCannonMountGaps(mountPos.above(2),level,Direction.SOUTH).contains(mountPos.above()))throw new AssertionError("CBC mount air gap does not connect turret to Sable hull");
            TowerChecks.verifySeparation(mount);
            var pose=new Pose3d(ship.logicalPose());pose.position().add(5,2,-3);pose.orientation().rotateY(.4);
            ship.logicalPose().set(pose);container.physicsSystem().getPipeline().teleport(ship,pose.position(),pose.orientation());ship.updateBoundingBox();ship.forceUpdateGlobalBounds();
            stage=3;began=server.getTickCount();return false;
        }
        if(stage==3){TowerChecks.verifySeparation(mount);mount.disassemble();stage=4;began=server.getTickCount();return false;}
        TowerChecks.verifyRestored(level,mountPos,Direction.SOUTH,Direction.DOWN);
        // These unoccupied physics fixtures are complete. Remove them through
        // Sable's API before the separate controller save/restart check.
        var fixtureBounds=new net.minecraft.world.phys.AABB(mountPos).inflate(8);
        level.getEntitiesOfClass(com.simibubi.create.content.contraptions.glue.SuperGlueEntity.class,fixtureBounds).forEach(net.minecraft.world.entity.Entity::discard);
        HoneyChecks.remove(level,fixtureBounds);
        ship.getPlot().destroyAllBlocks();
        container.removeSubLevel(ship,dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason.REMOVED);
        cases++;stage=0;return cases==2;
    }
}
