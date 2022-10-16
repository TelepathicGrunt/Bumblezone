package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Bumblezone.MODID);

    public static final RegistryObject<SimpleParticleType> POLLEN_PARTICLE = PARTICLE_TYPES.register("pollen_puff", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> HONEY_PARTICLE = PARTICLE_TYPES.register("honey_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SPARKLE_PARTICLE = PARTICLE_TYPES.register("sparkle_particle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ROYAL_JELLY_PARTICLE = PARTICLE_TYPES.register("royal_jelly_particle", () -> new SimpleParticleType(false));
}
