package dev.cbctanktower;

import net.minecraft.world.level.block.entity.BlockEntityType;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.*;

public final class TowerMountBlock extends CannonMountBlock {
    public TowerMountBlock(Properties properties){super(properties);}
    @Override public BlockEntityType<? extends CannonMountBlockEntity> getBlockEntityType(){return TankTower.MOUNT_ENTITY.get();}
}
