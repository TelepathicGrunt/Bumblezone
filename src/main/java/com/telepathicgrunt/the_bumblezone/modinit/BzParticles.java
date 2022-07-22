package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public class BzParticles {
    public static SimpleParticleType POLLEN = FabricParticleTypes.simple();
    public static SimpleParticleType HONEY_PARTICLE = FabricParticleTypes.simple();
    public static SimpleParticleType ROYAL_JELLY_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_particle"), HONEY_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(Bumblezone.MODID, "royal_jelly_particle"), ROYAL_JELLY_PARTICLE);
    }
}
