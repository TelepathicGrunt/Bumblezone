package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BzDimension {
    public static final ResourceKey<Level> BZ_WORLD_KEY = ResourceKey.create(Registries.DIMENSION, Bumblezone.MOD_DIMENSION_ID);

    public static final Codec<BzChunkGenerator> BZ_CHUNK_GENERATOR = BzChunkGenerator.CODEC;
    public static final Codec<BzBiomeProvider> BZ_BIOME_SOURCE = BzBiomeProvider.CODEC;
    public static final Codec<BzChunkGenerator.BiomeNoise> BZ_BIOME_FUNCTION = BzChunkGenerator.BiomeNoise.CODEC.codec();

    public static void registerDimensionParts() {
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, new ResourceLocation(Bumblezone.MODID, "chunk_generator"), BZ_CHUNK_GENERATOR);
        Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(Bumblezone.MODID, "biome_source"), BZ_BIOME_SOURCE);
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, new ResourceLocation(Bumblezone.MODID, "biome_function"), BZ_BIOME_FUNCTION);
    }
}
