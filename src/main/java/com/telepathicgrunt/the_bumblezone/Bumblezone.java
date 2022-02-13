package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import com.telepathicgrunt.the_bumblezone.effects.HiddenEffect;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.enchantments.NeurotoxinsEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.EnderpearlImpact;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modinit.BzBiomeHeightRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootFunctionTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzPOI;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import com.telepathicgrunt.the_bumblezone.modinit.BzPredicates;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStructures;
import com.telepathicgrunt.the_bumblezone.modinit.BzSurfaceRules;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.the_bumblezone.tags.BzEntityTags;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.MessageHandler;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Bumblezone.MODID)
public class Bumblezone{

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Bumblezone() {
        // TODO: Add bee armor!
        // TODO: new advancements
        // TODO: lang files
        // TODO: port to fabric
        // TODO: update mod images and description

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BzBlockTags.tagInit(); // Done extra early as some features needs the tag wrapper.
        BzItemTags.tagInit();
        BzEntityTags.tagInit();
        BzFluidTags.tagInit();
        BzBiomeHeightRegistry.initBiomeHeightRegistry();

        //Events
        forgeBus.addListener(BeeAggression::pickupItemAnger);
        forgeBus.addListener(EventPriority.LOWEST, BeeAggression::minedBlockAnger); // We want to make sure the block will be broken for angering bees
        forgeBus.addListener(WanderingTrades::addWanderingTrades);
        forgeBus.addListener(CombCutterEnchantment::attemptFasterMining);
        forgeBus.addListener(EventPriority.HIGH, EnderpearlImpact::onPearlHit); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        forgeBus.addListener(EntityTeleportationHookup::entityTick);
        forgeBus.addListener(BeeAggression::playerTick);
        forgeBus.addListener(BzWorldSavedData::worldTick);
        forgeBus.addListener(EntityTeleportationBackend::playerLeavingBz);
        forgeBus.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        forgeBus.addListener(HiddenEffect::hideEntity);
        forgeBus.addListener(NeurotoxinsEnchantment::entityHurtEvent);

        //Registration
        modEventBus.addListener(EventPriority.NORMAL, this::setup);
        modEventBus.addListener(EventPriority.LOWEST, this::modCompatSetup); //run after all mods
        modEventBus.addListener(EventPriority.NORMAL, BzEntities::registerEntityAttributes);
        BzItems.ITEMS.register(modEventBus);
        BzBlocks.BLOCKS.register(modEventBus);
        BzFluids.FLUIDS.register(modEventBus);
        BzPOI.POI_TYPES.register(modEventBus);
        BzItems.RECIPES.register(modEventBus);
        BzEffects.EFFECTS.register(modEventBus);
        BzFeatures.FEATURES.register(modEventBus);
        BzEntities.ENTITIES.register(modEventBus);
        BzSounds.SOUND_EVENTS.register(modEventBus);
        BzStructures.STRUCTURES.register(modEventBus);
        BzParticles.PARTICLE_TYPES.register(modEventBus);
        BzEnchantments.ENCHANTMENTS.register(modEventBus);
        BzBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        BzCapabilities.setupCapabilities();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BumblezoneClient::subscribeClientEvents);

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
            BzPredicates.registerPredicates();
            BzLootFunctionTypes.registerContainerLootFunctions();
            BzPlacements.registerPlacements();
            BzCriterias.registerCriteriaTriggers();
            BzProcessors.registerProcessors();
			BzDimension.setupDimension();
			BzEntities.registerAdditionalEntityInformation();
			BzStructures.setupStructures();
            BzSurfaceRules.registerSurfaceRules();
            BeeAggression.setupBeeHatingList();
		});
        MessageHandler.init();
    }

    private void modCompatSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Dispenser isn't synchronized. Needs to be enqueueWork to prevent crash if
            // another mod registers to it at the same exact time.
            DispenserItemSetup.setupDispenserBehaviors();

            // should run after most other mods just in case
            ModChecker.setupModCompat();
        });
    }
}
