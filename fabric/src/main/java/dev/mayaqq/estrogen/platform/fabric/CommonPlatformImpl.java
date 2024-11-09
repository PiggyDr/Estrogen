package dev.mayaqq.estrogen.platform.fabric;

import dev.mayaqq.estrogen.fabric.tooltip.FabricThighHighsTooltip;
import dev.mayaqq.estrogen.registry.items.ThighHighsItem;
import dev.mayaqq.estrogen.registry.tooltip.ThighHighsToolTipModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CommonPlatformImpl {

    public static TagKey<Item> getShearsTag() {
        return TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("c", "shears"));
    }

    public static ThighHighsToolTipModifier createThighHighTooltip(Item item) {
        return new FabricThighHighsTooltip((ThighHighsItem) item);
    }
}
