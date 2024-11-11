package dev.mayaqq.estrogen.client.features;

import com.mojang.math.Axis;
import dev.mayaqq.estrogen.resources.EstrogenSplashLoader;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class EstrogenSplashRenderer extends SplashRenderer {

    private final Component splash;

    public EstrogenSplashRenderer(RandomSource random) {
        super("");
        this.splash = EstrogenSplashLoader.INSTANCE.splashes.get(random.nextInt(EstrogenSplashLoader.INSTANCE.splashes.size()));
    }

    @Override
    public void render(GuiGraphics graphics, int screenWidth, Font font, int color) {
        graphics.pose().pushPose();
        graphics.pose().translate((float)screenWidth / 2.0F + 123.0F, 69.0F, 0.0F);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
        float f = 1.8F - Mth.abs(Mth.sin((float)(Util.getMillis() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
        f = f * 100.0F / (float)(font.width(this.splash) + 32);
        graphics.pose().scale(f, f, f);
        graphics.drawCenteredString(font, this.splash, 0, -8, 16776960 | color);
        graphics.pose().popPose();
    }
}
