package net.telepathicgrunt.bumblezone.generation;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.*;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.level.LevelGeneratorType;
import net.telepathicgrunt.bumblezone.biome.BzBiomesInit;
import net.telepathicgrunt.bumblezone.generation.layer.BzBiomeLayer;

import java.util.function.LongFunction;


public class BzBiomeProvider extends BiomeSource {

    private final BiomeLayerSampler biomeSampler;


    public BzBiomeProvider(long seed, LevelGeneratorType worldType) {
        super(BzBiomesInit.biomes);

        //generates the world and biome layouts
        biomeSampler = buildWorldProcedure(seed, worldType);
    }


    public BzBiomeProvider(World world) {
        this(world.getSeed(), world.getGeneratorType());
        BzBiomeLayer.setSeed(world.getSeed());
    }


    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer parent, LayerFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        LayerFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.create(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }


    public static BiomeLayerSampler buildWorldProcedure(long seed, LevelGeneratorType generatorType) {
        LayerFactory<CachingLayerSampler> layerFactory = build(generatorType, (salt) ->
        {
            return new CachingLayerContext(25, seed, salt);
        });
        return new BiomeLayerSampler(layerFactory);
    }


    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LevelGeneratorType worldTypeIn, LongFunction<C> contextFactory) {
        LayerFactory<T> layer = BzBiomeLayer.INSTANCE.create(contextFactory.apply(200L));
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2000L), layer);
        layer = ScaleLayer.NORMAL.create((LayerSampleContext<T>) contextFactory.apply(1001L), layer);
        layer = ScaleLayer.NORMAL.create((LayerSampleContext<T>) contextFactory.apply(1001L), layer);
        return layer;
    }


    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.biomeSampler.sample(x, z);
    }

}
