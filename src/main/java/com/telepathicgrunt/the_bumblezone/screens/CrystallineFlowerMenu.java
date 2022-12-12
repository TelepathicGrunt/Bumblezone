package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.EntityBlock;

import java.util.Comparator;
import java.util.List;

public class CrystallineFlowerMenu extends AbstractContainerMenu {
    public static final int CONSUME_SLOT = 0;
    private static final int BOOK_SLOT = 1;
    private static final int ENCHANTED_SLOT = 2;

    public static final int CONSUME_SLOT_X = 47;
    public static final int CONSUME_SLOT_Y = 80;
    private static final int BOOK_SLOT_X = 92;
    private static final int BOOK_SLOT_Y = 28;
    private static final int ENCHANTED_SLOT_X = 136;
    private static final int ENCHANTED_SLOT_Y = 28;

    private final ContainerLevelAccess access;
    private final Player player;
    public final CrystallineFlowerBlockEntity crystallineFlowerBlockEntity;
    final Slot consumeSlot;
    final Slot bookSlot;
    private final Slot enchantedSlot;

    final DataSlot selectedEnchantmentIndex = DataSlot.standalone();
    final DataSlot searchTreasure = DataSlot.standalone();
    final DataSlot searchLevel = DataSlot.standalone();
    final DataSlot xpBarPercent = DataSlot.standalone();
    final DataSlot xpTier = DataSlot.standalone();
    final DataSlot tierCost = DataSlot.standalone();
    final DataSlot bottomBlockPosX = DataSlot.standalone();
    final DataSlot bottomBlockPosY = DataSlot.standalone();
    final DataSlot bottomBlockPosZ = DataSlot.standalone();
    final DataSlot playerHasXPForTier = DataSlot.standalone();
    final DataSlot consumeSlotFullyObstructed = DataSlot.standalone();
    final DataSlot tooManyEnchantmentsOnInput = DataSlot.standalone();
    private final Container inputContainer = new SimpleContainer(3) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            slotsChanged(this);
        }
    };
    long lastSoundTime;

    public CrystallineFlowerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, 0, 0, null);
    }

    public CrystallineFlowerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, int searchLevel, int searchTreasure, CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
        super(BzMenuTypes.CRYSTALLINE_FLOWER.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.crystallineFlowerBlockEntity = crystallineFlowerBlockEntity;
        this.consumeSlot = addSlot(new Slot(inputContainer, CONSUME_SLOT, CONSUME_SLOT_X, CONSUME_SLOT_Y) {
            public boolean mayPlace(ItemStack itemStack) {
                if (!BzGeneralConfigs.crystallineFlowerConsumeItemUI.get()) {
                    return false;
                }
                if (itemStack.is(BzTags.CANNOT_CONSUMED_ITEMS)) {
                    return false;
                }
                else if (!itemStack.getItem().canFitInsideContainerItems()) {
                    return false;
                }
                else if (itemStack.getItem() instanceof BlockItem blockItem &&
                        blockItem.getBlock() instanceof EntityBlock entityBlock &&
                        entityBlock.newBlockEntity(BlockPos.ZERO, blockItem.getBlock().defaultBlockState()) instanceof Container)
                {
                    return false;
                }
                return true;
            }

            public void setChanged() {
                this.container.setChanged();
                consumeSlotFullyObstructed();
            }
        });
        this.bookSlot = addSlot(new Slot(inputContainer, BOOK_SLOT, BOOK_SLOT_X, BOOK_SLOT_Y) {
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(BzTags.CAN_BE_ENCHANTED_ITEMS);
            }

            public void setChanged() {
                this.container.setChanged();
                int existingEnchantments = 0;
                ItemStack itemStack = bookSlot.getItem();

                if (!itemStack.isEmpty()) {
                    existingEnchantments = EnchantmentHelper.getEnchantments(itemStack).size();
                }

                if (existingEnchantments >= 3) {
                    tooManyEnchantmentsOnInput.set(1);
                }
                else {
                    tooManyEnchantmentsOnInput.set(0);
                }

                broadcastChanges();
            }
        });
        this.enchantedSlot = addSlot(new Slot(inputContainer, ENCHANTED_SLOT, ENCHANTED_SLOT_X, ENCHANTED_SLOT_Y) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            public void onTake(Player player, ItemStack itemStack) {
                selectedEnchantmentIndex.set(-1);
                bookSlot.remove(1);

                access.execute((soundLevel, pos) -> {
                    long gameTime = soundLevel.getGameTime();
                    if (lastSoundTime != gameTime) {
                        soundLevel.playSound(null, pos, BzSounds.CRYSTALLINE_FLOWER_USE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        lastSoundTime = gameTime;
                    }

                });

                drainFlowerXPLevel(tierCost.get());
                super.onTake(player, itemStack);
            }
        });

        int playerInvYOffset = 115;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, playerInvYOffset + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, playerInvYOffset + 58));
        }

        this.searchTreasure.set(searchTreasure);
        this.searchLevel.set(searchLevel);
        this.selectedEnchantmentIndex.set(-1);
        this.xpBarPercent.set(0);
        this.xpTier.set(0);
        this.tierCost.set(0);
        this.playerHasXPForTier.set(0);
        this.consumeSlotFullyObstructed.set(0);
        this.tooManyEnchantmentsOnInput.set(0);
        this.bottomBlockPosX.set(0);
        this.bottomBlockPosY.set(0);
        this.bottomBlockPosZ.set(0);
        if (this.crystallineFlowerBlockEntity != null) {
            this.bottomBlockPosX.set(this.crystallineFlowerBlockEntity.getBlockPos().getX());
            this.bottomBlockPosY.set(this.crystallineFlowerBlockEntity.getBlockPos().getY());
            this.bottomBlockPosZ.set(this.crystallineFlowerBlockEntity.getBlockPos().getZ());
        }

        syncXpTier();

        addDataSlot(this.selectedEnchantmentIndex);
        addDataSlot(this.searchTreasure);
        addDataSlot(this.searchLevel);
        addDataSlot(this.xpBarPercent);
        addDataSlot(this.xpTier);
        addDataSlot(this.tierCost);
        addDataSlot(this.playerHasXPForTier);
        addDataSlot(this.consumeSlotFullyObstructed);
        addDataSlot(this.tooManyEnchantmentsOnInput);
        addDataSlot(this.bottomBlockPosX);
        addDataSlot(this.bottomBlockPosY);
        addDataSlot(this.bottomBlockPosZ);
    }

    private void syncXpTier() {
        if (this.crystallineFlowerBlockEntity != null) {
            int currentXP = this.crystallineFlowerBlockEntity.getCurrentXp();
            int maxXPForCurrentTier = this.crystallineFlowerBlockEntity.getMaxXpForTier(this.crystallineFlowerBlockEntity.getXpTier());
            xpBarPercent.set((int) ((currentXP / ((float)maxXPForCurrentTier)) * 100));
            xpTier.set(this.crystallineFlowerBlockEntity.getXpTier());


            if (player.getAbilities().instabuild) {
                playerHasXPForTier.set(Math.min(7 - this.crystallineFlowerBlockEntity.getXpTier(), 3));
            }
            else {
                int tierAbleToBeBought = 0;
                int totalXPRequires = 0;
                int playerXP = EnchantmentUtils.getPlayerXP(player);
                for (int i = 0; i < 3; i++) {
                    if (this.crystallineFlowerBlockEntity.getXpTier() + i < 7) {
                        totalXPRequires += this.crystallineFlowerBlockEntity.getMaxXpForTier(this.crystallineFlowerBlockEntity.getXpTier() + i);
                        if (i == 0) {
                            totalXPRequires -= currentXP;
                        }

                        if (totalXPRequires <= playerXP) {
                            tierAbleToBeBought++;
                        }
                    }
                }
                playerHasXPForTier.set(tierAbleToBeBought);
            }
            broadcastChanges();
        }
    }

    public void slotsChanged(Container inventory) {
        ItemStack bookSlotItem = bookSlot.getItem();

        if (bookSlotItem.isEmpty()) {
            if (enchantedSlot.hasItem()) {
                enchantedSlot.set(ItemStack.EMPTY);
            }
            searchLevel.set(0);
            searchTreasure.set(0);
            selectedEnchantmentIndex.set(-1);
        }

        if (selectedEnchantmentIndex.get() != -1) {
            setupResultSlot(selectedEnchantmentIndex.get());
        }
        else if (enchantedSlot.hasItem()) {
            enchantedSlot.set(ItemStack.EMPTY);
        }

        broadcastChanges();
    }

    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0) {
            selectedEnchantmentIndex.set(id);
            setupResultSlot(selectedEnchantmentIndex.get());
            return true;
        }
        // drain xp 1
        else if (id == -2) {
            drainPlayerXPLevel(1);
            return true;
        }
        // drain xp 2
        else if (id == -3) {
            drainPlayerXPLevel(2);
            return true;
        }
        // drain xp 3
        else if (id == -4) {
            drainPlayerXPLevel(3);
            return true;
        }
        // confirm consume
        else if (id == -5) {
            consumeItem();
            return true;
        }
        else {
            return false;
        }
    }

    private void consumeItem() {
        if (consumeSlot.hasItem() &&
            crystallineFlowerBlockEntity != null &&
            !crystallineFlowerBlockEntity.isMaxTier())
        {
            int tiersToMax = 7 - crystallineFlowerBlockEntity.getXpTier();
            int topBlock = CrystallineFlower.flowerHeightAbove(player.level, crystallineFlowerBlockEntity.getBlockPos());
            List<Boolean> obstructedAbove = CrystallineFlower.getObstructions(tiersToMax, player.level, crystallineFlowerBlockEntity.getBlockPos().above(topBlock + 1));

            int xpPerCount = CrystallineFlower.getXPPerItem(consumeSlot.getItem());
            int itemCount = consumeSlot.getItem().getCount();
            int xpForStack = itemCount * xpPerCount;

            int xpToHighestAvailableTier = CrystallineFlower.getXpToHighestAvailableTier(crystallineFlowerBlockEntity, tiersToMax, obstructedAbove);
            int xpGranted = Math.min(xpToHighestAvailableTier, xpForStack);
            int consumedItemCount = (int) Math.ceil(xpGranted / (float)xpPerCount);
            if (consumedItemCount == 0) {
                return;
            }

            crystallineFlowerBlockEntity.addXpAndTier(xpGranted);
            consumeSlot.remove(consumedItemCount);
            consumeSlotFullyObstructed();

            if(tiersToMax > 0 && crystallineFlowerBlockEntity.isMaxTier() && player instanceof ServerPlayer serverPlayer) {
                BzCriterias.GROW_CRYSTALLINE_FLOWER_TRIGGER.trigger(serverPlayer);
            }
            syncXpTier();
        }
    }

    public void consumeSlotFullyObstructed() {
        boolean fullyObstructed = false;
        if (consumeSlot.hasItem() &&
            crystallineFlowerBlockEntity != null)
        {
            if (!crystallineFlowerBlockEntity.isMaxTier()) {
                int topBlock = CrystallineFlower.flowerHeightAbove(player.level, crystallineFlowerBlockEntity.getBlockPos());
                List<Boolean> obstructedAbove = CrystallineFlower.getObstructions(1, player.level, crystallineFlowerBlockEntity.getBlockPos().above(topBlock + 1));

                if (!obstructedAbove.isEmpty() && obstructedAbove.get(0)) {
                    int xpPerCount = CrystallineFlower.getXPPerItem(consumeSlot.getItem());
                    int xpToMaxTier = crystallineFlowerBlockEntity.getXpForNextTiers(1) - 1;
                    int itemsConsumable = xpToMaxTier / xpPerCount;
                    fullyObstructed = itemsConsumable == 0;
                }
            }
            else {
                fullyObstructed = true;
            }

            if (fullyObstructed) {
                consumeSlotFullyObstructed.set(1);
            }
            else {
                consumeSlotFullyObstructed.set(0);
            }

            broadcastChanges();
        }

    }

    private void drainPlayerXPLevel(int desiredTierUpgrade) {
        if (crystallineFlowerBlockEntity != null && !crystallineFlowerBlockEntity.isMaxTier()) {
            List<Boolean> obstructions = CrystallineFlower.getObstructions(
                    desiredTierUpgrade,
                    crystallineFlowerBlockEntity.getLevel(),
                    crystallineFlowerBlockEntity.getBlockPos().above(crystallineFlowerBlockEntity.getXpTier()));

            int freeTierSpot = 0;
            for (boolean isSpotObstructed : obstructions) {
                if (isSpotObstructed) {
                    break;
                }
                else {
                    freeTierSpot++;
                }
            }

            int xpRequested = crystallineFlowerBlockEntity.getXpForNextTiers(freeTierSpot);
            int xpObtained;
            if (!player.getAbilities().instabuild) {
                xpObtained = Math.min(EnchantmentUtils.getPlayerXP(player), xpRequested);;
                player.giveExperiencePoints(-xpRequested);
            }
            else {
                xpObtained = xpRequested;
            }
            crystallineFlowerBlockEntity.addXpAndTier(xpObtained);
            consumeSlotFullyObstructed();
            syncXpTier();

            if(desiredTierUpgrade > 0 && crystallineFlowerBlockEntity.isMaxTier() && player instanceof ServerPlayer serverPlayer) {
                BzCriterias.GROW_CRYSTALLINE_FLOWER_TRIGGER.trigger(serverPlayer);
            }
        }
    }

    private void drainFlowerXPLevel(int levelToConsume) {
        if (crystallineFlowerBlockEntity != null && !crystallineFlowerBlockEntity.isMinTier()) {
            crystallineFlowerBlockEntity.decreaseTier(levelToConsume);
            consumeSlotFullyObstructed();
            syncXpTier();

            if(levelToConsume >= 5 && player instanceof ServerPlayer serverPlayer) {
                BzCriterias.ENCHANT_CRYSTALLINE_FLOWER_TRIGGER.trigger(serverPlayer);
            }
        }
        else if (xpTier.get() > 1) {
            xpTier.set(Math.max(1, xpTier.get() - levelToConsume));
        }
    }

    @Override
    protected void clearContainer(Player player, Container container) {
        enchantedSlot.container.removeItemNoUpdate(enchantedSlot.index);
        super.clearContainer(player, container);
    }

    /**
     * Called when the container is closed.
     */
    public void removed(Player player) {
        super.removed(player);
        access.execute((level, blockPos) -> clearContainer(player, inputContainer));
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player player) {
        return stillValid(access, player, BzBlocks.CRYSTALLINE_FLOWER.get());
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemStack = itemstack1.copy();
            if (index == enchantedSlot.index) {
                if (!moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemStack);
            }
            else if (index != consumeSlot.index && index != bookSlot.index) {
                if (!itemStack.is(BzTags.CAN_BE_ENCHANTED_ITEMS) && !moveItemStackTo(itemstack1, consumeSlot.index, consumeSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
                else if (itemStack.is(BzTags.CAN_BE_ENCHANTED_ITEMS) && !moveItemStackTo(itemstack1, bookSlot.index, bookSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
                else if (index >= 3 && index < 30 && !moveItemStackTo(itemstack1, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
                else if (index >= 30 && index < 39 && !moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemStack;
    }

    private void setupResultSlot(int selectedEnchantment) {
        if (selectedEnchantmentIndex.get() == -1 || tooManyEnchantmentsOnInput.get() == 1) {
            if (enchantedSlot.hasItem()) {
                enchantedSlot.set(ItemStack.EMPTY);
            }
            return;
        }
        else if (xpTier.get() <= 1) {
            selectedEnchantmentIndex.set(-1);
            broadcastChanges();
            if (enchantedSlot.hasItem()) {
                enchantedSlot.set(ItemStack.EMPTY);
            }
            return;
        }

        ItemStack toEnchant = bookSlot.getItem();
        if (!toEnchant.isEmpty()) {
            ItemStack tempCopy = toEnchant.copy();
            tempCopy.setCount(1);

            int level = xpTier.get() * BzGeneralConfigs.crystallineFlowerEnchantingPowerAllowedPerTier.get();
            List<EnchantmentInstance> availableEnchantments = EnchantmentUtils.allAllowedEnchantsWithoutMaxLimit(level, tempCopy, xpTier.get() == 7);
            if (availableEnchantments.size() == 0 && enchantedSlot.hasItem()) {
                enchantedSlot.container.removeItemNoUpdate(enchantedSlot.index);
                selectedEnchantmentIndex.set(-1);
                return;
            }

            availableEnchantments.removeIf(e -> xpTier.get() <= EnchantmentUtils.getEnchantmentTierCost(e));
            availableEnchantments.sort(
                    Comparator.comparing((EnchantmentInstance a) -> BuiltInRegistries.ENCHANTMENT.getResourceKey(a.enchantment).get())
                    .thenComparingInt(a -> a.level));
            if (availableEnchantments.size() > selectedEnchantment) {
                EnchantmentInstance enchantmentForItem = availableEnchantments.get(selectedEnchantment);

                if (tempCopy.is(Items.BOOK)) {
                    ItemStack enchantedBook = Items.ENCHANTED_BOOK.getDefaultInstance();
                    enchantedBook.setCount(1);

                    CompoundTag compoundtag = tempCopy.getTag();
                    if (compoundtag != null) {
                        enchantedBook.setTag(compoundtag.copy());
                    }
                    tempCopy = enchantedBook;
                }

                if (tempCopy.is(Items.BOOK) || tempCopy.is(Items.ENCHANTED_BOOK)) {
                    EnchantedBookItem.addEnchantment(tempCopy, enchantmentForItem);
                }
                else {
                    tempCopy.enchant(enchantmentForItem.enchantment, enchantmentForItem.level);
                }

                if (!ItemStack.matches(tempCopy, enchantedSlot.getItem())) {
                    enchantedSlot.set(tempCopy);
                    tierCost.set(EnchantmentUtils.getEnchantmentTierCost(enchantmentForItem));
                }
            }
        }
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return super.canTakeItemForPickAll(stack, slot);
    }
}