package com.telepathicgrunt.the_bumblezone.mixins.forge;

import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.forge.ForgeFluidInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(BzBucketItem.class)
public class BzBucketItemMixin extends BucketItem {

    @Unique
    private Supplier<? extends Fluid> bz$fluidSupplier;

    public BzBucketItemMixin(Fluid arg, Properties arg2) {
        super(arg, arg2);
    }

    @Inject(method = "<init>(Lcom/telepathicgrunt/the_bumblezone/fluids/base/FluidInfo;Lnet/minecraft/world/item/Item$Properties;)V", at = @At("RETURN"))
    public void bumblezone$onInit(FluidInfo info, Properties properties, CallbackInfo ci) {
        this.bz$fluidSupplier = info::source;
    }

    @NotNull
    @Override
    public Fluid getFluid() {
        return bz$fluidSupplier.get();
    }

    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}
