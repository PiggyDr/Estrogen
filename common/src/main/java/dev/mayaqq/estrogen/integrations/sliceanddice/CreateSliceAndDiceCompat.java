package dev.mayaqq.estrogen.integrations.sliceanddice;

import dev.mayaqq.estrogen.platform.SliceAndDicePerPlatform;
import dev.mayaqq.estrogen.registry.EstrogenTags;

public class CreateSliceAndDiceCompat {

    public static void init() {
        SliceAndDicePerPlatform.registerPotionSprinkleBehavior(EstrogenTags.Fluids.FEMINIZING);
    }
}
