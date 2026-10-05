package dev.cbctanktower.checks;

import dev.simulated_team.simulated.content.entities.honey_glue.HoneyGlueEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;

/** Loaded only when the optional Simulated mod is present. */
final class HoneyChecks {
    static void place(ServerLevel level,AABB bounds){level.addFreshEntity(new HoneyGlueEntity(level,bounds));}
    static boolean present(ServerLevel level,AABB bounds){return !level.getEntitiesOfClass(HoneyGlueEntity.class,bounds).isEmpty();}
    static void remove(ServerLevel level,AABB bounds){level.getEntitiesOfClass(HoneyGlueEntity.class,bounds).forEach(net.minecraft.world.entity.Entity::discard);}
    static boolean connected(ServerLevel level,net.minecraft.core.BlockPos a,net.minecraft.core.BlockPos b){return level.getEntitiesOfClass(HoneyGlueEntity.class,new AABB(a).inflate(3)).stream().anyMatch(e->e.contains(a)&&e.contains(b));}
}
