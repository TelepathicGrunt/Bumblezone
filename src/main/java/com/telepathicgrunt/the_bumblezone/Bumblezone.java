package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityEntityPosAndDim;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityEventHandler;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzBlockMechanicsConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDungeonsConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.data.DataGenerators;
import com.telepathicgrunt.the_bumblezone.enchantments.CombCutterEnchantment;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.EnderpearlImpact;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modcompat.HoneycombBroodEvents;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modcompat.PotionOfBeesBeeSplashPotionProjectile;
import com.telepathicgrunt.the_bumblezone.modinit.BzBiomes;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzConfiguredFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzPOI;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStructures;
import com.telepathicgrunt.the_bumblezone.modinit.BzSurfaceBuilders;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.the_bumblezone.tags.BzEntityTags;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzDimension;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Bumblezone.MODID)
public class Bumblezone{

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static BzBeeAggressionConfigs.BzBeeAggressionConfigValues BzBeeAggressionConfig = null;
    public static BzBlockMechanicsConfigs.BzBlockMechanicsConfigValues BzBlockMechanicsConfig = null;
    public static BzDimensionConfigs.BzDimensionConfigValues BzDimensionConfig = null;
    public static BzDungeonsConfigs.BzDungeonsConfigValues BzDungeonsConfig = null;
    public static BzModCompatibilityConfigs.BzModCompatibilityConfigValues BzModCompatibilityConfig = null;

    public Bumblezone() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BzBlockTags.tagInit(); // Done extra early as some features needs the tag wrapper.
        BzItemTags.tagInit();
        BzEntityTags.tagInit();
        BzFluidTags.tagInit();

        //Events
        forgeBus.addListener(BeeAggression::pickupItemAnger);
        forgeBus.addListener(EventPriority.LOWEST, BeeAggression::minedBlockAnger); // We want to make sure the block will be broken for angering bees
        forgeBus.addListener(WanderingTrades::addWanderingTrades);
        forgeBus.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        forgeBus.addListener(HoneycombBroodEvents::reviveByPotionOfBees);
        forgeBus.addListener(CombCutterEnchantment::attemptFasterMining);
        forgeBus.addListener(EventPriority.HIGH, EnderpearlImpact::onPearlHit); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        forgeBus.addGenericListener(Block.class, Bumblezone::missingMappingDimension);
        forgeBus.addListener(PotionOfBeesBeeSplashPotionProjectile::ProjectileImpactEvent);
        forgeBus.addGenericListener(Entity.class, CapabilityEventHandler::onAttachCapabilitiesToEntities);
        forgeBus.addListener(EntityTeleportationHookup::entityTick);
        forgeBus.addListener(BeeAggression::playerTick);

        //Registration
        modEventBus.addListener(DataGenerators::gatherData);
        modEventBus.addListener(EventPriority.NORMAL, this::setup);
        modEventBus.addListener(EventPriority.LOWEST, this::modCompatSetup); //run after all mods
        modEventBus.addListener(EventPriority.NORMAL, BzEntities::registerEntityAttributes);
        BzItems.ITEMS.register(modEventBus);
        BzBlocks.BLOCKS.register(modEventBus);
        BzFluids.FLUIDS.register(modEventBus);
        BzBiomes.BIOMES.register(modEventBus);
        BzPOI.POI_TYPES.register(modEventBus);
        BzItems.RECIPES.register(modEventBus);
        BzEffects.EFFECTS.register(modEventBus);
        BzFeatures.FEATURES.register(modEventBus);
        BzEntities.ENTITIES.register(modEventBus);
        BzSounds.SOUND_EVENTS.register(modEventBus);
        BzStructures.STRUCTURES.register(modEventBus);
        BzPlacements.DECORATORS.register(modEventBus);
        BzParticles.PARTICLE_TYPES.register(modEventBus);
        BzEnchantments.ENCHANTMENTS.register(modEventBus);
        BzSurfaceBuilders.SURFACE_BUILDERS.register(modEventBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BumblezoneClient::subscribeClientEvents);

        // generates/handles config
        BzModCompatibilityConfig = ConfigHelper.register(ModConfig.Type.COMMON, BzModCompatibilityConfigs.BzModCompatibilityConfigValues::new, "the_bumblezone-mod_compatibility.toml");
        BzBlockMechanicsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBlockMechanicsConfigs.BzBlockMechanicsConfigValues::new, "the_bumblezone-block_mechanics.toml");
        BzBeeAggressionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBeeAggressionConfigs.BzBeeAggressionConfigValues::new, "the_bumblezone-bee_aggression.toml");
        BzDimensionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDimensionConfigs.BzDimensionConfigValues::new, "the_bumblezone-dimension.toml");
        BzDungeonsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDungeonsConfigs.BzDungeonsConfigValues::new, "the_bumblezone-dungeons.toml");
    }


    private static final Map<ResourceLocation, ResourceLocation> RL_REMAP_MAP = new HashMap<ResourceLocation, ResourceLocation>() {{
        put(new ResourceLocation(MODID, "dead_honeycomb_larva_block"), new ResourceLocation(MODID, "empty_honeycomb_brood_block"));
        put(new ResourceLocation(MODID, "honeycomb_larva_block"), new ResourceLocation(MODID, "honeycomb_brood_block"));
        put(new ResourceLocation(MODID, "beeswax_planks"), new ResourceLocation(MODID, "beehive_beeswax"));
    }};

    public static void missingMappingDimension(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getMappings(Bumblezone.MODID)) {
            ResourceLocation newRL = RL_REMAP_MAP.get(entry.key);
            if(newRL != null) {
                entry.remap(ForgeRegistries.BLOCKS.getValue(newRL));
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	event.enqueueWork(() -> 
		{
            BzProcessors.registerProcessors();
			BzDimension.setupDimension();
			BzConfiguredFeatures.registerConfiguredFeatures();
			BzEntities.registerAdditionalEntityInformation();
			BzStructures.setupStructures();

			// Generates the tag data I want so I can update honeycombs_that_features_can_carve.json lol
//			StringBuilder s = new StringBuilder("\n\n\n");
//			for(Map.Entry<RegistryKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()){
//			    ResourceLocation rl = entry.getKey().getValue();
//			    if(rl.getPath().contains("comb") && (rl.getNamespace().equals("resourcefulbees") || rl.getNamespace().equals("productivebees"))){
//                    s.append("\n    {\n" + "      \"id\": \"").append(entry.getKey().getValue()).append("\",\n").append("      \"required\": false\n").append("    },");
//                }
//            }
//			LOGGER.log(Level.WARN, s.toString());
		});
        CapabilityEntityPosAndDim.register();
    }

    private void modCompatSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            // Dispenser isn't synchronized. Needs to be enqueueWork to prevent crash if
            // another mod registers to it at the same exact time.
            DispenserItemSetup.setupDispenserBehaviors();

            // should run after most other mods just in case
            ModChecker.setupModCompat();
        });
    }
}
