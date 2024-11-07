package dev.mayaqq.estrogen.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.mayaqq.estrogen.client.features.EstrogenSplashRenderer;
import dev.mayaqq.estrogen.config.EstrogenConfig;
import dev.mayaqq.estrogen.resources.EstrogenSplashLoader;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {

    @Shadow @Final private static RandomSource RANDOM;

    @ModifyReturnValue(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;",
            at = @At("RETURN")
    )
    private List<String> modifySplashList(List<String> list) {
        if (EstrogenConfig.client().estrogenSplashes.get()) {
            for (int i = 0; i < 30; i++) {
                list.add("estrogen:splashes");
            }
        }
        return list;
    }

    @ModifyReturnValue(
            method = "getSplash",
            at = @At("RETURN")
    )
    private SplashRenderer modifySplash(SplashRenderer original) {
        if (original instanceof SplashRendererAccessor accessor && "estrogen:splashes".equals(accessor.getSplash())) {
            return new EstrogenSplashRenderer(RANDOM);
        }
        return original;
    }
}
