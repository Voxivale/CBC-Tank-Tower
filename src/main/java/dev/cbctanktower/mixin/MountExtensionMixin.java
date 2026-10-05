package dev.cbctanktower.mixin;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.cbctanktower.TowerMountBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountExtensionBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;

@Mixin(value=CannonMountExtensionBlock.class,remap=false)
abstract class MountExtensionMixin {
    @Redirect(method="getPreferredFacing",at=@At(value="INVOKE",target="Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean tower$extension(BlockEntry<?> entry,BlockState state){return entry.has(state)||entry==CBCBlocks.CANNON_MOUNT&&state.getBlock() instanceof TowerMountBlock;}
}
