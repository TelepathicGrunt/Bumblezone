package com.telepathicgrunt.the_bumblezone.configs.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzClientConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue fogBrightnessPercentage;
    public static ForgeConfigSpec.DoubleValue fogThickness;
    public static ForgeConfigSpec.BooleanValue enableDimensionFog;
    public static ForgeConfigSpec.DoubleValue lgbtBeeRate;
    public static ForgeConfigSpec.DoubleValue ukraineBeeRate;
    public static ForgeConfigSpec.BooleanValue enableAltBeeSkinRenderer;
    public static ForgeConfigSpec.BooleanValue playWrathOfHiveEffectMusic;
    public static ForgeConfigSpec.BooleanValue renderBeeQueenBonusTradeItem;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("The Bumblezone Client Dimension Options");

        fogBrightnessPercentage = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How bright the fog is in the Bumblezone dimension. ",
                        " ",
                        " The brightness is represented as a percentage",
                        " so 0 will be pitch black, 50 will be half",
                        " as bright, 100 will be normal orange brightness,",
                        " and 100000 will be white.\n")
                .translation("the_bumblezone.config.fogbrightnesspercentage")
                .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D);

        fogThickness = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How thick the fog in Bumblezone is.",
                        " 2 is a little bit of fog and 50 is super thick fog. Decimal values are allowed.\n")
                .translation("the_bumblezone.config.fogthickness")
                .defineInRange("fogThickness", 4D, 0D, 100D);

        enableDimensionFog = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Bumblezone dimension has thick fog or not.\n")
                .translation("the_bumblezone.config.enabledimensionfog")
                .define("enableDimensionFog", true);

        builder.pop();

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

        renderBeeQueenBonusTradeItem = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether to show the item on the Bee Queen that the Queen wants for Bonus Trades.\n")
                .translation("the_bumblezone.config.renderbeequeenbonusTradeitem")
                .define("renderBeeQueenBonusTradeItem", true);
    }

    public static void copyToCommon() {
        BzDimensionConfigs.fogBrightnessPercentage = fogBrightnessPercentage.get();
        BzDimensionConfigs.fogThickness = fogThickness.get();
        BzDimensionConfigs.enableDimensionFog = enableDimensionFog.get();
        BzClientConfigs.lgbtBeeRate = lgbtBeeRate.get();
        BzClientConfigs.ukraineBeeRate = ukraineBeeRate.get();
        BzClientConfigs.enableAltBeeSkinRenderer = enableAltBeeSkinRenderer.get();
        BzClientConfigs.playWrathOfHiveEffectMusic = playWrathOfHiveEffectMusic.get();
        BzClientConfigs.renderBeeQueenBonusTradeItem = renderBeeQueenBonusTradeItem.get();
    }
}