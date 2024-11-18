package dev.mayaqq.estrogen.platform.fabric;

import com.possible_triangle.sliceanddice.block.sprinkler.SprinkleBehaviour;
import dev.mayaqq.estrogen.registry.EstrogenEffects;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;

public class SliceAndDicePerPlatformImpl {

    public static void registerPotionSprinkleBehavior(TagKey<Fluid> tag) {
        SprinkleBehaviour.Companion.register(tag, (range, level, fluidStack, random) -> {
            range.getEntities(LivingEntity.class, e -> true).forEach(entity ->
                    entity.addEffect(new MobEffectInstance(EstrogenEffects.ESTROGEN_EFFECT.get(), 119, 0, false, false, true))
            );
        }, new Vec3i(5, 7, 5));
    }
}
