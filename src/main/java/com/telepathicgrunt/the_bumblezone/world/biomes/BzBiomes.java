package com.telepathicgrunt.the_bumblezone.world.biomes;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BzBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Bumblezone.MODID);

    // Dummy biomes to reserve the numeric ID safely for the json biomes to overwrite.
    // No static variable to hold as these dummy biomes should NOT be held and referenced elsewhere.
    static {
        createBiome("hive_wall", BiomeMaker::makeVoidBiome);
        createBiome("hive_pillar", BiomeMaker::makeVoidBiome);
        createBiome("sugar_water_floor", BiomeMaker::makeVoidBiome);
    }

    public static RegistryObject<Biome> createBiome(String name, Supplier<Biome> biome) {
        return BIOMES.register(name, biome);
    }
}
