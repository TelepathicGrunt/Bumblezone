package com.telepathicgrunt.the_bumblezone.fluids.base;

import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BzLiquidBlock extends LiquidBlock implements FluidGetter {

    protected BzLiquidBlock(FluidInfo info, Properties properties) {
        super("forge".equals(ArchitecturyTarget.getCurrentTarget()) ? Fluids.FLOWING_WATER : info.source(), properties);
        // Rest of the code happens in the mixins
    }
}
