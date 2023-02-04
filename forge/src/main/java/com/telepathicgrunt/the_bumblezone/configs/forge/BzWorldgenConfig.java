package com.telepathicgrunt.the_bumblezone.configs.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzWorldgenConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.IntValue beeDungeonRarity;
    public static ForgeConfigSpec.IntValue spiderInfestedBeeDungeonRarity;
    public static ForgeConfigSpec.DoubleValue spawnerRateSpiderBeeDungeon;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Dungeon Options");

            beeDungeonRarity = builder
                .comment(" \n-----------------------------------------------------\n",
                       " How rare Bee Dungeons are. Higher numbers means more rare.",
                       " Default rate is 1. Setting to 1001 will disable Bee Dungeons.\n")
                .translation("the_bumblezone.config.beedungeonrarity")
                .defineInRange("beeDungeonRarity", 1, 1, 1001);

            spiderInfestedBeeDungeonRarity = builder
                .comment(" \n-----------------------------------------------------\n",
                       " How rare Spider Infested Bee Dungeons are. Higher numbers means more rare.",
                       " Default rate is 8. Setting to 1001 will disable Bee Dungeons.\n")
                .translation("the_bumblezone.config.spiderinfestedbeedungeonrarity")
                .defineInRange("spiderInfestedBeeDungeonRarity", 8, 1, 1001);

            spawnerRateSpiderBeeDungeon = builder
                .comment(" \n-----------------------------------------------------\n",
                       " How rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.",
                       " 0 is no spawners, 1 is maximum spawners, and default is 0.2D\n")
                .translation("the_bumblezone.config.spawnerratespiderbeedungeon")
                .defineInRange("spawnerRateSpiderBeeDungeon", 0.2D, 0D, 1D);

        builder.pop();

    }

    public static void copyToCommon() {
        BzWorldgenConfigs.beeDungeonRarity = beeDungeonRarity.get();
        BzWorldgenConfigs.spiderInfestedBeeDungeonRarity = spiderInfestedBeeDungeonRarity.get();
        BzWorldgenConfigs.spawnerRateSpiderBeeDungeon = spawnerRateSpiderBeeDungeon.get();
    }
}