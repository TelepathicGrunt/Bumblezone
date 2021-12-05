package com.telepathicgrunt.bumblezone.world.dimension.layer.vanilla;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public interface Context {
    int nextRandom(int i);

    ImprovedNoise getBiomeNoise();
}
