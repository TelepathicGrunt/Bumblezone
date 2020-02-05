package net.telepathicgrunt.bumblezone.features;

import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class FeatureInit
{

    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
    	//IForgeRegistry<Feature<?>> registry = event.getRegistry();

        Bumblezone.LOGGER.debug("FEATURE REGISTER");

    }
}
