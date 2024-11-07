package dev.mayaqq.estrogen.client;

import com.simibubi.create.foundation.block.connected.CTModel;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.client.config.ConfigSync;
import dev.mayaqq.estrogen.client.cosmetics.EstrogenCosmetics;
import dev.mayaqq.estrogen.client.registry.EstrogenKeybinds;
import dev.mayaqq.estrogen.client.registry.EstrogenRenderer;
import dev.mayaqq.estrogen.integrations.ears.EarsCompat;
import dev.mayaqq.estrogen.registry.EstrogenAttributes;
import dev.mayaqq.estrogen.registry.EstrogenItems;
import dev.mayaqq.estrogen.registry.EstrogenPonderScenes;
import earth.terrarium.botarium.util.CommonHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class EstrogenClient {
    public static void init() {
        EstrogenCosmetics.init();
        ConfigSync.cacheConfig();
        EstrogenRenderer.register();
        EstrogenPonderScenes.register();
        EstrogenKeybinds.register();

        ItemProperties.register(EstrogenItems.GENDER_CHANGE_POTION.get(), Estrogen.id("gender"), (itemStack, clientLevel, livingEntity, i) -> {
            Attribute boobs = EstrogenAttributes.SHOW_BOOBS.get();
            if(livingEntity != null && livingEntity.getAttributes().hasAttribute(boobs) && livingEntity.getAttributeValue(boobs) != 0) {
                return 1.0f;
            } else return 0.0f;
        });

        // mod compat
        if (CommonHooks.isModLoaded("ears")) {
            EarsCompat.boob();
        }
    }

    @Environment(EnvType.CLIENT)
    public record CTModelProvider(ConnectedTextureBehaviour behavior) implements NonNullFunction<BakedModel, BakedModel> {
        @Override
        public BakedModel apply(BakedModel bakedModel) {
            return new CTModel(bakedModel, behavior);
        }
    }
}
