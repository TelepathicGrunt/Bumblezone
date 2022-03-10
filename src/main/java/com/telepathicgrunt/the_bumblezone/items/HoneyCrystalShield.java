package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class HoneyCrystalShield extends ShieldItem {
    private static final int[] shieldDurabilityBoostPerLevel = new int[]{0,20,45,75,110,150,195,245,316,632};
    private static final int maxShieldLevel = shieldDurabilityBoostPerLevel.length - 1;

    public HoneyCrystalShield() {
        //starts off with 40 durability so it is super weak
        super(new Item.Properties().durability(40).tab(BzItems.BUMBLEZONE_CREATIVE_TAB));
    }

    /**
     * Specify what item can repair this shield
     */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(BzTags.HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS);
    }

    /**
     * Display the shield level (repair cost)
     */
    // CLIENT-SIDED
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), maxShieldLevel), 0);
            tooltip.add(new TranslatableComponent("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (shieldLevel + 1)));
        }
    }

    /**
     * Increases the durability of the shield by 10 for every shield level (repair cost)
     */
    @Override
    public int getMaxDamage(ItemStack stack) {
        if(stack.hasTag()) {
            upgradeLegacyShield(stack);

            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), maxShieldLevel), 0);
            if (shieldLevel != 0) {
                return stack.getItem().getMaxDamage() + shieldDurabilityBoostPerLevel[shieldLevel];
            }
        }
        return stack.getItem().getMaxDamage();
    }

    private void upgradeLegacyShield(ItemStack stack) {
        if(stack.hasTag() && !stack.getTag().contains("ShieldLevel")) {
            int repairCost = stack.getOrCreateTag().getInt("RepairCost");
            if (repairCost >= 32) {
                stack.getOrCreateTag().putInt("ShieldLevel", maxShieldLevel);
            }
            else if(repairCost >= 16) {
                stack.getOrCreateTag().putInt("ShieldLevel", maxShieldLevel - 1);
            }
            else if(repairCost >= 5) {
                stack.getOrCreateTag().putInt("ShieldLevel", maxShieldLevel / 2);
            }
        }
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    @Override
    public void setDamage(ItemStack stack, int damage) {
        int newDamage = damage;
        int oldDamage = this.getDamage(stack);
        int damageCaused = oldDamage - damage;
        int shieldLevel = stack.getOrCreateTag().getInt("ShieldLevel");

        // ignore anvil repairing
        if (damageCaused < 0) {
            int reducedDamage = -1 * Math.min(-1, damageCaused + (shieldLevel / 4));
            newDamage = Math.max(0, this.getDamage(stack) + reducedDamage);
        }
        // strengthen on significant repair
        else if (damageCaused > stack.getMaxDamage() / 5) {
            stack.getOrCreateTag().putInt("ShieldLevel", Math.min(maxShieldLevel, shieldLevel + 1));
        }

        stack.getOrCreateTag().putInt("Damage", Math.max(0, newDamage));
    }

    /**
     * blacklisted mending enchantment
     */
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment == Enchantments.MENDING) {
            return false;
        }

        return enchantment.category.canEnchant(stack.getItem());
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.round(13.0F - (float)itemStack.getDamageValue() * 13.0F / (float)itemStack.getMaxDamage());
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        float f = Math.max(0.0F, ((float)itemStack.getMaxDamage() - (float)itemStack.getDamageValue()) / (float)itemStack.getMaxDamage());
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}