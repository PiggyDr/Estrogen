package dev.mayaqq.estrogen.client.registry;

import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.registry.EstrogenAttributes;
import dev.mayaqq.estrogen.registry.EstrogenItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class EstrogenItemProperties {
    public static void register() {
        ItemProperties.register(EstrogenItems.GENDER_CHANGE_POTION.get(), Estrogen.id("gender"), (itemStack, clientLevel, livingEntity, i) -> {
            Attribute boobs = EstrogenAttributes.SHOW_BOOBS.get();
            if(livingEntity != null && livingEntity.getAttributes().hasAttribute(boobs) && livingEntity.getAttributeValue(boobs) != 0) {
                return 1.0f;
            } else return 0.0f;
        });
    }
}
