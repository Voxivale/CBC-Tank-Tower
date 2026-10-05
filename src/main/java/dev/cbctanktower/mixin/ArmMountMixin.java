package dev.cbctanktower.mixin;

import dev.cbctanktower.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rbasamoyai.createbigcannons.index.CBCArmInteractionPointTypes;

@Mixin(value=CBCArmInteractionPointTypes.CannonMountType.class,remap=false)
abstract class ArmMountMixin {
    @Inject(method="canCreatePoint",at=@At("HEAD"),cancellable=true)
    private void tower$arm(Level level,BlockPos pos,BlockState state,CallbackInfoReturnable<Boolean> cir){
        if(state.getBlock() instanceof TowerMountBlock)cir.setReturnValue(level.getBlockEntity(pos) instanceof TowerMountBlockEntity);
    }
}
