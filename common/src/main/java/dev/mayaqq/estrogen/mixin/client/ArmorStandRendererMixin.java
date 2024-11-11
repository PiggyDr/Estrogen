package dev.mayaqq.estrogen.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.mayaqq.estrogen.client.registry.entityRenderers.mothElytra.MothElytraLayer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public class ArmorStandRendererMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void estrogen$init(CallbackInfo ci, @Local(argsOnly = true) EntityRendererProvider.Context arg) {
        ArmorStandRenderer armorStandRenderer = (ArmorStandRenderer) (Object) this;
        armorStandRenderer.addLayer(new MothElytraLayer<>(armorStandRenderer, arg.getModelSet()));
    }
}
