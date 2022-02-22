package com.telepathicgrunt.the_bumblezone.utils;

public class WorldSeedHolder {
    private static long SEED = 0;

    public static long getSeed() {
        return SEED;
    }

    public static long setSeed(long seed) {
        SEED = seed;
        return seed;
    }
}
