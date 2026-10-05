package dev.cbctanktower;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlock;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.config.*;
import rbasamoyai.createbigcannons.index.*;
import java.util.concurrent.ConcurrentHashMap;

/** CBC datapacks may give different limits to individual native mount states. */
public record InheritedMountProperties(CannonMountBlockPropertiesProvider original) implements CannonMountBlockPropertiesProvider {
    private static final ConcurrentHashMap<ICannonContraptionType,InheritedMountProperties> CACHE=new ConcurrentHashMap<>();
    public static CannonMountBlockPropertiesProvider get(ICannonContraptionType type){
        var nativeProperties=CannonMountPropertiesHandler.getProperties(CBCBlockEntities.CANNON_MOUNT.get(),type);
        var cached=CACHE.get(type);
        if(cached==null||cached.original!=nativeProperties){cached=new InheritedMountProperties(nativeProperties);CACHE.put(type,cached);}
        return cached;
    }
    private static BlockState nativeState(BlockState state){
        return CBCBlocks.CANNON_MOUNT.getDefaultState()
            .setValue(CannonMountBlock.HORIZONTAL_FACING,state.getValue(CannonMountBlock.HORIZONTAL_FACING))
            .setValue(CannonMountBlock.VERTICAL_DIRECTION,state.getValue(CannonMountBlock.VERTICAL_DIRECTION))
            .setValue(CannonMountBlock.ASSEMBLY_POWERED,state.getValue(CannonMountBlock.ASSEMBLY_POWERED))
            .setValue(CannonMountBlock.FIRE_POWERED,state.getValue(CannonMountBlock.FIRE_POWERED));
    }
    @Override public float maximumElevation(Level level,BlockState state,BlockPos pos){return original.maximumElevation(level,nativeState(state),pos);}
    @Override public float maximumDepression(Level level,BlockState state,BlockPos pos){return original.maximumDepression(level,nativeState(state),pos);}
}
