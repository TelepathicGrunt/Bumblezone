package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
    @Invoker("updateNoiseAndGenerateBaseState")
    BlockState thebumblezone_callUpdateNoiseAndGenerateBaseState(int x, int y, int z);

    @Invoker("createNoiseInterpolator")
    NoiseChunk.NoiseInterpolator thebumblezone_callCreateNoiseInterpolator(NoiseChunk.NoiseFiller noiseFiller);
}
