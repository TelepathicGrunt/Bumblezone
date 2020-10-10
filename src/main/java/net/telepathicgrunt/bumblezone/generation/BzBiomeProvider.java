package net.telepathicgrunt.bumblezone.generation;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.*;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.generation.layer.BzBiomeLayer;
import net.telepathicgrunt.bumblezone.generation.layer.BzBiomePillarLayer;
import net.telepathicgrunt.bumblezone.generation.layer.BzBiomeScalePillarLayer;
import net.telepathicgrunt.bumblezone.mixin.BiomeLayerSamplerAccessor;

import java.util.List;
import java.util.function.LongFunction;

public class BzBiomeProvider extends BiomeSource {
    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(Bumblezone.MODID, "biome_source"), BzBiomeProvider.CODEC);
    }

    public static final Codec<BzBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(BzBiomeProvider::new)));

    private final BiomeLayerSampler BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> layersBiomeRegistry;
    private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(Bumblezone.MODID, "hive_pillar")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(Bumblezone.MODID, "hive_wall")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(Bumblezone.MODID, "sugar_water_floor")));


    public BzBiomeProvider(Registry<Biome> biomeRegistry) {
        // Need world seed passed here
        this(0, biomeRegistry);
    }

    public BzBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(BIOMES.stream().map((registryKey) -> () -> (Biome)biomeRegistry.get(registryKey)));
        BzBiomeLayer.setSeed(seed);
        this.BIOME_REGISTRY = biomeRegistry;
        BzBiomeProvider.layersBiomeRegistry = biomeRegistry;
        this.BIOME_SAMPLER = buildWorldProcedure(seed);
    }



    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer parent, LayerFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        LayerFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.create(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }


    public static BiomeLayerSampler buildWorldProcedure(long seed) {
        LayerFactory<CachingLayerSampler> layerFactory = build((salt) ->
                new CachingLayerContext(25, seed, salt));
        return new BiomeLayerSampler(layerFactory);
    }


    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> contextFactory) {
        LayerFactory<T> layer = BzBiomeLayer.INSTANCE.create(contextFactory.apply(200L));
        layer = BzBiomePillarLayer.INSTANCE.create(contextFactory.apply(1008L), layer);
        layer = BzBiomeScalePillarLayer.INSTANCE.create(contextFactory.apply(1055L), layer);
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2003L), layer);
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2523L), layer);
        return layer;
    }


    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.sample(this.BIOME_REGISTRY, x, z);
    }

    public Biome sample(Registry<Biome> registry, int i, int j) {
        int k = ((BiomeLayerSamplerAccessor)this.BIOME_SAMPLER).getSampler().sample(i, j);
        Biome biome = registry.get(k);
        if (biome == null) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("Unknown biome id: " + k));
            } else {
                return registry.get(BuiltinBiomes.fromRawId(0));
            }
        } else {
            return biome;
        }
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new BzBiomeProvider(seed, this.BIOME_REGISTRY);
    }
}
