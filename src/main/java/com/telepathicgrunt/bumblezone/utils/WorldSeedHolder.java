package com.telepathicgrunt.bumblezone.utils;

public class WorldSeedHolder {
    /**
     * World seed for worldgen when not specified by JSON by Haven King
     * https://github.com/Hephaestus-Dev/seedy-behavior/blob/master/src/main/java/dev/hephaestus/seedy/mixin/world/gen/GeneratorOptionsMixin.java
     */
    private static long SEED = 0;

    public static long getSeed() {
        return SEED;
    }

    public static long setSeed(long seed) {
        SEED = seed;
        return seed;
    }
}
