package dev.mayaqq.estrogen.utils;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public interface EstrogenParticleRegistrator {

    <T extends ParticleOptions> void register(ParticleType<T> type, PendingFactory<T> factory);

    <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> factory);

    @FunctionalInterface
    interface PendingFactory<T extends ParticleOptions> {
        ParticleProvider<T> create(SpriteSet spriteSet);
    }
}
