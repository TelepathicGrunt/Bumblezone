package com.telepathicgrunt.the_bumblezone.fluids.base;

import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.world.level.block.LiquidBlock;

public class BzLiquidBlock extends LiquidBlock implements FluidGetter {

    protected BzLiquidBlock(FluidInfo info, Properties properties) {
        super(PlatformService.INSTANCE.getFlowingFluid(info), properties);
        // Rest of the code happens in the mixins
    }
}
