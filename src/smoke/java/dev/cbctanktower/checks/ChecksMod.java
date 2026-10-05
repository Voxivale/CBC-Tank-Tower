package dev.cbctanktower.checks;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod("cbctanktower_checks")
public final class ChecksMod {
    public ChecksMod(){
        NeoForge.EVENT_BUS.register(TowerChecks.class);
        if(net.neoforged.fml.loading.FMLEnvironment.dist.isClient())NeoForge.EVENT_BUS.register(ClientChecks.class);
    }
}
