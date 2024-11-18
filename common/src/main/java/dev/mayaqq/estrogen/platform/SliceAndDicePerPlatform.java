package dev.mayaqq.estrogen.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class SliceAndDicePerPlatform {

    @ExpectPlatform
    public static void registerPotionSprinkleBehavior(TagKey<Fluid> tag) {}
}
