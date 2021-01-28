package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

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
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" How bright the fog is in the Bumblezone dimension. \r\n"
                            +" \r\n"
                            +" The brightness is represented as a percentage\r\n"
                            +" so 0 will be pitch black, 50 will be half\r\n"
                            +" as bright, 100 will be normal orange brightness,\r\n"
                            +" and 100000 will be white.\r\n")
                    .translation("the_bumblezone.config.dimension.fogbrightnesspercentage")
                    .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D));


            builder.pop();
            builder.push("The Bumblezone Teleportation Options");


                teleportationMode = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Which mode of teleportation should be used when \r\n"
                            +" leaving The Bumblezone dimension. \r\n"
                            +" \r\n"
                            +" Mode 1: Coordinates will be converted to the other \r\n"
                            +" dimension's coordinate scale and the game will look for\r\n"
                            +" a Bee Nest/Beehive at the new spot to spawn players at. \r\n"
                            +" If none is found, players will still be placed at the spot.\r\n"
                            +" \r\n"
                            +" Mode 2: Will always spawn players at the original spot \r\n"
                            +" in the non-BZ dimension where they threw the Enderpearl \r\n"
                            +" at a Bee Nest/Beehive.\r\n"
                            +" \r\n"
                            +"Mode 3: Will use mode 1's teleportation method if Bee Nest/Beehive\r\n"
                            +"is near the spot when exiting the dimension. If none is found,\r\n"
                            +"then mode 2's teleportation method is used instead.\r\n")
                    .translation("the_bumblezone.config.dimension.teleportationmode")
                    .defineInRange("teleportationMode", 1, 1, 3));

                generateBeenest = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Will a Beenest generate if no Beenest is  \r\n"
                            +" found when leaving The Bumblezone dimension.\r\n"
                            +" \r\n"
                            +" ONLY FOR TELEPORTATION MODE 1.\r\n")
                    .translation("the_bumblezone.config.dimension.generatebeenest")
                    .define("generateBeenest", true));

                forceExitToOverworld = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Makes leaving The Bumblezone dimension always places you back\r\n "
                            +" at the Overworld regardless of which dimension you originally \r\n"
                            +" came from. Use this option if this dimension becomes locked in  \r\n"
                            +" with another dimension so you are stuck teleporting between the \r\n"
                            +" two and cannot get back to the Overworld.\r\n")
                    .translation("the_bumblezone.config.dimension.forceexittooverworld")
                    .define("forceExitToOverworld", false));

            onlyOverworldHivesTeleports = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Makes throwing Enderpearls at Bee Nests or Hives only\r\n "
                            +" work in the Overworld. What this means setting this to true makes it \r\n"
                            +" only possible to enter The Bumblezone dimension from the Overworld")
                    .translation("the_bumblezone.config.dimension.onlyoverworldhivesteleports")
                    .define("onlyOverworldHivesTeleports", false));


                seaLevelOrHigherExitTeleporting = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Should exiting The Bumblezone always try and place you \r\n"
                            +" above sealevel in the target dimension? (Will only look \r\n"
                            +" for beehives above sealevel as well when placing you)"
                            +" \r\n"
                            +" ONLY FOR TELEPORTATION MODE 1 AND 3.\r\n")
                    .translation("the_bumblezone.config.dimension.sealevelorhigherexitteleporting")
                    .define("seaLevelOrHigherExitTeleporting", true));

                warnPlayersOfWrongBlockUnderHive = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" If the block tag file required_block_under_hive.json has blocks specified\r\n "
                            +" and this config is set to true, then player will get a warning if they\r\n"
                            +" throw an Enderpearl at a Bee Nest/Beehive but the block under it is \r\n"
                            +" not the correct required block. It will also tell the player what \r\n"
                            +" block is needed under the Bee Nest/Beehive to teleport to the dimension.\r\n")
                    .translation("the_bumblezone.config.dimension.warnplayersofwrongblockunderhive")
                    .define("warnPlayersOfWrongBlockUnderHive", true));


                allowTeleportationWithModdedBeehives = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" Should teleporting to and from The Bumblezone work \r\n"
                            +" with modded Bee Nests and modded Beehives as well. \r\n")
                    .translation("the_bumblezone.config.dimension.allowteleportationwithmoddedbeehives")
                    .define("allowTeleportationWithModdedBeehives", true));

            builder.pop();
        }
    }
}
