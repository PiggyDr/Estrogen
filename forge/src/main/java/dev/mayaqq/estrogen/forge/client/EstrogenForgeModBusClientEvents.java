package dev.mayaqq.estrogen.forge.client;

import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.client.config.ConfigSync;
import dev.mayaqq.estrogen.client.registry.EstrogenClientEvents;
import dev.mayaqq.estrogen.client.registry.EstrogenRenderer;
import dev.mayaqq.estrogen.utils.EstrogenParticleRegistrator;
import dev.mayaqq.estrogen.utils.LocationResolver;
import dev.mayaqq.estrogen.utils.client.EstrogenClientPaths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Estrogen.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class EstrogenForgeModBusClientEvents {
    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        ConfigSync.onLoad(event.getConfig());
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        ConfigSync.onReload(event.getConfig());
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        EstrogenClientEvents.onRegisterParticles(new EstrogenParticleRegistrator() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, PendingFactory<T> factory) {
                event.registerSpriteSet(type, factory::create);
            }

            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> factory) {
                event.registerSpecial(type, factory);
            }
        });
    }

    @SubscribeEvent
    public static void registerModelLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        EstrogenClientEvents.registerModelLayer(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onRegisterEntityLayers(EntityRenderersEvent.AddLayers event) {
        EstrogenRenderer.registerEntityLayers(event::getSkin);
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        LocationResolver resolver = LocationResolver.load(
            Minecraft.getInstance().getResourceManager(),
            EstrogenClientPaths.THIGH_HIGH_MODELS_DIRECTORY,
            "models", ".json"
        );
        resolver.locations().forEach(event::register);
    }
}
