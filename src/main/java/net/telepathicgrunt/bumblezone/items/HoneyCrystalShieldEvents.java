package net.telepathicgrunt.bumblezone.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HoneyCrystalShieldEvents {

    @Mod.EventBusSubscriber(modid = Bumblezone.MODID)
    private static class ForgeEvents {
	
	/**
	 * Deals massive damage to shield when blocking explosion or getting fire damage with Honey Crystal Shield
	 */
	@SubscribeEvent
	public static void damageShieldFromExplosionAndFire(LivingAttackEvent event) {
	    
	    // checks for explosion and player
	    if ((event.getSource().isExplosion() || event.getSource().isFireDamage()) && 
		    event.getEntityLiving() instanceof PlayerEntity) {

		// checks to see if player is blocking with our shield
		PlayerEntity player = (PlayerEntity) event.getEntityLiving();
		if (player.getActiveItemStack().getItem() instanceof HoneyCrystalShield) {

		    if(event.getSource().isExplosion() && player.isActiveItemStackBlocking()) {
			    // damage our shield greatly and 1 damage hit player to show shield weakness
			    player.attackEntityFrom(DamageSource.GENERIC, 1);
			    player.damageShield(Math.max(player.getActiveItemStack().getMaxDamage() / 3, 18));
		    }
		    else if(event.getSource().isFireDamage()) {
			    player.damageShield(Math.max(player.getActiveItemStack().getMaxDamage() / 100, 3));
		    }
		}
	    }
	}
	
	
	/**
	 * Applies slowness to physical attackers when blocking with Honey Crystal Shield
	 */
	@SubscribeEvent
	public static void slowPhysicalAttackers(LivingAttackEvent event) {
	    DamageSource damageSource = event.getSource();
	    
	    // checks for living attacker and player victim
	    // and also ignores explosions or magic damage
	    if (damageSource.getImmediateSource() instanceof LivingEntity &&
		    !damageSource.isExplosion() &&
		    !damageSource.isMagicDamage() &&
		    event.getEntityLiving() instanceof PlayerEntity) {

		// checks to see if player is blocking with our shield
		LivingEntity attacker = (LivingEntity) event.getSource().getImmediateSource();
		PlayerEntity player = (PlayerEntity) event.getEntityLiving();

		if (player.getActiveItemStack().getItem() instanceof HoneyCrystalShield
			&& player.isActiveItemStackBlocking()) {

		    // apply slowness to attacker
		    attacker.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 80, 0, false, false, false));
		}
	    }
	}
    }
}
