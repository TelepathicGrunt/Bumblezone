package net.telepathicgrunt.bumblezone.effects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.telepathicgrunt.bumblezone.modinit.BzEffects;
import net.telepathicgrunt.bumblezone.utils.GeneralUtils;

import java.util.List;


public class WrathOfTheHiveEffect extends StatusEffect {
    private final static TargetPredicate SEE_THROUGH_WALLS = (new TargetPredicate()).includeHidden();
    private final static TargetPredicate LINE_OF_SIGHT = (new TargetPredicate());
    public static boolean ACTIVE_WRATH = false;
    public static int NEARBY_WRATH_EFFECT_RADIUS = 8;

    public WrathOfTheHiveEffect(StatusEffectType type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    public boolean isInstant() {
        return true;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * Makes the bees swarm at the entity
     */
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.world;

        //Maximum aggression
        if (amplifier >= 2) {
            unBEElievablyHighAggression(world, entity);

            if(GeneralUtils.getEntityCountInBz() < Bumblezone.BZ_CONFIG.BZBlockMechanicsConfig.broodBlocksBeeSpawnCapacity * 3.0f){
                // Spawn bees when high wrath effect.
                // Must be very low as this method is fired every tick for status effects.
                // We don't want to spawn millions of bees
                if(!world.isClient() && world.random.nextFloat() <= 0.0045f){
                    // Grab a nearby air materialposition not in the player's field of view
                    BlockPos spawnBlockPos = new BlockPos(
                            entity.getX() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1),
                            entity.getY() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1),
                            entity.getZ() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1));
                    if(world.getBlockState(spawnBlockPos).getMaterial() != Material.AIR){
                        return;
                    }

                    BeeEntity bee = EntityType.BEE.create(world);
                    if(bee == null) return;

                    bee.updatePositionAndAngles(
                            spawnBlockPos.getX() + 0.5D,
                            spawnBlockPos.getY() + 0.5D,
                            spawnBlockPos.getZ() + 0.5D,
                            world.random.nextFloat() * 360.0F,
                            0.0F);

                    bee.initialize(
                            (ServerWorld) world,
                            world.getLocalDifficulty(spawnBlockPos),
                            SpawnReason.TRIGGERED,
                            null,
                            null);

                    world.spawnEntity(bee);
                }
            }
        }
        //Anything lower than 2 is medium aggression
        else {
            mediumAggression(world, entity);
        }

        // makes brood blocks grow faster near wrath of the hive entities.
        if(!world.isClient()){
            for(int attempts = 0; attempts < 5; attempts++){
                int range = NEARBY_WRATH_EFFECT_RADIUS * 2;
                BlockPos selectedBlockToGrow = new BlockPos(
                        entity.getX() + (world.random.nextInt(range) - NEARBY_WRATH_EFFECT_RADIUS),
                        entity.getY() + (world.random.nextInt(range) - NEARBY_WRATH_EFFECT_RADIUS),
                        entity.getZ() + (world.random.nextInt(range) - NEARBY_WRATH_EFFECT_RADIUS));

                BlockState state = world.getBlockState(selectedBlockToGrow);
                if(state.getBlock() instanceof HoneycombBrood){
                    state.scheduledTick((ServerWorld) world, selectedBlockToGrow, world.random);
                }
            }
        }
    }

    /**
     * Bees are angry but not crazy angry
     */
    public static void mediumAggression(World world, LivingEntity livingEntity) {
        setAggression(world,
                livingEntity,
                BeeEntity.class,
                LINE_OF_SIGHT,
                Math.max((Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.speedBoostLevel - 1), 1),
                Math.max((Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.absorptionBoostLevel - 1) / 2, 1),
                Math.max((Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.strengthBoostLevel - 1) / 3, 1));
    }


    /**
     * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
     */
    public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity) {
        setAggression(world,
                livingEntity,
                BeeEntity.class,
                SEE_THROUGH_WALLS,
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.speedBoostLevel - 1,
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.absorptionBoostLevel - 1,
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.strengthBoostLevel - 1);
    }

    private static void setAggression(World world, LivingEntity livingEntity, Class<? extends MobEntity> entityToFind, TargetPredicate sightMode, int speed, int absorption, int strength) {
        if(livingEntity instanceof BeeEntity) {
            return;
        }

        sightMode.setBaseMaxDistance(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius);
        List<MobEntity> beeList = world.getTargets(entityToFind, sightMode, livingEntity, livingEntity.getBoundingBox().expand(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius));
        for (MobEntity bee : beeList) {
            bee.setTarget(livingEntity);
            if(bee instanceof Angerable){
                ((Angerable)bee).setAngerTime(20);
                ((Angerable)bee).setAngryAt(livingEntity.getUuid());
            }

            StatusEffectInstance effect = livingEntity.getStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
            if(effect != null){
                int leftoverDuration = effect.getDuration();

                bee.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, leftoverDuration, speed, false, false));
                bee.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, leftoverDuration, absorption, false, false));
                bee.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, leftoverDuration, strength, false, true));
            }
        }
    }

    /**
     * Calm the bees that are attacking the incoming entity
     */
    public static void calmTheBees(World world, LivingEntity livingEntity)
    {
        SEE_THROUGH_WALLS.setBaseMaxDistance(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D);
        List<BeeEntity> beeList = world.getTargets(BeeEntity.class, SEE_THROUGH_WALLS, livingEntity, livingEntity.getBoundingBox().expand(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressionTriggerRadius*0.5D));
        for (BeeEntity bee : beeList)
        {
            if(bee.getTarget() == livingEntity) {
                bee.setTarget(null);
                bee.setAttacking(false);
                bee.setAngerTime(0);
                bee.removeStatusEffect(StatusEffects.STRENGTH);
                bee.removeStatusEffect(StatusEffects.SPEED);
                bee.removeStatusEffect(StatusEffects.ABSORPTION);
            }
        }
    }

    // Don't remove wrath effect from mobs that bees are to always be angry at (bears, non-bee insects)
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(BeeAggression.doesBeesHateEntity(entity)){
            //refresh the bee anger timer
            entity.addStatusEffect(new StatusEffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE,
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                    1,
                    false,
                    true));
        }
        else{
            // remove the effect like normal
            super.onRemoved(entity, attributes, amplifier);
        }
    }
}
