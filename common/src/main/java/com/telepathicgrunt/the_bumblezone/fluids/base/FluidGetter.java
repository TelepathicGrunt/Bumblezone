package com.telepathicgrunt.the_bumblezone.fluids.base;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public interface FluidGetter {

    default FlowingFluid getFlowingFluid() {
        throw new IllegalStateException("This should be overridden by a mixin!");
    }

    default Fluid getFluid() {
        throw new IllegalStateException("This should be overridden by a mixin!");
    }
}
