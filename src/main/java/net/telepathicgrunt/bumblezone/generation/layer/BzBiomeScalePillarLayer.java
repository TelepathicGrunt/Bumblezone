package net.telepathicgrunt.bumblezone.generation.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;


public enum BzBiomeScalePillarLayer implements CrossSamplingLayer {
    INSTANCE;

    private static final int HIVE_WALL = Registry.BIOME.getRawId(BzBiomes.HIVE_WALL);
    private static final int HIVE_PILLAR = Registry.BIOME.getRawId(BzBiomes.HIVE_PILLAR);
    private static final int SUGAR_WATER = Registry.BIOME.getRawId(BzBiomes.SUGAR_WATER);

    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {
        if(center != HIVE_PILLAR){
            boolean borderingHivePillar = false;

            if((n == HIVE_PILLAR || e == HIVE_PILLAR) ||(w == HIVE_PILLAR || s == HIVE_PILLAR)) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return HIVE_PILLAR;
            }
        }
        return center;
    }
}