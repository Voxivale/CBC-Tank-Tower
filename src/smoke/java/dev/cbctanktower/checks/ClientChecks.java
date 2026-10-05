package dev.cbctanktower.checks;

import dev.cbctanktower.*;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.worldselection.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.*;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import java.nio.file.*;
import java.util.*;

/** Actual client rendering with the packaged addon and installed mods. */
public final class ClientChecks {
    private static int phase,wait,serverPhase;
    private static boolean ready,done;
    private static final BlockPos POS=new BlockPos(0,200,0);
    private static Vec3 horizontalSample;
    private static final long BEGAN=System.nanoTime();
    @SubscribeEvent public static void client(ClientTickEvent.Post event){
        if(!Boolean.getBoolean("cbctanktower.client_smoke")||done)return;
        var mc=Minecraft.getInstance();
        try{
            check((System.nanoTime()-BEGAN)/1_000_000_000<420,"Client validation timed out");
            if(mc.screen!=null&&mc.screen.getClass().getSimpleName().equals("LoadingErrorScreen"))throw new AssertionError("NeoForge loading error");
            if(phase==0&&mc.screen instanceof AccessibilityOnboardingScreen onboarding)onboarding.onClose();
            else if(phase==0&&mc.screen instanceof TitleScreen title){
                models(mc);mc.options.hideGui=true;mc.options.cloudStatus().set(CloudStatus.OFF);phase=1;CreateWorldScreen.openFresh(mc,title);
            }else if(phase==1&&mc.screen instanceof CreateWorldScreen create){
                create.getUiState().setName("CBC Tank Tower Render "+System.currentTimeMillis());
                create.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
                create.getUiState().setGenerateStructures(false);phase=2;
                create.children().stream().filter(Button.class::isInstance).map(Button.class::cast).filter(b->b.getMessage().equals(Component.translatable("selectWorld.create"))).findFirst().orElseThrow().onPress();
            }else if(phase==2&&mc.screen instanceof ConfirmScreen confirm){
                confirm.children().stream().filter(Button.class::isInstance).map(Button.class::cast).filter(b->b.getMessage().equals(Component.translatable("gui.yes"))).findFirst().orElseThrow().onPress();
            }else if(phase==2&&ready&&mc.level!=null&&mc.player!=null&&mc.screen==null){
                var mount=(TowerMountBlockEntity)mc.level.getBlockEntity(POS);
                if(mount==null||mount.getTower()==null||mount.getContraption()==null)return;
                check(mc.getBlockEntityRenderDispatcher().getRenderer((CannonMountBlockEntity)mount) instanceof CannonMountBlockEntityRenderer,"Native CBC renderer missing");
                if(++wait<60)return;
                horizontalSample=mount.getTower().toGlobalVector(new Vec3(1.5,1.5,1.5),1);
                Screenshot.grab(mc.gameDirectory,"tank-tower-horizontal.png",mc.getMainRenderTarget(),c->{});
                phase=3;wait=0;
                mc.getSingleplayerServer().execute(()->{
                    var serverMount=(TowerMountBlockEntity)mc.getSingleplayerServer().overworld().getBlockEntity(POS);
                    serverMount.setPitch(20);serverMount.tick();serverMount.setChanged();serverMount.sendData();
                });
            }else if(phase==3&&mc.level!=null&&++wait>60){
                var mount=(TowerMountBlockEntity)mc.level.getBlockEntity(POS);
                check(mount.getTower()!=null&&Math.abs(mount.getDisplayPitch()-20)<.01,"Client did not receive gun elevation");
                check(horizontalSample.distanceTo(mount.getTower().toGlobalVector(new Vec3(1.5,1.5,1.5),1))<1e-3,"Client gun pitch moved armor");
                Screenshot.grab(mc.gameDirectory,"tank-tower-elevated.png",mc.getMainRenderTarget(),c->{});
                Files.writeString(Path.of("tower-client-result.txt"),"PASS: release addon loaded in real full-pack client; all 32 block states match original CBC model geometry; native renderer; assembled glued armor renders horizontally while barrel elevates; client aim/entity synchronization; two gameplay screenshots");
                done=true;mc.stop();
            }
        }catch(Throwable error){fail(error);}
    }
    @SubscribeEvent public static void server(ServerTickEvent.Post event){
        if(!Boolean.getBoolean("cbctanktower.client_smoke")||done||event.getServer().getPlayerList().getPlayers().isEmpty())return;
        try{
            var server=event.getServer();var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();
            if(serverPhase==0){
                level.setDayTime(6000);
                for(int x=-3;x<=3;x++)for(int z=-3;z<=3;z++)level.setBlock(POS.offset(x,-1,z),Blocks.IRON_BLOCK.defaultBlockState(),3);
                TowerChecks.build(level,POS,Direction.SOUTH,Direction.DOWN,false,true,false);
                BlockPos anchor=POS.above(2);
                for(int y=-1;y<=1;y++)for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++){
                    if(y!=1&&x==0)continue;
                    level.setBlock(anchor.offset(x,y,z),y==0&&z==0?Blocks.GLASS.defaultBlockState():Blocks.COPPER_BLOCK.defaultBlockState(),3);
                }
                level.addFreshEntity(new SuperGlueEntity(level,SuperGlueEntity.span(anchor.offset(-1,-1,-1),anchor.offset(1,1,1))));
                player.teleportTo(level,8,204,9,Set.of(),138,9);player.getAbilities().flying=true;player.onUpdateAbilities();
                serverPhase=1;return;
            }
            if(serverPhase==1){
                var mount=(TowerMountBlockEntity)level.getBlockEntity(POS);
                TowerChecks.activate(mount);
                if(!mount.isRunning()||mount.getTower()==null)return;
                check(mount.getTower().getContraption().getBlocks().size()>3,"Gameplay armor did not assemble");
                mount.setYaw(35);mount.setPitch(0);mount.tick();mount.sendData();ready=true;serverPhase=2;
            }
        }catch(Throwable error){fail(error);}
    }
    private static void models(Minecraft mc){
        for(var state:TankTower.MOUNT.get().getStateDefinition().getPossibleStates()){
            var original=CBCBlocks.CANNON_MOUNT.getDefaultState().setValue(CannonMountBlock.HORIZONTAL_FACING,state.getValue(CannonMountBlock.HORIZONTAL_FACING)).setValue(CannonMountBlock.VERTICAL_DIRECTION,state.getValue(CannonMountBlock.VERTICAL_DIRECTION)).setValue(CannonMountBlock.ASSEMBLY_POWERED,state.getValue(CannonMountBlock.ASSEMBLY_POWERED)).setValue(CannonMountBlock.FIRE_POWERED,state.getValue(CannonMountBlock.FIRE_POWERED));
            var model=mc.getBlockRenderer().getBlockModel(state);var nativeModel=mc.getBlockRenderer().getBlockModel(original);
            check(model!=mc.getModelManager().getMissingModel(),"Tower model missing: "+state);
            for(Direction face:new Direction[]{null,Direction.DOWN,Direction.UP,Direction.NORTH,Direction.SOUTH,Direction.WEST,Direction.EAST}){
                var a=model.getQuads(state,face,RandomSource.create(42));var b=nativeModel.getQuads(original,face,RandomSource.create(42));
                check(a.size()==b.size(),"Tower model geometry differs from CBC");
                for(int i=0;i<a.size();i++)check(Arrays.equals(a.get(i).getVertices(),b.get(i).getVertices()),"Tower vertices differ from native model");
            }
        }
    }
    private static void check(boolean condition,String message){if(!condition)throw new AssertionError(message);}
    private static void fail(Throwable error){done=true;TankTower.LOG.error("TOWER_CLIENT_FAILED",error);try{Files.writeString(Path.of("tower-client-result.txt"),"FAIL: "+error);}catch(Exception ignored){}Minecraft.getInstance().execute(()->Minecraft.getInstance().stop());}
}
