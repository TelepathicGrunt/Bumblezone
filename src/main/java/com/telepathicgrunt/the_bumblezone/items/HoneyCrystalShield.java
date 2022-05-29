package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.level.Level;

import java.util.List;

public class HoneyCrystalShield extends ShieldItem {
    protected static final int[] shieldDurabilityBoostPerLevel = new int[]{0,20,45,75,110,150,195,245,316,632};
    protected static final int maxShieldLevel = shieldDurabilityBoostPerLevel.length - 1;

    public HoneyCrystalShield(Properties properties) {
        //starts off with 40 durability so it is super weak
        super(properties.durability(40));
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
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), maxShieldLevel), 0);
            tooltip.add(new TranslatableComponent("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (shieldLevel + 1)));
        }
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

    public static boolean isInvalidForHoneyCrystalShield(ItemStack stack, Enchantment enchantment) {
        return stack.getItem() == BzItems.HONEY_CRYSTAL_SHIELD && enchantment instanceof MendingEnchantment;
    }
}
