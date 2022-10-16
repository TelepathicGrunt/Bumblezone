package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzGeneralConfigs {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue beehemothSpeed;
    public static ForgeConfigSpec.BooleanValue specialBeeSpawning;
    public static ForgeConfigSpec.IntValue nearbyBeesPerPlayerInBz;
    public static ForgeConfigSpec.BooleanValue dispensersDropGlassBottles;
    public static ForgeConfigSpec.IntValue broodBlocksBeeSpawnCapacity;
    public static ForgeConfigSpec.BooleanValue keepBeeEssenceOnRespawning;
    public static ForgeConfigSpec.IntValue musicDiscTimeLengthFlightOfTheBumblebee;
    public static ForgeConfigSpec.IntValue musicDiscTimeLengthHoneyBee;
    public static ForgeConfigSpec.BooleanValue crystallineFlowerConsumeItemEntities;
    public static ForgeConfigSpec.BooleanValue crystallineFlowerConsumeExperienceOrbEntities;

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


        builder.push("Special Bee Spawning Options");

        specialBeeSpawning = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Bumblezone will handle spawning vanilla bees near players in the Bumblezone to make it feel full of Bees.",
                        " Bees too far will be despawned in Bumblezone unless the bee has a hive, is name tagged, or is set to persistent.",
                        " Note: Modded bees will not be spawned through this system. Those will be spawned by normal biome spawning.\n")
                .translation("the_bumblezone.config.specialbeespawning")
                .define("specialBeeSpawning", true);


        nearbyBeesPerPlayerInBz = builder
                .comment(" \n-----------------------------------------------------\n",
                        " If specialBeeSpawning is set to true, this config controls how many vanilla bees should be",
                        " near each player in Bumblezone dimension. Higher numbers like 100 is insane lol. 25 is nice.\n")
                .translation("the_bumblezone.config.nearbybeesperplayerinbz")
                .defineInRange("nearbyBeesPerPlayerInBz", 25, 0, 1000);

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

        keepBeeEssenceOnRespawning = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Essence of the Bees's effect should stay on the player if they die and then respawn.\n")
                .translation("the_bumblezone.config.keepBeeEssenceOnRespawning")
                .define("keepBeeEssenceOnRespawning", false);


        musicDiscTimeLengthFlightOfTheBumblebee = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in seconds this music disc will be playing music.",
                        " This is used for the server to know when to make Allays stop dancing when Jukebox plays this music disc.\n")
                .translation("the_bumblezone.config.broodblocksbeespawncapacity")
                .defineInRange("broodBlocksBeeSpawnCapacity", 84, 0 , 1000000);


        musicDiscTimeLengthHoneyBee = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in seconds this music disc will be playing music.",
                        " This is used for the server to know when to make Allays stop dancing when Jukebox plays this music disc.\n")
                .translation("the_bumblezone.config.musicdisctimelengthhoneybee")
                .defineInRange("musicDiscTimeLengthHoneyBee", 216, 0 , 1000000);

        crystallineFlowerConsumeItemEntities = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether the Crystalline Flower block will eat any item entity that touches the block's collision box in the world\n")
                .translation("the_bumblezone.config.crystallineflowerconsumeitementities")
                .define("crystallineFlowerConsumeItemEntities", true);

        crystallineFlowerConsumeExperienceOrbEntities = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether the Crystalline Flower block will pull in and eat any experience orb that touches it in the world\n")
                .translation("the_bumblezone.config.crystallineflowerconsumeexperienceorbentities")
                .define("crystallineFlowerConsumeExperienceOrbEntities", true);

        builder.pop();
    }
}
