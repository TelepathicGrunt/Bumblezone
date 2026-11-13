package com.telepathicgrunt.the_bumblezone.fluids.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.neoforge.BzGeneralConfig;
import com.telepathicgrunt.the_bumblezone.items.RoyalJellyBottle;
import com.telepathicgrunt.the_bumblezone.items.SugarWaterBottle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.Objects;

public class BzFluidBottlesWrapper extends ItemAccessResourceHandler<FluidResource> {
    /**
     * The number of fluid units that a bottle represents.
     */
    public static final int BOTTLE_VOLUME = 250;

    protected ItemStack container;
    protected Fluid fluid;

    public BzFluidBottlesWrapper(ItemStack container, Fluid fluid, ItemAccess itemAccess) {
        super(itemAccess, 1);
        this.container = container;
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return new FluidStack(this.fluid, BOTTLE_VOLUME);
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
        if (accessResource.value() instanceof RoyalJellyBottle ||
            accessResource.value() instanceof SugarWaterBottle ||
            (BzGeneralConfig.bzHoneyFluidFromHoneyBottles.get() && accessResource.value() == Items.HONEY_BOTTLE))
        {
            return FluidResource.of(getFluid());
        }
        else {
            return FluidResource.EMPTY;
        }
    }

    @Override
    protected int getAmountFrom(ItemResource accessResource, int index) {
        var resource = getResourceFrom(accessResource, index);
        return resource.isEmpty() ? 0 : BOTTLE_VOLUME;
    }

    @Override
    protected ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
        if (newAmount == 0) {
            return ItemResource.of(Items.GLASS_BOTTLE);
        }
        else if (newAmount != BOTTLE_VOLUME) {
            return ItemResource.EMPTY;
        }
        else {
            return ItemResource.of(container.getItem().getDefaultInstance());
        }
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        return BOTTLE_VOLUME;
    }
}
