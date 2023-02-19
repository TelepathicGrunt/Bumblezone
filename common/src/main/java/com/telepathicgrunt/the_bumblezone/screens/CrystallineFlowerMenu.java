package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.CrystallineFlowerEnchantmentPacket;
import com.telepathicgrunt.the_bumblezone.utils.EnchantmentUtils;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
    final DataSlot xpBarPercent = DataSlot.standalone();
    final DataSlot xpTier = DataSlot.standalone();
    final DataSlot tierCost = DataSlot.standalone();
    final DataSlot bottomBlockPosXUpper = DataSlot.standalone();
    final DataSlot bottomBlockPosXLower = DataSlot.standalone();
    final DataSlot bottomBlockPosYUpper = DataSlot.standalone();
    final DataSlot bottomBlockPosYLower = DataSlot.standalone();
    final DataSlot bottomBlockPosZUpper = DataSlot.standalone();
    final DataSlot bottomBlockPosZLower = DataSlot.standalone();
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
                if (!BzGeneralConfigs.crystallineFlowerConsumeItemUI) {
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

                ItemStack bookSlotItem = bookSlot.getItem();
                if (bookSlotItem.isEmpty()) {
                    selectedEnchantmentIndex.set(-1);
                }

                if (!player.level.isClientSide()) {
                    setupResultSlot();
                    broadcastChanges();
                }
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

                ItemStack bookSlotItem = bookSlot.getItem();
                if (bookSlotItem.isEmpty()) {
                    selectedEnchantmentIndex.set(-1);
                }

                if (!player.level.isClientSide()) {
                    setupResultSlot();
                    broadcastChanges();
                }

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

        this.selectedEnchantmentIndex.set(-1);
        this.xpBarPercent.set(0);
        this.xpTier.set(0);
        this.tierCost.set(0);
        this.playerHasXPForTier.set(0);
        this.consumeSlotFullyObstructed.set(0);
        this.tooManyEnchantmentsOnInput.set(0);
        this.bottomBlockPosXUpper.set(0);
        this.bottomBlockPosXLower.set(0);
        this.bottomBlockPosYUpper.set(0);
        this.bottomBlockPosYLower.set(0);
        this.bottomBlockPosZUpper.set(0);
        this.bottomBlockPosZLower.set(0);
        if (this.crystallineFlowerBlockEntity != null) {
            this.bottomBlockPosXUpper.set(GeneralUtils.split(this.crystallineFlowerBlockEntity.getBlockPos().getX(), true));
            this.bottomBlockPosXLower.set(GeneralUtils.split(this.crystallineFlowerBlockEntity.getBlockPos().getX(), false));
            this.bottomBlockPosYUpper.set(GeneralUtils.split(this.crystallineFlowerBlockEntity.getBlockPos().getY(), true));
            this.bottomBlockPosYLower.set(GeneralUtils.split(this.crystallineFlowerBlockEntity.getBlockPos().getY(), false));
            this.bottomBlockPosZUpper.set(GeneralUtils.split(this.crystallineFlowerBlockEntity.getBlockPos().getZ(), true));
            this.bottomBlockPosZLower.set(GeneralUtils.split(this.crystallineFlowerBlockEntity.getBlockPos().getZ(), false));
        }

        syncXpTier();

        addDataSlot(this.selectedEnchantmentIndex);
        addDataSlot(this.xpBarPercent);
        addDataSlot(this.xpTier);
        addDataSlot(this.tierCost);
        addDataSlot(this.playerHasXPForTier);
        addDataSlot(this.consumeSlotFullyObstructed);
        addDataSlot(this.tooManyEnchantmentsOnInput);
        addDataSlot(this.bottomBlockPosXUpper);
        addDataSlot(this.bottomBlockPosXLower);
        addDataSlot(this.bottomBlockPosYUpper);
        addDataSlot(this.bottomBlockPosYLower);
        addDataSlot(this.bottomBlockPosZUpper);
        addDataSlot(this.bottomBlockPosZLower);
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

    public void slotsChanged(Container inventory) {}

    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0) {
            selectedEnchantmentIndex.set(id);
            if (!player.level.isClientSide()) {
                setupResultSlot();
                broadcastChanges();
            }
            return true;
        }
        // drain xp 1
        else if (id == -2) {
            drainPlayerXPLevel(1);
            if (!player.level.isClientSide()) {
                setupResultSlot();
                broadcastChanges();
            }
            return true;
        }
        // drain xp 2
        else if (id == -3) {
            drainPlayerXPLevel(2);
            if (!player.level.isClientSide()) {
                setupResultSlot();
                broadcastChanges();
            }
            return true;
        }
        // drain xp 3
        else if (id == -4) {
            drainPlayerXPLevel(3);
            if (!player.level.isClientSide()) {
                setupResultSlot();
                broadcastChanges();
            }
            return true;
        }
        // confirm consume
        else if (id == -5) {
            consumeItem();
            if (!player.level.isClientSide()) {
                setupResultSlot();
                broadcastChanges();
            }
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
                xpObtained = Math.min(EnchantmentUtils.getPlayerXP(player), xpRequested);
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

    private void setupResultSlot() {
        ItemStack bookSlotItem = bookSlot.getItem();
        int existingEnchantments;
        if (!bookSlotItem.isEmpty() && xpTier.get() > 1) {
            existingEnchantments = EnchantmentHelper.getEnchantments(bookSlotItem).size();
        }
        else {
            tooManyEnchantmentsOnInput.set(0);
            selectedEnchantmentIndex.set(-1);
            if (enchantedSlot.hasItem()) {
                enchantedSlot.set(ItemStack.EMPTY);
            }
            return;
        }

        if (existingEnchantments >= 3) {
            tooManyEnchantmentsOnInput.set(1);
            selectedEnchantmentIndex.set(-1);
            if (enchantedSlot.hasItem()) {
                enchantedSlot.set(ItemStack.EMPTY);
            }
            return;
        }
        else {
            tooManyEnchantmentsOnInput.set(0);
        }

        ItemStack toEnchant = bookSlot.getItem();
        if (!toEnchant.isEmpty()) {
            ItemStack tempCopy = toEnchant.copy();
            tempCopy.setCount(1);

            int level = xpTier.get() * BzGeneralConfigs.crystallineFlowerEnchantingPowerAllowedPerTier;
            List<EnchantmentInstance> availableEnchantments = EnchantmentUtils.allAllowedEnchantsWithoutMaxLimit(level, tempCopy, xpTier.get());

            if (availableEnchantments.size() == 0) {
                if (enchantedSlot.hasItem()) {
                    enchantedSlot.set(ItemStack.EMPTY);
                }
                selectedEnchantmentIndex.set(-1);
            }

            if (selectedEnchantmentIndex.get() != -1) {
                if (selectedEnchantmentIndex.get() != -1 && availableEnchantments.size() > selectedEnchantmentIndex.get()) {
                    EnchantmentInstance enchantmentForItem = availableEnchantments.get(selectedEnchantmentIndex.get());

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
                return;
            }

            if (player instanceof ServerPlayer serverPlayer) {
                List<EnchantmentSkeleton> availableEnchantmentsSkeletons =
                        availableEnchantments.stream().map(e -> {
                            ResourceLocation resourceLocation = BuiltInRegistries.ENCHANTMENT.getKey(e.enchantment);
                            return new EnchantmentSkeleton(
                                    resourceLocation.getPath(),
                                    resourceLocation.getNamespace(),
                                    e.level,
                                    e.enchantment.getMinCost(2),
                                    e.level == e.enchantment.getMaxLevel(),
                                    e.enchantment.isCurse(),
                                    e.enchantment.isTreasureOnly()
                            );
                        }).toList();
                CrystallineFlowerEnchantmentPacket.sendToClient(serverPlayer, availableEnchantmentsSkeletons);
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