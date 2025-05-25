package com.telepathicgrunt.the_bumblezone.fluids.base;

import net.minecraft.world.level.material.Fluid;

public interface FluidGetter {

    default Fluid getFluid() {
        throw new IllegalStateException("This should be overridden by a mixin!");
    }
}
