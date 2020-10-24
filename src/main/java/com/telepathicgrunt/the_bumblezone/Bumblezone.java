package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityEventHandler;
import com.telepathicgrunt.the_bumblezone.capabilities.CapabilityPlayerPosAndDim;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.configs.*;
import com.telepathicgrunt.the_bumblezone.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.BzEntities;
import com.telepathicgrunt.the_bumblezone.features.BzConfiguredFeatures;
import com.telepathicgrunt.the_bumblezone.features.BzFeatures;
import com.telepathicgrunt.the_bumblezone.features.decorators.BzPlacements;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.items.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modCompat.HoneycombBroodEvents;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.PotionOfBeesBeeSplashPotionProjectile;
import com.telepathicgrunt.the_bumblezone.modCompat.ModdedBeesBeesSpawning;
import com.telepathicgrunt.the_bumblezone.surfacebuilders.BzSurfaceBuilders;
import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

        forgeBus.addListener(BzDimension::biomeModification);
        forgeBus.addListener(BeeAggression::HoneyPickupEvent);
        forgeBus.addListener(ModdedBeesBeesSpawning::MobSpawnEvent);
        forgeBus.addListener(HoneycombBroodEvents::reviveByPotionOfBees);
        forgeBus.addListener(PotionOfBeesBeeSplashPotionProjectile::ProjectileImpactEvent);
        forgeBus.addGenericListener(Entity.class, CapabilityEventHandler::onAttachCapabilitiesToEntities);

        modEventBus.addListener(this::setup);
        modEventBus.addGenericListener(Item.class, this::registerItems);
        modEventBus.addGenericListener(Biome.class, this::registerBiomes);
        modEventBus.addGenericListener(Block.class, this::registerBlocks);
        modEventBus.addGenericListener(Effect.class, this::registerEffects);
        modEventBus.addGenericListener(Feature.class, this::registerFeatures);
        modEventBus.addGenericListener(EntityType.class, this::registerEntity);
        modEventBus.addGenericListener(Placement.class, this::registerPlacements);
        modEventBus.addGenericListener(IRecipeSerializer.class, this::registerSerializers);
        modEventBus.addGenericListener(SurfaceBuilder.class, this::registerSurfacebuilders);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BumblezoneClient::subscribeClientEvents);

        // generates/handles config
        BzModCompatibilityConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzModCompatibilityConfigs.BzModCompatibilityConfigValues::new, "the_bumblezone-mod_compatibility.toml");
        BzBlockMechanicsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBlockMechanicsConfigs.BzBlockMechanicsConfigValues::new, "the_bumblezone-block_mechanics.toml");
        BzBeeAggressionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzBeeAggressionConfigs.BzBeeAggressionConfigValues::new, "the_bumblezone-bee_aggression.toml");
        BzDimensionConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDimensionConfigs.BzDimensionConfigValues::new, "the_bumblezone-dimension.toml");
        BzDungeonsConfig = ConfigHelper.register(ModConfig.Type.SERVER, BzDungeonsConfigs.BzDungeonsConfigValues::new, "the_bumblezone-dungeons.toml");

    }


    private void setup(final FMLCommonSetupEvent event) {
        CapabilityPlayerPosAndDim.register();
        BzDimension.setupDimension();

        // Dispenser isn't synchronized. Needs to be enqueueWork to prevent crash if
        // another mod registers to it at the same exact time.
        event.enqueueWork(DispenserItemSetup::setupDispenserBehaviors);
        event.enqueueWork(Bumblezone::lateSetup);
    }


    // should run after most other mods just in case
    private static void lateSetup() {
        ModChecker.setupModCompat();
    }

    public void registerBlocks(final RegistryEvent.Register<Block> event) {
        BzBlocks.registerBlocks();
    }

    public void registerItems(final RegistryEvent.Register<Item> event) {
        BzItems.registerItems();
    }

    public void registerEntity(final RegistryEvent.Register<EntityType<?>> event) {
        BzEntities.registerEntities();
    }

    /**
     * This method will be called by Forge when it is time for the mod to register features.
     */
    public void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
        BzFeatures.registerFeatures();
        BzConfiguredFeatures.registerConfiguredFeatures();
    }

    /**
     * This method will be called by Forge when it is time for the mod to register effects.
     */
    public void registerEffects(final RegistryEvent.Register<Effect> event) {
        BzEffects.registerEffects();
    }

    /**
     * This method will be called by Forge when it is time for the mod to register placement.
     */
    public void registerPlacements(final RegistryEvent.Register<Placement<?>> event) {
        BzPlacements.registerPlacements();
    }

    /**
     * This method will be called by Forge when it is time for the mod to register surface builders.
     */
    public void registerSurfacebuilders(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
        BzSurfaceBuilders.registerSurfaceBuilders();
    }

    public void registerSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        BzItems.registerCustomRecipes(event);
    }

    public void registerBiomes(final RegistryEvent.Register<Biome> event) {
        reserveBiomeIDs();
    }

    public static void reserveBiomeIDs() {
        //Reserve Bumblezone biome IDs for the json version to replace
        Registry.register(WorldGenRegistries.BIOME, new ResourceLocation(Bumblezone.MODID, "hive_wall"), BiomeMaker.createNormalOcean(false));
        Registry.register(WorldGenRegistries.BIOME, new ResourceLocation(Bumblezone.MODID, "hive_pillar"), BiomeMaker.createNormalOcean(false));
        Registry.register(WorldGenRegistries.BIOME, new ResourceLocation(Bumblezone.MODID, "sugar_water_floor"), BiomeMaker.createNormalOcean(false));
    }
}
