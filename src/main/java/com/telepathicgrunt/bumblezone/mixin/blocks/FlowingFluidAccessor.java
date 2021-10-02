package com.telepathicgrunt.bumblezone.mixin.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FlowableFluid.class)
public interface FlowingFluidAccessor {

    @Invoker("method_15744")
    void thebumblezone_callSpreadToSides(WorldAccess p_207937_1_, BlockPos p_207937_2_, FluidState p_207937_3_, BlockState p_207937_4_);

    @Invoker("method_15740")
    int thebumblezone_callSourceNeighborCount(WorldView p_207936_1_, BlockPos p_207936_2_);

    @Invoker("receivesFlow")
    boolean thebumblezone_callCanPassThroughWall(Direction p_212751_1_, BlockView p_212751_2_, BlockPos p_212751_3_, BlockState p_212751_4_, BlockPos p_212751_5_, BlockState p_212751_6_);
}
