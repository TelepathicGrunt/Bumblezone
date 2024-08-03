package com.telepathicgrunt.the_bumblezone.worldgen.dimension;

import com.telepathicgrunt.the_bumblezone.mixin.world.BiomeManagerAccessor;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

public class NoVerticalBlendBiomeManager extends BiomeManager{
    private final BiomeManager biomeManager;
    private final Long2ObjectMap<Holder<Biome>> cachedResult = new Long2ObjectOpenHashMap<>();

    public NoVerticalBlendBiomeManager(BiomeManager biomeManager) {
        super(((BiomeManagerAccessor)biomeManager).getNoiseBiomeSource(), ((BiomeManagerAccessor)biomeManager).getBiomeZoomSeed());
        this.biomeManager = biomeManager;
    }

    @Override
    public Holder<Biome> getBiome(BlockPos arg) {
        int xMinus2 = arg.getX() - 2;
        int zMinus2 = arg.getZ() - 2;
        int xShifted = xMinus2 >> 2;
        int zShifted = zMinus2 >> 2;

        //Caching
        long key = packXZ(xShifted, zShifted);
        Holder<Biome> result = cachedResult.get(key);
        if (result != null) {
            return result;
        }

        double xMagic = (double)(xMinus2 & 3) / 4.0;
        double zMagic = (double)(zMinus2 & 3) / 4.0;
        int lastIteration = 0;
        double currentDistance = Double.POSITIVE_INFINITY;

        for(int iteration = 0; iteration < 8; ++iteration) {

            boolean flag4 = (iteration & 4) == 0;
            boolean flag1 = (iteration & 1) == 0;
            int xShiftedFlagged = flag4 ? xShifted : xShifted + 1;
            int zShiftedFlagged = flag1 ? zShifted : zShifted + 1;
            double xMagicFlagged = flag4 ? xMagic : xMagic - 1.0;
            double zMagicFlagged = flag1 ? zMagic : zMagic - 1.0;

            double fiddledDistance = getFiddledDistance(
                    ((BiomeManagerAccessor)biomeManager).getBiomeZoomSeed(),
                    xShiftedFlagged,
                    zShiftedFlagged,
                    xMagicFlagged,
                    zMagicFlagged);

            if (currentDistance > fiddledDistance) {
                lastIteration = iteration;
                currentDistance = fiddledDistance;
            }
        }

        int finalX = (lastIteration & 4) == 0 ? xShifted : xShifted + 1;
        int finalZ = (lastIteration & 1) == 0 ? zShifted : zShifted + 1;
        Holder<Biome> biomeHolder = ((BiomeManagerAccessor)biomeManager).getNoiseBiomeSource().getNoiseBiome(finalX, 0, finalZ);

        //Caching
        if (cachedResult.size() > 512) {
            cachedResult.clear();
        }
        cachedResult.put(key, biomeHolder);

        return biomeHolder;
    }

    private long packXZ(int x, int z) {
        return (((long) x) << 32) | z;
    }

    private static double getFiddledDistance(long zoom, int xShiftedFlag, int zShiftedFlag, double magicXFlagged, double magicZFlagged) {
        long congruent = LinearCongruentialGenerator.next(zoom, xShiftedFlag);
        congruent = LinearCongruentialGenerator.next(congruent);
        congruent = LinearCongruentialGenerator.next(congruent, zShiftedFlag);
        congruent = LinearCongruentialGenerator.next(congruent, xShiftedFlag);
        congruent = LinearCongruentialGenerator.next(congruent);
        congruent = LinearCongruentialGenerator.next(congruent, zShiftedFlag);
        double xCongruent = getFiddle(congruent);
        congruent = LinearCongruentialGenerator.next(congruent, zoom);
        double yCongruent = getFiddle(congruent);
        congruent = LinearCongruentialGenerator.next(congruent, zoom);
        double zCongruent = getFiddle(congruent);
        double zResult = magicZFlagged + zCongruent;
        double xResult = magicXFlagged + xCongruent;
        return (zResult * zResult) + (yCongruent * yCongruent) + (xResult * xResult);
    }

    private static double getFiddle(long l) {
        double d = Math.floorMod(l >> 24, 1024) / 1024.0d;
        return (d - 0.5d) * 0.9d;
    }

    private static class LinearCongruentialGenerator {
        private static final long MULTIPLIER = 6364136223846793005L;
        private static final long INCREMENT = 1442695040888963407L;

        public static long next(long l, long m) {
            l *= l * MULTIPLIER + INCREMENT;
            return l + m;
        }

        public static long next(long l) {
            l *= l * MULTIPLIER + INCREMENT;
            return l;
        }
    }
}
