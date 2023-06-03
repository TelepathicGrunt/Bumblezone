package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class BuzzingBreifcaseMenu extends AbstractContainerMenu {
    private final Player player;
    public final ItemStack breifcaseItem;

    public BuzzingBreifcaseMenu(int id, Inventory inventory, ItemStack breifcaseItem) {
        super(BzMenuTypes.BUZZING_BRIEFCASE.get(), id);
        this.player = inventory.player;
        this.breifcaseItem = breifcaseItem;
    }

    public BuzzingBreifcaseMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}