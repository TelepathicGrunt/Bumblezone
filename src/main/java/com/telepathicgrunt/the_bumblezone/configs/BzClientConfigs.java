package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzClientConfigs {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue lgbtBeeRate;
    public static ForgeConfigSpec.DoubleValue ukraineBeeRate;
    public static ForgeConfigSpec.BooleanValue enableAltBeeSkinRenderer;
    public static ForgeConfigSpec.BooleanValue playWrathOfHiveEffectMusic;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        lgbtBeeRate = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Rate for how often a bee will have an LGBT+ pajama!\n")
                .translation("the_bumblezone.config.lgbtbeerate")
                .defineInRange("lgbt+ bee rates", 0.013D, 0D, 1D);

        ukraineBeeRate = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Rate for how often a bee will have an Ukraine pajama!\n")
                .translation("the_bumblezone.config.ukrainebeerate")
                .defineInRange("ukraine bee rates", 0.004D, 0D, 1D);

        enableAltBeeSkinRenderer = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Enable replacing the bee renderer for alternative skins.",
                        " Set this to false if the render is messing with other mod's bee renderers.\n")
                .translation("the_bumblezone.config.enablelgbtbeerenderer")
                .define("Enable lgbt+ bee renderer", true);

        playWrathOfHiveEffectMusic = builder
                .comment(" \n-----------------------------------------------------\n",
                        " If on, Flight of the Bumblebee by Rimsky Korsakov will play when you have Wrath of the Hive effect on.\n")
                .translation("the_bumblezone.config.playwrathofhiveeffectmusic")
                .define("playWrathOfHiveEffectMusic", true);
    }
}
