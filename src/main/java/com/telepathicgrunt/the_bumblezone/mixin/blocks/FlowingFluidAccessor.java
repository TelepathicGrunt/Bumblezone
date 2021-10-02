package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FlowingFluid.class)
public interface FlowingFluidAccessor {

    @Invoker("spreadToSides")
    void thebumblezone_callSpreadToSides(IWorld p_207937_1_, BlockPos p_207937_2_, FluidState p_207937_3_, BlockState p_207937_4_);

    @Invoker("sourceNeighborCount")
    int thebumblezone_callSourceNeighborCount(IWorldReader p_207936_1_, BlockPos p_207936_2_);

    @Invoker("canPassThroughWall")
    boolean thebumblezone_callCanPassThroughWall(Direction p_212751_1_, IBlockReader p_212751_2_, BlockPos p_212751_3_, BlockState p_212751_4_, BlockPos p_212751_5_, BlockState p_212751_6_);
}
