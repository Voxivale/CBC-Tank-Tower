package dev.cbctanktower;

import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.fml.ModList;
import java.util.*;

/** Glue queries are cached by section; an entire volume is saved once by UUID. */
final class GlueLinks {
    private final Level level;
    private final BlockPos anchor;
    private final Map<BlockPos,List<Entity>> sections=new HashMap<>();
    private final Map<UUID,Entity> used=new LinkedHashMap<>();
    GlueLinks(Level level,BlockPos anchor){this.level=level;this.anchor=anchor;}
    boolean connected(BlockPos a,BlockPos b){
        BlockPos key=new BlockPos(a.getX()>>4,a.getY()>>4,a.getZ()>>4);
        List<Entity> candidates=sections.computeIfAbsent(key,k->{
            AABB box=new AABB(k.getX()*16,k.getY()*16,k.getZ()*16,k.getX()*16+16,k.getY()*16+16,k.getZ()*16+16).inflate(1);
            List<Entity> all=new ArrayList<>(level.getEntitiesOfClass(SuperGlueEntity.class,box));
            if(ModList.get().isLoaded("simulated"))all.addAll(HoneyGlueCompat.find(level,box));
            return all;
        });
        boolean glued=false;
        for(Entity entity:candidates){
            boolean contains=entity instanceof SuperGlueEntity glue?glue.contains(a)&&glue.contains(b):HoneyGlueCompat.contains(entity,a,b);
            if(contains){used.put(entity.getUUID(),entity);glued=true;}
        }
        return glued;
    }
    ListTag snapshot(){
        ListTag list=new ListTag();
        for(Entity entity:used.values()){
            CompoundTag tag=new CompoundTag();tag.putUUID("UUID",entity.getUUID());tag.putBoolean("Honey",!(entity instanceof SuperGlueEntity));
            SuperGlueEntity.writeBoundingBox(tag,entity.getBoundingBox().move(-anchor.getX(),-anchor.getY(),-anchor.getZ()));list.add(tag);
        }
        return list;
    }
    void remove(){used.values().forEach(Entity::discard);}
    static void restore(Level level,BlockPos anchor,ListTag joints){
        if(level.isClientSide)return;
        for(Tag raw:joints){
            CompoundTag tag=(CompoundTag)raw;
            if(level instanceof net.minecraft.server.level.ServerLevel server&&server.getEntity(tag.getUUID("UUID"))!=null)continue;
            AABB box=SuperGlueEntity.readBoundingBox(tag).move(anchor);
            Entity glue=tag.getBoolean("Honey")&&ModList.get().isLoaded("simulated")?HoneyGlueCompat.create(level,box):new SuperGlueEntity(level,box);
            glue.setUUID(tag.getUUID("UUID"));level.addFreshEntity(glue);
        }
    }
}
