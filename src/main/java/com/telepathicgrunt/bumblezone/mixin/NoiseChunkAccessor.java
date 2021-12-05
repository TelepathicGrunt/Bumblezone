package com.telepathicgrunt.bumblezone.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
    @Invoker("updateNoiseAndGenerateBaseState")
    BlockState thebumblezone_callUpdateNoiseAndGenerateBaseState(int x, int y, int z);

    @Invoker("oreVeinify")
    BlockState thebumblezone_callOreVeinify(int x, int y, int z);
}
