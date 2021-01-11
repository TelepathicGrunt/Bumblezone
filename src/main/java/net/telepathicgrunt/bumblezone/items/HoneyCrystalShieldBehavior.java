package net.telepathicgrunt.bumblezone.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.telepathicgrunt.bumblezone.mixin.PlayerDamageShieldInvoker;

public class HoneyCrystalShieldBehavior {
    /**
     * Deals massive damage to shield when blocking explosion or getting fire damage with Honey Crystal Shield
     */
    public static boolean damageShieldFromExplosionAndFire(DamageSource source, PlayerEntity player) {

        // checks for explosion and player
        if ((source.isExplosive() || source.isFire())) {
            if (player.getActiveItem().getItem() instanceof HoneyCrystalShield) {

                if (source.isExplosive() && player.isBlocking()) {
                    // damage our shield greatly and 1 damage hit player to show shield weakness
                    player.damage(DamageSource.GENERIC, 1);
                    ((PlayerDamageShieldInvoker) player).bz_callDamagedShield(Math.max(player.getActiveItem().getMaxDamage() / 3, 18));
                } else if (source.isFire()) {
                    if(source.isProjectile()){
                        ((PlayerDamageShieldInvoker) player).bz_callDamagedShield(Math.max(player.getActiveItem().getMaxDamage() / 6, 3));
                    }
                    else{
                        ((PlayerDamageShieldInvoker) player).bz_callDamagedShield(Math.max(player.getActiveItem().getMaxDamage() / 100, 3));
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
        if (source.getSource() instanceof LivingEntity &&
                !source.isExplosive() &&
                !source.getMagic()) {

            // checks to see if player is blocking with our shield
            LivingEntity attacker = (LivingEntity) source.getSource();

            if (player.getActiveItem().getItem() instanceof HoneyCrystalShield
                    && player.isBlocking()) {

                // apply slowness to attacker
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 0, false, false, false));
            }
        }
    }

    public static void setShieldCooldown(PlayerEntity playerEntity, MobEntity mob){
        float f = 0.25F + (float) EnchantmentHelper.getEfficiency(mob) * 0.05F;
        if (mob.getRandom().nextFloat() < f) {
            playerEntity.getItemCooldownManager().set(BzItems.HONEY_CRYSTAL_SHIELD, 100);
            mob.world.sendEntityStatus(playerEntity, (byte)30);
        }
    }

    public static boolean damageHoneyCrystalShield(PlayerEntity player, float amount){
        if(player.getActiveItem().getItem() == BzItems.HONEY_CRYSTAL_SHIELD){
            if (amount >= 3.0F) {
                int damageToDo = 1 + MathHelper.floor(amount);
                Hand hand = player.getActiveHand();
                player.getActiveItem().damage(damageToDo, player, (playerEntity) -> playerEntity.sendToolBreakStatus(hand));
                if (player.getActiveItem().isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        player.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    player.clearActiveItem();
                    player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.world.random.nextFloat() * 0.4F);
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
                return BzItems.HONEY_CRYSTAL_SHIELD.getMaxDamage() + repairLevel * 10;
            }
        }
        return BzItems.HONEY_CRYSTAL_SHIELD.getMaxDamage();
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
