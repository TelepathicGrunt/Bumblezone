package com.telepathicgrunt.the_bumblezone.mixins.forge;

import com.telepathicgrunt.the_bumblezone.fluids.base.BzLiquidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.forge.ForgeFluidInfo;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(BzLiquidBlock.class)
public class BzLiquidBlockMixin extends LiquidBlock implements FluidGetter {

    @Unique
    private List<FluidState> bz$stateCache = null;

    @Unique
    private Supplier<? extends FlowingFluid> bz$fluidSupplier;

    public BzLiquidBlockMixin(FlowingFluid arg, Properties arg2) {
        super(arg, arg2);
    }

    @Inject(method = "<init>(Lcom/telepathicgrunt/the_bumblezone/fluids/base/FluidInfo;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", at = @At("RETURN"))
    public void bumblezone$onInit(FluidInfo info, BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.bz$fluidSupplier = info::source;
        if (info instanceof ForgeFluidInfo forgeInfo) {
            forgeInfo.setBlock(() -> this);
        }
    }

    @NotNull
    @Override
    public FlowingFluid getFluid() {
        return bz$fluidSupplier.get();
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState arg) {
        int i = arg.getValue(LEVEL);
        if (bz$stateCache == null) {
            this.initFluidStateCache();
        }

        return this.bz$stateCache.get(Math.min(i, 8));
    }

    protected synchronized void initFluidStateCache() {
        if (bz$stateCache == null) {
            bz$stateCache = new ArrayList<>();
            this.bz$stateCache.add(this.getFluid().getSource(false));

            for(int i = 1; i < 8; ++i) {
                this.bz$stateCache.add(this.getFluid().getFlowing(8 - i, false));
            }

            this.bz$stateCache.add(this.getFluid().getFlowing(8, true));
        }
    }
}
