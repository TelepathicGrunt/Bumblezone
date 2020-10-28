package com.telepathicgrunt.the_bumblezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityEventHandler;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityPlayerPosAndDim;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzBlockMechanicsConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDungeonsConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.BzEntities;
import com.telepathicgrunt.the_bumblezone.features.BzConfiguredFeatures;
import com.telepathicgrunt.the_bumblezone.features.BzFeatures;
import com.telepathicgrunt.the_bumblezone.features.decorators.BzPlacements;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.items.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modCompat.HoneycombBroodEvents;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.modCompat.PotionOfBeesBeeSplashPotionProjectile;
import com.telepathicgrunt.the_bumblezone.surfacebuilders.BzSurfaceBuilders;
import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

        //Events
        forgeBus.addListener(BzDimension::biomeModification);
        forgeBus.addListener(BeeAggression::HoneyPickupEvent);
        forgeBus.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        forgeBus.addListener(HoneycombBroodEvents::reviveByPotionOfBees);
        forgeBus.addListener(PotionOfBeesBeeSplashPotionProjectile::ProjectileImpactEvent);
        forgeBus.addGenericListener(Entity.class, CapabilityEventHandler::onAttachCapabilitiesToEntities);

        //Registration
        modEventBus.addListener(this::setup);
        BzItems.ITEMS.register(modEventBus);
        BzBlocks.BLOCKS.register(modEventBus);
        BzFluids.FLUIDS.register(modEventBus);//TODO I tried putting this before and after BLOCKS, it didnt change anything from what i can tell - andrew
        BzEffects.EFFECTS.register(modEventBus);
        modEventBus.addGenericListener(Biome.class, this::registerBiomes);
//      BzBiomes.BIOMES.register(modEventBus); had to comment it out because biomes get registered in the biome provider and it causes a duplicate
        BzFeatures.FEATURES.register(modEventBus);
        BzEntities.ENTITIES.register(modEventBus);
        BzPlacements.DECORATORS.register(modEventBus);
        BzItems.RECIPES.register(modEventBus);
        BzSurfaceBuilders.SURFACE_BUILDERS.register(modEventBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BumblezoneClient::subscribeClientEvents);

        // generates/handles config
        BzModCompatibilityConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzModCompatibilityConfigs.BzModCompatibilityConfigValues::new, "the_bumblezone-mod_compatibility.toml");
        BzBlockMechanicsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBlockMechanicsConfigs.BzBlockMechanicsConfigValues::new, "the_bumblezone-block_mechanics.toml");
        BzBeeAggressionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBeeAggressionConfigs.BzBeeAggressionConfigValues::new, "the_bumblezone-bee_aggression.toml");
        BzDimensionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDimensionConfigs.BzDimensionConfigValues::new, "the_bumblezone-dimension.toml");
        BzDungeonsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDungeonsConfigs.BzDungeonsConfigValues::new, "the_bumblezone-dungeons.toml");

    }


    private void setup(final FMLCommonSetupEvent event)
    {
    	event.enqueueWork(() -> 
		{
			BzDimension.setupDimension();
			BzConfiguredFeatures.registerConfiguredFeatures();
			BzEntities.registerAdditionalEntityInformation();
			// Dispenser isn't synchronized. Needs to be enqueueWork to prevent crash if
	        // another mod registers to it at the same exact time.
	        DispenserItemSetup.setupDispenserBehaviors();
	        // should run after most other mods just in case
	        ModChecker.setupModCompat();
		});
        CapabilityPlayerPosAndDim.register();
    }
    
    public void registerBiomes(final RegistryEvent.Register<Biome> event)
    {
        //Reserve Bumblezone biome IDs for the json version to replace
        event.getRegistry().register(BiomeMaker.createTheVoid().setRegistryName(BzBiomeProvider.HIVE_WALL));
        event.getRegistry().register(BiomeMaker.createTheVoid().setRegistryName(BzBiomeProvider.HIVE_PILLAR));
        event.getRegistry().register(BiomeMaker.createTheVoid().setRegistryName(BzBiomeProvider.SUGAR_WATER_FLOOR));
    }
}
