package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modcompat.CarrierBeeRedirection;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;


public class WrathOfTheHiveEffect extends Effect {
    public final static EntityPredicate SEE_THROUGH_WALLS = (new EntityPredicate()).allowUnseeable();
    public final static EntityPredicate LINE_OF_SIGHT = (new EntityPredicate());
    public static boolean ACTIVE_WRATH = false;
    public static int NEARBY_WRATH_EFFECT_RADIUS = 8;

    public WrathOfTheHiveEffect(EffectType type, int potionColor) {
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
        World world = entity.level;

        //Maximum aggression
        if (amplifier >= 2) {
            unBEElievablyHighAggression(world, entity);

            if(GeneralUtils.getEntityCountInBz() < Bumblezone.BzBlockMechanicsConfig.broodBlocksBeeSpawnCapacity.get() * 3.0f){
                // Spawn bees when high wrath effect.
                // Must be very low as this method is fired every tick for status effects.
                // We don't want to spawn millions of bees
                if(!world.isClientSide() && world.random.nextFloat() <= 0.0045f){
                    // Grab a nearby air materialposition not in the player's field of view
                    BlockPos spawnBlockPos = new BlockPos(
                            entity.getX() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1),
                            entity.getY() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1),
                            entity.getZ() + (world.random.nextInt(30) + 10) * (world.random.nextBoolean() ? 1 : -1));
                    if(world.getBlockState(spawnBlockPos).getMaterial() != Material.AIR){
                        return;
                    }

                    if(ModChecker.carrierBeesPresent && world.random.nextBoolean()){
                        CarrierBeeRedirection.CBMobSpawn(entity, spawnBlockPos);
                    }
                    else{
                        BeeEntity bee = EntityType.BEE.create(world);
                        if(bee == null) return;
                        bee.moveTo(
                                spawnBlockPos.getX() + 0.5D,
                                spawnBlockPos.getY() + 0.5D,
                                spawnBlockPos.getZ() + 0.5D,
                                world.random.nextFloat() * 360.0F,
                                0.0F);

                        bee.finalizeSpawn(
                                (IServerWorld) world,
                                world.getCurrentDifficultyAt(spawnBlockPos),
                                SpawnReason.TRIGGERED,
                                null,
                                null);

                        // Trigger forge event but not allow them to cancel this special bee spawning.
                        // Thus they can do stuff to the bee or spawn other mobs.
                        net.minecraftforge.common.ForgeHooks.canEntitySpawn(
                                bee,
                                world,
                                spawnBlockPos.getX() + 0.5D,
                                spawnBlockPos.getY() + 0.5D,
                                spawnBlockPos.getZ() + 0.5D,
                                null,
                                SpawnReason.TRIGGERED);

                        world.addFreshEntity(bee);
                    }
                }
            }
        }
        //Anything lower than 2 is medium aggression
        else {
            mediumAggression(world, entity);
        }

        // makes brood blocks grow faster near wrath of the hive entities.
        if(!world.isClientSide()){
            for(int attempts = 0; attempts < 5; attempts++){
                int range = NEARBY_WRATH_EFFECT_RADIUS * 2;
                BlockPos selectedBlockToGrow = new BlockPos(
                        entity.getX() + (world.random.nextInt(range) - NEARBY_WRATH_EFFECT_RADIUS),
                        entity.getY() + (world.random.nextInt(range) - NEARBY_WRATH_EFFECT_RADIUS),
                        entity.getZ() + (world.random.nextInt(range) - NEARBY_WRATH_EFFECT_RADIUS));

                BlockState state = world.getBlockState(selectedBlockToGrow);
                if(state.getBlock() instanceof HoneycombBrood){
                    state.tick((ServerWorld) world, selectedBlockToGrow, world.random);
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
                Math.max((Bumblezone.BzBeeAggressionConfig.speedBoostLevel.get() - 1), 1),
                Math.max((Bumblezone.BzBeeAggressionConfig.absorptionBoostLevel.get() - 1) / 2, 1),
                Math.max((Bumblezone.BzBeeAggressionConfig.strengthBoostLevel.get() - 1) / 3, 1));

        if(ModChecker.carrierBeesPresent) {
            setAggression(world,
                    livingEntity,
                    CarrierBeeRedirection.CBGetAppleBeeClass(),
                    LINE_OF_SIGHT,
                    Math.max((Bumblezone.BzBeeAggressionConfig.speedBoostLevel.get() - 1), 1),
                    Math.max((Bumblezone.BzBeeAggressionConfig.absorptionBoostLevel.get() - 1) / 2, 1),
                    Math.max((Bumblezone.BzBeeAggressionConfig.strengthBoostLevel.get() - 1) / 3, 1));
        }
    }


    /**
     * Bees are REALLY angry!!! HIGH TAIL IT OUTTA THERE BRUH!!!
     */
    public static void unBEElievablyHighAggression(World world, LivingEntity livingEntity) {
        setAggression(world,
                livingEntity,
                BeeEntity.class,
                SEE_THROUGH_WALLS,
                Bumblezone.BzBeeAggressionConfig.speedBoostLevel.get() - 1,
                Bumblezone.BzBeeAggressionConfig.absorptionBoostLevel.get() - 1,
                Bumblezone.BzBeeAggressionConfig.strengthBoostLevel.get() - 1);

        if(ModChecker.carrierBeesPresent) {
            setAggression(world,
                    livingEntity,
                    CarrierBeeRedirection.CBGetAppleBeeClass(),
                    SEE_THROUGH_WALLS,
                    Bumblezone.BzBeeAggressionConfig.speedBoostLevel.get() - 1,
                    Bumblezone.BzBeeAggressionConfig.absorptionBoostLevel.get() - 1,
                    Bumblezone.BzBeeAggressionConfig.strengthBoostLevel.get() - 1);
        }
    }

    private static void setAggression(World world, LivingEntity livingEntity, Class<? extends MobEntity> entityToFind, EntityPredicate sightMode, int speed, int absorption, int strength) {
        if(livingEntity instanceof BeeEntity || (ModChecker.carrierBeesPresent && CarrierBeeRedirection.CBGetAppleBeeClass().isInstance(livingEntity))) {
            return;
        }

        sightMode.range(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get());
        List<MobEntity> beeList = world.getNearbyEntities(entityToFind, sightMode, livingEntity, livingEntity.getBoundingBox().inflate(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()));

        for (MobEntity bee : beeList) {
            bee.setTarget(livingEntity);
            if(bee instanceof IAngerable){
                ((IAngerable)bee).setRemainingPersistentAngerTime(20);
                ((IAngerable)bee).setPersistentAngerTarget(livingEntity.getUUID());
            }

            EffectInstance effect = livingEntity.getEffect(BzEffects.WRATH_OF_THE_HIVE.get());
            if(effect != null) {
                int leftoverDuration = effect.getDuration();

                // weaker potion effects for when attacking bears
                bee.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, leftoverDuration, speed, false, false));
                bee.addEffect(new EffectInstance(Effects.ABSORPTION, leftoverDuration, absorption, false, false));
                bee.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, leftoverDuration, strength, false, true));
            }
        }
    }

    /**
     * Calm the bees that are attacking the incoming entity
     */
    public static void calmTheBees(World world, LivingEntity livingEntity)
    {
        clearAggression(world,
                livingEntity,
                BeeEntity.class,
                SEE_THROUGH_WALLS);

        if(ModChecker.carrierBeesPresent){
            clearAggression(world,
                    livingEntity,
                    CarrierBeeRedirection.CBGetAppleBeeClass(),
                    SEE_THROUGH_WALLS);
        }
    }

    private static void clearAggression(World world, LivingEntity livingEntity, Class<? extends MobEntity> entityToFind, EntityPredicate sightMode) {

        sightMode.range(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D);
        List<MobEntity> beeList = world.getNearbyEntities(entityToFind, sightMode, livingEntity, livingEntity.getBoundingBox().inflate(Bumblezone.BzBeeAggressionConfig.aggressionTriggerRadius.get()*0.5D));
        for (MobEntity bee : beeList)
        {
            if(bee.getTarget() == livingEntity) {
                bee.setTarget(null);
                bee.setAggressive(false);
                bee.removeEffect(Effects.DAMAGE_BOOST);
                bee.removeEffect(Effects.MOVEMENT_SPEED);
                bee.removeEffect(Effects.ABSORPTION);

                if(bee instanceof BeeEntity){
                    ((BeeEntity)bee).setRemainingPersistentAngerTime(0);
                }
            }
        }
    }

    // Don't remove wrath effect from mobs that bees are to always be angry at (bears, non-bee insects)
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager attributes, int amplifier) {
        if(BeeAggression.doesBeesHateEntity(entity)){
            //refresh the bee anger timer
            entity.addEffect(new EffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE.get(),
                    Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
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
