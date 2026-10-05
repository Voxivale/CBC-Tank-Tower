package dev.cbctanktower;

import com.simibubi.create.api.contraption.BlockMovementChecks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.*;

/** The decoration is a native bearing contraption, separate from all cannon calculations. */
final class TurretContraption extends BearingContraption {
    private TurretContraption(){super(false,Direction.UP);}
    static TurretContraption collect(Level world,BlockPos pivot,BlockPos mount,Set<BlockPos> cannon,GlueLinks glue)throws AssemblyException{
        TurretContraption result=new TurretContraption();result.anchor=pivot;result.bounds=new AABB(BlockPos.ZERO);
        Set<BlockPos> visited=new HashSet<>(cannon);ArrayDeque<BlockPos> queue=new ArrayDeque<>(cannon);
        int limit=AllConfigs.server().kinetics.maxBlocksMoved.get();
        while(!queue.isEmpty()){
            BlockPos from=queue.removeFirst();
            for(Direction direction:Direction.values()){
                BlockPos next=from.relative(direction);
                if(next.equals(mount)||visited.contains(next)||!glue.connected(from,next))continue;
                if(!world.isLoaded(next))throw new AssemblyException(Component.translatable("exception.cbctanktower.unloaded",next.toShortString()));
                var state=world.getBlockState(next);
                if(state.isAir()||!BlockMovementChecks.isMovementNecessary(state,world,next))continue;
                if(!BlockMovementChecks.isMovementAllowed(state,world,next))throw new AssemblyException(Component.translatable("exception.cbctanktower.immovable",next.toShortString()));
                if(result.getBlocks().size()>=limit)throw new AssemblyException(Component.translatable("exception.cbctanktower.too_many",limit));
                visited.add(next);queue.addLast(next);result.addBlock(world,next,result.capture(world,next));
            }
        }
        result.startMoving(world);result.expandBoundsAroundAxis(Direction.Axis.Y);return result;
    }
}
