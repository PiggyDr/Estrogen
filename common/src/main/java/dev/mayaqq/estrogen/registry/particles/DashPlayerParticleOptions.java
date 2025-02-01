package dev.mayaqq.estrogen.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import dev.mayaqq.estrogen.client.registry.particles.DashPlayerParticle;
import dev.mayaqq.estrogen.registry.EstrogenParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record DashPlayerParticleOptions(@Nullable LivingEntity entity) implements ParticleOptions {

    public static final Codec<DashPlayerParticleOptions> CODEC = Codec.unit(() -> new DashPlayerParticleOptions(null));

    @SuppressWarnings("Deprecation")
    public static final Deserializer<DashPlayerParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public DashPlayerParticleOptions fromCommand(ParticleType<DashPlayerParticleOptions> particleType, StringReader reader) throws CommandSyntaxException {
            return new DashPlayerParticleOptions(null);
        }

        @Override
        public DashPlayerParticleOptions fromNetwork(ParticleType<DashPlayerParticleOptions> particleType, FriendlyByteBuf buffer) {
            Level level = Minecraft.getInstance().level;
            int entityId = buffer.readInt();
            return new DashPlayerParticleOptions((entityId == 0) ? null : (LivingEntity) level.getEntity(entityId));
        }
    };

    @Override
    public ParticleType<?> getType() {
        return EstrogenParticles.DASH_PLAYER.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt((entity != null) ? entity.getId() : 0);
    }

    @Override
    public String writeToString() {
        return "DashPlayerParticleOptions";
    }
}
