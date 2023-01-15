package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
    @Invoker("getInterpolatedState")
    BlockState callGetInterpolatedState();

    @Invoker("cachedClimateSampler")
    Climate.Sampler callCachedClimateSampler(NoiseRouter noiseRouter, List<Climate.ParameterPoint> points);
}
