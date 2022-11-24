package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Layer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final LazyArea area;

    public Layer(AreaFactory<LazyArea> areaFactory) {
        this.area = areaFactory.make();
    }

    public Holder<Biome> sample(int x, int z) {
        int resultBiomeID = this.area.get(x, z);
        Optional<Holder.Reference<Biome>> biome = BiomeRegistryHolder.BIOME_REGISTRY.getHolder(resultBiomeID);
        if (biome.isEmpty()) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            }
            else {
                // Spawn ocean if we can't resolve the biome from the layers.
                ResourceKey<Biome> backupBiomeKey = Biomes.OCEAN;
                Bumblezone.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.location());
                return BiomeRegistryHolder.BIOME_REGISTRY.getHolderOrThrow(backupBiomeKey);
            }
        }
        else {
            return biome.get();
        }
    }
}
