package dev.cbctanktower.client;

import dev.cbctanktower.TankTower;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.*;

@EventBusSubscriber(modid=TankTower.ID,value=Dist.CLIENT,bus=EventBusSubscriber.Bus.MOD)
public final class TowerClient {
    @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(TankTower.MOUNT_ENTITY.get(),CannonMountBlockEntityRenderer::new);
    }
    @SubscribeEvent public static void setup(FMLClientSetupEvent event){
        event.enqueueWork(()->SimpleBlockEntityVisualizer.builder(TankTower.MOUNT_ENTITY.get()).factory(CannonMountVisual::new).apply());
    }
}
