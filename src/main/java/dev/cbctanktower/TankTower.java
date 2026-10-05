package dev.cbctanktower;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDisplaySources;

@Mod(TankTower.ID)
public final class TankTower {
    public static final String ID="cbctanktower";
    public static final Logger LOG=LogUtils.getLogger();
    private static final DeferredRegister.Blocks BLOCKS=DeferredRegister.createBlocks(ID);
    private static final DeferredRegister.Items ITEMS=DeferredRegister.createItems(ID);
    private static final DeferredRegister<BlockEntityType<?>> ENTITIES=DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,ID);
    private static final DeferredRegister<CreativeModeTab> TABS=DeferredRegister.create(Registries.CREATIVE_MODE_TAB,ID);
    public static final DeferredBlock<TowerMountBlock> MOUNT=BLOCKS.register("tank_cannon_mount",()->new TowerMountBlock(BlockBehaviour.Properties.ofFullCopy(CBCBlocks.CANNON_MOUNT.get())));
    public static final DeferredItem<BlockItem> MOUNT_ITEM=ITEMS.registerSimpleBlockItem(MOUNT);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<CannonMountBlockEntity>> MOUNT_ENTITY=ENTITIES.register("tank_cannon_mount",()->BlockEntityType.Builder.<CannonMountBlockEntity>of(TowerMountBlockEntity::new,MOUNT.get()).build(null));
    static {
        TABS.register("main",()->CreativeModeTab.builder().title(net.minecraft.network.chat.Component.translatable("itemGroup.cbctanktower")).icon(()->new ItemStack(MOUNT_ITEM.get())).displayItems((p,out)->out.accept(MOUNT_ITEM.get())).build());
    }
    public TankTower(IEventBus bus) {
        BLOCKS.register(bus);ITEMS.register(bus);ENTITIES.register(bus);TABS.register(bus);
        bus.addListener(this::capabilities);
        bus.addListener(this::setup);
    }
    private void capabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,MOUNT_ENTITY.get(),(be,side)->be.getItemHandler(side));
    }
    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(()->DisplaySource.BY_BLOCK.add(MOUNT.get(),CBCDisplaySources.CANNON_MOUNT.get()));
    }
    public static ResourceLocation id(String name){return ResourceLocation.fromNamespaceAndPath(ID,name);}
}
