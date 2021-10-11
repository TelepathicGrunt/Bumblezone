package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.tags.BzItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class HoneyCrystalShield extends ShieldItem {

    public HoneyCrystalShield() {
        //starts off with 20 durability so it is super weak
        super(new Item.Properties().durability(20).tab(BzItems.BUMBLEZONE_CREATIVE_TAB));
    }

    /**
     * Specify what item can repair this shield
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return BzItemTags.HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS.contains(repair.getItem());
    }

    /**
     * Display the shield level (repair cost)
     */
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            tooltip.add(new TranslatableComponent("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (repairLevel + 1)));
        }
    }
}
