package dev.mayaqq.estrogen.client.registry.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.registry.EstrogenEvents;
import dev.mayaqq.estrogen.registry.particles.DashPlayerParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DashPlayerParticle extends Particle {

    private static final ParticleRenderType DASH_PLAYER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
            RenderSystem.setShaderColor(1.0f,1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
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

    private final PoseStack matrices = new PoseStack();
    private final float[] vertices;
    private final boolean isLocalPlayer;
    private final float yRot;
    private final int vertexCount;
    private int alphaTick = 20;
    private int oAlphaTick = 20;

    public DashPlayerParticle(DashPlayerParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this(level, x, y, z, (options.entity() != null) ? options.entity() : Minecraft.getInstance().player);
    }

    protected DashPlayerParticle(ClientLevel level, double x, double y, double z, LivingEntity entity) {
        super(level, x, y, z);
        this.hasPhysics = false;
        this.setBoundingBox(entity.getBoundingBox());
        this.setLifetime(20);
        this.yRot = entity.yBodyRot + 180.0f;
        this.isLocalPlayer = entity == Minecraft.getInstance().getCameraEntity();

        LivingEntityRenderer<?, ?> renderer = (LivingEntityRenderer<?, ?>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        ModelConsumer consumer = new ModelConsumer();
        matrices.pushPose();
        renderer.getModel().renderToBuffer(matrices, consumer, 0, OverlayTexture.NO_OVERLAY, 255, 255, 255, 255);
        matrices.popPose();
        vertices = consumer.data;
        vertexCount = consumer.vertexCount;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {

        Vec3 pos = renderInfo.getPosition();
        if(isLocalPlayer && Minecraft.getInstance().options.getCameraType().isFirstPerson() && pos.distanceToSqr(x, y, z) < 4.0f) return;
        float x = (float)(this.x - pos.x());
        float y = (float)(this.y - pos.y());
        float z = (float)(this.z - pos.z());

        matrices.pushPose();
        matrices.translate(x, y + 1.5f, z);
        matrices.scale(1.0f, -1.0f, 1.0f);
        matrices.mulPose(Axis.YN.rotationDegrees(yRot));

        float alpha = Mth.lerp(partialTicks, oAlphaTick, alphaTick) / 20f;

        for(int i = 0; i < vertexCount; i++) {
            int v = i * ModelConsumer.STRIDE;
            buffer.vertex(matrices.last().pose(), vertices[v], vertices[v + 1], vertices[v + 2])
                    .color(0.5F, 0.7F, 1.0F, alpha)
                    .uv2(LightTexture.FULL_BRIGHT)
                    .endVertex();
        }

        matrices.popPose();
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        oAlphaTick = alphaTick;
        alphaTick--;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return DASH_PLAYER;
    }

    private static class ModelConsumer implements VertexConsumer {
        private static final int STRIDE = 3;

        private float[] data = new float[12];
        private int position = 0;
        private int vertexCount;
        private int capacity = 4;

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            data[position] = (float) x;
            data[position + 1] = (float) y;
            data[position + 2] = (float) z;
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
                float[] newData = new float[capacity * STRIDE];
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
