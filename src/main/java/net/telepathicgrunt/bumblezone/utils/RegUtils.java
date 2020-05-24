package net.telepathicgrunt.bumblezone.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class RegUtils
{
	/**
	 * Helper method to quickly register features, blocks, items, structures, biomes, anything that can be registered.
	 */
	public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey)
	{
		entry.setRegistryName(new ResourceLocation(Bumblezone.MODID, registryKey));
		registry.register(entry);
		return entry;
	}
}
