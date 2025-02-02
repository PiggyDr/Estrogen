package dev.mayaqq.estrogen.fabric.client;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.client.command.EstrogenClientCommands;
import dev.mayaqq.estrogen.client.config.ConfigSync;
import dev.mayaqq.estrogen.client.features.dash.DashOverlay;
import dev.mayaqq.estrogen.client.registry.EstrogenClientEvents;
import dev.mayaqq.estrogen.fabric.client.menu.OpenEstrogenMenuFabric;
import dev.mayaqq.estrogen.utils.EstrogenParticleRegistrator;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EstrogenFabricClientEvents {
    public static void register() {
        HudRenderCallback.EVENT.register((guiGraphics, delta) -> {
            DashOverlay.drawOverlay(guiGraphics);
        });
        ModConfigEvents.loading(Estrogen.MOD_ID).register(ConfigSync::onLoad);
        ModConfigEvents.reloading(Estrogen.MOD_ID).register(ConfigSync::onReload);

        EstrogenClientEvents.onRegisterParticles(new EstrogenParticleRegistrator() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, PendingFactory<T> factory) {
                ParticleFactoryRegistry.getInstance().register(type, factory::create);
            }

            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> factory) {
                ParticleFactoryRegistry.getInstance().register(type, factory);
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            EstrogenClientEvents.onDisconnect();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, context) -> {
            EstrogenClientCommands.register(dispatcher, new FabricClientCommandManager());
        });

        EstrogenClientEvents.registerModelLayer((location, definition) -> EntityModelLayerRegistry.registerModelLayer(location, definition::get));

        ResourceLocation latePhase = Estrogen.id("late");
        ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, latePhase);
        ScreenEvents.AFTER_INIT.register(latePhase, OpenEstrogenMenuFabric::onGuiInit);
    }

    public static class FabricClientCommandManager implements EstrogenClientCommands.ClientCommandManager<FabricClientCommandSource> {

        @Override
        public LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
            return ClientCommandManager.literal(name);
        }

        @Override
        public <I> RequiredArgumentBuilder<FabricClientCommandSource, I> argument(String name, ArgumentType<I> type) {
            return ClientCommandManager.argument(name, type);
        }

        @Override
        public boolean hasPermission(FabricClientCommandSource source, int permissionLevel) {
            return source.hasPermission(permissionLevel);
        }

        @Override
        public void sendFailure(FabricClientCommandSource source, Component component) {
            source.sendError(component);
        }
    }
}
