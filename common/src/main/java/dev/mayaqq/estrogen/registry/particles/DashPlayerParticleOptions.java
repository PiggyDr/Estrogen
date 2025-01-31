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

public record DashPlayerParticleOptions(double x, double y, double z, LivingEntity entity) implements ParticleOptions {

    public static final Codec<DashPlayerParticleOptions> CODEC = Vec3.CODEC.xmap(
        vec3 -> new DashPlayerParticleOptions(vec3.x, vec3.y, vec3.z, null),
        particle -> new Vec3(particle.x(), particle.y, particle.z)
    );

    public static final Deserializer<DashPlayerParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public DashPlayerParticleOptions fromCommand(ParticleType<DashPlayerParticleOptions> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float x = reader.readFloat();
            reader.expect(' ');
            float y = reader.readFloat();
            reader.expect(' ');
            float z = reader.readFloat();
            return new DashPlayerParticleOptions(x, y, z, null);
        }

        @Override
        public DashPlayerParticleOptions fromNetwork(ParticleType<DashPlayerParticleOptions> particleType, FriendlyByteBuf buffer) {
            Level level = Minecraft.getInstance().level;
            return new DashPlayerParticleOptions(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), null);
        }
    };

    @Override
    public ParticleType<?> getType() {
        return EstrogenParticles.DASH_PLAYER.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    @Override
    public String writeToString() {
        return "DashPlayerParticleOptions at [%f, %f, %f] with entity".formatted(x, y, z);
    }
}
