package com.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FlowingFluid.class)
public interface FlowingFluidAccessor {

    @Invoker("spreadToSides")
    void thebumblezone_callSpreadToSides(LevelAccessor p_207937_1_, BlockPos p_207937_2_, FluidState p_207937_3_, BlockState p_207937_4_);

    @Invoker("sourceNeighborCount")
    int thebumblezone_callSourceNeighborCount(LevelReader p_207936_1_, BlockPos p_207936_2_);

    @Invoker("canPassThroughWall")
    boolean thebumblezone_callCanPassThroughWall(Direction p_212751_1_, BlockGetter p_212751_2_, BlockPos p_212751_3_, BlockState p_212751_4_, BlockPos p_212751_5_, BlockState p_212751_6_);
}
