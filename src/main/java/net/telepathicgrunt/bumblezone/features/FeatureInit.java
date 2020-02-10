package net.telepathicgrunt.bumblezone.features;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class FeatureInit
{
    public static Feature<NoFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(NoFeatureConfig::deserialize);

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
    	IForgeRegistry<Feature<?>> registry = event.getRegistry();
        Bumblezone.register(registry, HONEYCOMB_HOLE, "honeycomb_hole");
    }
}
