package net.telepathicgrunt.bumblezone;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
public class MissingMappingHandler
{

	// Converts from old names to new names to maintain compatibility to old worlds

	private static Map<ResourceLocation, ResourceLocation> RL_CONVERSION;
	static {
		Map<ResourceLocation, ResourceLocation> map = new HashMap<ResourceLocation, ResourceLocation>();

		map.put(new ResourceLocation("the_bumblezone:honeycomb_larva_block"), new ResourceLocation("the_bumblezone:honeycomb_brood_block"));
		map.put(new ResourceLocation("the_bumblezone:dead_honeycomb_larva_block"), new ResourceLocation("the_bumblezone:empty_honeycomb_brood_block"));
		
		RL_CONVERSION = map;
	}
	
	
	// items
	@SubscribeEvent
	public static void missingMappingItem(RegistryEvent.MissingMappings<Item> event)
	{
		for (MissingMappings.Mapping<Item> entry : event.getAllMappings())
		{
			if (RL_CONVERSION.containsKey(entry.key))
			{
				entry.remap(ForgeRegistries.ITEMS.getValue(RL_CONVERSION.get(entry.key)));
			}
		}
	}


	// blocks
	@SubscribeEvent
	public static void missingMappingBlock(RegistryEvent.MissingMappings<Block> event)
	{
		for (MissingMappings.Mapping<Block> entry : event.getAllMappings())
		{
			if (RL_CONVERSION.containsKey(entry.key))
			{
				entry.remap(ForgeRegistries.BLOCKS.getValue(RL_CONVERSION.get(entry.key)));
			}
		}
	}
}
