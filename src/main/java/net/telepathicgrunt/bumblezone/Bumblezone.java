package net.telepathicgrunt.bumblezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.blocks.BzBlocksInit;
import net.telepathicgrunt.bumblezone.capabilities.CapabilityPlayerPosAndDim;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.features.BzFeatures;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;


// The value here should match an entry in the META-INF/mods.toml file
@SuppressWarnings("deprecation")
@Mod(Bumblezone.MODID)
public class Bumblezone
{
	public static final String MODID = "the_bumblezone";
	public static final Logger LOGGER = LogManager.getLogger(MODID);


	public Bumblezone()
	{
		ModLoadingContext modLoadingContext = ModLoadingContext.get();
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		modEventBus.addListener(this::setup);

		//generates/handles config
		modEventBus.addListener(this::modConfig);
		modLoadingContext.registerConfig(ModConfig.Type.SERVER, BzConfig.SERVER_SPEC);
	}


	private void setup(final FMLCommonSetupEvent event)
	{
		CapabilityPlayerPosAndDim.register();
		DeferredWorkQueue.runLater(ModChecking::setupModCompat);

		BzBiomes.addVanillaSlimeMobs();
	}


	public void modConfig(final ModConfig.ModConfigEvent event)
	{
		ModConfig config = event.getConfig();
		if (config.getSpec() == BzConfig.SERVER_SPEC)
			BzConfig.refreshServer();
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{

		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event)
		{
			//registers all my modified biomes
			BzBiomes.registerBiomes(event);
		}


		/**
		 * This method will be called by Forge when it is time for the mod to register its Blocks. This method will always be
		 * called before the Item registry method.
		 */
		@SubscribeEvent
		public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
		{
			BzBlocksInit.registerBlocks(event);
		}


		/**
		 * This method will be called by Forge when it is time for the mod to register its Items. This method will always be
		 * called after the Block registry method.
		 */
		@SubscribeEvent
		public static void onRegisterItems(final RegistryEvent.Register<Item> event)
		{
			BzBlocksInit.registerItems(event);
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
	}
}
