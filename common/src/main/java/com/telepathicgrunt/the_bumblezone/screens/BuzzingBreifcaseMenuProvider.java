package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BuzzingBreifcaseMenuProvider implements MenuProvider {
    public final ItemStack breifcaseItem;

    public BuzzingBreifcaseMenuProvider(ItemStack stack) {
        this.breifcaseItem = stack;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Buzzing Briefcase");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BuzzingBreifcaseMenu(i, player.getInventory(), breifcaseItem);
    }
}