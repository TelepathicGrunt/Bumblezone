package com.telepathicgrunt.the_bumblezone.modinit;

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
    // Technically, this isn't needed at all for my mod. Legacy code.
    static {
        createBiome("hive_wall", BiomeMaker::theVoidBiome);
        createBiome("hive_pillar", BiomeMaker::theVoidBiome);
        createBiome("sugar_water_floor", BiomeMaker::theVoidBiome);
        createBiome("pollinated_fields", BiomeMaker::theVoidBiome);
        createBiome("pollinated_pillar", BiomeMaker::theVoidBiome);
    }

    public static RegistryObject<Biome> createBiome(String name, Supplier<Biome> biome) {
        return BIOMES.register(name, biome);
    }
}
