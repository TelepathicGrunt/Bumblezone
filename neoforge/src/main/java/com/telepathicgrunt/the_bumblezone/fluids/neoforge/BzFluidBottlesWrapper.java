package com.telepathicgrunt.the_bumblezone.fluids.neoforge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class BzFluidBottlesWrapper implements IFluidHandlerItem {
    /**
     * The number of fluid units that a bottle represents.
     */
    public static final int BOTTLE_VOLUME = 250;

    protected ItemStack container;
    protected Fluid fluid;

    public BzFluidBottlesWrapper(ItemStack container, Fluid fluid) {
        this.container = container;
        this.fluid = fluid;
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }

    public boolean canFillFluidType(FluidStack fluid) {
        return !fluid.getFluidType().getBucket(fluid).isEmpty();
    }

    public FluidStack getFluid() {
        return new FluidStack(this.fluid, BOTTLE_VOLUME);
    }

    protected void setFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            container = new ItemStack(Items.GLASS_BOTTLE);
        }
        else {
            container = new ItemStack(this.container.getItem());
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return BOTTLE_VOLUME;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.getAmount() < BOTTLE_VOLUME || !getFluid().isEmpty() || !canFillFluidType(resource)) {
            return 0;
        }

        if (action.execute()) {
            setFluid(resource);
        }

        return BOTTLE_VOLUME;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.getAmount() < BOTTLE_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return fluidStack;
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (container.getCount() != 1 || maxDrain < BOTTLE_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty()) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return fluidStack;
        }

        return FluidStack.EMPTY;
    }
}
