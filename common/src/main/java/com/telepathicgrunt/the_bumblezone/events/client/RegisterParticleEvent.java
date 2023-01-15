package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public record RegisterParticleEvent(Registrar registrar) {

    public static final EventHandler<RegisterParticleEvent> EVENT = new EventHandler<>();

    public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> registration) {
        registrar.register(type, registration);
    }

    @FunctionalInterface
    public interface Registrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> registration);
    }
}
