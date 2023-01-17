package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.advancements.TargetAdvancementDoneTrigger;
import com.telepathicgrunt.the_bumblezone.blocks.IncenseCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.StringCurtain;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.*;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.*;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers.PollenPuffEntityPollinateManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.events.AddWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.*;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.*;
import com.telepathicgrunt.the_bumblezone.events.player.*;
import com.telepathicgrunt.the_bumblezone.items.BeeStinger;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompatRegs;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompatRegs;
import com.telepathicgrunt.the_bumblezone.modinit.*;
import com.telepathicgrunt.the_bumblezone.modules.EntityMiscHandler;
import com.telepathicgrunt.the_bumblezone.packets.MessageHandler;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import com.telepathicgrunt.the_bumblezone.world.dimension.BiomeRegistryHolder;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import com.telepathicgrunt.the_bumblezone.world.surfacerules.PollinatedSurfaceSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.resource.PathPackResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Bumblezone.MODID)
public class Bumblezone{

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static void init() {
        BzTags.initTags();

        //Events
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        RegisterCommandsEvent.EVENT.addListener(BzCommands::registerCommand);
        EntitySpawnEvent.EVENT.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        PlayerTickEvent.EVENT.addListener(BeeAggression::playerTick);
        PlayerPickupItemEvent.EVENT.addListener(BeeAggression::pickupItemAnger);
        EntityHurtEvent.EVENT_LOWEST.addListener(BeeAggression::onLivingEntityHurt);
        BlockBreakEvent.EVENT_LOWEST.addListener(BeeAggression::minedBlockAnger); // We want to make sure the block will be broken for angering bees
        PlayerRightClickedBlockEvent.EVENT.addListener(StringCurtain::onBlockInteractEvent);
        PlayerEntityInteractEvent.EVENT.addListener(BeeInteractivity::onEntityInteractEvent);
        EntityDeathEvent.EVENT.addListener(WrathOfTheHiveEffect::onLivingEntityDeath);
        LevelTickEvent.EVENT.addListener(BzWorldSavedData::worldTick);
        EntityTickEvent.EVENT.addListener(EntityTeleportationHookup::entityTick);
        EntityTravelingToDimensionEvent.EVENT.addListener(EntityTeleportationBackend::entityChangingDimension);
        ProjectileHitEvent.EVENT_HIGH.addListener(EnderpearlImpact::onPearlHit); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        EntityVisabilitityEvent.EVENT.addListener(HiddenEffect::hideEntity);
        EntityAttackedEvent.EVENT.addListener(NeurotoxinsEnchantment::entityHurtEvent);
        PlayerBreakSpeedEvent.EVENT.addListener(CombCutterEnchantment::attemptFasterMining);
        PlayerLocateProjectileEvent.EVENT.addListener(BeeStinger::bowUsable);
        EntityMiscHandler.initEvents();
        PlayerCraftedItemEvent.EVENT.addListener(IncenseCandleBase::multiPotionCandleCrafted);
        PlayerGrantAdvancementEvent.EVENT.addListener(TargetAdvancementDoneTrigger::OnAdvancementGiven);
        AddWanderingTradesEvent.EVENT.addListener(WanderingTrades::addWanderingTrades);
        TagsUpdatedEvent.EVENT.addListener(QueensTradeManager.QUEENS_TRADE_MANAGER::resolveQueenTrades);
        ServerStoppingEvent.EVENT.addListener(ThreadExecutor::handleServerStoppingEvent);
        ServerGoingToStartEvent.EVENT.addListener(Bumblezone::serverAboutToStart);
        RegisterReloadListenerEvent.EVENT.addListener(Bumblezone::registerDatapackListener);

        //Registration
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SetupEvent.EVENT.addListener(Bumblezone::setup);
        FinalSetupEvent.EVENT.addListener(Bumblezone::modCompatSetup); //run after all mods

        AddBuiltinResourcePacks.EVENT.addListener(Bumblezone::setupBuiltInResourcePack);
        modEventBus.addListener(EventPriority.NORMAL, BzItems::registerCreativeModeTab);
        modEventBus.addListener(EventPriority.NORMAL, BzItems::addToCreativeModeTabs);
        RegisterEntityAttributesEvent.EVENT.addListener(BzEntities::registerEntityAttributes);
        RegisterSpawnPlacementsEvent.EVENT.addListener(BzEntities::registerEntitySpawnRestrictions);;
        BzItems.ITEMS.init();
        BzBlocks.BLOCKS.init();
        BzFluids.FLUIDS.init();
        BzPOI.POI_TYPES.init();
        BzRecipes.RECIPES.init();
        BzEffects.EFFECTS.init();
        BzMenuTypes.MENUS.init();
        BzStats.CUSTOM_STAT.init();
        BzFeatures.FEATURES.init();
        BzEntities.ENTITIES.init();
        BzFluids.FLUID_TYPES.register(modEventBus);
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
        BzBiomeHeightRegistry.BIOME_HEIGHT.init();
        BzBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        BzLootFunctionTypes.LOOT_ITEM_FUNCTION_TYPE.init();

        if (ModList.get().isLoaded("productivebees")) {
            ProductiveBeesCompatRegs.CONFIGURED_FEATURES.register(modEventBus);
            ProductiveBeesCompatRegs.PLACED_FEATURES.register(modEventBus);
        }

        if (ModList.get().isLoaded("buzzier_bees")) {
            BuzzierBeesCompatRegs.CONFIGURED_FEATURES.register(modEventBus);
            BuzzierBeesCompatRegs.PLACED_FEATURES.register(modEventBus);
        }

        // generates/handles config
        FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve("the_bumblezone"), "the_bumblezone");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BzClientConfigs.GENERAL_SPEC, "the_bumblezone/client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzGeneralConfigs.GENERAL_SPEC, "the_bumblezone/general.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzWorldgenConfigs.GENERAL_SPEC, "the_bumblezone/worldgen.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzDimensionConfigs.GENERAL_SPEC, "the_bumblezone/dimension.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzBeeAggressionConfigs.GENERAL_SPEC, "the_bumblezone/bee_aggression.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzModCompatibilityConfigs.GENERAL_SPEC, "the_bumblezone/mod_compatibility.toml");
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

    private static void modCompatSetup(final FinalSetupEvent event) {
        event.enqueueWork(() -> {
            EntityDataSerializers.registerSerializer(BeeQueenEntity.QUEEN_POSE_SERIALIZER);

            // Dispenser isn't synchronized. Needs to be enqueueWork to prevent crash if
            // another mod registers to it at the same exact time.
            DispenserItemSetup.setupDispenserBehaviors();

            // should run after most other mods just in case
            ModChecker.setupModCompat();
        });
    }

    public static void registerDatapackListener(final RegisterReloadListenerEvent event) {
        event.register(QueensTradeManager.QUEENS_TRADE_MANAGER);
        event.register(PollenPuffEntityPollinateManager.POLLEN_PUFF_ENTITY_POLLINATE_MANAGER);
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
