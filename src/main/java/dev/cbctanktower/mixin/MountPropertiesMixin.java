package dev.cbctanktower.mixin;

import dev.cbctanktower.TowerMountBlockEntity;
import dev.cbctanktower.InheritedMountProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.config.*;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

@Mixin(value=CannonMountPropertiesHandler.class,remap=false)
abstract class MountPropertiesMixin {
    @Inject(method="getProperties(Lnet/minecraft/world/level/block/entity/BlockEntity;Lrbasamoyai/createbigcannons/cannon_control/cannon_types/ICannonContraptionType;)Lrbasamoyai/createbigcannons/cannon_control/config/CannonMountBlockPropertiesProvider;",at=@At("HEAD"),cancellable=true)
    private static void tower$properties(BlockEntity be,ICannonContraptionType type,CallbackInfoReturnable<CannonMountBlockPropertiesProvider> cir){
        if(be instanceof TowerMountBlockEntity)cir.setReturnValue(InheritedMountProperties.get(type));
    }
}
