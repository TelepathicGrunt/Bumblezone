package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
    void callSpreadToSides(LevelAccessor levelAccessor, BlockPos pos, FluidState fluidState, BlockState blockState);

    @Invoker("sourceNeighborCount")
    int callSourceNeighborCount(LevelReader levelReader, BlockPos pos);

    @Invoker("canPassThroughWall")
    boolean callCanPassThroughWall(Direction direction, BlockGetter blockGetter, BlockPos pos, BlockState blockState, BlockPos pos1, BlockState blockState1);
}
