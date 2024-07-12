package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BzDimensionConfig {
        // teleportation
    public static final ModConfigSpec GENERAL_SPEC;

    // dimension
    public static ModConfigSpec.BooleanValue enableInitialWelcomeMessage;
    public static ModConfigSpec.BooleanValue forceExitToOverworld;
    public static ModConfigSpec.BooleanValue onlyOverworldHivesTeleports;
    public static ModConfigSpec.BooleanValue warnPlayersOfWrongBlockUnderHive;
    public static ModConfigSpec.BooleanValue allowTeleportationWithModdedBeehives;
    public static ModConfigSpec.BooleanValue enableExitTeleportation;
    public static ModConfigSpec.BooleanValue enableEntranceTeleportation;
    public static ModConfigSpec.BooleanValue forceBumblezoneOriginMobToOverworldCenter;
    public static ModConfigSpec.ConfigValue<String> defaultDimension;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
        builder.translation("the_bumblezone.configuration.thebumblezonewelcomeoptions").push("The Bumblezone Welcome Options");

        enableInitialWelcomeMessage = builder
                .comment("----------------------------\n",
                        " Whether to show message saying to check out Bumblezone's advancements when you get near beehive for first time.\n")
                .translation("the_bumblezone.configuration.enableinitialwelcomemessage")
                .define("enableInitialWelcomeMessage", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.teleportationoptions").push("The Bumblezone Teleportation Options");

            enableEntranceTeleportation = builder
                    .comment("----------------------------\n",
                            " Allow Bumblezone mod to handle teleporting into the Bumblezone dimension.\n")
                    .translation("the_bumblezone.configuration.enableentranceteleportation")
                    .define("enableEntranceTeleportation", true);

            enableExitTeleportation = builder
                    .comment("----------------------------\n",
                            " Allow Bumblezone mod to handle teleporting out of the Bumblezone dimension.\n")
                    .translation("the_bumblezone.configuration.enableexitteleportation")
                    .define("enableExitTeleportation", true);

            forceBumblezoneOriginMobToOverworldCenter = builder
                .comment("----------------------------\n",
                        " If this is enabled, mobs that originally spawned in Bumblezone will be teleported",
                        " to 0, 0 center of the Overworld when exiting Bumblezone dimension.\n")
                .translation("the_bumblezone.configuration.forcebumblezoneoriginmobtooverworldcenter")
                .define("forceBumblezoneOriginMobToOverworldCenter", true);

            forceExitToOverworld = builder
                .comment("----------------------------\n",
                       " Makes leaving The Bumblezone dimension always places you back",
                       " at the Overworld regardless of which dimension you originally",
                       " came from. Use this option if this dimension becomes locked in",
                       " with another dimension so you are stuck teleporting between the",
                       " two and cannot get back to the Overworld.\n")
                .translation("the_bumblezone.configuration.forceexittooverworld")
                .define("forceExitToOverworld", false);

            onlyOverworldHivesTeleports = builder
                .comment("----------------------------\n",
                       " Makes throwing Enderpearls at Bee Nests or Hives only",
                       " work in the Overworld. What this means setting this to true makes it",
                       " only possible to enter The Bumblezone dimension from the Overworld")
                .translation("the_bumblezone.configuration.onlyoverworldhivesteleports")
                .define("onlyOverworldHivesTeleports", false);

            warnPlayersOfWrongBlockUnderHive = builder
                .comment("----------------------------\n",
                        " If the block tag `the_bumblezone:dimension_teleportation/required_blocks_under_beehive_to_teleport`",
                        " has blocks specified and this config is set to true, then player will get a warning if they",
                        " throw an Enderpearl at a Bee Nest/Beehive but the block under it is",
                        " not the correct required block. It will also tell the player what",
                        " block is needed under the Bee Nest/Beehive to teleport to the dimension.\n")
                .translation("the_bumblezone.configuration.warnplayersofwrongblockunderhive")
                .define("warnPlayersOfWrongBlockUnderHive", true);

            allowTeleportationWithModdedBeehives = builder
                .comment("----------------------------\n",
                       " Should teleporting to and from The Bumblezone work",
                       " with modded Bee Nests and modded Beehives as well.\n")
                .translation("the_bumblezone.configuration.allowteleportationwithmoddedbeehives")
                .define("allowTeleportationWithModdedBeehives", true);

            defaultDimension = builder
                    .comment("----------------------------\n",
                            " Changes the default dimension that teleporting exiting will take mobs to",
                            " if there is no previously saved dimension on the mob.",
                            " Use this option ONLY if your modpack's default dimension is not the Overworld.",
                            " This will affect forceExitToOverworld, forceBumblezoneOriginMobToOverworldCenter, and onlyOverworldHivesTeleports",
                            " config options so that they use this new default dimension instead of Overworld.\n")
                    .translation("the_bumblezone.configuration.defaultdimension")
                    .define("defaultDimension", "minecraft:overworld");

        builder.pop();
    }

    public static void copyToCommon() {
        BzDimensionConfigs.enableInitialWelcomeMessage = enableInitialWelcomeMessage.get();
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