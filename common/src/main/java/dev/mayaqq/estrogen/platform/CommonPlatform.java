package dev.mayaqq.estrogen.platform;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.mayaqq.estrogen.registry.tooltip.ThighHighsToolTipModifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CommonPlatform {

    @ExpectPlatform
    public static TagKey<Item> getShearsTag() {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static ThighHighsToolTipModifier createThighHighTooltip(Item item) {
        throw  new NotImplementedException();
    }
}
