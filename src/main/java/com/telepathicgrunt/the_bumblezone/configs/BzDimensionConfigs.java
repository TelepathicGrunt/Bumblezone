package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzDimensionConfigs{
        // teleportation
    public static final ForgeConfigSpec GENERAL_SPEC;

    // dimension
    public static ForgeConfigSpec.DoubleValue fogBrightnessPercentage;
    public static ForgeConfigSpec.DoubleValue fogThickness;
    public static ForgeConfigSpec.BooleanValue enableDimensionFog;
    public static ForgeConfigSpec.BooleanValue forceExitToOverworld;
    public static ForgeConfigSpec.BooleanValue onlyOverworldHivesTeleports;
    public static ForgeConfigSpec.BooleanValue warnPlayersOfWrongBlockUnderHive;
    public static ForgeConfigSpec.BooleanValue allowTeleportationWithModdedBeehives;
    public static ForgeConfigSpec.BooleanValue enableExitTeleportation;
    public static ForgeConfigSpec.BooleanValue enableEntranceTeleportation;
    public static ForgeConfigSpec.BooleanValue forceBumblezoneOriginMobToOverworldCenter;
    public static ForgeConfigSpec.ConfigValue<String> defaultDimension;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("The Bumblezone Dimension Options");

            fogBrightnessPercentage = builder
                .comment(" \n-----------------------------------------------------\n",
                       " How bright the fog is in the Bumblezone dimension. ",
                       " ",
                       " The brightness is represented as a percentage",
                       " so 0 will be pitch black, 50 will be half",
                       " as bright, 100 will be normal orange brightness,",
                       " and 100000 will be white.\n")
                .translation("the_bumblezone.config.fogbrightnesspercentage")
                .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D);

            fogThickness = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How thick the fog in Bumblezone is.",
                        " 2 is a little bit of fog and 50 is super thick fog. Decimal values are allowed.\n")
                .translation("the_bumblezone.config.fogthickness")
                .defineInRange("fogThickness", 4D, 0D, 100D);

            enableDimensionFog = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Bumblezone dimension has thick fog or not.\n")
                .translation("the_bumblezone.config.enabledimensionfog")
                .define("enableDimensionFog", true);

        builder.pop();

        builder.push("The Bumblezone Teleportation Options");

            enableEntranceTeleportation = builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Allow Bumblezone mod to handle teleporting into the Bumblezone dimension.\n")
                    .translation("the_bumblezone.config.enableentranceteleportation")
                    .define("enableEntranceTeleportation", true);

            enableExitTeleportation = builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Allow Bumblezone mod to handle teleporting out of the Bumblezone dimension.\n")
                    .translation("the_bumblezone.config.enableexitteleportation")
                    .define("enableExitTeleportation", true);

            forceBumblezoneOriginMobToOverworldCenter = builder
                .comment(" \n-----------------------------------------------------\n",
                        " If this is enabled, mobs that originally spawned in Bumblezone will be teleported",
                        " to 0, 0 center of the Overworld when exiting Bumblezone dimension.\n")
                .translation("the_bumblezone.config.forcebumblezoneoriginmobtooverworldcenter")
                .define("forceBumblezoneOriginMobToOverworldCenter", true);

            forceExitToOverworld = builder
                .comment(" \n-----------------------------------------------------\n",
                       " Makes leaving The Bumblezone dimension always places you back",
                       " at the Overworld regardless of which dimension you originally ",
                       " came from. Use this option if this dimension becomes locked in  ",
                       " with another dimension so you are stuck teleporting between the ",
                       " two and cannot get back to the Overworld.\n")
                .translation("the_bumblezone.config.forceexittooverworld")
                .define("forceExitToOverworld", false);

            onlyOverworldHivesTeleports = builder
                .comment(" \n-----------------------------------------------------\n",
                       " Makes throwing Enderpearls at Bee Nests or Hives only ",
                       " work in the Overworld. What this means setting this to true makes it ",
                       " only possible to enter The Bumblezone dimension from the Overworld")
                .translation("the_bumblezone.config.onlyoverworldhivesteleports")
                .define("onlyOverworldHivesTeleports", false);

            warnPlayersOfWrongBlockUnderHive = builder
                .comment(" \n-----------------------------------------------------\n",
                       " If the block tag file required_block_under_hive.json has blocks specified ",
                       " and this config is set to true, then player will get a warning if they",
                       " throw an Enderpearl at a Bee Nest/Beehive but the block under it is ",
                       " not the correct required block. It will also tell the player what ",
                       " block is needed under the Bee Nest/Beehive to teleport to the dimension.\n")
                .translation("the_bumblezone.config.warnplayersofwrongblockunderhive")
                .define("warnPlayersOfWrongBlockUnderHive", true);

            allowTeleportationWithModdedBeehives = builder
                .comment(" \n-----------------------------------------------------\n",
                       " Should teleporting to and from The Bumblezone work ",
                       " with modded Bee Nests and modded Beehives as well. \n")
                .translation("the_bumblezone.config.allowteleportationwithmoddedbeehives")
                .define("allowTeleportationWithModdedBeehives", true);

            defaultDimension = builder
                    .comment(" \n-----------------------------------------------------\n",
                            " Changes the default dimension that teleporting exiting will take mobs to ",
                            " if there is no previously saved dimension on the mob. ",
                            " Use this option ONLY if your modpack's default dimension is not the Overworld. ",
                            " This will affect forceExitToOverworld, forceBumblezoneOriginMobToOverworldCenter, and onlyOverworldHivesTeleports",
                            " config options so that they use this new default dimension instead of overworld.\n")
                    .translation("the_bumblezone.config.defaultdimension")
                    .define("defaultDimension", "minecraft:overworld");

        builder.pop();
    }
}
