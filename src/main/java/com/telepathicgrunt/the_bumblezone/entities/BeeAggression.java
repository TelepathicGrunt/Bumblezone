package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;
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
    public static void setupBeeHatingList(final ServerAboutToStartEvent event) {
        ServerLevel world = event.getServer().overworld();

        // Build list only once
        if(SET_OF_BEE_HATED_ENTITIES.size() != 0) return;

        for(EntityType<?> entityType : ForgeRegistries.ENTITIES) {
            if(entityType.getCategory() == MobCategory.MONSTER ||
                entityType.getCategory() == MobCategory.CREATURE ||
                entityType.getCategory() == MobCategory.AMBIENT )
            {
                Entity entity;

                // We could crash if a modded entity is super picky about when they are created or if
                // the mob we grab is actually unfinished and wasn't supposed to be created.
                // If it does fail to be made, use weaker way to check if bear or wasp.
                try {
                    entity = entityType.create(world);
                }
                catch(Exception e) {
                    Bumblezone.LOGGER.log(Level.WARN, "Failed to temporary create " + Registry.ENTITY_TYPE.getResourceKey(entityType) +
                            " mob in order to check if it is an arthropod that bees should be naturally angry at. " +
                            "Will check if mob is a bear or wasp in its name instead. Error message is: " + e.getMessage());

                    String mobName = ForgeRegistries.ENTITIES.getResourceKey(entityType).toString();
                    if(mobName.contains("bear") || mobName.contains("wasp")) {
                        SET_OF_BEE_HATED_ENTITIES.add(entityType);
                    }
                    continue;
                }

                if(entity instanceof Mob mobEntity) {
                    String mobName = ForgeRegistries.ENTITIES.getKey(entityType).toString();

                    if((mobEntity.getMobType() == MobType.ARTHROPOD && !mobName.contains("bee")) ||
                            mobEntity instanceof Panda ||
                            mobName.contains("bear") ||
                            mobName.contains("wasp"))
                    {
                        SET_OF_BEE_HATED_ENTITIES.add(entityType);
                    }
                }
            }
        }
    }

    //if player mines a tagged angerable block, bees gets very mad...
    public static void minedBlockAnger(BlockEvent.BreakEvent event) {
        if(event.isCanceled()) return;

        Player player = event.getPlayer();
        BlockState blockState = event.getState();

        if (BzBlockTags.WRATH_ACTIVATING_BLOCKS_WHEN_MINED.contains(blockState.getBlock())) {
            angerBees(player);
        }
    }

    //if player picks up a tagged angerable item, bees gets very mad...
    public static void pickupItemAnger(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getPlayer();
        Item item = event.getStack().getItem();

        if (BzItemTags.WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP.contains(item)) {
            angerBees(player);
        }
    }

    private static void angerBees(Player player) {
        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if ((player.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                BzBeeAggressionConfigs.aggressiveBees.get() &&
                !player.isCreative() &&
                !player.isSpectator())
        {
            if(!player.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                //Bumblezone.LOGGER.log(Level.INFO, "ANGRY BEES");
                player.addEffect(new MobEffectInstance(
                        BzEffects.WRATH_OF_THE_HIVE.get(),
                        BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(),
                        2,
                        false,
                        BzBeeAggressionConfigs.showWrathOfTheHiveParticles.get(),
                        true));
            }
        }
    }

    //Bees hit by a mob or player will inflict Wrath of the Hive onto the attacker.
    public static void beeHitAndAngered(Entity entity, Entity attackerEntity) {
        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that if it is a player, that they aren't in creative or spectator
        if (!entity.level.isClientSide() &&
                entity instanceof Bee &&
                attackerEntity != null)
        {
            if(attackerEntity instanceof Player player &&
                    !((Player)attackerEntity).isCreative() &&
                    !attackerEntity.isSpectator())
            {
                if(player.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                    player.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                    WrathOfTheHiveEffect.calmTheBees(player.level, player); // prevent bees from be naturally angry
                }
                else if((entity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                        BzBeeAggressionConfigs.aggressiveBees.get())
                {
                    if(player instanceof ServerPlayer && player.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get())) {
                        BzCriterias.EXTENDED_WRATH_OF_THE_HIVE_TRIGGER.trigger((ServerPlayer) player, attackerEntity);
                    }

                    player.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            BzBeeAggressionConfigs.showWrathOfTheHiveParticles.get(),
                            true));
                }
            }
            else if(attackerEntity instanceof Mob mob) {
                if(mob.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                    mob.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                    WrathOfTheHiveEffect.calmTheBees(mob.level, mob); // prevent bees from be naturally angry
                }
                else if((entity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                        BzBeeAggressionConfigs.aggressiveBees.get())
                {
                    mob.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            true));
                }
            }
        }
    }


    //bees attacks bear and insect mobs that are in the dimension
    public static void entityTypeBeeAnger(Entity entity) {
        if(doesBeesHateEntity(entity)) {
            ((Mob) entity).addEffect(new MobEffectInstance(
                    BzEffects.WRATH_OF_THE_HIVE.get(),
                    BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(),
                    1,
                    false,
                    true));
        }
    }

    //Returns true if bees hate the entity type. (bears, non-bee insects)
    public static boolean doesBeesHateEntity(Entity entity) {

        //Also checks to make sure we are in the dimension.
        if (!entity.level.isClientSide() &&
                entity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
                BzBeeAggressionConfigs.aggressiveBees.get() &&
                entity instanceof Mob mobEntity)
        {
            //must be a bear or insect animal with no wrath of the hive effect on
            return SET_OF_BEE_HATED_ENTITIES.contains(entity.getType()) && !mobEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get());
        }

        return false;
    }

    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player playerEntity = event.player;

        //removes the wrath of the hive if it is disallowed outside dimension
        if(!playerEntity.level.isClientSide() &&
                playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get()) &&
                !(BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get() ||
                        playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)))
        {
            playerEntity.removeEffect(BzEffects.WRATH_OF_THE_HIVE.get());
            WrathOfTheHiveEffect.calmTheBees(playerEntity.level, playerEntity);
        }

        //Makes the fog redder when this effect is active
        if(playerEntity.level.isClientSide()) {
            boolean wrathEffect = playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get());
            if(wrathEffect) {
                MusicHandler.playAngryBeeMusic(playerEntity);
            }

            if(!WrathOfTheHiveEffect.ACTIVE_WRATH && wrathEffect) {
                WrathOfTheHiveEffect.ACTIVE_WRATH = true;
            }
            else if(WrathOfTheHiveEffect.ACTIVE_WRATH && !wrathEffect) {
                MusicHandler.stopAngryBeeMusic(playerEntity);
                WrathOfTheHiveEffect.calmTheBees(playerEntity.level, playerEntity);
                WrathOfTheHiveEffect.ACTIVE_WRATH = false;
            }
        }
    }
}