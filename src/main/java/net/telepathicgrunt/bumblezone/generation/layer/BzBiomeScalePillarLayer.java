package net.telepathicgrunt.bumblezone.generation.layer;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.generation.BzBiomeProvider;


public enum BzBiomeScalePillarLayer implements CrossSamplingLayer {
    INSTANCE;

    private static final RegistryKey<Biome> HIVE_PILLAR = RegistryKey.of(Registry.BIOME_KEY, new Identifier(Bumblezone.MODID, "hive_pillar"));
    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {
        if(center != BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR))){
            boolean borderingHivePillar = false;

            if((n == BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR)) ||
                e == BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR))) ||
                    (w == BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR)) ||
                    s == BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR)))) {
                borderingHivePillar = true;
            }

            if (borderingHivePillar) {
                return BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR));
            }
        }
        return center;
    }
}