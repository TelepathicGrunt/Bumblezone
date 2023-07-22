package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BiomeManager.class)
public interface BiomeManagerAccessor {
    @Accessor("noiseBiomeSource")
    BiomeManager.NoiseBiomeSource getNoiseBiomeSource();

    @Accessor("biomeZoomSeed")
    long getBiomeZoomSeed();

    @Invoker("getFiddledDistance")
    static double callGetFiddledDistance(long l, int i, int j, int k, double d, double e, double f) {
        throw new UnsupportedOperationException();
    }
}
