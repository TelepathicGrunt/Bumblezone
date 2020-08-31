package com.telepathicgrunt.bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import com.telepathicgrunt.bumblezone.utils.ConfigHelper;

@Mod.EventBusSubscriber
public class BzBlockMechanicsConfigs {

    public static class BzBlockMechanicsConfigValues {
        public ConfigHelper.ConfigValueListener<Boolean> dispensersDropGlassBottles;

        public BzBlockMechanicsConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            builder.push("General Mechanics Options");

                dispensersDropGlassBottles = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" Should Dispensers always drop the Glass Bottle when using specific \r\n"
                        +" bottle items on certain The Bumblezone blocks? \r\n"
                        +" \r\n"
                        +" Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and\r\n"
                        +" drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.\r\n")
                    .translation("the_bumblezone.config.modcompat.potionofbees.dispensersDropGlassBottles")
                    .define("dispensersDropGlassBottles", false));

            builder.pop();
        }
    }
}
