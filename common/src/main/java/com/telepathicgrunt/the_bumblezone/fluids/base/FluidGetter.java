package com.telepathicgrunt.the_bumblezone.fluids.base;

import net.minecraft.world.level.material.FlowingFluid;

public interface FluidGetter {

    default FlowingFluid getFluid() {
        throw new IllegalStateException("This should be overridden by a mixin!");
    }
}
