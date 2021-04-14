package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.biomes.BzBiomes;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityEventHandler;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityPlayerPosAndDim;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.*;
import com.telepathicgrunt.the_bumblezone.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.BzEntities;
import com.telepathicgrunt.the_bumblezone.entities.EnderpearlImpact;
import com.telepathicgrunt.the_bumblezone.features.BzConfiguredFeatures;
import com.telepathicgrunt.the_bumblezone.features.BzFeatures;
import com.telepathicgrunt.the_bumblezone.features.decorators.BzPlacements;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.items.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modCompat.HoneycombBroodEvents;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modCompat.PotionOfBeesBeeSplashPotionProjectile;
import com.telepathicgrunt.the_bumblezone.surfacebuilders.BzSurfaceBuilders;
import com.telepathicgrunt.the_bumblezone.tags.BZBlockTags;
import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
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
        BZBlockTags.tagInit(); // Done extra early as some features needs the tag wrapper.

        //Events
        //forgeBus.addListener(BzDimension::biomeModification);
        forgeBus.addListener(BeeAggression::HoneyPickupEvent);
        forgeBus.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        forgeBus.addListener(HoneycombBroodEvents::reviveByPotionOfBees);
        forgeBus.addListener(EventPriority.HIGH, EnderpearlImpact::onPearlHit); // High because we want to cancel other mod's impact checks and stuff if it hits a hive.
        forgeBus.addListener(PotionOfBeesBeeSplashPotionProjectile::ProjectileImpactEvent);
        forgeBus.addGenericListener(Entity.class, CapabilityEventHandler::onAttachCapabilitiesToEntities);
        forgeBus.addGenericListener(Block.class, Bumblezone::missingMappingDimension);

        //Registration
        modEventBus.addListener(EventPriority.NORMAL, this::setup);
        modEventBus.addListener(EventPriority.LOWEST, this::modCompatSetup); //run after all mods
        modEventBus.addListener(EventPriority.NORMAL, BzEntities::registerEntityAttributes);
        BzItems.ITEMS.register(modEventBus);
        BzBlocks.BLOCKS.register(modEventBus);
        BzFluids.FLUIDS.register(modEventBus);
        BzBiomes.BIOMES.register(modEventBus);
        BzItems.RECIPES.register(modEventBus);
        BzEffects.EFFECTS.register(modEventBus);
        BzFeatures.FEATURES.register(modEventBus);
        BzEntities.ENTITIES.register(modEventBus);
        BzPlacements.DECORATORS.register(modEventBus);
        BzSurfaceBuilders.SURFACE_BUILDERS.register(modEventBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BumblezoneClient::subscribeClientEvents);

        // generates/handles config
        BzModCompatibilityConfig = ConfigHelper.register(ModConfig.Type.COMMON, BzModCompatibilityConfigs.BzModCompatibilityConfigValues::new, "the_bumblezone-mod_compatibility.toml");
        BzBlockMechanicsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBlockMechanicsConfigs.BzBlockMechanicsConfigValues::new, "the_bumblezone-block_mechanics.toml");
        BzBeeAggressionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBeeAggressionConfigs.BzBeeAggressionConfigValues::new, "the_bumblezone-bee_aggression.toml");
        BzDimensionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDimensionConfigs.BzDimensionConfigValues::new, "the_bumblezone-dimension.toml");
        BzDungeonsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDungeonsConfigs.BzDungeonsConfigValues::new, "the_bumblezone-dungeons.toml");
    }


    private static final ResourceLocation old1 = new ResourceLocation(MODID, "dead_honeycomb_larva_block");
    private static final ResourceLocation new1 = new ResourceLocation(MODID, "empty_honeycomb_brood_block");
    private static final ResourceLocation old2 = new ResourceLocation(MODID, "honeycomb_larva_block");
    private static final ResourceLocation new2 = new ResourceLocation(MODID, "honeycomb_brood_block");
    public static void missingMappingDimension(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getMappings(Bumblezone.MODID)) {
            if (entry.key.equals(old1)) {
                entry.remap(ForgeRegistries.BLOCKS.getValue(new1));
            }
            else if (entry.key.equals(old2)) {
                entry.remap(ForgeRegistries.BLOCKS.getValue(new2));
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	event.enqueueWork(() -> 
		{
			BzDimension.setupDimension();
			BzConfiguredFeatures.registerConfiguredFeatures();
			BzEntities.registerAdditionalEntityInformation();

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
        CapabilityPlayerPosAndDim.register();
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
