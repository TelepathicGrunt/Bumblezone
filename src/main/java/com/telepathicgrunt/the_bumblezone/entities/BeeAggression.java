package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeeAggression {

    public static void setupEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register((MinecraftServer minecraftServer) ->
                BeeAggression.setupBeeHatingList());

        PlayerBlockBreakEvents.AFTER.register((world, playerEntity, blockPos, blockState, blockEntity) ->
                blockBreakAnger(playerEntity, blockState.getBlock()));
    }


    private static final Set<String> LIST_OF_BEE_HATING_NAMES = Set.of("bear", "panda", "wasp", "spider");
    private static final Set<EntityType<?>> SET_OF_BEE_HATED_NAMED_ENTITIES = new HashSet<>();
    private static final Set<EntityType<?>> SET_OF_BEE_NAMED_ENTITIES = new HashSet<>();

    /*
     * This sets up the list of entitytype of mobs for bees to always attack if named triggers some keywords.
     * Making the list can be expensive which is why we make it at start of world rather than every tick.
     */
    public static void setupBeeHatingList() {
        if(SET_OF_BEE_HATED_NAMED_ENTITIES.size() != 0) return;
        if(SET_OF_BEE_NAMED_ENTITIES.size() != 0) return;

        for(Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : Registry.ENTITY_TYPE.entrySet()) {

            String mobName = entry.getKey().location().getPath();

            if(mobName.contains("bee")) {
                SET_OF_BEE_NAMED_ENTITIES.add(entry.getValue());
            }
            if(LIST_OF_BEE_HATING_NAMES.stream().anyMatch(mobName::contains)) {
                SET_OF_BEE_HATED_NAMED_ENTITIES.add(entry.getValue());
            }
        }
    }

    //if player mines an angerable tagged block, bees gets very mad...
    public static void blockBreakAnger(Player player, Block block) {
        if (player instanceof ServerPlayer serverPlayer && block.defaultBlockState().is(BzTags.WRATH_ACTIVATING_BLOCKS_WHEN_MINED)) {
            angerBees(serverPlayer);
        }
    }

    //if player picks up an angerable tagged item, bees gets very mad...
    public static void itemPickupAnger(Player player, Item item) {
        if (player instanceof ServerPlayer serverPlayer && item.getDefaultInstance().is(BzTags.WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP)) {
            angerBees(serverPlayer);
        }
    }

    private static void angerBees(ServerPlayer player) {
        //Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
        //Also checks to make sure we are in dimension and that player isn't in creative or spectator
        if ((player.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                BzConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                BzConfig.aggressiveBees &&
                !player.isCreative() &&
                !player.isSpectator())
        {
            if(!player.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                if (!EssenceOfTheBees.hasEssence(player)) {
                    Component message = Component.translatable("system.the_bumblezone.no_protection").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
                    player.displayClientMessage(message, true);

                    player.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            BzConfig.howLongWrathOfTheHiveLasts,
                            2,
                            false,
                            BzConfig.showWrathOfTheHiveParticles,
                            true));
                }
            }
            else {
                BzCriterias.HONEY_PERMISSION_TRIGGER.trigger(player);
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
                if(player.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                    player.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    WrathOfTheHiveEffect.calmTheBees(player.level, player); // prevent bees from be naturally angry
                }
                else if((entity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        BzConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                        BzConfig.aggressiveBees)
                {
                    if(player instanceof ServerPlayer && player.hasEffect(BzEffects.WRATH_OF_THE_HIVE)) {
                        BzCriterias.EXTENDED_WRATH_OF_THE_HIVE_TRIGGER.trigger((ServerPlayer) player, attackerEntity);
                    }

                    player.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            BzConfig.howLongWrathOfTheHiveLasts,
                            2,
                            false,
                            BzConfig.showWrathOfTheHiveParticles,
                            true));
                }
            }
            else if(attackerEntity instanceof Mob mob) {
                if(mob.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                    mob.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    WrathOfTheHiveEffect.calmTheBees(mob.level, mob); // prevent bees from be naturally angry
                }
                else if((entity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                        BzConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                        BzConfig.aggressiveBees)
                {
                    mob.addEffect(new MobEffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE,
                            BzConfig.howLongWrathOfTheHiveLasts,
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
                    BzEffects.WRATH_OF_THE_HIVE,
                    BzConfig.howLongWrathOfTheHiveLasts,
                    1,
                    false,
                    true));
        }
    }

    //Returns true if bees hate the entity type. (bears, non-bee insects)
    public static boolean doesBeesHateEntity(Entity entity) {

        //Also checks to make sure we are in the dimension.
        if (entity != null &&
            entity.level != null &&
            !entity.level.isClientSide() &&
            entity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            BzConfig.aggressiveBees &&
            entity instanceof Mob mobEntity)
        {
            //must be a bear or insect animal with no wrath of the hive effect on
            if(SET_OF_BEE_HATED_NAMED_ENTITIES.contains(entity.getType()) ||
                (!SET_OF_BEE_NAMED_ENTITIES.contains(entity.getType()) && mobEntity.getMobType() == MobType.ARTHROPOD))
            {
                return !mobEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE);
            }
        }

        return false;
    }

    public static boolean isBeelikeEntity(Entity entity) {
        return SET_OF_BEE_NAMED_ENTITIES.contains(entity.getType());
    }

    public static void playerTick(Player playerEntity) {
        //removes the wrath of the hive if it is disallowed outside dimension
        if(!playerEntity.level.isClientSide() &&
                playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE) &&
                !(BzConfig.allowWrathOfTheHiveOutsideBumblezone ||
                        playerEntity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)))
        {
            playerEntity.removeEffect(BzEffects.WRATH_OF_THE_HIVE);
            WrathOfTheHiveEffect.calmTheBees(playerEntity.level, playerEntity);
        }

        //Makes the fog redder when this effect is active
        if(playerEntity.level.isClientSide()) {
            boolean wrathEffect = playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE);
            if(wrathEffect) {
                if(BzConfig.playWrathOfHiveEffectMusic) {
                    MusicHandler.playAngryBeeMusic(playerEntity);
                }
                else {
                    MusicHandler.stopAngryBeeMusic(playerEntity);
                }
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

    // Makes bees angry if in Cell Maze or other tagged structures.
    public static void applyAngerIfInTaggedStructures(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative() || serverPlayer.isSpectator() || !BzConfig.aggressiveBees) {
            return;
        }

        StructureManager structureManager = ((ServerLevel)serverPlayer.level).structureManager();
        if (structureManager.getStructureWithPieceAt(serverPlayer.blockPosition(), BzTags.WRATH_CAUSING).isValid()) {
            if (!serverPlayer.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                if (!serverPlayer.hasEffect(BzEffects.WRATH_OF_THE_HIVE)) {
                    Component message = Component.translatable("system.the_bumblezone.no_protection").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
                    serverPlayer.displayClientMessage(message, true);
                }

                serverPlayer.addEffect(new MobEffectInstance(
                        BzEffects.WRATH_OF_THE_HIVE,
                        BzConfig.howLongWrathOfTheHiveLasts,
                        2,
                        false,
                        BzConfig.showWrathOfTheHiveParticles,
                        true));
            }
        }
    }

    // Make Essence of the Bees players be able to take honey from hive blocks/break hive blocks without angering bees
    public static void preventAngerOnEssencedPlayers(List<Bee> beeList, List<Player> playerList) {
        for (int i = playerList.size() - 1; i >= 0; i--) {
            Player player = playerList.get(i);
            if (player instanceof ServerPlayer serverPlayer && EssenceOfTheBees.hasEssence(serverPlayer)) {
                for (Bee bee : beeList) {
                    if (bee.getTarget() == player) {
                        bee.setTarget(null);
                    }
                }
                playerList.remove(i);
            }
        }
    }

    // Make Essence of the Bees players be able to take shear hive blocks without angering bees
    public static void preventAngerOnEssencedPlayers(Player player, List<Entity> entityList) {
        // Don't spawn bees on client side. Server can handle that just fine. Prevents bees from briefly appearing on clientside for 1 frame.
        if (player != null && player.level.isClientSide() || (player instanceof ServerPlayer serverPlayer && EssenceOfTheBees.hasEssence(serverPlayer))) {
            entityList.clear();
        }
    }
}