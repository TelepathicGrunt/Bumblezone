package net.telepathicgrunt.bumblezone.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.telepathicgrunt.bumblezone.mixin.PlayerDamageShieldInvoker;

public class HoneyCrystalShieldBehavior {
    /**
     * Deals massive damage to shield when blocking explosion or getting fire damage with Honey Crystal Shield
     */
    public static void damageShieldFromExplosionAndFire(DamageSource source, PlayerEntity player) {

        // checks for explosion and player
        if ((source.isExplosive() || source.isFire())) {
            if (player.getActiveItem().getItem() instanceof HoneyCrystalShield) {

                if (source.isExplosive() && player.isBlocking()) {
                    // damage our shield greatly and 1 damage hit player to show shield weakness
                    player.damage(DamageSource.GENERIC, 1);
                    ((PlayerDamageShieldInvoker) player).callDamagedShield(Math.max(player.getActiveItem().getMaxDamage() / 3, 18));
                } else if (source.isFire()) {
                    ((PlayerDamageShieldInvoker) player).callDamagedShield(Math.max(player.getActiveItem().getMaxDamage() / 100, 3));
                }
            }
        }
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
}
