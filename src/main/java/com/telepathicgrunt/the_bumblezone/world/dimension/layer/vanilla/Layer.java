package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
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

    public Holder<Biome> sample(Registry<Biome> dynamicBiomeRegistry, int x, int z) {
        int resultBiomeID = this.area.get(x, z);
        Optional<Holder<Biome>> biome = dynamicBiomeRegistry.getHolder(resultBiomeID);
        if (biome.isEmpty()) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            }
            else {
                // Spawn ocean if we can't resolve the biome from the layers.
                ResourceKey<Biome> backupBiomeKey = Biomes.OCEAN;
                Bumblezone.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.location());
                return dynamicBiomeRegistry.getHolderOrThrow(backupBiomeKey);
            }
        }
        else {
            return biome.get();
        }
    }
}
