package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StrictChestMenu extends ChestMenu {
    
    private StrictChestMenu(MenuType<?> menuType, int id, Inventory inventory, int rows) {
        super(menuType, id, inventory, new SimpleContainer(9 * rows), rows);

        for(int row = 0; row < rows; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.slots.set(col + (row * 9), new ShulkerBoxSlot(this.getContainer(), col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }
    }

    public StrictChestMenu(MenuType<?> menuType, int id, Inventory inventory, Container container, int rows) {
        super(menuType, id, inventory, container, rows);

        for(int row = 0; row < rows; ++row) {
            for(int col = 0; col < 9; ++col) {
                this.slots.set(col + (row * 9), new ShulkerBoxSlot(this.getContainer(), col + row * 9, 8 + col * 18, 18 + row * 18));
            }
        }
    }

    public static StrictChestMenu oneRow(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x1, id, player, 1);
    }

    public static StrictChestMenu twoRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x2, id, player, 2);
    }

    public static StrictChestMenu threeRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x3, id, player, 3);
    }

    public static StrictChestMenu fourRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x4, id, player, 4);
    }

    public static StrictChestMenu fiveRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x5, id, player, 5);
    }

    public static StrictChestMenu sixRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x6, id, player, 6);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.getContainer().getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.getContainer().getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.getContainer().getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}