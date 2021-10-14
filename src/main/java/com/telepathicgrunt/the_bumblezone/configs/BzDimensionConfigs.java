package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzDimensionConfigs{

    public static class BzDimensionConfigValues {

        // dimension
        public ConfigHelper.ConfigValueListener<Double> fogBrightnessPercentage;


        // teleportation
        public ConfigHelper.ConfigValueListener<Integer> teleportationMode;
        public ConfigHelper.ConfigValueListener<Boolean> generateBeenest;
        public ConfigHelper.ConfigValueListener<Boolean> forceExitToOverworld;
        public ConfigHelper.ConfigValueListener<Boolean> onlyOverworldHivesTeleports;
        public ConfigHelper.ConfigValueListener<Boolean> warnPlayersOfWrongBlockUnderHive;
        public ConfigHelper.ConfigValueListener<Boolean> allowTeleportationWithModdedBeehives;
        public ConfigHelper.ConfigValueListener<Boolean> seaLevelOrHigherExitTeleporting;


        public BzDimensionConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {


            builder.push("The Bumblezone Dimension Options");

                fogBrightnessPercentage = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " How bright the fog is in the Bumblezone dimension. ",
                           " ",
                           " The brightness is represented as a percentage",
                           " so 0 will be pitch black, 50 will be half",
                           " as bright, 100 will be normal orange brightness,",
                           " and 100000 will be white.\n")
                    .translation("the_bumblezone.config.dimension.fogbrightnesspercentage")
                    .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D));


            builder.pop();
            builder.push("The Bumblezone Teleportation Options");


                teleportationMode = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Which mode of teleportation should be used when ",
                           " leaving The Bumblezone dimension. ",
                           " ",
                           " Mode 1: Coordinates will be converted to the other ",
                           " dimension's coordinate scale and the game will look for",
                           " a Bee Nest/Beehive at the new spot to spawn players at. ",
                           " If none is found, players will still be placed at the spot.",
                           " ",
                           " Mode 2: Will always spawn players at the original spot ",
                           " in the non-BZ dimension where they threw the Enderpearl ",
                           " at a Bee Nest/Beehive.",
                           " ",
                           "Mode 3: Will use mode 1's teleportation method if Bee Nest/Beehive",
                           "is near the spot when exiting the dimension. If none is found,",
                           "then mode 2's teleportation method is used instead.\n")
                    .translation("the_bumblezone.config.dimension.teleportationmode")
                    .defineInRange("teleportationMode", 1, 1, 3));

                generateBeenest = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Will a Beenest generate if no Beenest is  ",
                           " found when leaving The Bumblezone dimension.",
                           " ",
                           " ONLY FOR TELEPORTATION MODE 1.\n")
                    .translation("the_bumblezone.config.dimension.generatebeenest")
                    .define("generateBeenest", true));

                forceExitToOverworld = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Makes leaving The Bumblezone dimension always places you back\n "
                            +" at the Overworld regardless of which dimension you originally ",
                           " came from. Use this option if this dimension becomes locked in  ",
                           " with another dimension so you are stuck teleporting between the ",
                           " two and cannot get back to the Overworld.\n")
                    .translation("the_bumblezone.config.dimension.forceexittooverworld")
                    .define("forceExitToOverworld", false));

            onlyOverworldHivesTeleports = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Makes throwing Enderpearls at Bee Nests or Hives only\n "
                            +" work in the Overworld. What this means setting this to true makes it ",
                           " only possible to enter The Bumblezone dimension from the Overworld")
                    .translation("the_bumblezone.config.dimension.onlyoverworldhivesteleports")
                    .define("onlyOverworldHivesTeleports", false));


                seaLevelOrHigherExitTeleporting = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Should exiting The Bumblezone always try and place you ",
                           " above sealevel in the target dimension? (Will only look ",
                           " for beehives above sealevel as well when placing you)"
                            +" ",
                           " ONLY FOR TELEPORTATION MODE 1 AND 3.\n")
                    .translation("the_bumblezone.config.dimension.sealevelorhigherexitteleporting")
                    .define("seaLevelOrHigherExitTeleporting", true));

                warnPlayersOfWrongBlockUnderHive = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " If the block tag file required_block_under_hive.json has blocks specified\n "
                            +" and this config is set to true, then player will get a warning if they",
                           " throw an Enderpearl at a Bee Nest/Beehive but the block under it is ",
                           " not the correct required block. It will also tell the player what ",
                           " block is needed under the Bee Nest/Beehive to teleport to the dimension.\n")
                    .translation("the_bumblezone.config.dimension.warnplayersofwrongblockunderhive")
                    .define("warnPlayersOfWrongBlockUnderHive", true));


                allowTeleportationWithModdedBeehives = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " Should teleporting to and from The Bumblezone work ",
                           " with modded Bee Nests and modded Beehives as well. \n")
                    .translation("the_bumblezone.config.dimension.allowteleportationwithmoddedbeehives")
                    .define("allowTeleportationWithModdedBeehives", true));

            builder.pop();
        }
    }
}
