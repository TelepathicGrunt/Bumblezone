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
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;

import java.util.HashSet;
import java.util.Set;

public class BeeAggression {
    private static Set<EntityType<?>> SetOfBeeHatedEntities = new HashSet<EntityType<?>>();

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
        if(SetOfBeeHatedEntities.size() != 0) return;

        for(EntityType<?> entityType : Registry.ENTITY_TYPE)
        {
            if(entityType.getSpawnGroup() == SpawnGroup.MONSTER ||
                    entityType.getSpawnGroup() == SpawnGroup.CREATURE ||
                    entityType.getSpawnGroup() == SpawnGroup.AMBIENT )
            {
                Entity entity = entityType.create(world);
                if(entity instanceof MobEntity)
                {
                    String mobName = entityType.getLootTableId().toString();
                    MobEntity mobEntity = (MobEntity) entity;

                    if((mobEntity.getGroup() == EntityGroup.ARTHROPOD && !mobName.contains("bee")) ||
                            mobEntity instanceof PandaEntity ||
                            mobName.contains("bear"))
                    {
                        SetOfBeeHatedEntities.add(entityType);
                    }
                }
            }
        }
    }

    //bees attack player that picks up honey blocks
    public static void honeyPickupAnger(PlayerEntity player, Item item)
    {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        World world = player.world;

        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if ((player.getEntityWorld().getRegistryKey().getValue() == Bumblezone.MOD_FULL_ID  || Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone) &&
                !player.isCreative() &&
                !player.isSpectator()) {
            //if player picks up a honey block, bees gets very mad...
            if (item == Items.HONEY_BLOCK && Bumblezone.BZ_CONFIG.aggressiveBees) {
                //Bumblezone.LOGGER.log(Level.INFO, "ANGRY BEES");
                player.addStatusEffect(new StatusEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts, 2, false, Bumblezone.BZ_CONFIG.showWrathOfTheHiveParticles, true));
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
                    (playerEntity.getEntityWorld().getRegistryKey().getValue() == Bumblezone.MOD_FULL_ID || Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator()) {
                //if player drinks honey, bees gets very mad...
                if (stack.getItem() == Items.HONEY_BOTTLE && Bumblezone.BZ_CONFIG.aggressiveBees) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts, 2, false, Bumblezone.BZ_CONFIG.showWrathOfTheHiveParticles, true));
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
                (entity.getEntityWorld().getRegistryKey().getValue() == Bumblezone.MOD_FULL_ID  || Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone) &&
                Bumblezone.BZ_CONFIG.aggressiveBees &&
                entity instanceof BeeEntity &&
                attackerEntity != null)
        {
            if(attackerEntity instanceof PlayerEntity &&
                    !((PlayerEntity)attackerEntity).isCreative() &&
                    !((PlayerEntity)attackerEntity).isSpectator())
            {
                ((PlayerEntity)attackerEntity).addStatusEffect(new StatusEffectInstance(
                        BzEffects.WRATH_OF_THE_HIVE,
                        Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts,
                        2,
                        false,
                        Bumblezone.BZ_CONFIG.showWrathOfTheHiveParticles,
                        true));
            }
            else if(attackerEntity instanceof MobEntity)
            {
                ((MobEntity)attackerEntity).addStatusEffect(new StatusEffectInstance(
                        BzEffects.WRATH_OF_THE_HIVE,
                        Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts,
                        2,
                        false,
                        true));
            }
        }
    }


    //bees attacks bear and insect mobs that are in the dimension
    public static void entityTypeBeeAnger(Entity entity)
    {
        //Also checks to make sure we are in the dimension.
        if (!entity.world.isClient &&
                entity.getEntityWorld().getRegistryKey().getValue() == Bumblezone.MOD_FULL_ID  &&
                Bumblezone.BZ_CONFIG.aggressiveBees &&
                entity instanceof MobEntity)
        {
            MobEntity mobEntity = (MobEntity)entity;

            //must be a bear or insect animal with no wrath of the hive effect on
            if(SetOfBeeHatedEntities.contains(entity.getType()) && !mobEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE))
            {
                ((MobEntity)entity).addStatusEffect(new StatusEffectInstance(
                        BzEffects.WRATH_OF_THE_HIVE,
                        Bumblezone.BZ_CONFIG.howLongWrathOfTheHiveLasts,
                        1,
                        false,
                        true));
            }
        }
    }


    public static void playerTick(PlayerEntity playerEntity)
    {
        //removes the wrath of the hive if it is disallowed outside dimension
        if(!playerEntity.world.isClient &&
                playerEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE) &&
                !(Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone ||
                        playerEntity.getEntityWorld().getRegistryKey().getValue() == Bumblezone.MOD_FULL_ID))
        {
            playerEntity.removeStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
        }

        //Makes the fog redder when this effect is active
        Boolean wrathEffect = playerEntity.hasStatusEffect(BzEffects.WRATH_OF_THE_HIVE);
        if(WrathOfTheHiveEffect.ACTIVE_WRATH == false && wrathEffect)
        {
            WrathOfTheHiveEffect.ACTIVE_WRATH = true;
        }
        else if(WrathOfTheHiveEffect.ACTIVE_WRATH == true && !wrathEffect)
        {
            WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
            WrathOfTheHiveEffect.ACTIVE_WRATH = false;
        }
    }
}