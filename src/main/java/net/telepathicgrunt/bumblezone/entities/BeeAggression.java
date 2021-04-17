package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.client.BumblezoneClient;
import net.telepathicgrunt.bumblezone.client.MusicHandler;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.telepathicgrunt.bumblezone.modinit.BzEffects;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

public class BeeAggression {
    private static final Set<EntityType<?>> SET_OF_BEE_HATED_ENTITIES = new HashSet<>();

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
            if(entityType.getSpawnGroup() == SpawnGroup.MONSTER ||
                    entityType.getSpawnGroup() == SpawnGroup.CREATURE ||
                    entityType.getSpawnGroup() == SpawnGroup.AMBIENT )
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
                    String mobName = Registry.ENTITY_TYPE.getId(entityType).toString();
                    MobEntity mobEntity = (MobEntity) entity;

                    if((mobEntity.getGroup() == EntityGroup.ARTHROPOD && !mobName.contains("bee")) ||
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
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                !player.isCreative() &&
                !player.isSpectator()) {

            //if player picks up a honey block, bees gets very mad...
            if (item == Items.HONEY_BLOCK && Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees) {
                if(player.hasStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE)){
                    player.removeStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                }
                else {
                    //Bumblezone.LOGGER.log(Level.INFO, "ANGRY BEES");
                    player.addStatusEffect(new StatusEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                            2,
                            false,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.showWrathOfTheHiveParticles,
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
            if (!world.isClient &&
                    (playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) ||
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees)
            {

                //if player drinks honey, bees gets very mad...
                if (stack.getItem() == Items.HONEY_BOTTLE) {
                    if(playerEntity.hasStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE)){
                        playerEntity.removeStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    }
                    else{
                        playerEntity.addStatusEffect(new StatusEffectInstance(
                                BzEffects.WRATH_OF_THE_HIVE,
                                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                                2,
                                false,
                                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.showWrathOfTheHiveParticles,
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
        if (!entity.world.isClient &&
                (entity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees &&
                entity instanceof BeeEntity &&
                attackerEntity != null)
        {
            if(attackerEntity instanceof PlayerEntity &&
                    !((PlayerEntity)attackerEntity).isCreative() &&
                    !attackerEntity.isSpectator())
            {
                PlayerEntity player = ((PlayerEntity) attackerEntity);
                if(player.hasStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE)){
                    player.removeStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    WrathOfTheHiveEffect.calmTheBees(player.world, player); // prevent bees from be naturally angry
                }
                else {
                    player.addStatusEffect(new StatusEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                            2,
                            false,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.showWrathOfTheHiveParticles,
                            true));
                }
            }
            else if(attackerEntity instanceof MobEntity)
            {
                MobEntity mob = ((MobEntity) attackerEntity);
                if(mob.hasStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE)){
                    mob.removeStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    WrathOfTheHiveEffect.calmTheBees(mob.world, mob); // prevent bees from be naturally angry
                }
                else {
                    mob.addStatusEffect(new StatusEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
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
            ((MobEntity) entity).addStatusEffect(new StatusEffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE,
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts,
                    1,
                    false,
                    true));
        }
    }

    //Returns true if bees hate the entity type. (bears, non-bee insects)
    public static boolean doesBeesHateEntity(Entity entity){

        //Also checks to make sure we are in the dimension.
        if (!entity.world.isClient &&
                entity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) &&
                Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees &&
                entity instanceof MobEntity)
        {
            MobEntity mobEntity = (MobEntity)entity;

            //must be a bear or insect animal with no wrath of the hive effect on
            return SET_OF_BEE_HATED_ENTITIES.contains(entity.getType()) && !mobEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
        }

        return false;
    }

    public static void playerTick(PlayerEntity playerEntity)
    {
        //removes the wrath of the hive if it is disallowed outside dimension
        if(!playerEntity.world.isClient() &&
                playerEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE) &&
                !(Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone ||
                        playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)))
        {
            playerEntity.removeStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
        }

        //Makes the fog redder when this effect is active
        if(playerEntity.world.isClient()){
            boolean wrathEffect = playerEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
            if(wrathEffect){
                MusicHandler.playAngryBeeMusic(playerEntity);
            }

            if(!WrathOfTheHiveEffect.ACTIVE_WRATH && wrathEffect) {
                WrathOfTheHiveEffect.ACTIVE_WRATH = true;
            }
            else if(WrathOfTheHiveEffect.ACTIVE_WRATH && !wrathEffect) {
                MusicHandler.stopAngryBeeMusic(playerEntity);
                WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
                WrathOfTheHiveEffect.ACTIVE_WRATH = false;
            }
        }
    }
}