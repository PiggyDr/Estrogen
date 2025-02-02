package dev.mayaqq.estrogen.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.color.Color;
import dev.mayaqq.estrogen.registry.EstrogenParticles;
import dev.mayaqq.estrogen.utils.EstrogenCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Cod;

import java.util.UUID;

public record DashTrailParticleOptions(UUID playerUUID, float r, float g, float b) implements ParticleOptions {

    public static final Codec<DashTrailParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("uuid").forGetter(DashTrailParticleOptions::playerUUID),
            EstrogenCodecs.NORMALIZED_FLOAT.fieldOf("red").forGetter(DashTrailParticleOptions::r),
            EstrogenCodecs.NORMALIZED_FLOAT.fieldOf("green").forGetter(DashTrailParticleOptions::g),
            EstrogenCodecs.NORMALIZED_FLOAT.fieldOf("blue").forGetter(DashTrailParticleOptions::b)
    ).apply(instance, DashTrailParticleOptions::new));

    @SuppressWarnings("Deprecation")
    public static final Deserializer<DashTrailParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public DashTrailParticleOptions fromCommand(ParticleType<DashTrailParticleOptions> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            Color color = Color.parse(reader.readString());
            return new DashTrailParticleOptions(null, color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue());
        }

        @Override
        public DashTrailParticleOptions fromNetwork(ParticleType<DashTrailParticleOptions> particleType, FriendlyByteBuf buffer) {
            int color = buffer.readInt();
            return new DashTrailParticleOptions(
                    buffer.readUUID(),
                    FastColor.ARGB32.red(color) / 255f,
                    FastColor.ARGB32.green(color) / 255f,
                    FastColor.ARGB32.blue(color) / 255f
            );
        }
    };

    @Override
    public ParticleType<?> getType() {
        return EstrogenParticles.DASH_PLAYER.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(FastColor.ARGB32.color(0, (int) (r * 255), (int) (g * 255), (int) (b * 255)));
        buffer.writeUUID(playerUUID);
    }

    @Override
    public String writeToString() {
        return "DashPlayerParticleOptions";
    }
}
