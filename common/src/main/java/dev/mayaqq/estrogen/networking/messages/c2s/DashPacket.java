package dev.mayaqq.estrogen.networking.messages.c2s;

import com.simibubi.create.foundation.utility.Color;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.features.dash.CommonDash;
import dev.mayaqq.estrogen.registry.EstrogenEffects;
import dev.mayaqq.estrogen.registry.EstrogenSounds;
import dev.mayaqq.estrogen.registry.particles.DashTrailParticleOptions;
import dev.mayaqq.estrogen.utils.EstrogenColors;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record DashPacket(boolean sound, int dashLevel) implements Packet<DashPacket> {

    public static final ServerboundPacketType<DashPacket> TYPE = new Type();

    @Override
    public PacketType<DashPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType.Server<DashPacket> {

        public Type() {
            super(
                    DashPacket.class,
                    Estrogen.id("dash"),
                    ObjectByteCodec.create(
                            ByteCodec.BOOLEAN.fieldOf(DashPacket::sound),
                            ByteCodec.INT.fieldOf(DashPacket::dashLevel),
                            DashPacket::new
                    )
            );
        }

        @Override
        public Consumer<Player> handle(DashPacket message) {
            return (player) -> {
                if (player.hasEffect(EstrogenEffects.ESTROGEN_EFFECT.get()) && player.level() instanceof ServerLevel serverLevel) {
                    if (message.sound) serverLevel.playSound(null, player.blockPosition(), EstrogenSounds.DASH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    // dash cooldown
                    if (message.sound) CommonDash.setDashing(player.getUUID());
                    // summon particles around player
                    serverLevel.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 10, 0.5, 0.5, 0.5, 0.5);

                    Color dashColor = EstrogenColors.getDashColor(message.dashLevel, true);
                    serverLevel.sendParticles(
                            new DashTrailParticleOptions(player.getUUID(), dashColor.getRedAsFloat(), dashColor.getGreenAsFloat(), dashColor.getBlueAsFloat()),
                            player.xOld, player.yOld, player.zOld,
                            0, 0, 0, 0, 0
                    );
                }
            };
        }
    }
}
