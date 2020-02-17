package net.telepathicgrunt.bumblezone.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.world.World;


public class WrathOfTheHiveEffect extends StatusEffect
{
	public WrathOfTheHiveEffect(StatusEffectType type, int potionColor)
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
	
	
	/**
	 * Bees are angry but not crazy angry
	 */
	public static void mediumAggression(World world, LivingEntity livingEntity) 
	{
//		EntityPredicate line_of_sight = (new EntityPredicate()).setDistance(BzConfig.aggressionTriggerRadius).setLineOfSiteRequired();
//		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, line_of_sight, livingEntity, livingEntity.getBoundingBox().grow(BzConfig.aggressionTriggerRadius));
//		
//		for(BeeEntity bee : beeList)
//		{
//			bee.setBeeAttacker(livingEntity);
//			
//			// weaker potion effects for when attacking bears
//			bee.addPotionEffect(new EffectInstance(Effects.SPEED, 20, Math.max(BzConfig.speedBoostLevel, 1), false, false));
//			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20, Math.max(BzConfig.absorptionBoostLevel/2, 1), false, false));
//			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, 20, Math.max(BzConfig.strengthBoostLevel/3, 1), false, true));
//		}
	}
	
	
	/**
	 * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
	 */
	public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity) 
	{
//		EntityPredicate see_through_walls = (new EntityPredicate()).setDistance(BzConfig.aggressionTriggerRadius);
//		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, see_through_walls, livingEntity, livingEntity.getBoundingBox().grow(BzConfig.aggressionTriggerRadius));
//		for(BeeEntity bee : beeList)
//		{
//			bee.setBeeAttacker(livingEntity);
//			bee.addPotionEffect(new EffectInstance(Effects.SPEED, 20, BzConfig.speedBoostLevel, false, false));
//			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20, BzConfig.absorptionBoostLevel, false, false));
//			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, 20, BzConfig.strengthBoostLevel, false, true));
//		}
	}
}
