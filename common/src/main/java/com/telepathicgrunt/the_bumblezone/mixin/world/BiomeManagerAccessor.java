package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeManager.class)
public interface BiomeManagerAccessor {
    @Accessor("noiseBiomeSource")
    BiomeManager.NoiseBiomeSource bumblezone$getNoiseBiomeSource();

    @Accessor("biomeZoomSeed")
    long bumblezone$getBiomeZoomSeed();
}
