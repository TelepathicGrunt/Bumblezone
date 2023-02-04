package com.telepathicgrunt.the_bumblezone.fluids.base;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public interface FluidInfo {

    FluidProperties properties();

    FlowingFluid flowing();

    void setFlowing(Supplier<? extends FlowingFluid> source);

    FlowingFluid source();

    void setSource(Supplier<? extends FlowingFluid> source);

    BucketItem bucket();

    void setBucket(Supplier<? extends BucketItem> block);

    LiquidBlock block();

    void setBlock(Supplier<? extends LiquidBlock> block);
}
