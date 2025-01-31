package dev.mayaqq.estrogen.client.registry.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.mayaqq.estrogen.registry.particles.DashPlayerParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class DashPlayerParticle extends Particle {

    private static final ParticleRenderType DASH_PLAYER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
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
    private final int vertexCount;
    private int alpha = 255;
    private int oAlpha = 255;

    public DashPlayerParticle(DashPlayerParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(level, type.x(), type.y(), type.z(), Minecraft.getInstance().player);
    }

    protected DashPlayerParticle(ClientLevel level, double x, double y, double z, LivingEntity entity) {
        super(level, x, y, z);
        this.hasPhysics = false;
        this.setBoundingBox(entity.getBoundingBox());
        this.setLifetime(100);
        LivingEntityRenderer<?, ?> renderer = (LivingEntityRenderer<?, ?>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);

        ModelConsumer consumer = new ModelConsumer();
        renderer.getModel().renderToBuffer(new PoseStack(), consumer, 0, OverlayTexture.NO_OVERLAY, 255, 255, 255, 255);
        vertices = consumer.data;
        vertexCount = consumer.vertexCount;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 pos = renderInfo.getPosition();
        float x = (float)(this.x - pos.x());
        float y = (float)(this.y - pos.y());
        float z = (float)(this.z - pos.z());

        Matrix4f mat = new Matrix4f();
        //mat.rotate(Axis.XP.rotationDegrees(renderInfo.getXRot()));
        //mat.rotate(Axis.YP.rotationDegrees(renderInfo.getYRot() + 180.0f));
        mat.translate(x, y, z);
        //mat.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));

        //float a = Mth.lerp(partialTicks, oAlpha, alpha);
        for(int i = 0; i < vertexCount; i++) {
            int v = i * ModelConsumer.STRIDE;
            float vx = Float.intBitsToFloat(vertices[v]);
            float vy = Float.intBitsToFloat(vertices[v] + 1);
            float vz = Float.intBitsToFloat(vertices[v] + 2);

            buffer.vertex(mat, vx, vy, vz).color(0, 0, 255, 255).endVertex();
        }
    }

    @Override
    public void tick() {
//        oAlpha = alpha;
//        alpha--;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return DASH_PLAYER;
    }

    private static class ModelConsumer implements VertexConsumer {
        private static final int STRIDE = 3;

        private int[] data = new int[12];
        private int position = 0;
        private int vertexCount;
        private int capacity = 4;

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
            vertexCount++;
            if(vertexCount >= capacity) {
                capacity += 4;
                int[] newData = new int[capacity * STRIDE];
                System.arraycopy(data, 0, newData, 0, data.length);
                data = newData;
            }
        }

        @Override
        public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {}

        @Override
        public void unsetDefaultColor() {}
    }
}
