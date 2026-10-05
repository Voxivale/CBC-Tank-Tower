package dev.cbctanktower;

import dev.simulated_team.simulated.content.entities.honey_glue.HoneyGlueEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.List;

/** This class is reached only with Simulated/Aeronautics loaded. */
final class HoneyGlueCompat {
    static List<HoneyGlueEntity> find(Level level,AABB box){return level.getEntitiesOfClass(HoneyGlueEntity.class,box);}
    static boolean contains(Entity entity,BlockPos a,BlockPos b){return entity instanceof HoneyGlueEntity honey&&honey.contains(a)&&honey.contains(b);}
    static Entity create(Level level,AABB box){return new HoneyGlueEntity(level,box);}
}
