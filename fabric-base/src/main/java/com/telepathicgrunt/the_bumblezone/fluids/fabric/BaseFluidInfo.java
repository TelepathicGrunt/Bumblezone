package com.telepathicgrunt.the_bumblezone.fluids.fabric;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class BaseFluidInfo implements FluidInfo {

    private Supplier<? extends FlowingFluid> stillFluid;
    private Supplier<? extends FlowingFluid> flowingFluid;
    private Supplier<? extends BucketItem> bucket;
    private Supplier<? extends LiquidBlock> block;
    private final FluidProperties properties;

    public BaseFluidInfo(FluidProperties properties) {
        this.properties = properties;
    }

    @Override
    public FluidProperties properties() {
        return properties;
    }

    @Override
    public FlowingFluid flowing() {
        return flowingFluid.get();
    }

    @Override
    public void setFlowing(Supplier<? extends FlowingFluid> source) {
        this.flowingFluid = source;
    }

    @Override
    public FlowingFluid source() {
        return stillFluid.get();
    }

    @Override
    public void setSource(Supplier<? extends FlowingFluid> source) {
        this.stillFluid = source;
    }

    @Override
    public BucketItem bucket() {
        return bucket.get();
    }

    @Override
    public void setBucket(Supplier<? extends BucketItem> bucket) {
        this.bucket = bucket;
    }

    @Override
    public LiquidBlock block() {
        return block.get();
    }

    @Override
    public void setBlock(Supplier<? extends LiquidBlock> block) {
        this.block = block;
    }
}
