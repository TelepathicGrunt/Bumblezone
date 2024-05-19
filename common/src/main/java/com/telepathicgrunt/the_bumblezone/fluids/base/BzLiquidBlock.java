package com.telepathicgrunt.the_bumblezone.fluids.base;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluids;

public class BzLiquidBlock extends LiquidBlock implements FluidGetter {

    protected BzLiquidBlock(FluidData info, Properties properties) {
        super("neoforge".equals(ArchitecturyTarget.getCurrentTarget()) ? Fluids.FLOWING_WATER : info.still().get(), properties);
        // Rest of the code happens in the mixins
    }
}
