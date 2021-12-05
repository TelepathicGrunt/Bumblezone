package com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Layer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final LazyArea area;

    public Layer(AreaFactory<LazyArea> areaFactory) {
        this.area = areaFactory.make();
    }

    public Biome sample(Registry<Biome> dynamicBiomeRegistry, int x, int z) {
        int resultBiomeID = this.area.get(x, z);
        Biome biome = dynamicBiomeRegistry.byId(resultBiomeID);
        if (biome == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            }
            else {
                // Spawn ocean if we can't resolve the biome from the layers.
                ResourceKey<Biome> backupBiomeKey = net.minecraft.world.level.biome.Biomes.OCEAN;
                Bumblezone.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.location());
                return dynamicBiomeRegistry.get(backupBiomeKey);
            }
        }
        else {
            return biome;
        }
    }
}
