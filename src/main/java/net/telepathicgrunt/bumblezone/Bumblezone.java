package net.telepathicgrunt.bumblezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityClassification;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.telepathicgrunt.bumblezone.biome.BzBaseBiome;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.biome.surfacebuilders.BzSurfaceBuilders;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.SugarWaterEvents;
import net.telepathicgrunt.bumblezone.capabilities.CapabilityPlayerPosAndDim;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.config.BzConfig.BzConfigValues;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.features.BzFeatures;
import net.telepathicgrunt.bumblezone.features.placement.BzPlacements;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.items.DispenserItemSetup;
import net.telepathicgrunt.bumblezone.modcompatibility.BeesourcefulRedirection;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;
import net.telepathicgrunt.bumblezone.modcompatibility.ProductiveBeesRedirection;
import net.telepathicgrunt.bumblezone.utils.ConfigHelper;


// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("deprecation")
@Mod(Bumblezone.MODID)
public class Bumblezone
{
    public static final String MODID = "the_bumblezone";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static BzConfigValues BzConfig = null;

    public Bumblezone() 
    {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
	
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::resetBiomes);
		
		BzBlocks.BLOCKS.register(modEventBus);
		BzItems.ITEMS.register(modEventBus);
		BzBlocks.FLUIDS.register(modEventBus);
	
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientEvents.subscribeClientEvents(modEventBus, forgeBus));
	
		// generates/handles config
		BzConfig = ConfigHelper.register(ModConfig.Type.SERVER, (builder, subscriber) -> new BzConfig.BzConfigValues(builder, subscriber));
    }


    private void setup(final FMLCommonSetupEvent event) 
    {
		CapabilityPlayerPosAndDim.register();
		SugarWaterEvents.setup();
		DispenserItemSetup.setupDispenserBehaviors();
		
		DeferredWorkQueue.runLater(Bumblezone::lateSetup);
    }


    // should run after most other mods just in case
    private static void lateSetup() 
    {
		DispenserItemSetup.lateSetupDespenserBehavior();
		BzBaseBiome.addSprings();
		ModChecking.setupModCompat();
		BzBiomes.biomes.forEach(biome -> ((BzBaseBiome) biome).increaseVanillaSlimeMobsRates());
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
	
		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event) 
		{
		    // registers all my modified biomes
		    BzBiomes.registerBiomes(event);
		}
	
	
		/**
		 * This method will be called by Forge when it is time for the mod to register features.
		 */
		@SubscribeEvent
		public static void onRegisterFeatures(final RegistryEvent.Register<Feature<?>> event) 
		{
		    BzFeatures.registerFeatures(event);
		}
	
	
		/**
		 * This method will be called by Forge when it is time for the mod to register effects.
		 */
		@SubscribeEvent
		public static void onRegisterEffects(final RegistryEvent.Register<Effect> event) 
		{
		    BzEffects.registerEffects(event);
		}
	
	
		/**
		 * This method will be called by Forge when it is time for the mod to register placement.
		 */
		@SubscribeEvent
		public static void onRegisterPlacements(final RegistryEvent.Register<Placement<?>> event) 
		{
		    BzPlacements.registerPlacements(event);
		}
	
	
		/**
		 * This method will be called by Forge when it is time for the mod to register surface builders.
		 */
		@SubscribeEvent
		public static void onRegisterSurfacebuilders(final RegistryEvent.Register<SurfaceBuilder<?>> event) 
		{
		    BzSurfaceBuilders.registerSurfaceBuilders(event);
		}
		
		
	    @SubscribeEvent
	    public static void onRegisterSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
	    	BzItems.registerCustomRecipes(event);
	    }
    }
    

	public void resetBiomes(final ModConfig.Reloading event)
	{
    	if(Bumblezone.BzConfig.clearUnwantedBiomeFeatures.get()) {
    		for(Biome biome : BzBiomes.biomes) {
    			for(GenerationStage.Decoration stage : Decoration.values()) {
    				biome.getFeatures(stage).clear();
    			}
    			
    			biome.structures.clear();
    			
    			((BzBaseBiome)biome).addBiomeFeatures();
    		}
    		
    		//re-add biome springs and certain modded features
			BzBaseBiome.addSprings();
			BeesourcefulRedirection.BSAddHoneycombs();
			ProductiveBeesRedirection.PBAddHoneycombs();
    	}

    	if(Bumblezone.BzConfig.clearUnwantedBiomeMobs.get()) {
    		for(Biome biome : BzBiomes.biomes) {
    			for(EntityClassification creatureType : EntityClassification.values()) {
    				biome.getSpawns(creatureType).clear();
    			}
    			
    			((BzBaseBiome)biome).addBiomeMobs();
    		}
    		
    		//re-add slime
    		BzBiomes.biomes.forEach(biome -> ((BzBaseBiome) biome).increaseVanillaSlimeMobsRates());
    	}
	}
}
