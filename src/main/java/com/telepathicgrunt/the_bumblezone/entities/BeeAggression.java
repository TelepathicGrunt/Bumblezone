package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

public class BeeAggression {
    private static final Set<EntityType<?>> SET_OF_BEE_HATED_ENTITIES = new HashSet<>();

    //bees attack player that picks up honey blocks
    public static void HoneyPickupEvent(PlayerEvent.ItemPickupEvent event)
    {
        BeeAggression.honeyPickupAnger(event.getPlayer(), event.getStack().getItem());
    }

    /*
     * Have to run this code at world startup because the only way to check a CreatureAttribute
     * from an EntityType is the make an Entity but you cannot pass null into Entitytype.create(null)
     * because some mobs will crash the game. Thus, that's why this code runs here instead of in FMLCommonSetupEvent.
     *
     *  gg. Mojang. gg.
     *
     *  But yeah, this sets up the list of entitytype of mobs for bees to always attack. Making
     *  the list can be expensive which is why we make it at start of world rather than every tick.
     */
    public static void setupBeeHatingList(World world)
    {
        // Build list only once
        if(SET_OF_BEE_HATED_ENTITIES.size() != 0) return;

        for(EntityType<?> entityType : Registry.ENTITY_TYPE)
        {
            if(entityType.getClassification() == EntityClassification.MONSTER ||
                    entityType.getClassification() == EntityClassification.CREATURE ||
                    entityType.getClassification() == EntityClassification.AMBIENT )
            {
                Entity entity;

                // We could crash if a modded entity is super picky about when they are created or if
                // the mob we grab is actually unfinished and wasn't supposed to be created.
                // If it does fail to be made, use weaker way to check if bear or wasp.
                try {
                    entity = entityType.create(world);
                }
                catch(Exception e){
                    Bumblezone.LOGGER.log(Level.WARN, "Failed to temporary create " + Registry.ENTITY_TYPE.getKey(entityType) +
                            " mob in order to check if it is an arthropod that bees should be naturally angry at. " +
                            "Will check if mob is a bear or wasp in its name instead. Error message is: " + e.getMessage());

                    String mobName = Registry.ENTITY_TYPE.getKey(entityType).toString();
                    if(mobName.contains("bear") || mobName.contains("wasp"))
                    {
                        SET_OF_BEE_HATED_ENTITIES.add(entityType);
                    }
                    continue;
                }

                if(entity instanceof MobEntity)
                {
                    String mobName = Registry.ENTITY_TYPE.getKey(entityType).toString();
                    MobEntity mobEntity = (MobEntity) entity;

                    if((mobEntity.getCreatureAttribute() == CreatureAttribute.ARTHROPOD && !mobName.contains("bee")) ||
                            mobEntity instanceof PandaEntity ||
                            mobName.contains("bear") ||
                            mobName.contains("wasp"))
                    {
                        SET_OF_BEE_HATED_ENTITIES.add(entityType);
                    }
                }
            }
        }
    }

