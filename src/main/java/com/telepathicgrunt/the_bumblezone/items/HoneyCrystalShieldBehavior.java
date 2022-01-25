package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.items.PlayerDamageShieldInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class HoneyCrystalShieldBehavior {
    /**
     * Deals massive damage to shield when blocking explosion or getting fire damage with Honey Crystal Shield
     */
    public static boolean damageShieldFromExplosionAndFire(DamageSource source, Player player) {

        // checks for explosion and player
        if ((source.isExplosion() || source.isFire())) {
            if (player.getUseItem().getItem() instanceof HoneyCrystalShield) {
                if(player instanceof ServerPlayer) {
                    BzCriterias.HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER.trigger((ServerPlayer) player);
                }

                if (source.isExplosion() && player.isBlocking()) {
                    // damage our shield greatly and 1 damage hit player to show shield weakness
                    player.hurt(DamageSource.GENERIC, 1);
                    ((PlayerDamageShieldInvoker) player).thebumblezone_callDamagedShield(Math.max(player.getUseItem().getMaxDamage() / 3, 18));
                }
                else if (source.isFire()) {
                    if(source.isProjectile()) {
                        ((PlayerDamageShieldInvoker) player).thebumblezone_callDamagedShield(Math.max(player.getUseItem().getMaxDamage() / 6, 3));
                    }
                    else{
                        ((PlayerDamageShieldInvoker) player).thebumblezone_callDamagedShield(Math.max(player.getUseItem().getMaxDamage() / 100, 3));
                        return false; //continue the damaging
                    }
                }

                return true;
            }
        }
        return false;
    }


    /**
     * Applies slowness to physical attackers when blocking with Honey Crystal Shield
     */
    public static void slowPhysicalAttackers(DamageSource source, Player player) {

        // checks for living attacker and player victim
        // and also ignores explosions or magic damage
        if (source.getDirectEntity() instanceof LivingEntity attacker && !source.isExplosion() && !source.isMagic()) {

            // checks to see if player is blocking with our shield
            if (player.getUseItem().getItem() instanceof HoneyCrystalShield && player.isBlocking()) {

                // apply slowness to attacker
                attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 165, 1, true, true, false));
            }
        }
    }

    public static void setShieldCooldown(Player playerEntity, LivingEntity livingEntity) {
        float disableChance = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(livingEntity) * 0.05F;
        if (livingEntity.getRandom().nextFloat() < disableChance) {
            playerEntity.getCooldowns().addCooldown(BzItems.HONEY_CRYSTAL_SHIELD, 100);
            livingEntity.level.broadcastEntityEvent(playerEntity, (byte)30);
        }
    }

    public static boolean damageHoneyCrystalShield(Player player, float amount) {
        if(player.getUseItem().getItem() == BzItems.HONEY_CRYSTAL_SHIELD) {
            if (amount >= 3.0F) {
                int damageToDo = 1 + Mth.floor(amount);
                InteractionHand hand = player.getUsedItemHand();
                player.getUseItem().hurtAndBreak(damageToDo, player, (playerEntity) -> playerEntity.broadcastBreakEvent(hand));
                if (player.getUseItem().isEmpty()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                    else {
                        player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    player.stopUsingItem();
                    player.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + player.level.random.nextFloat() * 0.4F);
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Increases the durability of the shield by 10 for every shield level (repair cost)
     */
    public static int getMaximumDamage(ItemStack stack) {
        if(stack.hasTag()) {
            upgradeLegacyShield(stack);

            int shieldLevel = Math.max(Math.min(stack.getOrCreateTag().getInt("ShieldLevel"), HoneyCrystalShield.maxShieldLevel), 0);
            if (shieldLevel != 0) {
                return BzItems.HONEY_CRYSTAL_SHIELD.getMaxDamage() + HoneyCrystalShield.shieldDurabilityBoostPerLevel[shieldLevel];
            }
        }
        return BzItems.HONEY_CRYSTAL_SHIELD.getMaxDamage();
    }

    private static void upgradeLegacyShield(ItemStack stack) {
        if(stack.hasTag() && !stack.getTag().contains("ShieldLevel")) {
            int repairCost = stack.getOrCreateTag().getInt("RepairCost");
            if (repairCost >= 32) {
                stack.getOrCreateTag().putInt("ShieldLevel", HoneyCrystalShield.maxShieldLevel);
            }
            else if(repairCost >= 16) {
                stack.getOrCreateTag().putInt("ShieldLevel", HoneyCrystalShield.maxShieldLevel - 1);
            }
            else if(repairCost >= 5) {
                stack.getOrCreateTag().putInt("ShieldLevel", HoneyCrystalShield.maxShieldLevel / 2);
            }
        }
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    public static int setDamage(ItemStack stack, int damage) {
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
            stack.getOrCreateTag().putInt("ShieldLevel", Math.min(HoneyCrystalShield.maxShieldLevel, shieldLevel + 1));
        }

        return newDamage;
    }
}
