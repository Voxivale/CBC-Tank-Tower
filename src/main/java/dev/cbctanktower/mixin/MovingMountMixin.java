package dev.cbctanktower.mixin;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.cbctanktower.TowerMountBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

@Mixin(value=ContraptionRemix.class,remap=false)
abstract class MovingMountMixin {
    @Redirect(method="customChecks",at=@At(value="INVOKE",target="Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private static boolean tower$mount(BlockEntry<?> entry,BlockState state){return entry.has(state)||entry==CBCBlocks.CANNON_MOUNT&&state.getBlock() instanceof TowerMountBlock;}
}
