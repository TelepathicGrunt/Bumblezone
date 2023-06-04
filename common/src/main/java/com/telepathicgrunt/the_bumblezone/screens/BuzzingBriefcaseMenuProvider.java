package com.telepathicgrunt.the_bumblezone.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BuzzingBriefcaseMenuProvider implements MenuProvider {
    public final ItemStack briefcaseItem;

    public BuzzingBriefcaseMenuProvider(ItemStack stack) {
        this.briefcaseItem = stack;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Buzzing Briefcase");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BuzzingBriefcaseMenu(i, player.getInventory(), briefcaseItem);
    }
}