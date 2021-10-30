package com.telepathicgrunt.the_bumblezone.configs;

import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzWorldgenConfigs {

    public static class BzWorldgenConfigValues {
        public ConfigHelper.ConfigValueListener<Integer> beeDungeonRarity;
        public ConfigHelper.ConfigValueListener<Integer> spiderInfestedBeeDungeonRarity;
        public ConfigHelper.ConfigValueListener<Double> spawnerRateSpiderBeeDungeon;

        public BzWorldgenConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            builder.push("Dungeon Options");

                beeDungeonRarity = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " How rare Bee Dungeons are. Higher numbers means more rare.",
                           " Default rate is 1. Setting to 1001 will disable Bee Dungeons.\n")
                    .translation("the_bumblezone.config.beedungeonrarity")
                    .defineInRange("beeDungeonRarity", 1, 1, 1001));

                spiderInfestedBeeDungeonRarity = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " How rare Spider Infested Bee Dungeons are. Higher numbers means more rare.",
                           " Default rate is 8. Setting to 1001 will disable Bee Dungeons.\n")
                    .translation("the_bumblezone.config.spiderinfestedbeedungeonrarity")
                    .defineInRange("spiderInfestedBeeDungeonRarity", 8, 1, 1001));

                spawnerRateSpiderBeeDungeon = subscriber.subscribe(builder
                    .comment(" \n-----------------------------------------------------\n",
                           " How rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.",
                           " 0 is no spawners, 1 is maximum spawners, and default is 0.2D\n")
                    .translation("the_bumblezone.config.spawnerratespiderbeedungeon")
                    .defineInRange("spawnerRateSpiderBeeDungeon", 0.2D, 0D, 1D));

            builder.pop();

        }
    }
}
