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
import net.telepathicgrunt.bumblezone.Bumblezone;


public class WrathOfTheHiveEffect extends Effect
{
	public WrathOfTheHiveEffect(EffectType type, int potionColor)
	{
		super(type, potionColor);
	}


	/**
	 * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
	 */
	@Override
	public boolean isInstant()
	{
		return true;
	}


	/**
	 * checks if Potion effect is ready to be applied this tick.
	 */
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return duration >= 1;
	}


	/**
	 * Makes the bees swarm at the entity
	 */
	@Override
	public void performEffect(LivingEntity entity, int amplifier)
	{
		//Maximum aggression
		if (amplifier >= 2)
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
		EntityPredicate line_of_sight = (new EntityPredicate()).setDistance(Bumblezone.BzConfig.aggressionTriggerRadius.get()).setLineOfSiteRequired();
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, line_of_sight, livingEntity, livingEntity.getBoundingBox().grow(Bumblezone.BzConfig.aggressionTriggerRadius.get()));

		for (BeeEntity bee : beeList)
		{
			bee.setBeeAttacker(livingEntity);

			// weaker potion effects for when attacking bears
			bee.addPotionEffect(new EffectInstance(Effects.SPEED, 20, Math.max(Bumblezone.BzConfig.speedBoostLevel.get(), 1), false, false));
			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20, Math.max(Bumblezone.BzConfig.absorptionBoostLevel.get() / 2, 1), false, false));
			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, 20, Math.max(Bumblezone.BzConfig.strengthBoostLevel.get() / 3, 1), false, true));
		}
	}


	/**
	 * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
	 */
	public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity)
	{
		EntityPredicate see_through_walls = (new EntityPredicate()).setDistance(Bumblezone.BzConfig.aggressionTriggerRadius.get());
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, see_through_walls, livingEntity, livingEntity.getBoundingBox().grow(Bumblezone.BzConfig.aggressionTriggerRadius.get()));
		for (BeeEntity bee : beeList)
		{
			bee.setBeeAttacker(livingEntity);
			bee.addPotionEffect(new EffectInstance(Effects.SPEED, 20, Bumblezone.BzConfig.speedBoostLevel.get(), false, false));
			bee.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20, Bumblezone.BzConfig.absorptionBoostLevel.get(), false, false));
			bee.addPotionEffect(new EffectInstance(Effects.STRENGTH, 20, Bumblezone.BzConfig.strengthBoostLevel.get(), false, true));
		}
	}
	
	/**
	 * Calm the bees that are attacking the incoming entity
	 */
	public static void calmTheBees(World world, LivingEntity livingEntity)
	{
		EntityPredicate see_through_walls = (new EntityPredicate()).setDistance(Bumblezone.BzConfig.aggressionTriggerRadius.get()*0.5D);
		List<BeeEntity> beeList = world.getTargettableEntitiesWithinAABB(BeeEntity.class, see_through_walls, livingEntity, livingEntity.getBoundingBox().grow(Bumblezone.BzConfig.aggressionTriggerRadius.get()*0.5D));
		for (BeeEntity bee : beeList)
		{
			if(bee.getAttackTarget() == livingEntity) {
				bee.setBeeAttacker(null);
				bee.setAggroed(false);
				bee.setAnger(0);
				bee.removePotionEffect(Effects.STRENGTH);
				bee.removePotionEffect(Effects.SPEED);
				bee.removePotionEffect(Effects.ABSORPTION);
			}
		}
	}
}
