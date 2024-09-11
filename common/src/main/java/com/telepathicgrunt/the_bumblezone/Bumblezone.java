package com.telepathicgrunt.the_bumblezone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.advancements.TargetAdvancementDoneTrigger;
import com.telepathicgrunt.the_bumblezone.blocks.InfinityBarrier;
import com.telepathicgrunt.the_bumblezone.blocks.StringCurtain;
import com.telepathicgrunt.the_bumblezone.blocks.datamanagers.CrystallineFlowerDataManager;
import com.telepathicgrunt.the_bumblezone.blocks.datamanagers.PotionCandleDataManager;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantmentApplication;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.pollenpuffentityflowers.PollenPuffEntityPollinateManager;
import com.telepathicgrunt.the_bumblezone.entities.datamanagers.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.BzWorldSavedData;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.ItemUseOnBlock;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.ProjectileImpact;
import com.telepathicgrunt.the_bumblezone.events.block.BzBlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTravelingToDimensionEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzRegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.item.BzRegisterBrewingRecipeEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinDataPacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzDatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzFinalSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterDataSerializersEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterFlammabilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterReloadListenerEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzTagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerEntityInteractEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemAttackBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerPickupItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.items.BuzzingBriefcase;
import com.telepathicgrunt.the_bumblezone.items.DispenserAddedSpawnEgg;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.items.essence.CalmingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.ContinuityEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import com.telepathicgrunt.the_bumblezone.modcompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modinit.BzArmorMaterials;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCommands;
import com.telepathicgrunt.the_bumblezone.modinit.BzCreativeTabs;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootConditionTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzPOI;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import com.telepathicgrunt.the_bumblezone.modinit.BzPotions;
import com.telepathicgrunt.the_bumblezone.modinit.BzPredicates;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzStructurePlacementType;
import com.telepathicgrunt.the_bumblezone.modinit.BzStructures;
import com.telepathicgrunt.the_bumblezone.modinit.BzSubPredicates;
import com.telepathicgrunt.the_bumblezone.modinit.BzSurfaceRules;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.packets.MessageHandler;
import com.telepathicgrunt.the_bumblezone.packets.QueenMainTradesSyncPacket;
import com.telepathicgrunt.the_bumblezone.packets.QueenRandomizerTradesSyncPacket;
import com.telepathicgrunt.the_bumblezone.packets.SyncBeehemothSpeedConfigFromServer;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import com.telepathicgrunt.the_bumblezone.worldgen.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.worldgen.surfacerules.PollinatedSurfaceSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Bumblezone {
    public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    // Note to self:
    // -XX:+AllowEnchancedClassRedefinition arg with JetBrainsRuntime SDK (JBRSDK) + Single HotSwap Plugin to enable better hotswapping
    // -Dmixin.debug.export=true for mixin dump of transformed classes.

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, Bumblezone.MODID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        BzTags.initTags();

        //Events
        BzRegisterCommandsEvent.EVENT.addListener(BzCommands::registerCommand);
        BzEntitySpawnEvent.EVENT.addListener(ModdedBeesBeesSpawning::onEntitySpawn);
        BzPlayerTickEvent.EVENT.addListener(BeeAggression::playerTick);
        BzEntityTickEvent.EVENT.addListener(HoneyBeeLeggings::armorStandTick);
        BzPlayerPickupItemEvent.EVENT.addListener(BeeAggression::pickupItemAnger);
        BzEntityHurtEvent.EVENT_LOWEST.addListener(CalmingEssence::OnAttack);
        BzEntityHurtEvent.EVENT_LOWEST.addListener(BeeAggression::onLivingEntityHurt);
        BzBlockBreakEvent.EVENT_LOWEST.addListener(BeeAggression::minedBlockAnger); // We want to make sure the block will be broken for angering bees
        BzPlayerEntityInteractEvent.EVENT.addListener(BeeInteractivity::onEntityInteractEvent);
        BzEntityDeathEvent.EVENT.addListener(WrathOfTheHiveEffect::onLivingEntityDeath);
        BzEntityDeathEvent.EVENT.addListener(RagingEssence::OnEntityDeath);
        BzEntityDeathEvent.EVENT.addListener(ContinuityEssence::CancelledDeath);
        BzServerLevelTickEvent.EVENT.addListener(BzWorldSavedData::worldTick);
        BzPlayerTickEvent.EVENT.addListener(EntityTeleportationHookup::playerTick);
        BzEntityTickEvent.EVENT.addListener(EntityTeleportationHookup::entityTick);
        BzEntityTravelingToDimensionEvent.EVENT.addListener(EntityTeleportationBackend::entityChangingDimension);
        BzPlayerItemAttackBlockEvent.EVENT_HIGH.addListener(BuzzingBriefcase::onLeftClickBlock);
        BzPlayerItemUseOnBlockEvent.EVENT_HIGH.addListener(StringCurtain::onBlockInteractEvent);
        BzPlayerItemUseOnBlockEvent.EVENT_HIGH.addListener(InfinityBarrier::onBlockInteractEvent);
        BzPlayerItemUseOnBlockEvent.EVENT_HIGH.addListener(ItemUseOnBlock::onItemUseOnBlock); // High because we want to cancel other mod's stuff if it uses on a hive.
        BzPlayerItemUseEvent.EVENT_HIGH.addListener(ItemUseOnBlock::onEarlyItemUseOnBlock); // High because we want to cancel other mod's stuff if it uses on a hive.
        BzProjectileHitEvent.EVENT_HIGH.addListener(ProjectileImpact::onProjectileImpact); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        BzEntityVisibilityEvent.EVENT.addListener(HiddenEffect::hideEntity);
        BzEntityAttackedEvent.EVENT.addListener(NeurotoxinsEnchantmentApplication::entityHurtEvent);
        BzEntityAttackedEvent.EVENT.addListener(HoneyCrystalShield::handledPlayerHurtBehavior);
        BzPlayerBreakSpeedEvent.EVENT.addListener(CombCutterEnchantmentApplication::attemptFasterMining);
        PlayerDataHandler.initEvents();
        BzPlayerGrantAdvancementEvent.EVENT.addListener(TargetAdvancementDoneTrigger::OnAdvancementGiven);
        BzRegisterWanderingTradesEvent.EVENT.addListener(WanderingTrades::addWanderingTrades);
        BzTagsUpdatedEvent.EVENT.addListener(QueensTradeManager.QUEENS_TRADE_MANAGER::resolveQueenTrades);
        BzTagsUpdatedEvent.EVENT.addListener(CrystallineFlowerDataManager.CRYSTALLINE_FLOWER_DATA_MANAGER::resolveFlowerData);
        BzServerGoingToStopEvent.EVENT.addListener(ThreadExecutor::handleServerStoppingEvent);
        BzServerGoingToStartEvent.EVENT.addListener(Bumblezone::serverAboutToStart);
        BzRegisterReloadListenerEvent.EVENT.addListener(Bumblezone::registerDatapackListener);
        BzAddBuiltinResourcePacks.EVENT.addListener(Bumblezone::setupBuiltInResourcePack);
        BzAddBuiltinDataPacks.EVENT.addListener(Bumblezone::setupBuiltInDataPack);
        BzSetupEvent.EVENT.addListener(Bumblezone::setup);
        BzRegisterDataSerializersEvent.EVENT.addListener(Bumblezone::registerDataSerializers);
        BzFinalSetupEvent.EVENT.addListener(Bumblezone::onFinalSetup); //run after all mods
        BzRegisterFlammabilityEvent.EVENT.addListener(Bumblezone::onRegisterFlammablity);
        BzSetupEvent.EVENT.addListener(DispenserAddedSpawnEgg::onSetup);
        BzAddCreativeTabEntriesEvent.EVENT.addListener(BzCreativeTabs::addCreativeTabEntries);
        BzRegisterEntityAttributesEvent.EVENT.addListener(BzEntities::registerEntityAttributes);
        BzRegisterSpawnPlacementsEvent.EVENT.addListener(BzEntities::registerEntitySpawnRestrictions);
        BzDatapackSyncEvent.EVENT.addListener(QueenRandomizerTradesSyncPacket::sendToClient);
        BzDatapackSyncEvent.EVENT.addListener(QueenMainTradesSyncPacket::sendToClient);
        BzDatapackSyncEvent.EVENT.addListener(SyncBeehemothSpeedConfigFromServer::sendToClient);
        BzRegisterBrewingRecipeEvent.EVENT.addListener(BzRecipes::registerBrewingStandRecipes);

        //Registration
        BzItems.ITEMS.init();
        BzBlocks.BLOCKS.init();
        BzFluids.FLUIDS.init();
        BzPOI.POI_TYPES.init();
        BzRecipes.RECIPES.init();
        BzPotions.POTIONS.init();
        BzEffects.EFFECTS.init();
        BzMenuTypes.MENUS.init();
        BzStats.CUSTOM_STAT.init();
        BzFeatures.FEATURES.init();
        BzEntities.ENTITIES.init();
        BzFluids.FLUID_TYPES.init();
        BzSounds.SOUND_EVENTS.init();
        BzPredicates.RULE_TEST.init();
        BzStructures.STRUCTURES.init();
        BzDimension.BIOME_SOURCE.init();
        BzParticles.PARTICLE_TYPES.init();
        BzPredicates.POS_RULE_TEST.init();
        BzDimension.CHUNK_GENERATOR.init();
        BzSurfaceRules.SURFACE_RULES.init();
        BzDimension.DENSITY_FUNCTIONS.init();
        BzSubPredicates.SUB_PREDICATES.init();
        BzBlockEntities.BLOCK_ENTITIES.init();
        BzCriterias.CRITERION_TRIGGERS.init();
        BzArmorMaterials.ARMOR_MATERIAL.init();
        BzPlacements.PLACEMENT_MODIFIER.init();
        BzProcessors.STRUCTURE_PROCESSOR.init();
        BzCreativeTabs.CREATIVE_MODE_TABS.init();
       // BzBiomeHeightRegistry.BIOME_HEIGHT.init();
        BzDataComponents.DATA_COMPONENT_TYPE.init();
        BzLootFunctionTypes.LOOT_ITEM_FUNCTION_TYPE.init();
        BzLootConditionTypes.LOOT_ITEM_CONDITION_TYPE.init();
        BzEnchantments.ENCHANTMENT_EFFECT_COMPONENT_TYPE.init();
        BzStructurePlacementType.STRUCTURE_PLACEMENT_TYPE.init();
    }

    public static void onRegisterFlammablity(BzRegisterFlammabilityEvent event) {
        BzBlocks.CURTAINS.stream().map(RegistryEntry::get).forEach(block -> event.register(block, 60, 20));
        event.register(BzBlocks.HONEY_COCOON.get(), 200, 20);
        event.register(BzBlocks.PILE_OF_POLLEN.get(), BzGeneralConfigs.pileOfPollenHyperFireSpread ? 400 : 10, 40);
        event.register(BzBlocks.PILE_OF_POLLEN_SUSPICIOUS.get(), BzGeneralConfigs.pileOfPollenHyperFireSpread ? 400 : 10, 40);
    }

    private static void setup(final BzSetupEvent event) {
    	event.enqueueWork(() -> {
            BeeAggression.setupBeeHatingList();
            BzStats.initStatEntries();
            BzItems.setupCauldronCompat();
            BzItems.setupDispenserBehaviors();
		});
        MessageHandler.init();
    }

    private static void registerDataSerializers(BzRegisterDataSerializersEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "queen_pose"), BeeQueenEntity.QUEEN_POSE_SERIALIZER);
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "rootmin_pose"), RootminEntity.ROOTMIN_POSE_SERIALIZER);
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "cosmic_crystal_state"), CosmicCrystalEntity.COSMIC_CRYSTAL_STATE_SERIALIZER);
    }

    private static void onFinalSetup(final BzFinalSetupEvent event) {
        event.enqueueWork(DispenserItemSetup::setupDispenserBehaviors);
    }

    public static void registerDatapackListener(final BzRegisterReloadListenerEvent event) {
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "queens_trades"), QueensTradeManager.QUEENS_TRADE_MANAGER);
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "pollen_puff"), PollenPuffEntityPollinateManager.POLLEN_PUFF_ENTITY_POLLINATE_MANAGER);
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "crystalline_flower"), CrystallineFlowerDataManager.CRYSTALLINE_FLOWER_DATA_MANAGER);
        event.register(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle"), PotionCandleDataManager.POTION_CANDLE_DATA_MANAGER);
    }

    private static void serverAboutToStart(final BzServerGoingToStartEvent event) {
        PollinatedSurfaceSource.RandomLayerStateRule.initNoise(event.getServer().getWorldData().worldGenOptions().seed());
        BiomeRegistryHolder.setupBiomeRegistry(event.getServer());
        ThreadExecutor.setupExecutorService();
    }

    private static void setupBuiltInResourcePack(final BzAddBuiltinResourcePacks event) {
        event.add(
                ResourceLocation.fromNamespaceAndPath(MODID, "anti_tropophobia"),
                Component.literal("Bumblezone - Anti Trypophobia"),
                BzAddBuiltinResourcePacks.PackMode.USER_CONTROLLED
        );
    }

    public static final List<Consumer<BzAddBuiltinDataPacks>> MOD_COMPAT_DATAPACKS = new ArrayList<>();
    private static void setupBuiltInDataPack(final BzAddBuiltinDataPacks event) {
        MOD_COMPAT_DATAPACKS.forEach(consumer -> consumer.accept(event));
    }
}
