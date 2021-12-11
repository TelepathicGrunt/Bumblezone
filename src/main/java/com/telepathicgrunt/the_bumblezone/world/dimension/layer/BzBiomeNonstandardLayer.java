package com.telepathicgrunt.the_bumblezone.world.dimension.layer;


import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaTransformer0;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;

public record BzBiomeNonstandardLayer(Registry<Biome> biomeRegistry) implements AreaTransformer0 {

    public int applyPixel(Context noise, int x, int z) {
        if (!BzBiomeProvider.nonstandardBiome.isEmpty() && noise.nextRandom(10) == 0) {
            return biomeRegistry.getId(
                    BzBiomeProvider.nonstandardBiome.get(
                            noise.nextRandom(BzBiomeProvider.nonstandardBiome.size())));
        }
        else {
            return -1;
        }
    }
}