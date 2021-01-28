package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzBlockMechanicsConfigs {

    public static class BzBlockMechanicsConfigValues {
        public ConfigHelper.ConfigValueListener<Boolean> dispensersDropGlassBottles;
        public ConfigHelper.ConfigValueListener<Integer> broodBlocksBeeSpawnCapacity;

        public BzBlockMechanicsConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            builder.push("General Mechanics Options");

                dispensersDropGlassBottles = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" Should Dispensers always drop the Glass Bottle when using specific \r\n"
                        +" bottle items on certain The Bumblezone blocks? \r\n"
                        +" \r\n"
                        +" Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and\r\n"
                        +" drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.\r\n")
                    .translation("the_bumblezone.config.bzblockmechanicsconfigs.dispensersdropglassbottles")
                    .define("dispensersDropGlassBottles", false));

            broodBlocksBeeSpawnCapacity = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Brood Blocks will automatically spawn bees until the number of active bees is the value below. \r\n"
                            +" Set this higher to allow Brood Blocks to spawn more bees in a smaller area or set it to 0 to turn \r\n"
                            +" off automatic Brood Block bee spawning.\r\n")
                    .translation("the_bumblezone.config.bzblockmechanicsconfigs.broodblocksbeespawncapacity")
                    .defineInRange("broodBlocksBeeSpawnCapacity", 60, 0, 1000));

            builder.pop();
        }
    }
}
