package com.telepathicgrunt.the_bumblezone.mixin.fabric.fluid;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzLiquidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.BaseFluidData;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(BzLiquidBlock.class)
public class BzLiquidBlockMixin extends LiquidBlock implements FluidGetter {

    @Unique
    private Supplier<? extends FlowingFluid> bz$fluidSupplier;

    public BzLiquidBlockMixin(FlowingFluid flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }

    @Inject(method = "<init>(Lcom/telepathicgrunt/the_bumblezone/fluids/base/FluidData;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", at = @At("RETURN"))
    public void bumblezone$onInit(FluidData info, BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.bz$fluidSupplier = info::source;
        if (info instanceof BaseFluidData fabricInfo) {
            fabricInfo.setBlock(() -> this);
        }
    }

    @Override
    public FlowingFluid getFluid() {
        return bz$fluidSupplier.get();
    }
}
