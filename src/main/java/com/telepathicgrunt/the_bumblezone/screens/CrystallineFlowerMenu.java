package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    final Slot consumeSlot;
    final Slot bookSlot;
    private final Slot enchantedSlot;
    Runnable slotUpdateListener = () -> {};
    final DataSlot selectedEnchantmentIndex = DataSlot.standalone();
    final DataSlot searchTreasure = DataSlot.standalone();
    final DataSlot searchLevel = DataSlot.standalone();
    final DataSlot consumeXP = DataSlot.standalone();
    private final Container inputContainer = new SimpleContainer(3) {
        /**
         * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            slotsChanged(this);
            slotUpdateListener.run();
        }
    };
    long lastSoundTime;

    public CrystallineFlowerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, 0, 0);
    }

    public CrystallineFlowerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, int searchLevel, int searchTreasure) {
        super(BzMenuTypes.CRYSTALLINE_FLOWER.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;

        this.consumeSlot = addSlot(new Slot(inputContainer, CONSUME_SLOT, CONSUME_SLOT_X, CONSUME_SLOT_Y) {
            public boolean mayPlace(ItemStack itemStack) {
                return !(itemStack.is(Items.BOOK));
            }
        });
        this.bookSlot = addSlot(new Slot(inputContainer, BOOK_SLOT, BOOK_SLOT_X, BOOK_SLOT_Y) {
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(Items.BOOK);
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
                bookSlot.remove(1);
                if (!bookSlot.hasItem()) {
                    selectedEnchantmentIndex.set(-1);
                }

                access.execute((soundLevel, pos) -> {
                    long gameTime = soundLevel.getGameTime();
                    if (lastSoundTime != gameTime) {
                        soundLevel.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        lastSoundTime = gameTime;
                    }

                });
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

        addDataSlot(this.selectedEnchantmentIndex);
        addDataSlot(this.searchTreasure);
        addDataSlot(this.searchLevel);
        addDataSlot(this.consumeXP);
    }

    public void slotsChanged(Container inventory) {
        ItemStack consumeSlotItem = consumeSlot.getItem();
        ItemStack bookSlotItem = bookSlot.getItem();

        if (bookSlotItem.isEmpty()) {
            if (enchantedSlot.hasItem()) {
                enchantedSlot.set(ItemStack.EMPTY);
            }
            searchLevel.set(0);
            searchTreasure.set(0);
            selectedEnchantmentIndex.set(-1);
        }

        if (consumeSlotItem.isEmpty()) {
            consumeXP.set(0);
        }
        else {
            consumeXP.set(1);
        }

        if (selectedEnchantmentIndex.get() != -1) {
            setupResultSlot(selectedEnchantmentIndex.get());
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
            drainXPLevel(1);
            return true;
        }
        // drain xp 2
        else if (id == -3) {
            drainXPLevel(2);
            return true;
        }
        // drain xp 3
        else if (id == -4) {
            drainXPLevel(3);
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
        ItemStack consumedItem = consumeSlot.getItem();
        // check to make sure not maximum xp without tier upgrade
        if (true) {
            consumeSlot.container.removeItemNoUpdate(consumeSlot.index);
            //grant xp and tier upgrade
        }
    }

    private void drainXPLevel(int levelToConsume) {
        // check to make sure not maximum xp without tier upgrade
        if (true) {
            if (!player.getAbilities().instabuild) {
                player.onEnchantmentPerformed(ItemStack.EMPTY, levelToConsume);
            }
            //grant xp and tier upgrade
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
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == enchantedSlot.index) {
                if (!moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index != consumeSlot.index && index != bookSlot.index) {
                if (!itemstack1.is(Items.BOOK) && !moveItemStackTo(itemstack1, consumeSlot.index, consumeSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
                else if (itemstack1.is(Items.BOOK) && !moveItemStackTo(itemstack1, bookSlot.index, bookSlot.index + 1, false)) {
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

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private void setupResultSlot(int selectedEnchantment) {
        ItemStack book = bookSlot.getItem();
        if (!book.isEmpty()) {

            ItemStack tempBook = book.copy();
            tempBook.setCount(1);
            CompoundTag compoundtag = book.getTag();
            if (compoundtag != null) {
                tempBook.setTag(compoundtag.copy());
            }

            List<EnchantmentInstance> availableEnchantments = GeneralUtils.allAllowedEnchantsWithoutMaxLimit(100, tempBook, false);
            availableEnchantments.sort(
                    Comparator.comparing((EnchantmentInstance a) -> Registry.ENCHANTMENT.getResourceKey(a.enchantment).get())
                    .thenComparingInt(a -> a.level));
            if (availableEnchantments.size() >= selectedEnchantment) {
                EnchantmentInstance enchantmentForBook = availableEnchantments.get(selectedEnchantment);
                EnchantedBookItem.addEnchantment(tempBook, enchantmentForBook);

                if (!player.getAbilities().instabuild) {
                    bookSlot.remove(1);
                }

                ItemStack enchantedBook = Items.ENCHANTED_BOOK.getDefaultInstance();
                enchantedBook.setCount(1);
                compoundtag = tempBook.getTag();
                if (compoundtag != null) {
                    enchantedBook.setTag(compoundtag.copy());
                    if (!ItemStack.matches(enchantedBook, enchantedSlot.getItem())) {
                        enchantedSlot.set(enchantedBook);
                    }
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