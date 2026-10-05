package dev.cbctanktower.mixin;

import dev.cbctanktower.TowerMountBlock;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import rbasamoyai.createbigcannons.compat.sable.CBCSableConnectivityUtils;
import rbasamoyai.createbigcannons.index.CBCBlocks;

@Mixin(value=CBCSableConnectivityUtils.class,remap=false)
abstract class SableMountMixin {
    @Redirect(method={"isCannonMountAirGap","getCannonMountGaps"},at=@At(value="INVOKE",target="Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/core/Holder;)Z"))
    private static boolean tower$mount(BlockState state,Holder<Block> block){return state.is(block)||block==CBCBlocks.CANNON_MOUNT&&state.getBlock() instanceof TowerMountBlock;}
}
