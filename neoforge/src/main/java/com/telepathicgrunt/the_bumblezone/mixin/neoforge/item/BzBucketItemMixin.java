package com.telepathicgrunt.the_bumblezone.mixin.neoforge.item;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidGetter;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(BzBucketItem.class)
public class BzBucketItemMixin extends BucketItem implements FluidGetter {

    @Unique
    private Supplier<? extends FlowingFluid> bz$fluidSupplier;

    public BzBucketItemMixin(Fluid arg, Properties arg2) {
        super(arg, arg2);
    }

    @Inject(method = "<init>(Lcom/telepathicgrunt/the_bumblezone/fluids/base/FluidData;Lnet/minecraft/world/item/Item$Properties;)V", at = @At("RETURN"))
    public void bumblezone$onInit(FluidData info, Properties properties, CallbackInfo ci) {
        this.bz$fluidSupplier = info.still();
    }

    @NotNull
    @Override
    public FlowingFluid getFluid() {
        return bz$fluidSupplier.get();
    }
}
