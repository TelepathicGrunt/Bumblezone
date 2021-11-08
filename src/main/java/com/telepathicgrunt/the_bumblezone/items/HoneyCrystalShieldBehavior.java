package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import com.telepathicgrunt.the_bumblezone.mixin.items.PlayerDamageShieldInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class HoneyCrystalShieldBehavior {
    /**
     * Deals massive damage to shield when blocking explosion or getting fire damage with Honey Crystal Shield
     */
    public static boolean damageShieldFromExplosionAndFire(DamageSource source, PlayerEntity player) {

        // checks for explosion and player
        if ((source.isExplosion() || source.isFire())) {
            if (player.getUseItem().getItem() instanceof HoneyCrystalShield) {
                if(player instanceof ServerPlayerEntity) {
                    BzCriterias.HONEY_CRYSTAL_SHIELD_BLOCK_INEFFECTIVELY_TRIGGER.trigger((ServerPlayerEntity) player);
                }

                if (source.isExplosion() && player.isBlocking()) {
                    // damage our shield greatly and 1 damage hit player to show shield weakness
                    player.hurt(DamageSource.GENERIC, 1);
                    ((PlayerDamageShieldInvoker) player).thebumblezone_callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 3, 18));
                }
                else if (source.isFire()) {
                    if(source.isProjectile()){
                        ((PlayerDamageShieldInvoker) player).thebumblezone_callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 6, 3));
                    }
                    else{
                        ((PlayerDamageShieldInvoker) player).thebumblezone_callHurtCurrentlyUsedShield(Math.max(player.getUseItem().getMaxDamage() / 100, 3));
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
    public static void slowPhysicalAttackers(DamageSource source, PlayerEntity player) {

        // checks for living attacker and player victim
        // and also ignores explosions or magic damage
        if (source.getDirectEntity() instanceof LivingEntity &&
                !source.isExplosion() &&
                !source.isMagic()) {

            // checks to see if player is blocking with our shield
            LivingEntity attacker = (LivingEntity) source.getDirectEntity();

            if (player.getUseItem().getItem() instanceof HoneyCrystalShield
                    && player.isBlocking()) {

                // apply slowness to attacker
                attacker.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 80, 0, false, false, false));
            }
        }
    }

    public static void setShieldCooldown(PlayerEntity playerEntity, MobEntity mob){
        float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(mob) * 0.05F;
        if (mob.getRandom().nextFloat() < f) {
            playerEntity.getCooldowns().addCooldown(BzItems.HONEY_CRYSTAL_SHIELD.get(), 100);
            mob.level.broadcastEntityEvent(playerEntity, (byte)30);
        }
    }

    public static boolean damageHoneyCrystalShield(PlayerEntity player, float amount){
        if(player.getUseItem().getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get()){
            if (amount >= 3.0F) {
                int damageToDo = 1 + MathHelper.floor(amount);
                Hand hand = player.getUsedItemHand();
                player.getUseItem().hurtAndBreak(damageToDo, player, (playerEntity) -> playerEntity.broadcastBreakEvent(hand));
                if (player.getUseItem().isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        player.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        player.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
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
    public static int getMaximumDamage(ItemStack stack)
    {
        if(stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            if (repairLevel != 0) {
                return BzItems.HONEY_CRYSTAL_SHIELD.get().getMaxDamage() + repairLevel * 10;
            }
        }
        return BzItems.HONEY_CRYSTAL_SHIELD.get().getMaxDamage();
    }

    /**
     * reduces damage done to the shield for higher shield levels (repair cost)
     */
    public static int setDamage(ItemStack stack, int damage) {
        if (stack.hasTag()) {
            int repairLevel = stack.getTag().contains("RepairCost", 3) ? stack.getTag().getInt("RepairCost") : 0;
            int damageCaused = stack.getDamageValue() - damage;

            // ignore anvil repairing
            if (damageCaused < 0 && repairLevel != 0) {

                int reducedDamage = Math.min(-1, damageCaused + (repairLevel / 14));
               return stack.getDamageValue() + (-reducedDamage);
            }
        }

        return damage;
    }

}
