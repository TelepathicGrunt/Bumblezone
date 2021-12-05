package com.telepathicgrunt.bumblezone.world.dimension.layer;


import com.telepathicgrunt.bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.AreaTransformer0;
import com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;

public class BzBiomeNonstandardLayer implements AreaTransformer0 {

    private final Registry<Biome> biomeRegistry;

    public BzBiomeNonstandardLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }


    public int applyPixel(Context noise, int x, int z) {
        if(!BzBiomeProvider.nonstandardBiome.isEmpty() && noise.nextRandom(10) == 0) {
            return biomeRegistry.getId(
                        BzBiomeProvider.nonstandardBiome.get(
                            noise.nextRandom(BzBiomeProvider.nonstandardBiome.size())));
        }
        else {
            return -1;
        }
    }
}