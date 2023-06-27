package com.telepathicgrunt.the_bumblezone.configs.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzDimensionConfig {
        // teleportation
    public static final ForgeConfigSpec GENERAL_SPEC;

    // dimension
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
                        " If the block tag the_bumblezone:dimension_teleportation/required_blocks_under_beehive_to_teleport",
                        " has blocks specified and this config is set to true, then player will get a warning if they",
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
                            " config options so that they use this new default dimension instead of Overworld.\n")
                    .translation("the_bumblezone.config.defaultdimension")
                    .define("defaultDimension", "minecraft:overworld");

        builder.pop();
    }

    public static void copyToCommon() {
        BzDimensionConfigs.enableEntranceTeleportation = enableEntranceTeleportation.get();
        BzDimensionConfigs.enableExitTeleportation = enableExitTeleportation.get();
        BzDimensionConfigs.forceBumblezoneOriginMobToOverworldCenter = forceBumblezoneOriginMobToOverworldCenter.get();
        BzDimensionConfigs.forceExitToOverworld = forceExitToOverworld.get();
        BzDimensionConfigs.onlyOverworldHivesTeleports = onlyOverworldHivesTeleports.get();
        BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive = warnPlayersOfWrongBlockUnderHive.get();
        BzDimensionConfigs.allowTeleportationWithModdedBeehives = allowTeleportationWithModdedBeehives.get();
        BzDimensionConfigs.defaultDimension = defaultDimension.get();
    }
}