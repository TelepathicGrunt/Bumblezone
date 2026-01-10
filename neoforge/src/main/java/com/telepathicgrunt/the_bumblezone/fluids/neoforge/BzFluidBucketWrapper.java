package com.telepathicgrunt.the_bumblezone.fluids.neoforge;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.CauldronFluidContent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;

public class BzFluidBucketWrapper extends SnapshotJournal<ItemStack> implements ResourceHandler<FluidResource> {
    protected ItemStack container;
    protected ItemStack snapshotContainer;
    private final ItemAccess itemAccess;

    public BzFluidBucketWrapper(ItemStack container, ItemAccess itemAccess) {
        this.container = container;
        this.itemAccess = itemAccess;
    }

    public FluidStack getFluid() {
        Item item = container.getItem();
        if (item instanceof BucketItem) {
            return new FluidStack(((BucketItem) item).content, FluidType.BUCKET_VOLUME);
        }
        else {
            return FluidStack.EMPTY;
        }
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        Objects.checkIndex(index, size());
        return FluidResource.of(this.getFluid());
    }

    @Override
    public long getAmountAsLong(int index) {
        Objects.checkIndex(index, size());
        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmpty(resource);
        return resource.isComponentsPatchEmpty() && CauldronFluidContent.getForFluid(resource.getFluid()) != null;
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
        if (!resource.is(this.getFluid().getFluidType()) || !resource.isComponentsPatchEmpty()) {
            return 0;
        }
        updateSnapshots(transaction);
        return Math.min(amount, FluidType.BUCKET_VOLUME);
    }

    @Override
    protected ItemStack createSnapshot() {
        snapshotContainer = container;
        return snapshotContainer;
    }

    @Override
    protected void revertToSnapshot(ItemStack snapshot) {
        container = snapshotContainer;
    }
}
