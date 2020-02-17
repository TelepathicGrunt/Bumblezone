package net.telepathicgrunt.bumblezone.features;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class BzFeatureInit
{
    public static Feature<DefaultFeatureConfig> HONEYCOMB_HOLE = new HoneycombHole(DefaultFeatureConfig::deserialize);

    public static void registerFeatures()
    {
    	Registry.register(Registry.FEATURE, new Identifier(Bumblezone.MODID, "honeycomb_hole"), HONEYCOMB_HOLE);
    }
}
