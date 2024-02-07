package dev.mayaqq.estrogen.fabric;

import com.simibubi.create.foundation.config.ConfigBase;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.config.EstrogenConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvents;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

public class EstrogenFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register and configure the config
        EstrogenConfig.register();
        for (Map.Entry<ModConfig.Type, ConfigBase> pair : EstrogenConfig.CONFIGS.entrySet())
            ModLoadingContext.registerConfig(Estrogen.MOD_ID, pair.getKey(), pair.getValue().specification);
        // init Estrogen main class
        Estrogen.init();
        // Register everything using REGISTRATE
        Estrogen.REGISTRATE.register();
        // Register Fabric specific Events
        EstrogenFabricEvents.register();

        ModConfigEvents.loading(Estrogen.MOD_ID).register(EstrogenConfig::onLoad);
        ModConfigEvents.reloading(Estrogen.MOD_ID).register(EstrogenConfig::onReload);
    }
}