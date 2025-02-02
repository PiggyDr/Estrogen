package dev.mayaqq.estrogen.registry;

import com.mojang.serialization.Codec;
import dev.mayaqq.estrogen.Estrogen;
import dev.mayaqq.estrogen.registry.particles.DashTrailParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import uwu.serenity.critter.api.entry.RegistryEntry;
import uwu.serenity.critter.api.generic.Registrar;

public class EstrogenParticles {
    public static final Registrar<ParticleType<?>> PARTICLES = Estrogen.REGISTRIES.getRegistrar(Registries.PARTICLE_TYPE);

    public static final RegistryEntry<SimpleParticleType> DASH = PARTICLES.<SimpleParticleType>entry("dash", () -> new SimpleParticleType(true) {}).register();
    public static final RegistryEntry<SimpleParticleType> MOTH_FUZZ = PARTICLES.<SimpleParticleType>entry("moth_fuzz", () -> new SimpleParticleType(true) {}).register();
    public static final RegistryEntry<ParticleType<DashTrailParticleOptions>> DASH_PLAYER = PARTICLES.entry("dash_player", () -> new ParticleType<>(true, DashTrailParticleOptions.DESERIALIZER) {
        @Override
        public Codec<DashTrailParticleOptions> codec() {
            return DashTrailParticleOptions.CODEC;
        }
    }).register();
}
