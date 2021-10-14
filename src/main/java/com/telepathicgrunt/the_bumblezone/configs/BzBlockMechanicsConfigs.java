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
                    .comment(" \n-----------------------------------------------------\n",
                       " Should Dispensers always drop the Glass Bottle when using specific ",
                       " bottle items on certain The Bumblezone blocks? ",
                       " ",
                       " Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and",
                       " drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.\n")
                    .translation("the_bumblezone.config.bzblockmechanicsconfigs.dispensersdropglassbottles")
                    .define("dispensersDropGlassBottles", false));

            broodBlocksBeeSpawnCapacity = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Brood Blocks will automatically spawn bees until the number of active bees is the value below. ",
                           " Set this higher to allow Brood Blocks to spawn more bees in a smaller area or set it to 0 to turn ",
                           " off automatic Brood Block bee spawning.\n")
                    .translation("the_bumblezone.config.bzblockmechanicsconfigs.broodblocksbeespawncapacity")
                    .defineInRange("broodBlocksBeeSpawnCapacity", 60, 0, 1000));

            builder.pop();
        }
    }
}
