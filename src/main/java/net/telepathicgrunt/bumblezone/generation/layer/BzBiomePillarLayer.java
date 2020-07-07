package net.telepathicgrunt.bumblezone.generation.layer;

import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.minecraft.world.gen.ChunkRandom;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;

import java.util.stream.IntStream;


public enum BzBiomePillarLayer implements CrossSamplingLayer {
    INSTANCE;

    private static final int SUGAR_WATER = Registry.BIOME.getRawId(BzBiomes.SUGAR_WATER);
    private static final int HIVE_PILLAR = Registry.BIOME.getRawId(BzBiomes.HIVE_PILLAR);


    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {

        if (context.nextInt(6) == 0 && n == center && e == center && s == center && w == center)
        {
            return HIVE_PILLAR;
        }

        return center;
    }

}