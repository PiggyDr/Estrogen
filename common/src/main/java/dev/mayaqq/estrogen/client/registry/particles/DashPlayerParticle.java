package dev.mayaqq.estrogen.client.registry.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;

public class DashPlayerParticle extends Particle {

    private static final ParticleRenderType DASH_PLAYER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        @Override
        public String toString() {
            return "DashPlayerParticle";
        }
    };

    private final int[] vertices;
    private int alpha = 255;

    protected DashPlayerParticle(ClientLevel level, double x, double y, double z, LivingEntity entity) {
        super(level, x, y, z);
        LivingEntityRenderer<?, ?> renderer = (LivingEntityRenderer<?, ?>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);

        ModelConsumer consumer = new ModelConsumer();
        renderer.getModel().renderToBuffer(new PoseStack(), consumer, 0, OverlayTexture.NO_OVERLAY, 255, 255, 255, 255);
        vertices = consumer.data;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

    }

    @Override
    public ParticleRenderType getRenderType() {
        return DASH_PLAYER;
    }

    private class ModelConsumer implements VertexConsumer {
        private static final int STRIDE = 3;

        private int[] data = new int[4];
        private int position = 0;

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            data[position] = Float.floatToIntBits((float) x);
            data[position + 1] = Float.floatToIntBits((float) y);
            data[position + 2] = Float.floatToIntBits((float) z);
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        @Override
        public void endVertex() {
            position += STRIDE;
            if(position * STRIDE > data.length) {
                int[] newData = new int[data.length + 4 * STRIDE];
                System.arraycopy(data, 0, newData, 0, newData.length);
                data = newData;
            }
        }

        @Override
        public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {}

        @Override
        public void unsetDefaultColor() {}
    }
}
