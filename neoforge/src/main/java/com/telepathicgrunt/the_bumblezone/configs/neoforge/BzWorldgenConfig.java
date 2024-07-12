package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BzWorldgenConfig {
    public static final ModConfigSpec GENERAL_SPEC;

    public static ModConfigSpec.IntValue beeDungeonRarity;
    public static ModConfigSpec.IntValue treeDungeonRarity;
    public static ModConfigSpec.IntValue spiderInfestedBeeDungeonRarity;
    public static ModConfigSpec.DoubleValue spawnerRateSpiderBeeDungeon;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
        builder.translation("the_bumblezone.configuration.dungeonoptions").push("Dungeon Options");

            beeDungeonRarity = builder
                .comment("----------------------------\n",
                       " How rare Bee Dungeons are. Higher numbers means more rare.",
                       " Default rate is 1. Setting to 1001 will disable Bee Dungeons.\n")
                .translation("the_bumblezone.configuration.beedungeonrarity")
                .defineInRange("beeDungeonRarity", 1, 1, 1001);

            treeDungeonRarity = builder
                .comment("----------------------------\n",
                       " How rare Tree Dungeons are. Higher numbers means more rare.",
                       " Default rate is 1. Setting to 1001 will disable Tree Dungeons.\n")
                .translation("the_bumblezone.configuration.treedungeonrarity")
                .defineInRange("treeDungeonRarity", 2, 1, 1001);

            spiderInfestedBeeDungeonRarity = builder
                .comment("----------------------------\n",
                       " How rare Spider Infested Bee Dungeons are. Higher numbers means more rare.",
                       " Default rate is 5. Setting to 1001 will disable Bee Dungeons.\n")
                .translation("the_bumblezone.configuration.spiderinfestedbeedungeonrarity")
                .defineInRange("spiderInfestedBeeDungeonRarity", 5, 1, 1001);

            spawnerRateSpiderBeeDungeon = builder
                .comment("----------------------------\n",
                       " How rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.",
                       " 0 is no spawners, 1 is maximum spawners, and default is 0.2D\n")
                .translation("the_bumblezone.configuration.spawnerratespiderbeedungeon")
                .defineInRange("spawnerRateSpiderBeeDungeon", 0.2D, 0D, 1D);

        builder.pop();

    }

    public static void copyToCommon() {
        BzWorldgenConfigs.beeDungeonRarity = beeDungeonRarity.get();
        BzWorldgenConfigs.treeDungeonRarity = treeDungeonRarity.get();
        BzWorldgenConfigs.spiderInfestedBeeDungeonRarity = spiderInfestedBeeDungeonRarity.get();
        BzWorldgenConfigs.spawnerRateSpiderBeeDungeon = spawnerRateSpiderBeeDungeon.get();
    }
}