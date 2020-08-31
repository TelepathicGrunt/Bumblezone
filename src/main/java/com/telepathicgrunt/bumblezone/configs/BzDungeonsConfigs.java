package com.telepathicgrunt.bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import com.telepathicgrunt.bumblezone.utils.ConfigHelper;

@Mod.EventBusSubscriber
public class BzDungeonsConfigs {

    public static class BzDungeonsConfigValues {
        public ConfigHelper.ConfigValueListener<Integer> beeDungeonRarity;
        public ConfigHelper.ConfigValueListener<Integer> spiderInfestedBeeDungeonRarity;
        public ConfigHelper.ConfigValueListener<Double> spawnerRateSpiderBeeDungeon;

        public BzDungeonsConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            builder.push("Dungeon Options");

                beeDungeonRarity = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" How rare Bee Dungeons are. Higher numbers means more rare.\r\n"
                            +" Default rate is 1. Setting to 1001 will disable Bee Dungeons.\r\n")
                    .translation("the_bumblezone.config.dungeons.beedungeonrarity")
                    .defineInRange("beeDungeonRarity", 1, 1, 1001));

                spiderInfestedBeeDungeonRarity = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" How rare Spider Infested Bee Dungeons are. Higher numbers means more rare.\r\n"
                            +" Default rate is 8. Setting to 1001 will disable Bee Dungeons.\r\n")
                    .translation("the_bumblezone.config.dungeons.spiderinfestedbeedungeonrarity")
                    .defineInRange("spiderInfestedBeeDungeonRarity", 8, 1, 1001));

                spawnerRateSpiderBeeDungeon = subscriber.subscribe(builder
                    .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                            +" How rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.\r\n"
                            +" 0 is no spawners, 1 is maximum spawners, and default is 0.2D\r\n")
                    .translation("the_bumblezone.config.dungeons.spawnerratespiderbeedungeon")
                    .defineInRange("spawnerRateSpiderBeeDungeon", 0.2D, 0D, 1D));

            builder.pop();

        }
    }
}