    //bees attack player that picks up honey blocks
    public static void honeyPickupAnger(PlayerEntity player, Item item)
    {
        //Bumblezone.LOGGER.log(Level.INFO, "started");

        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if ((player.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) ||
                Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                !player.isCreative() &&
                !player.isSpectator()) {

            //if player picks up a honey block, bees gets very mad...
            if (item == Items.HONEY_BLOCK && Bumblezone.BzBeeAggressionConfig.aggressiveBees.get()) {
                if(player.isPotionActive(BzEffects.PROTECTION_OF_THE_HIVE.get())){
                    player.removePotionEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                }
                else {
                    //Bumblezone.LOGGER.log(Level.INFO, "ANGRY BEES");
                    player.addPotionEffect(new EffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(),
                            true));
                }
            }
        }
    }


    //bees attack player that drinks honey.
    public static void honeyDrinkAnger(ItemStack stack, World world, LivingEntity user)
    {
        // Bumblezone.LOGGER.log(Level.INFO, "just drank");
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) user;

            //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
            //Also checks to make sure we are in dimension and that player isn't in creative or spectator
            if (!world.isRemote &&
                    (playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) ||
                            Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BzBeeAggressionConfig.aggressiveBees.get())
            {

                //if player drinks honey, bees gets very mad...
                if (stack.getItem() == Items.HONEY_BOTTLE) {
                    if(playerEntity.isPotionActive(BzEffects.PROTECTION_OF_THE_HIVE.get())){
                        playerEntity.removePotionEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                    }
                    else{
                        playerEntity.addPotionEffect(new EffectInstance(
                                BzEffects.WRATH_OF_THE_HIVE.get(),
                                Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                                2,
                                false,
                                Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(),
                                true));
                    }
                }
            }
        }
    }


    //Bees hit by a mob or player will inflict Wrath of the Hive onto the attacker.
    public static void beeHitAndAngered(Entity entity, Entity attackerEntity)
    {
        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that if it is a player, that they aren't in creative or spectator
        if (!entity.world.isRemote &&
                (entity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                Bumblezone.BzBeeAggressionConfig.aggressiveBees.get() &&
                entity instanceof BeeEntity &&
                attackerEntity != null)
        {
            if(attackerEntity instanceof PlayerEntity &&
                    !((PlayerEntity)attackerEntity).isCreative() &&
                    !attackerEntity.isSpectator())
            {
                PlayerEntity player = ((PlayerEntity) attackerEntity);
                if(player.isPotionActive(BzEffects.PROTECTION_OF_THE_HIVE.get())){
                    player.removePotionEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                    WrathOfTheHiveEffect.calmTheBees(player.world, player); // prevent bees from be naturally angry
                }
                else {
                    player.addPotionEffect(new EffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(),
                            true));
                }
            }
            else if(attackerEntity instanceof MobEntity)
            {
                MobEntity mob = ((MobEntity) attackerEntity);
                if(mob.isPotionActive(BzEffects.PROTECTION_OF_THE_HIVE.get())){
                    mob.removePotionEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                    WrathOfTheHiveEffect.calmTheBees(mob.world, mob); // prevent bees from be naturally angry
                }
                else {
                    mob.addPotionEffect(new EffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            true));
                }
            }
        }
    }


    //bees attacks bear and insect mobs that are in the dimension
    public static void entityTypeBeeAnger(Entity entity)
    {
        if(doesBeesHateEntity(entity)) {
            ((MobEntity) entity).addPotionEffect(new EffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE.get(),
                    Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                    1,
                    false,
                    true));
        }
    }

    //Returns true if bees hate the entity type. (bears, non-bee insects)
    public static boolean doesBeesHateEntity(Entity entity){

        //Also checks to make sure we are in the dimension.
        if (!entity.world.isRemote &&
                entity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) &&
                Bumblezone.BzBeeAggressionConfig.aggressiveBees.get() &&
                entity instanceof MobEntity)
        {
            MobEntity mobEntity = (MobEntity)entity;

            //must be a bear or insect animal with no wrath of the hive effect on
            return SET_OF_BEE_HATED_ENTITIES.contains(entity.getType()) && !mobEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE.get());
        }

        return false;
    }

    public static void playerTick(PlayerEntity playerEntity)
    {
        //removes the wrath of the hive if it is disallowed outside dimension
        if(!playerEntity.world.isRemote &&
                playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE.get()) &&
                !(Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get() ||
                        playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)))
        {
            playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE.get());
            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
        }

        //Makes the fog redder when this effect is active
        boolean wrathEffect = playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE.get());
        if(!WrathOfTheHiveEffect.ACTIVE_WRATH && wrathEffect)
        {
            WrathOfTheHiveEffect.ACTIVE_WRATH = true;
        }
        else if(WrathOfTheHiveEffect.ACTIVE_WRATH && !wrathEffect)
        {
            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
            WrathOfTheHiveEffect.ACTIVE_WRATH = false;
        }
    }
}