package net.telepathicgrunt.bumblezone.dimension.layer;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzBiomeProvider;


public enum BzBiomePillarLayer implements CrossSamplingLayer {
    INSTANCE;

    private static final Identifier HIVE_PILLAR = new Identifier(Bumblezone.MODID, "hive_pillar");
    public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {

        if (context.nextInt(12) == 0 && n == center && e == center && s == center && w == center) {
            return BzBiomeProvider.layersBiomeRegistry.getRawId(BzBiomeProvider.layersBiomeRegistry.get(HIVE_PILLAR));
        }

        return center;
    }

}