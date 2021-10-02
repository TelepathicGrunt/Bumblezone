package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.bumblezone.client.particles.PollenPuff;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.impl.client.particle.ParticleFactoryRegistryImpl;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BzParticles {
    public static DefaultParticleType POLLEN = FabricParticleTypes.simple();
    public static DefaultParticleType HONEY_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Bumblezone.MODID, "pollen_puff"), POLLEN);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Bumblezone.MODID, "honey_particle"), HONEY_PARTICLE);
        ParticleFactoryRegistryImpl.INSTANCE.register(BzParticles.POLLEN, PollenPuff.Factory::new);
        ParticleFactoryRegistryImpl.INSTANCE.register(BzParticles.HONEY_PARTICLE, HoneyParticle.Factory::new);
    }
}
