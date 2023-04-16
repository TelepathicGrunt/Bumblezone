package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class HoneyCrystalShield extends BzShieldItem implements ItemExtension {
    private static final int[] shieldDurabilityBoostPerLevel = new int[]{0,20,45,75,110,150,195,245,316,632};
    private static final int maxShieldLevel = shieldDurabilityBoostPerLevel.length - 1;

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

    @Override
    public EquipmentSlot bz$getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.OFFHAND;
    }

    /**
     * Display the shield level (repair cost)
     */
    // CLIENT-SIDED
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), maxShieldLevel), 0);
            tooltip.add(Component.translatable("item.the_bumblezone.honey_crystal_shield.level_tooltip").append(": " + (shieldLevel + 1)));
        }
    }

    /**
     * Increases the durability of the shield by 10 for every shield level (repair cost)
     */
    @Override
    public int bz$getMaxDamage(ItemStack stack) {
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
    public void bz$setDamage(ItemStack stack, int damage) {
        int newDamage = damage;
        int oldDamage = stack.getDamageValue();
        int damageCaused = oldDamage - damage;
        int shieldLevel = stack.getOrCreateTag().getInt("ShieldLevel");

        // ignore anvil repairing
        if (damageCaused < 0) {
            int reducedDamage = -1 * Math.min(-1, damageCaused + (shieldLevel / 4));
            newDamage = Math.max(0, stack.getDamageValue() + reducedDamage);
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
    public OptionalBoolean bz$canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if(enchantment == Enchantments.MENDING) {
            return OptionalBoolean.FALSE;
        }

        return OptionalBoolean.of(enchantment.category.canEnchant(stack.getItem()));
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

    @Override
    public boolean bz$canPerformAction(ItemStack stack, String toolAction) {
        return toolAction.equals("shield_block") && stack.is(this);
    }

    //extra effects for honey shield such as slow attackers or melt shield when hit by fire
    public static boolean handledPlayerHurtBehavior(EntityAttackedEvent event) {
        if (event.entity() instanceof Player player) {
            HoneyCrystalShieldBehavior.slowPhysicalAttackers(event.source(), player);
            return HoneyCrystalShieldBehavior.damageShieldFromExplosionAndFire(event.source(), player);
        }
        return false;
    }
}