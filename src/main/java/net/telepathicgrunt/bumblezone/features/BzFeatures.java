package net.telepathicgrunt.bumblezone.features;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.utils.RegUtils;

public class BzFeatures
{
    public static Feature<NoFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(NoFeatureConfig::deserialize);
    public static Feature<NoFeatureConfig> HONEYCOMB_CAVES = new HoneycombCaves(NoFeatureConfig::deserialize);

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
    	IForgeRegistry<Feature<?>> registry = event.getRegistry();
    	RegUtils.register(registry, HONEYCOMB_HOLE, "honeycomb_hole");
    	RegUtils.register(registry, HONEYCOMB_CAVES, "honeycomb_caves");
    }
}
