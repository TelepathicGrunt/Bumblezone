package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzClientConfigs {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue lgbtBeeRate;
    public static ForgeConfigSpec.BooleanValue enableLgbtBeeRenderer;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        lgbtBeeRate = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Rate for how often a bee will have an LGBT+ coat!\n")
                .translation("the_bumblezone.config.lgbtBeeRate")
                .defineInRange("lgbt+ bee rates", 0.02D, 0D, 1D);

        enableLgbtBeeRenderer = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Enable replacing the bee renderer for LGBT+ skins.",
                        " Set this to false if the render is messing with other mod's bee renderers.\n")
                .translation("the_bumblezone.config.enableLgbtBeeRenderer")
                .define("Enable lgbt+ bee renderer", true);
    }
}
