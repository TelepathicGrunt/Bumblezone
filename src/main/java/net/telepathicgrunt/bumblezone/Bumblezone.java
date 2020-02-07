package net.telepathicgrunt.bumblezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.telepathicgrunt.bumblezone.blocks.BlocksInit;
import net.telepathicgrunt.bumblezone.capabilities.CapabilityPlayerPosAndDim;
import net.telepathicgrunt.bumblezone.world.biome.BiomeInit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Bumblezone.MODID)
public class Bumblezone
{
	public static final String MODID = "the_bumblezone";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Bumblezone() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
		CapabilityPlayerPosAndDim.register();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

		@SubscribeEvent
		public static void registerBiomes(final RegistryEvent.Register<Biome> event)
		{
			//registers all my modified biomes
			BiomeInit.registerBiomes(event);
		}
		
		
		/**
		 * This method will be called by Forge when it is time for the mod to register its Blocks. This method will always be
		 * called before the Item registry method.
		 */
		@SubscribeEvent
		public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
		{
			BlocksInit.registerBlocks(event);
		}


		/**
		 * This method will be called by Forge when it is time for the mod to register its Items. This method will always be
		 * called after the Block registry method.
		 */
		@SubscribeEvent
		public static void onRegisterItems(final RegistryEvent.Register<Item> event)
		{
			BlocksInit.registerItems(event);
		}
    }
	
    
	/*
	 * Helper method to quickly register features, blocks, items, structures, biomes, anything that can be registered.
	 */
	public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey)
	{
		entry.setRegistryName(new ResourceLocation(MODID, registryKey.toLowerCase().replace(' ', '_')));
		registry.register(entry);
		return entry;
	}
}
