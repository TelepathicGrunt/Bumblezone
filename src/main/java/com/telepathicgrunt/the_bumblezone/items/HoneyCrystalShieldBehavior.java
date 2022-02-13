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

    public static void setShieldCooldown(Player playerEntity, LivingEntity livingEntity){
        float disableChance = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(livingEntity) * 0.05F;
        if (livingEntity.getRandom().nextFloat() < disableChance) {
            playerEntity.getCooldowns().addCooldown(BzItems.HONEY_CRYSTAL_SHIELD.get(), 100);
            livingEntity.level.broadcastEntityEvent(playerEntity, (byte)30);
        }
    }

    public static boolean damageHoneyCrystalShield(Player player, float amount){
        if(player.getUseItem().getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get()){
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
}