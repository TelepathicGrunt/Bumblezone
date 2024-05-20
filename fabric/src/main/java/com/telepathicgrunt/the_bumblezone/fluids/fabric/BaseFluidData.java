package com.telepathicgrunt.the_bumblezone.fluids.fabric;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class BaseFluidData implements FluidData {

    private Supplier<? extends FlowingFluid> stillFluid;
    private Supplier<? extends FlowingFluid> flowingFluid;
    private Supplier<? extends BucketItem> bucket;
    private Supplier<? extends LiquidBlock> block;
    private final FluidProperties properties;

    public BaseFluidData(FluidProperties properties) {
        this.properties = properties;
    }

    @Override
    public FluidProperties properties() {
        return properties;
    }

    @Override
    public Supplier<? extends FlowingFluid> flowing() {
        return flowingFluid;
    }

    @Override
    public void setFlowing(Supplier<? extends FlowingFluid> source) {
        this.flowingFluid = source;
    }

    @Override
    public Supplier<? extends FlowingFluid> still() {
        return stillFluid;
    }

    @Override
    public void setStill(Supplier<? extends FlowingFluid> source) {
        this.stillFluid = source;
    }

    @Override
    public Supplier<? extends Item> bucket() {
        return bucket;
    }

    @Override
    public void setBucket(Supplier<? extends Item> bucket) {
        this.bucket = (Supplier<? extends BucketItem>) bucket;
    }

    @Override
    public Supplier<? extends LiquidBlock> block() {
        return block;
    }

    @Override
    public void setBlock(Supplier<? extends LiquidBlock> block) {
        this.block = block;
    }

    @Override
    public <T> T data() {
        return null;
    }
}
