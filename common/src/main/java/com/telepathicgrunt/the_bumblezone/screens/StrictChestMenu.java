package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StrictChestMenu extends AbstractContainerMenu {
    private final Container container;
    private final int rows;
    
    private StrictChestMenu(MenuType<?> menuType, int id, Inventory inventory, int rows) {
        this(menuType, id, inventory, new SimpleContainer(9 * rows), rows);
    }

    public StrictChestMenu(MenuType<?> menuType, int id, Inventory inventory, Container container, int rows) {
        super(menuType, id);
        int i = (rows - 4) * 18;
        this.container = container;
        this.rows = rows;

        for(int j = 0; j < rows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new ShulkerBoxSlot(container, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public static StrictChestMenu oneRow(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x1.get(), id, player, 1);
    }

    public static StrictChestMenu twoRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x2.get(), id, player, 2);
    }

    public static StrictChestMenu threeRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x3.get(), id, player, 3);
    }

    public static StrictChestMenu fourRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x4.get(), id, player, 4);
    }

    public static StrictChestMenu fiveRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x5.get(), id, player, 5);
    }

    public static StrictChestMenu sixRows(int id, Inventory player) {
        return new StrictChestMenu(BzMenuTypes.STRICT_9x6.get(), id, player, 6);
    }

    public int getRows() {
        return rows;
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
            if (index < container.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, container.getContainerSize(), false)) {
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

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}