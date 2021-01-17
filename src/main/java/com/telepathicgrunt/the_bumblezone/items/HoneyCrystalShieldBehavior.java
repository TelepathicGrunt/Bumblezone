package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.items.PlayerDamageShieldInvoker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        if ((source.isExplosion() || source.isFireDamage())) {
            if (player.getActiveItemStack().getItem() instanceof HoneyCrystalShield) {

                if (source.isExplosion() && player.isActiveItemStackBlocking()) {
                    // damage our shield greatly and 1 damage hit player to show shield weakness
                    player.attackEntityFrom(DamageSource.GENERIC, 1);
                    ((PlayerDamageShieldInvoker) player).bz_callDamagedShield(Math.max(player.getActiveItemStack().getMaxDamage() / 3, 18));
                } else if (source.isFireDamage()) {
                    if(source.isProjectile()){
                        ((PlayerDamageShieldInvoker) player).bz_callDamagedShield(Math.max(player.getActiveItemStack().getMaxDamage() / 6, 3));
                    }
                    else{
                        ((PlayerDamageShieldInvoker) player).bz_callDamagedShield(Math.max(player.getActiveItemStack().getMaxDamage() / 100, 3));
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
        if (source.getImmediateSource() instanceof LivingEntity &&
                !source.isExplosion() &&
                !source.isMagicDamage()) {

            // checks to see if player is blocking with our shield
            LivingEntity attacker = (LivingEntity) source.getImmediateSource();

            if (player.getActiveItemStack().getItem() instanceof HoneyCrystalShield
                    && player.isActiveItemStackBlocking()) {

                // apply slowness to attacker
                attacker.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 80, 0, false, false, false));
            }
        }
    }

    public static void setShieldCooldown(PlayerEntity playerEntity, MobEntity mob){
        float f = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(mob) * 0.05F;
        if (mob.getRNG().nextFloat() < f) {
            playerEntity.getCooldownTracker().setCooldown(BzItems.HONEY_CRYSTAL_SHIELD.get(), 100);
            mob.world.setEntityState(playerEntity, (byte)30);
        }
    }

    public static boolean damageHoneyCrystalShield(PlayerEntity player, float amount){
        if(player.getActiveItemStack().getItem() == BzItems.HONEY_CRYSTAL_SHIELD.get()){
            if (amount >= 3.0F) {
                int damageToDo = 1 + MathHelper.floor(amount);
                Hand hand = player.getActiveHand();
                player.getActiveItemStack().damageItem(damageToDo, player, (playerEntity) -> playerEntity.sendBreakAnimation(hand));
                if (player.getActiveItemStack().isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                    }

                    player.resetActiveHand();
                    player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
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
            int damageCaused = stack.getDamage() - damage;

            // ignore anvil repairing
            if (damageCaused < 0 && repairLevel != 0) {

                int reducedDamage = Math.min(-1, damageCaused + (repairLevel / 14));
               return stack.getDamage() + (-reducedDamage);
            }
        }

        return damage;
    }

}
