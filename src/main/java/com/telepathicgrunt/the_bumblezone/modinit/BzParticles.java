package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Bumblezone.MODID);

    public static final RegistryObject<BasicParticleType> POLLEN = PARTICLE_TYPES.register("pollen_puff", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> HONEY_PARTICLE = PARTICLE_TYPES.register("honey_particle", () -> new BasicParticleType(false));
}
