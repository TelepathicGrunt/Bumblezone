package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.advancements.TargetAdvancementDoneTrigger;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityMisc;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.BeeInteractivity;
import com.telepathicgrunt.the_bumblezone.entities.EnderpearlImpact;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers.PollenPuffEntityPollinateManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.items.BeeStinger;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompatRegs;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompatRegs;
import com.telepathicgrunt.the_bumblezone.modinit.*;
import com.telepathicgrunt.the_bumblezone.packets.MessageHandler;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import com.telepathicgrunt.the_bumblezone.world.surfacerules.PollinatedSurfaceSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
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

import java.io.IOException;

@Mod(Bumblezone.MODID)
public class Bumblezone{

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Bumblezone() {
        BzTags.initTags();
        BzBiomeHeightRegistry.initBiomeHeightRegistry();

        //Events
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(BeeAggression::pickupItemAnger);
        forgeBus.addListener(EventPriority.LOWEST, BeeAggression::onLivingEntityHurt);
        forgeBus.addListener(BeeInteractivity::onEntityInteractEvent);
        forgeBus.addListener(WrathOfTheHiveEffect::onLivingEntityDeath);
        forgeBus.addListener(EventPriority.LOWEST, BeeAggression::minedBlockAnger); // We want to make sure the block will be broken for angering bees
        forgeBus.addListener(WanderingTrades::addWanderingTrades);
        forgeBus.addListener(CombCutterEnchantment::attemptFasterMining);
        forgeBus.addListener(EventPriority.HIGH, EnderpearlImpact::onPearlHit); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        forgeBus.addListener(EntityTeleportationHookup::entityTick);
        forgeBus.addListener(BeeAggression::playerTick);
        forgeBus.addListener(BzWorldSavedData::worldTick);
        forgeBus.addListener(EntityTeleportationBackend::entityChangingDimension);
        forgeBus.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        forgeBus.addListener(HiddenEffect::hideEntity);
        forgeBus.addListener(NeurotoxinsEnchantment::entityHurtEvent);
        forgeBus.addListener(this::serverAboutToStart);
        forgeBus.addListener(BeeStinger::bowUsable);
        forgeBus.addListener(EntityMisc::resetValueOnRespawn);
        forgeBus.addListener(EntityMisc::onItemCrafted);
        forgeBus.addListener(EntityMisc::onBeeBreed);
        forgeBus.addListener(EventPriority.LOWEST, EntityMisc::onEntityKilled);
        forgeBus.addListener(EntityMisc::onHoneyBottleDrank);
        forgeBus.addListener(EntityMisc::onHoneySlimeBred);
        forgeBus.addListener(TargetAdvancementDoneTrigger::OnAdvancementGiven);
        forgeBus.addListener(QueensTradeManager.QUEENS_TRADE_MANAGER::resolveQueenTrades);
        forgeBus.addListener(ThreadExecutor::handleServerAboutToStartEvent);
        forgeBus.addListener(ThreadExecutor::handleServerStoppingEvent);
        forgeBus.addListener(this::registerDatapackListener);
        forgeBus.addListener(BzCommands::registerCommand);

        //Registration
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(EventPriority.NORMAL, this::setup);
        modEventBus.addListener(EventPriority.LOWEST, this::modCompatSetup); //run after all mods
        modEventBus.addListener(EventPriority.NORMAL, this::setupBuiltInResourcePack);
        modEventBus.addListener(EventPriority.NORMAL, BzEntities::registerEntityAttributes);
        BzItems.ITEMS.register(modEventBus);
        BzBlocks.BLOCKS.register(modEventBus);
        BzFluids.FLUIDS.register(modEventBus);
        BzPOI.POI_TYPES.register(modEventBus);
        BzRecipes.RECIPES.register(modEventBus);
        BzEffects.EFFECTS.register(modEventBus);
        BzMenuTypes.MENUS.register(modEventBus);
        BzStats.CUSTOM_STAT.register(modEventBus);
        BzFeatures.FEATURES.register(modEventBus);
        BzEntities.ENTITIES.register(modEventBus);
        BzFluids.FLUID_TYPES.register(modEventBus);
        BzSounds.SOUND_EVENTS.register(modEventBus);
        BzStructures.STRUCTURES.register(modEventBus);
        BzDimension.BIOME_SOURCE.register(modEventBus);
        BzParticles.PARTICLE_TYPES.register(modEventBus);
        BzPredicates.POS_RULE_TEST.register(modEventBus);
        BzDimension.CHUNK_GENERATOR.register(modEventBus);
        BzEnchantments.ENCHANTMENTS.register(modEventBus);
        BzSurfaceRules.SURFACE_RULES.register(modEventBus);
        BzDimension.DENSITY_FUNCTIONS.register(modEventBus);
        BzBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        BzPlacements.PLACEMENT_MODIFIER.register(modEventBus);
        BzProcessors.STRUCTURE_PROCESSOR.register(modEventBus);
        BzBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        BzLootFunctionTypes.LOOT_ITEM_FUNCTION_TYPE.register(modEventBus);

        if (ModList.get().isLoaded("productivebees")) {
            ProductiveBeesCompatRegs.CONFIGURED_FEATURES.register(modEventBus);
            ProductiveBeesCompatRegs.PLACED_FEATURES.register(modEventBus);
        }

        if (ModList.get().isLoaded("buzzier_bees")) {
            BuzzierBeesCompatRegs.CONFIGURED_FEATURES.register(modEventBus);
            BuzzierBeesCompatRegs.PLACED_FEATURES.register(modEventBus);
        }

        BzCapabilities.setupCapabilities();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BumblezoneClient.subscribeClientEvents();
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

    private void setup(final FMLCommonSetupEvent event) {
    	event.enqueueWork(() -> {
            BzCriterias.registerCriteriaTriggers();
			BzEntities.registerAdditionalEntityInformation();
            BeeAggression.setupBeeHatingList();
            BzStats.initStatEntries();
            BzRecipes.registerBrewingStandRecipes();
		});
        MessageHandler.init();
    }

    private void modCompatSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EntityDataSerializers.registerSerializer(BeeQueenEntity.QUEEN_POSE_SERIALIZER);

            // Dispenser isn't synchronized. Needs to be enqueueWork to prevent crash if
            // another mod registers to it at the same exact time.
            DispenserItemSetup.setupDispenserBehaviors();

            // should run after most other mods just in case
            ModChecker.setupModCompat();
        });
    }

    public void registerDatapackListener(final AddReloadListenerEvent event) {
        event.addListener(QueensTradeManager.QUEENS_TRADE_MANAGER);
        event.addListener(PollenPuffEntityPollinateManager.POLLEN_PUFF_ENTITY_POLLINATE_MANAGER);
    }

    private void serverAboutToStart(final ServerAboutToStartEvent event) {
        PollinatedSurfaceSource.RandomLayerStateRule.initNoise(event.getServer().getWorldData().worldGenSettings().seed());
    }

    private void setupBuiltInResourcePack(final AddPackFindersEvent event) {
        try {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("resourcepacks/anti_tropophobia");
                var pack = new PathPackResources(ModList.get().getModFileById(MODID).getFile().getFileName() + ":" + resourcePath, resourcePath);
                var metadataSection = pack.getMetadataSection(PackMetadataSection.SERIALIZER);
                if (metadataSection != null) {
                    event.addRepositorySource((packConsumer, packConstructor) ->
                            packConsumer.accept(packConstructor.create(
                                    "builtin/the_bumblezone", Component.literal("Bumblezone - Anti Trypophobia"), false,
                                    () -> pack, metadataSection, Pack.Position.BOTTOM, PackSource.BUILT_IN, false)));
                }
            }
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
