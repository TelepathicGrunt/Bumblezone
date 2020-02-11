package net.telepathicgrunt.bumblezone.effects;

import java.util.List;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.config.BzConfig;


public class WrathOfTheHiveEffect extends Effect
{
	public WrathOfTheHiveEffect(EffectType type, int potionColor)
	{
		super(type, potionColor);
	}


	/**
	 * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
	 */
	public boolean isInstant()
	{
		return true;
	}


	/**
	 * checks if Potion effect is ready to be applied this tick.
	 */
	public boolean isReady(int duration, int amplifier)
	{
		return duration >= 1;
	}


	/**
	 * Makes the bees swarm at the entity
	 */
	public void performEffect(LivingEntity entity, int amplifier)
	{
		if(BzConfig.aggressiveBees)
		{
			//Maximum aggression
			if(amplifier >= 2) 
			{
				unBEElievablyHighAggression(entity.world, entity);
			}
			//Anything lower than 2 is medium aggression
			else 
			{
				mediumAggression(entity.world, entity);
			}
		}
		else 
		{
			//Aggressive bees is turned off. Remove this effect.
			entity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
		}
	}
	
	
	/**
	 * Bees are angry but not crazy angry
	 */
	public static void mediumAggression(World world, LivingEntity livingEntity) 
	{
		EntityPredicate line_of_sight = (new EntityPredicate()).setDistance(BzConfig.aggressionTriggerRadius).setLineOfSiteRequired();
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, line_of_sight, livingEntity, livingEntity.getBoundingBox().grow(BzConfig.aggressionTriggerRadius));
		
		for(BeeEntity bee : beeList)
		{
			bee.setBeeAttacker(livingEntity);
			
			// weaker potion effects for when attacking bears
			bee.addPotionEffect(new EffectInstance(Effects.SPEED, BzConfig.howLongBeesKeepEffects, Math.max(BzConfig.speedBoostLevel, 1), false, false));
			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, BzConfig.howLongBeesKeepEffects, Math.max(BzConfig.absorptionBoostLevel/2, 1), false, false));
			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, BzConfig.howLongBeesKeepEffects, Math.max(BzConfig.strengthBoostLevel/3, 1), false, true));
		}
	}
	
	
	/**
	 * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
	 */
	public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity) 
	{
		EntityPredicate see_through_walls = (new EntityPredicate()).setDistance(BzConfig.aggressionTriggerRadius);
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, see_through_walls, livingEntity, livingEntity.getBoundingBox().grow(BzConfig.aggressionTriggerRadius));
		for(BeeEntity bee : beeList)
		{
			bee.setBeeAttacker(livingEntity);
			bee.addPotionEffect(new EffectInstance(Effects.SPEED, BzConfig.howLongBeesKeepEffects, BzConfig.speedBoostLevel, false, false));
			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, BzConfig.howLongBeesKeepEffects, BzConfig.absorptionBoostLevel, false, false));
			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, BzConfig.howLongBeesKeepEffects, BzConfig.strengthBoostLevel, false, true));
		}
	}
}
