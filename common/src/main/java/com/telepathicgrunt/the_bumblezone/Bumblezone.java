package com.telepathicgrunt.the_bumblezone;

import com.mojang.logging.LogUtils;
import com.telepathicgrunt.the_bumblezone.advancements.TargetAdvancementDoneTrigger;
import com.telepathicgrunt.the_bumblezone.blocks.IncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.StringCurtain;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.ItemUseOnBlock;
import com.telepathicgrunt.the_bumblezone.entities.ProjectileImpact;
import com.telepathicgrunt.the_bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers.PollenPuffEntityPollinateManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.AddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTravelingToDimensionEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.FinalSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterDataSerializersEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterFlammabilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterReloadListenerEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.SetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.TagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerCraftedItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerEntityInteractEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerPickupItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.items.DispenserAddedSpawnEgg;
import com.telepathicgrunt.the_bumblezone.items.HoneyCrystalShield;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.items.essence.CalmingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.ContinuityEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import com.telepathicgrunt.the_bumblezone.modcompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modinit.*;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataHandler;
import com.telepathicgrunt.the_bumblezone.packets.MessageHandler;
import com.telepathicgrunt.the_bumblezone.packets.QueenMainTradesSyncPacket;
import com.telepathicgrunt.the_bumblezone.packets.QueenRandomizerTradesSyncPacket;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import com.telepathicgrunt.the_bumblezone.world.surfacerules.PollinatedSurfaceSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Bumblezone{

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        BzTags.initTags();

        //Events
        RegisterCommandsEvent.EVENT.addListener(BzCommands::registerCommand);
        EntitySpawnEvent.EVENT.addListener(ModdedBeesBeesSpawning::onEntitySpawn);
        PlayerTickEvent.EVENT.addListener(BeeAggression::playerTick);
        PlayerPickupItemEvent.EVENT.addListener(BeeAggression::pickupItemAnger);
        EntityHurtEvent.EVENT_LOWEST.addListener(CalmingEssence::OnAttack);
        EntityHurtEvent.EVENT_LOWEST.addListener(BeeAggression::onLivingEntityHurt);
        BlockBreakEvent.EVENT_LOWEST.addListener(BeeAggression::minedBlockAnger); // We want to make sure the block will be broken for angering bees
        PlayerEntityInteractEvent.EVENT.addListener(BeeInteractivity::onEntityInteractEvent);
        EntityDeathEvent.EVENT.addListener(WrathOfTheHiveEffect::onLivingEntityDeath);
        EntityDeathEvent.EVENT.addListener(RagingEssence::OnEntityDeath);
        EntityDeathEvent.EVENT.addListener(ContinuityEssence::CancelledDeath);
        ServerLevelTickEvent.EVENT.addListener(BzWorldSavedData::worldTick);
        PlayerTickEvent.EVENT.addListener(EntityTeleportationHookup::playerTick);
        EntityTickEvent.EVENT.addListener(EntityTeleportationHookup::entityTick);
        EntityTravelingToDimensionEvent.EVENT.addListener(EntityTeleportationBackend::entityChangingDimension);
        PlayerItemUseOnBlockEvent.EVENT_HIGH.addListener(StringCurtain::onBlockInteractEvent);
        PlayerItemUseOnBlockEvent.EVENT_HIGH.addListener(ItemUseOnBlock::onItemUseOnBlock); // High because we want to cancel other mod's stuff if it uses on a hive.
        PlayerItemUseEvent.EVENT_HIGH.addListener(ItemUseOnBlock::onEarlyItemUseOnBlock); // High because we want to cancel other mod's stuff if it uses on a hive.
        ProjectileHitEvent.EVENT_HIGH.addListener(ProjectileImpact::onProjectileImpact); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        EntityVisibilityEvent.EVENT.addListener(HiddenEffect::hideEntity);
        EntityAttackedEvent.EVENT.addListener(NeurotoxinsEnchantment::entityHurtEvent);
        EntityAttackedEvent.EVENT.addListener(HoneyCrystalShield::handledPlayerHurtBehavior);
        PlayerBreakSpeedEvent.EVENT.addListener(CombCutterEnchantment::attemptFasterMining);
        PlayerDataHandler.initEvents();
        PlayerCraftedItemEvent.EVENT.addListener(IncenseCandleBase::multiPotionCandleCrafted);
        PlayerGrantAdvancementEvent.EVENT.addListener(TargetAdvancementDoneTrigger::OnAdvancementGiven);
        RegisterWanderingTradesEvent.EVENT.addListener(WanderingTrades::addWanderingTrades);
        TagsUpdatedEvent.EVENT.addListener(QueensTradeManager.QUEENS_TRADE_MANAGER::resolveQueenTrades);
        ServerGoingToStopEvent.EVENT.addListener(ThreadExecutor::handleServerStoppingEvent);
        ServerGoingToStartEvent.EVENT.addListener(Bumblezone::serverAboutToStart);
        RegisterReloadListenerEvent.EVENT.addListener(Bumblezone::registerDatapackListener);
        AddBuiltinResourcePacks.EVENT.addListener(Bumblezone::setupBuiltInResourcePack);
        SetupEvent.EVENT.addListener(Bumblezone::setup);
        RegisterDataSerializersEvent.EVENT.addListener(Bumblezone::registerDataSerializers);
        FinalSetupEvent.EVENT.addListener(Bumblezone::onFinalSetup); //run after all mods
        RegisterFlammabilityEvent.EVENT.addListener(Bumblezone::onRegisterFlammablity);
        SetupEvent.EVENT.addListener(DispenserAddedSpawnEgg::onSetup);
        AddCreativeTabEntriesEvent.EVENT.addListener(BzCreativeTabs::addCreativeTabEntries);
        RegisterEntityAttributesEvent.EVENT.addListener(BzEntities::registerEntityAttributes);
        RegisterSpawnPlacementsEvent.EVENT.addListener(BzEntities::registerEntitySpawnRestrictions);
        DatapackSyncEvent.EVENT.addListener(QueenRandomizerTradesSyncPacket::sendToClient);
        DatapackSyncEvent.EVENT.addListener(QueenMainTradesSyncPacket::sendToClient);

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
        BzStructures.STRUCTURES.init();
        BzDimension.BIOME_SOURCE.init();
        BzParticles.PARTICLE_TYPES.init();
        BzPredicates.POS_RULE_TEST.init();
        BzDimension.CHUNK_GENERATOR.init();
        BzEnchantments.ENCHANTMENTS.init();
        BzSurfaceRules.SURFACE_RULES.init();
        BzDimension.DENSITY_FUNCTIONS.init();
        BzBlockEntities.BLOCK_ENTITIES.init();
        BzPlacements.PLACEMENT_MODIFIER.init();
        BzProcessors.STRUCTURE_PROCESSOR.init();
        BzCreativeTabs.CREATIVE_MODE_TABS.init();
        BzBiomeHeightRegistry.BIOME_HEIGHT.init();
        BzLootFunctionTypes.LOOT_ITEM_FUNCTION_TYPE.init();
        BzLootConditionTypes.LOOT_ITEM_CONDITION_TYPE.init();
        BzStructurePlacementType.STRUCTURE_PLACEMENT_TYPE.init();
    }

    public static void onRegisterFlammablity(RegisterFlammabilityEvent event) {
        BzBlocks.CURTAINS.stream().map(RegistryEntry::get).forEach(block -> event.register(block, 60, 20));
    }

    private static void setup(final SetupEvent event) {
    	event.enqueueWork(() -> {
            BzCriterias.registerCriteriaTriggers();
            BeeAggression.setupBeeHatingList();
            BzStats.initStatEntries();
            BzRecipes.registerBrewingStandRecipes();
		});
        MessageHandler.init();
    }

    private static void registerDataSerializers(RegisterDataSerializersEvent event) {
        event.register(new ResourceLocation(Bumblezone.MODID, "queen_pose"), BeeQueenEntity.QUEEN_POSE_SERIALIZER);
        event.register(new ResourceLocation(Bumblezone.MODID, "rootmin_pose"), RootminEntity.ROOTMIN_POSE_SERIALIZER);
    }

    private static void onFinalSetup(final FinalSetupEvent event) {
        event.enqueueWork(DispenserItemSetup::setupDispenserBehaviors);
    }

    public static void registerDatapackListener(final RegisterReloadListenerEvent event) {
        event.register(new ResourceLocation(Bumblezone.MODID, "queens_trades"), QueensTradeManager.QUEENS_TRADE_MANAGER);
        event.register(new ResourceLocation(Bumblezone.MODID, "pollen_puff"),PollenPuffEntityPollinateManager.POLLEN_PUFF_ENTITY_POLLINATE_MANAGER);
    }

    private static void serverAboutToStart(final ServerGoingToStartEvent event) {
        PollinatedSurfaceSource.RandomLayerStateRule.initNoise(event.getServer().getWorldData().worldGenOptions().seed());
        BiomeRegistryHolder.setupBiomeRegistry(event.getServer());
        ThreadExecutor.setupExecutorService();
    }

    private static void setupBuiltInResourcePack(final AddBuiltinResourcePacks event) {
        event.add(
                new ResourceLocation(MODID, "anti_tropophobia"),
                Component.literal("Bumblezone - Anti Trypophobia"),
                AddBuiltinResourcePacks.PackMode.USER_CONTROLLED
        );
    }
}
