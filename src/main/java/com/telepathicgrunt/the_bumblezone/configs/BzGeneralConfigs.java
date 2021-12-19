package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzGeneralConfigs {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue beehemothSpeed;
    public static ForgeConfigSpec.BooleanValue dispensersDropGlassBottles;
    public static ForgeConfigSpec.IntValue broodBlocksBeeSpawnCapacity;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Beehemoth Options");

        beehemothSpeed = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Base speed for the Beehemoth when being rode by a player.\n")
                .translation("the_bumblezone.config.beehemothSpeed")
                .defineInRange("beehemothSpeed", 0.95D, 0D, 100D);

        builder.pop();

        builder.push("General Mechanics Options");

        dispensersDropGlassBottles = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Should Dispensers always drop the Glass Bottle when using specific ",
                        " bottle items on certain The Bumblezone blocks? ",
                        " ",
                        " Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and",
                        " drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.\n")
                .translation("the_bumblezone.config.dispensersdropglassbottles")
                .define("dispensersDropGlassBottles", false);

        broodBlocksBeeSpawnCapacity = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Brood Blocks will automatically spawn bees until the number of active bees is the value below. ",
                        " Set this higher to allow Brood Blocks to spawn more bees in a smaller area or set it to 0 to turn ",
                        " off automatic Brood Block bee spawning.\n")
                .translation("the_bumblezone.config.broodblocksbeespawncapacity")
                .defineInRange("broodBlocksBeeSpawnCapacity", 60, 0, 1000);

        builder.pop();
    }
}
