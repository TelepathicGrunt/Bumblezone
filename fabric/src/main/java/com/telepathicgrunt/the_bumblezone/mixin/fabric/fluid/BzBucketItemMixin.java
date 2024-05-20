package com.telepathicgrunt.the_bumblezone.mixin.fabric.fluid;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzBucketItem.class)
public class BzBucketItemMixin implements FluidGetter {

    @Final
    @Shadow
    protected FluidData info;

    @Override
    public FlowingFluid getFluid() {
        return this.info.still().get();
    }
}
