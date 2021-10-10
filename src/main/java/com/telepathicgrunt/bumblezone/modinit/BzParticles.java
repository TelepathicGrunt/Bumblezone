package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.bumblezone.client.particles.PollenPuff;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.impl.client.particle.ParticleFactoryRegistryImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public class BzParticles {
    public static SimpleParticleType POLLEN = FabricParticleTypes.simple();
    public static SimpleParticleType HONEY_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_particle"), HONEY_PARTICLE);
        ParticleFactoryRegistryImpl.INSTANCE.register(BzParticles.POLLEN, PollenPuff.Factory::new);
        ParticleFactoryRegistryImpl.INSTANCE.register(BzParticles.HONEY_PARTICLE, HoneyParticle.Factory::new);
    }
}
