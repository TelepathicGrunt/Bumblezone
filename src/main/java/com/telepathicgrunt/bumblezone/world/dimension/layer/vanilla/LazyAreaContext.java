package com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla;

import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public class LazyAreaContext implements BigContext<LazyArea> {
    private static final int MAX_CACHE = 1024;
    private final Long2IntLinkedOpenHashMap cache;
    private final int maxCache;
    private final ImprovedNoise biomeNoise;
    private final long seed;
    private long rval;

    public LazyAreaContext(int maxCache, long seed1, long seed2) {
        this.seed = mixSeed(seed1, seed2);
        this.biomeNoise = new ImprovedNoise(new LegacyRandomSource(seed1));
        this.cache = new Long2IntLinkedOpenHashMap(16, 0.25F);
        this.cache.defaultReturnValue(Integer.MIN_VALUE);
        this.maxCache = maxCache;
    }

    public LazyArea createResult(PixelTransformer pixelTransformer) {
        return new LazyArea(this.cache, this.maxCache, pixelTransformer);
    }

    public LazyArea createResult(PixelTransformer pixelTransformer, LazyArea lazyArea) {
        return new LazyArea(this.cache, Math.min(1024, lazyArea.getMaxCache() * 4), pixelTransformer);
    }

    public LazyArea createResult(PixelTransformer pixelTransformer, LazyArea lazyArea, LazyArea lazyArea2) {
        return new LazyArea(this.cache, Math.min(1024, Math.max(lazyArea.getMaxCache(), lazyArea2.getMaxCache()) * 4), pixelTransformer);
    }

    @Override
    public void initRandom(long l, long m) {
        long n = this.seed;
        n = LinearCongruentialGenerator.next(n, l);
        n = LinearCongruentialGenerator.next(n, m);
        n = LinearCongruentialGenerator.next(n, l);
        n = LinearCongruentialGenerator.next(n, m);
        this.rval = n;
    }

    @Override
    public int nextRandom(int i) {
        int j = Math.floorMod(this.rval >> 24, i);
        this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
        return j;
    }

    @Override
    public ImprovedNoise getBiomeNoise() {
        return this.biomeNoise;
    }

    private static long mixSeed(long l, long m) {
        long n = LinearCongruentialGenerator.next(m, m);
        n = LinearCongruentialGenerator.next(n, m);
        n = LinearCongruentialGenerator.next(n, m);
        long o = LinearCongruentialGenerator.next(l, n);
        o = LinearCongruentialGenerator.next(o, n);
        return LinearCongruentialGenerator.next(o, n);
    }
}
