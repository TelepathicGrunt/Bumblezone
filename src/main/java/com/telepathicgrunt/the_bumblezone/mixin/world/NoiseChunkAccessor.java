package com.telepathicgrunt.the_bumblezone.mixin.world;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
    @Invoker("getInterpolatedState")
    BlockState callGetInterpolatedState();

    @Accessor("blockStateRule")
    NoiseChunk.BlockStateFiller getBlockStateRule();
}
