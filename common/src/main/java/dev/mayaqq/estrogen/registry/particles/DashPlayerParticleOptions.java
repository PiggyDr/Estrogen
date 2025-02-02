package dev.mayaqq.estrogen.registry.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.color.Color;
import dev.mayaqq.estrogen.registry.EstrogenParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;

import java.util.UUID;

public record DashPlayerParticleOptions(UUID playerUUID, float r, float g, float b) implements ParticleOptions {

    public static final Codec<DashPlayerParticleOptions> CODEC = Color.CODEC.xmap(
            color -> new DashPlayerParticleOptions(null, color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue()),
            data -> new Color((int) (data.r * 255), (int) (data.g * 255), (int) (data.b * 255), 0)
    );

    @SuppressWarnings("Deprecation")
    public static final Deserializer<DashPlayerParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public DashPlayerParticleOptions fromCommand(ParticleType<DashPlayerParticleOptions> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            Color color = Color.parse(reader.readString());
            return new DashPlayerParticleOptions(null, color.getFloatRed(), color.getFloatGreen(), color.getFloatBlue());
        }

        @Override
        public DashPlayerParticleOptions fromNetwork(ParticleType<DashPlayerParticleOptions> particleType, FriendlyByteBuf buffer) {
            int color = buffer.readInt();
            return new DashPlayerParticleOptions(
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
