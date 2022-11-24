package com.telepathicgrunt.the_bumblezone.world.dimension.layer;


import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.AreaTransformer0;
import com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla.Context;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;

public record BzBiomeNonstandardLayer(HolderSet<Biome> nonstandardBiomes) implements AreaTransformer0 {

    public int applyPixel(Context noise, int x, int z) {
        if (nonstandardBiomes.size() != 0 && noise.nextRandom(10) == 0) {
            return BiomeRegistryHolder.BIOME_REGISTRY.getId(
                nonstandardBiomes.get(noise.nextRandom(nonstandardBiomes.size())).value()
            );
        }
        else {
            return -1;
        }
    }
}