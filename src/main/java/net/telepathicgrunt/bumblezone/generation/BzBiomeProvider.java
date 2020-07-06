package net.telepathicgrunt.bumblezone.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.*;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.generation.layer.BzBiomeLayer;

import java.util.ArrayList;
import java.util.function.LongFunction;


public class BzBiomeProvider extends BiomeSource {
    public static void registerBiomeprovider() {
        Registry.register(Registry.BIOME_SOURCE, Bumblezone.MOD_FULL_ID, BzBiomeProvider.CODEC);
    }

    public static final Codec<BzBiomeProvider> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.LONG.fieldOf("seed").stable().forGetter((bzBiomeProvider) -> {
            return bzBiomeProvider.seed;
        })).apply(instance, instance.stable(BzBiomeProvider::new));
    });

    private final BiomeLayerSampler biomeSampler;
    private final long seed;


    public BzBiomeProvider(long seed) {
        super(new ArrayList<>(BzBiomes.biomes));
        BzBiomeLayer.setSeed(seed);
        this.seed = seed;
        this.biomeSampler = buildWorldProcedure(seed, 4);
    }



    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer parent, LayerFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        LayerFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.create(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }


    public static BiomeLayerSampler buildWorldProcedure(long seed, int generatorType) {
        LayerFactory<CachingLayerSampler> layerFactory = build(generatorType, (salt) ->
        {
            return new CachingLayerContext(25, seed, salt);
        });
        return new BiomeLayerSampler(layerFactory);
    }


    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(int worldTypeIn, LongFunction<C> contextFactory) {
        LayerFactory<T> layer = BzBiomeLayer.INSTANCE.create(contextFactory.apply(200L));
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2000L), layer);
        layer = ScaleLayer.NORMAL.create((LayerSampleContext<T>) contextFactory.apply(1001L), layer);
        layer = ScaleLayer.NORMAL.create((LayerSampleContext<T>) contextFactory.apply(1001L), layer);
        return layer;
    }


    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.biomeSampler.sample(x, z);
    }

    @Override
    protected Codec<? extends BiomeSource> method_28442() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new BzBiomeProvider(seed);
    }
}
