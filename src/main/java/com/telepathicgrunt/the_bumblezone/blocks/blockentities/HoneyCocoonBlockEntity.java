package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyCocoon;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

public class HoneyCocoonBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(18, ItemStack.EMPTY);

    protected HoneyCocoonBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public HoneyCocoonBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.HONEY_COCOON_BE, blockPos, blockState);
    }

    @Override
    public int getContainerSize() {
        return itemStacks.size();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("the_bumblezone.container.honey_cocoon");
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.loadFromTag(compoundTag);
    }

    public void loadFromTag(CompoundTag compoundTag) {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag) && compoundTag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(compoundTag, this.itemStacks);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.itemStacks);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    protected AbstractContainerMenu createMenu(int slot, Inventory inventory) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x2, slot, inventory, this, this.getContainerSize() / 9);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack item, Direction direction) {
        return direction == Direction.UP && item.getItem().canFitInsideContainerItems();
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN;
    }

    @Override
    public boolean triggerEvent(int i, int i1) {
        if (i == 1) {
            return true;
        }
        else {
            return super.triggerEvent(i, i1);
        }
    }

    public boolean isUnpackedLoottable() {
        return this.lootTable == null;
    }

    @Override
    public void unpackLootTable(Player player) {
        super.unpackLootTable(player);

        if (this.level != null) {
            BlockState blockState = this.level.getBlockState(this.worldPosition);
            if (blockState.getValue(HoneyCocoon.WATERLOGGED)) {
                this.level.scheduleTick(this.worldPosition, BzFluids.SUGAR_WATER_FLUID, BzFluids.SUGAR_WATER_FLUID.getTickDelay(this.level));
                this.level.scheduleTick(this.worldPosition, blockState.getBlock(), HoneyCocoon.waterDropDelay);
            }
        }
    }
}
