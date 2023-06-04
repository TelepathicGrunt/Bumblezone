package com.telepathicgrunt.the_bumblezone.screens;

import com.telepathicgrunt.the_bumblezone.items.BuzzingBriefcase;
import com.telepathicgrunt.the_bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class BuzzingBriefcaseMenu extends AbstractContainerMenu {
    public static final int RELEASE_ID = 0;
    public static final int HEALTH_ID = 1;
    public static final int STINGER_ID = 2;
    public static final int GROW_UP_ID = 3;
    public static final int POLLEN_ID = 4;
    public static final int NUMBER_OF_BUTTONS = 5;

    public final Player player;
    public final Slot briefcaseSlot;
    private final Container container = new SimpleContainer(1) {};

    public BuzzingBriefcaseMenu(int id, Inventory inventory, ItemStack briefcaseItem) {
        super(BzMenuTypes.BUZZING_BRIEFCASE.get(), id);
        this.player = inventory.player;

        this.briefcaseSlot = addSlot(new Slot(container, 0, Integer.MIN_VALUE, Integer.MIN_VALUE) {});
        this.briefcaseSlot.set(briefcaseItem);
    }

    public BuzzingBriefcaseMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ItemStack.EMPTY);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().is(BzItems.BUZZING_BRIEFCASE.get()) || player.getOffhandItem().is(BzItems.BUZZING_BRIEFCASE.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < 0) {
            return false;
        }

        int numberOfButtons = 5;
        int beeIndex = id / numberOfButtons;
        int buttonPressed = id % numberOfButtons;

        Inventory inventory = player.getInventory();
        ItemStack briefcase = container.getItem(0);
        List<Entity> beesStored = BuzzingBriefcase.getBeesStored(player.level(), briefcase, false);

        if (beeIndex < beesStored.size() && beesStored.get(beeIndex) instanceof Bee bee) {
            if (buttonPressed == RELEASE_ID) {

                BuzzingBriefcase.dumpBees(player, beeIndex, true);
                container.setChanged();
                return true;
            }
            else if (buttonPressed == HEALTH_ID && bee.getHealth() < bee.getMaxHealth()) {
                int honeyBottleSlotIndex = inventory.findSlotMatchingItem(Items.HONEY_BOTTLE.getDefaultInstance());
                ItemStack playerHoneyBottleStack = inventory.getItem(honeyBottleSlotIndex);

                if (!playerHoneyBottleStack.isEmpty()) {
                    bee.heal(2);

                    if (!player.getAbilities().instabuild) {
                        playerHoneyBottleStack.shrink(1);
                        if (playerHoneyBottleStack.isEmpty()) {
                            inventory.add(honeyBottleSlotIndex, Items.GLASS_BOTTLE.getDefaultInstance());
                        }
                        else if (!inventory.add(Items.GLASS_BOTTLE.getDefaultInstance())) {
                            // drops result item if inventory is full
                            player.drop(Items.GLASS_BOTTLE.getDefaultInstance(), false);
                        }
                    }

                    BuzzingBriefcase.overrwriteBees(briefcase, beesStored);
                    container.setChanged();
                    return true;
                }
            }
            else if (buttonPressed == STINGER_ID && bee.hasStung()) {
                int stingerSlotIndex = inventory.findSlotMatchingItem(BzItems.BEE_STINGER.get().getDefaultInstance());
                ItemStack playerStingerStack = inventory.getItem(stingerSlotIndex);

                if (!playerStingerStack.isEmpty()) {
                    ((BeeEntityInvoker)bee).callSetHasStung(false);

                    if (!player.getAbilities().instabuild) {
                        playerStingerStack.shrink(1);
                    }

                    BuzzingBriefcase.overrwriteBees(briefcase, beesStored);
                    container.setChanged();
                    return true;
                }
            }
            else if (buttonPressed == GROW_UP_ID && bee.isBaby()) {
                int honeyBottleSlotIndex = inventory.findSlotMatchingItem(Items.HONEY_BOTTLE.getDefaultInstance());
                ItemStack playerHoneyBottleStack = inventory.getItem(honeyBottleSlotIndex);

                if (!playerHoneyBottleStack.isEmpty()) {
                    bee.setBaby(false);

                    if (!player.getAbilities().instabuild) {
                        playerHoneyBottleStack.shrink(1);
                        if (playerHoneyBottleStack.isEmpty()) {
                            inventory.add(honeyBottleSlotIndex, Items.GLASS_BOTTLE.getDefaultInstance());
                        }
                        else if (!inventory.add(Items.GLASS_BOTTLE.getDefaultInstance())) {
                            // drops result item if inventory is full
                            player.drop(Items.GLASS_BOTTLE.getDefaultInstance(), false);
                        }
                    }

                    BuzzingBriefcase.overrwriteBees(briefcase, beesStored);
                    container.setChanged();
                    return true;
                }
            }
            else if (buttonPressed == POLLEN_ID && !bee.hasNectar()) {
                int pollenSlotIndex = inventory.findSlotMatchingItem(BzItems.POLLEN_PUFF.get().getDefaultInstance());
                ItemStack playerPollenStack = inventory.getItem(pollenSlotIndex);

                if (!playerPollenStack.isEmpty()) {
                    ((BeeEntityInvoker)bee).callSetHasNectar(true);

                    if (!player.getAbilities().instabuild) {
                        playerPollenStack.shrink(1);
                    }

                    BuzzingBriefcase.overrwriteBees(briefcase, beesStored);
                    container.setItem(0, briefcase);
                    return true;
                }
            }
        }

        return false;
    }
}