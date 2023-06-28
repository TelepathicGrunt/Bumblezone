package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyCocoon;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.stream.IntStream;

public class HoneyCocoonBlockEntity extends BzRandomizableContainerBlockEntity {
    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(18, ItemStack.EMPTY);
    private UUID blockEntityUuid = null;

    protected HoneyCocoonBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public HoneyCocoonBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.HONEY_COCOON.get(), blockPos, blockState);
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
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.loadFromTag(tag);
    }

    public void loadFromTag(CompoundTag compoundTag) {
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag) && compoundTag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(compoundTag, this.itemStacks);
        }
        if (compoundTag.hasUUID("blockEntityUuid")) {
            this.blockEntityUuid = compoundTag.getUUID("blockEntityUuid");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.itemStacks);
        }
        tag.putUUID("blockEntityUuid", this.getBlockEntityUuid());
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            if (honeyCocoonBlockEntity.getLootTable() != null && blockState.getValue(HoneyCocoon.IS_LOOT_CONTAINER) != ModChecker.lootrPresent) {
                serverLevel.setBlock(blockPos, blockState.setValue(HoneyCocoon.IS_LOOT_CONTAINER, ModChecker.lootrPresent), 2);
            }
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        if (isLootWithLootrOn()) {
            return NonNullList.create();
        }
        return this.itemStacks;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> itemStacks) {
        if (isLootWithLootrOn()) {
            return;
        }
        this.itemStacks = itemStacks;
    }

    private boolean isLootWithLootrOn() {
        return this.getLootTable() != null && this.getBlockState().getValue(HoneyCocoon.IS_LOOT_CONTAINER) && ModChecker.lootrPresent;
    }

    @Override
    protected AbstractContainerMenu createMenu(int slot, @NotNull Inventory inventory) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x2.get(), slot, inventory, this, this.getContainerSize() / 9);
    }

    @Override
    public int[] getSlotsForFace(@NotNull Direction direction) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public Direction getInputDirection() {
        return Direction.UP;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack stack, Direction direction) {
        if (isLootWithLootrOn()) {
            return false;
        }
        return super.canPlaceItemThroughFace(i, stack, direction);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack stack, @NotNull Direction direction) {
        if (isLootWithLootrOn()) {
            return false;
        }
        return super.canTakeItemThroughFace(i, stack, direction);
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
        if (ModChecker.lootrPresent) {
            return;
        }

        super.unpackLootTable(player);

        if (this.level != null) {
            BlockState blockState = this.level.getBlockState(this.worldPosition);
            if (blockState.is(BzBlocks.HONEY_COCOON.get()) && blockState.getValue(HoneyCocoon.WATERLOGGED)) {
                this.level.scheduleTick(this.worldPosition, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(this.level));
                this.level.scheduleTick(this.worldPosition, blockState.getBlock(), HoneyCocoon.waterDropDelay);
            }
        }
    }

    public UUID getBlockEntityUuid () {
        if (this.blockEntityUuid == null) {
            this.blockEntityUuid = UUID.randomUUID();
        }

        return this.blockEntityUuid;
    }

    @Nullable
    public ResourceLocation getLootTable () {
        return this.lootTable;
    }

    public long getLootSeed () {
        return this.lootTableSeed;
    }
}
