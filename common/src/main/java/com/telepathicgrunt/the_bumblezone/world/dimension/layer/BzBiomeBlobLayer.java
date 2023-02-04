package com.telepathicgrunt.the_bumblezone.world.dimension.layer;


import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaTransformer0;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;

public record BzBiomeBlobLayer(HolderSet<Biome> blobBiomes) implements AreaTransformer0 {

    public int applyPixel(Context noise, int x, int z) {
        if (blobBiomes.size() != 0 && noise.nextRandom(12) == 0) {
            return BiomeRegistryHolder.BIOME_REGISTRY.getId(
                blobBiomes.get(noise.nextRandom(blobBiomes.size())).value()
            );
        }
        else {
            return -1;
        }
    }
}