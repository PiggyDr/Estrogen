package dev.mayaqq.estrogen.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.mayaqq.estrogen.registry.EstrogenEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapOperation(
            method = "onEffectUpdated",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"
            )
    )
    public void onEffectUpdated(MobEffect effect, LivingEntity livingEntity, AttributeMap attributeMap, int amplifier, Operation<Void> original) {
        // Mojang calls remove and then add when updating an effect we do not want this for the Girl Power effect.
        if (!effect.equals(EstrogenEffects.ESTROGEN_EFFECT.get())) {
            original.call(effect, livingEntity, attributeMap, amplifier);
        }
    }
}
