package com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer;


import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.AreaTransformer0;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.layer.vanilla.Context;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;

public record BzBiomeBlobLayer(HolderSet<Biome> blobBiomes, HolderSet<Biome> rareBlobBiomes) implements AreaTransformer0 {

    public int applyPixel(Context noise, int x, int z) {
        if (blobBiomes.size() != 0 && noise.nextRandom(12) == 0) {
            return BiomeRegistryHolder.BIOME_REGISTRY.getId(
                    blobBiomes.get(noise.nextRandom(blobBiomes.size())).value()
            );
        }
        else if (rareBlobBiomes.size() != 0 && noise.nextRandom(48) == 0) {
            return BiomeRegistryHolder.BIOME_REGISTRY.getId(
                    rareBlobBiomes.get(noise.nextRandom(rareBlobBiomes.size())).value()
            );
        }
        else {
            return -1;
        }
    }
}