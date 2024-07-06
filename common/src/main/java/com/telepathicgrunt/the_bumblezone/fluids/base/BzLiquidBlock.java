package com.telepathicgrunt.the_bumblezone.fluids.base;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.minecraft.world.level.block.LiquidBlock;

public class BzLiquidBlock extends LiquidBlock implements FluidGetter {

    protected BzLiquidBlock(FluidData info, Properties properties) {
        super(info.still().get(), properties);
    }
}
