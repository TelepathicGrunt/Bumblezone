package com.telepathicgrunt.bumblezone.effects;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.modinit.BzEffects;
import com.telepathicgrunt.bumblezone.modinit.BzPOI;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.List;
import java.util.stream.Collectors;


public class WrathOfTheHiveEffect extends MobEffect {
    private final static TargetingConditions SEE_THROUGH_WALLS = (TargetingConditions.forCombat()).ignoreLineOfSight();
    private final static TargetingConditions LINE_OF_SIGHT = (TargetingConditions.forCombat());
    public static boolean ACTIVE_WRATH = false;
    public static int NEARBY_WRATH_EFFECT_RADIUS = 8;

    public WrathOfTheHiveEffect(MobEffectCategory type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstantenous() {
        return true;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Makes the bees swarm at the entity
     */
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Level world = entity.level;

        //Maximum aggression
        if (amplifier >= 2) {
            unBEElievablyHighAggression(world, entity);

            if(GeneralUtils.getEntityCountInBz() < Bumblezone.BZ_CONFIG.BZBlockMechanicsConfig.broodBlocksBeeSpawnCapacity * 3.0f) {
                // Spawn bees when high wrath effect.
                // Must be very low as this method is fired every tick for status effects.
                // We don't want to spawn millions of bees
                if(!world.isClientSide() && world.random.nextFloat() <= 0.0045f) {
                    // Grab a nearby air materialposition not in the player's field of view
                    BlockPos spawnBlockPos = new BlockPos(
                            entity.getX() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1),
                            entity.getY() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1),
                            entity.getZ() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1));
                    if(world.getBlockState(spawnBlockPos).getMaterial() != Material.AIR) {
                        return;
                    }

                    Bee bee = EntityType.BEE.create(world);
                    if(bee == null) return;

                    bee.absMoveTo(
                            spawnBlockPos.getX() + 0.5D,
                            spawnBlockPos.getY() + 0.5D,
                            spawnBlockPos.getZ() + 0.5D,
                            world.random.nextFloat() * 360.0F,
                            0.0F);

                    bee.finalizeSpawn(
                            (ServerLevel) world,
                            world.getCurrentDifficultyAt(spawnBlockPos),
                            MobSpawnType.TRIGGERED,
                            null,
                            null);

                    world.addFreshEntity(bee);
                }
            }
        }
        //Anything lower than 2 is medium aggression
        else {
            mediumAggression(world, entity);
        }

        // makes brood blocks grow faster near wrath of the hive entities.
        if(!world.isClientSide()) {
            PoiManager pointofinterestmanager = ((ServerLevel)world).getPoiManager();
            List<PoiRecord> poiInRange = pointofinterestmanager.getInSquare(
                    (pointOfInterestType) -> pointOfInterestType == BzPOI.BROOD_BLOCK_POI,
                    entity.blockPosition(),
                    NEARBY_WRATH_EFFECT_RADIUS,
                    PoiManager.Occupancy.ANY)
                    .collect(Collectors.toList());

            float chanceofGrowth = 0.001f;
            if(poiInRange.size() != 0) {
                for(int index = poiInRange.size() - 1; index >= 0; index--) {
                    PoiRecord poi = poiInRange.remove(index);
                    if(world.random.nextFloat() < chanceofGrowth) {
                        BlockState state = world.getBlockState(poi.getPos());
                        if(state.getBlock() instanceof HoneycombBrood) {
                            state.tick((ServerLevel) world, poi.getPos(), world.random);
                        }
                    }
                }
            }
        }
    }

    /**
     * Bees are angry but not crazy angry
     */
    public static void mediumAggression(Level world, LivingEntity livingEntity) {
        setAggression(world,
                livingEntity,
                Bee.class,
                LINE_OF_SIGHT,
                Math.max((Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.speedBoostLevel - 1), 1),
                Math.max((Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.absorptionBoostLevel - 1) / 2, 1),
                Math.max((Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.strengthBoostLevel - 1) / 3, 1));
    }


    /**
     * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
     */
    public static void unBEElievablyHighAggression(Level world, LivingEntity livingEntity) {
        setAggression(world,
                livingEntity,
                Bee.class,
                SEE_THROUGH_WALLS,
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.speedBoostLevel - 1,
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.absorptionBoostLevel - 1,
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.strengthBoostLevel - 1);
    }

    private static void setAggression(Level world, LivingEntity livingEntity, Class<? extends Mob> entityToFind, TargetingConditions sightMode, int speed, int absorption, int strength) {
        if(livingEntity instanceof Bee) {
            return;
        }

        sightMode.range(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius);
        List<? extends Mob> beeList = world.getNearbyEntities(entityToFind, sightMode, livingEntity, livingEntity.getBoundingBox().inflate(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius));
        for (Mob bee : beeList) {
            bee.setTarget(livingEntity);
            if(bee instanceof NeutralMob) {
                ((NeutralMob)bee).setRemainingPersistentAngerTime(20);
                ((NeutralMob)bee).setPersistentAngerTarget(livingEntity.getUUID());
            }

            MobEffectInstance effect = livingEntity.getEffect(BzEffects.WRATH_OF_THE_HIVE);
            if(effect != null) {
                int leftoverDuration = effect.getDuration();

                bee.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, leftoverDuration, speed, false, false));
                bee.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, leftoverDuration, absorption, false, false));
                bee.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, leftoverDuration, strength, false, true));
            }
        }
    }

    /**
     * Calm the bees that are attacking the incoming entity
     */
    public static void calmTheBees(Level world, LivingEntity livingEntity)
    {
        SEE_THROUGH_WALLS.range(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D);
        List<Bee> beeList = world.getNearbyEntities(Bee.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().inflate(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D));
        for (Bee bee : beeList)
        {
            if(bee.getTarget() == livingEntity) {
                bee.setTarget(null);
                bee.setAggressive(false);
                bee.setRemainingPersistentAngerTime(0);
                bee.removeEffect(MobEffects.DAMAGE_BOOST);
                bee.removeEffect(MobEffects.MOVEMENT_SPEED);
                bee.removeEffect(MobEffects.ABSORPTION);
            }
        }
    }

    // Don't remove wrath effect from mobs that bees are to always be angry at (bears, non-bee insects)
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        if(BeeAggression.doesBeesHateEntity(entity)) {
            //refresh the bee anger timer
            entity.addEffect(new MobEffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE,
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                    1,
                    false,
                    true));
        }
        else{
            // remove the effect like normal
            super.removeAttributeModifiers(entity, attributes, amplifier);
        }
    }
}
