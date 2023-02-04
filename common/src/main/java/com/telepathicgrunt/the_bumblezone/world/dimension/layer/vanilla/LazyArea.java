package com.telepathicgrunt.the_bumblezone.world.dimension.layer.vanilla;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.world.level.ChunkPos;


public class LazyArea implements Area {
    private final PixelTransformer transformer;
    private final Long2IntLinkedOpenHashMap cache;
    private final int maxCache;

    public LazyArea(Long2IntLinkedOpenHashMap long2IntLinkedOpenHashMap, int maxCache, PixelTransformer pixelTransformer) {
        this.cache = long2IntLinkedOpenHashMap;
        this.maxCache = maxCache;
        this.transformer = pixelTransformer;
    }

    @Override
    public int get(int x, int z) {
        long longPos = ChunkPos.asLong(x, z);
        synchronized(this.cache) {
            int cachedBiomeInt = this.cache.get(longPos);
            if (cachedBiomeInt != Integer.MIN_VALUE) {
                return cachedBiomeInt;
            }
            else {
                int biomeInt = this.transformer.apply(x, z);
                this.cache.put(longPos, biomeInt);
                if (this.cache.size() > this.maxCache) {
                    for(int n = 0; n < this.maxCache / 16; ++n) {
                        this.cache.removeFirstInt();
                    }
                }

                return biomeInt;
            }
        }
    }

    public int getMaxCache() {
        return this.maxCache;
    }
}
