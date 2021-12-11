package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
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
    // CLIENT-SIDED
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            tooltip.add(new TranslatableComponent("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (repairLevel + 1)));
        }
    }

    /**
     * Increases the durability of the shield by 10 for every shield level (repair cost)
     */
    @Override
    public int getMaxDamage(ItemStack stack) {
        if(stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            if (repairLevel != 0) {
                return stack.getItem().getMaxDamage() + repairLevel * 10;
            }
        }
        return stack.getItem().getMaxDamage();
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            int damageCaused = this.getDamage(stack) - damage;

            // ignore anvil repairing
            if (damageCaused < 0 && repairLevel != 0) {
                int reducedDamage = Math.min(-1, damageCaused + (repairLevel / 14));
                stack.getOrCreateTag().putInt("Damage", Math.max(0, this.getDamage(stack) + reducedDamage));
            }
        }
        stack.getOrCreateTag().putInt("Damage", Math.max(0, damage));
    }

    /**
     * blacklisted mending enchantment
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment == Enchantments.MENDING)
            return false;

        return enchantment.category.canEnchant(stack.getItem());
    }
}