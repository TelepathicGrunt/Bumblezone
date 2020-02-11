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
	private static final double AGGRESSION_DISTANCE = 64.0D;
	private static final EntityPredicate LINE_OF_SIGHT = (new EntityPredicate()).setDistance(AGGRESSION_DISTANCE).setLineOfSiteRequired();
	private static final EntityPredicate SEE_THROUGH_WALLS = (new EntityPredicate()).setDistance(AGGRESSION_DISTANCE);

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
	public static void mediumAggression(World world, LivingEntity livingEntity) {
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, LINE_OF_SIGHT, livingEntity, livingEntity.getBoundingBox().grow(AGGRESSION_DISTANCE));
		for(BeeEntity bee : beeList)
		{
			bee.setBeeAttacker(livingEntity);
			bee.addPotionEffect(new EffectInstance(Effects.SPEED, 350, 1, false, false));
			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 350, 1, false, false));
			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, 350, 1, false, true));
		}
	}
	
	
	/**
	 * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
	 */
	public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity) {
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().grow(AGGRESSION_DISTANCE));
		for(BeeEntity bee : beeList)
		{
			bee.setBeeAttacker(livingEntity);
			bee.addPotionEffect(new EffectInstance(Effects.SPEED, 350, 1, false, false));
			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 350, 2, false, false));
			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, 350, 4, false, true));
		}
	}
}
