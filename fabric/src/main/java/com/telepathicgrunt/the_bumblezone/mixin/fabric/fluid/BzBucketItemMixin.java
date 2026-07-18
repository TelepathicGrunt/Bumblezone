package com.telepathicgrunt.the_bumblezone.mixin.fabric.fluid;

import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzBucketItem.class)
public class BzBucketItemMixin implements FluidGetter {

    @Final
    @Shadow
    protected FluidInfo info;

    @Override
    public FlowingFluid getFlowingFluid() {
        return this.info.source();
    }

    @Override
    public Fluid getFluid() {
        return this.info.source();
    }
}
