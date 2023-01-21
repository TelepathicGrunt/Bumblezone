package com.telepathicgrunt.the_bumblezone.mixins.fabric.items;

import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzBucketItem.class)
public class BzBucketItemMixin implements FluidGetter {

    @Final
    @Shadow
    protected FluidInfo info;

    @Override
    public FlowingFluid getFluid() {
        return this.info.source();
    }
}
