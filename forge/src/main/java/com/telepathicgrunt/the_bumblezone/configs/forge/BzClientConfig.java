package com.telepathicgrunt.the_bumblezone.configs.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.minecraftforge.common.ForgeConfigSpec;

public class BzClientConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue fogBrightnessPercentage;
    public static ForgeConfigSpec.DoubleValue fogThickness;
    public static ForgeConfigSpec.BooleanValue enableDimensionFog;
    public static ForgeConfigSpec.BooleanValue playWrathOfHiveEffectMusic;
    public static ForgeConfigSpec.BooleanValue renderBeeQueenBonusTradeItem;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightBosses;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightMonsters;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightTamed;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightLivingEntities;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightCommonItems;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightUncommonItems;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightRareItems;
    public static ForgeConfigSpec.BooleanValue knowingEssenceHighlightEpicItems;
    public static ForgeConfigSpec.BooleanValue knowingEssenceStructureNameClient;

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

        builder.push("Knowing Essense Highlighting Configs (see block and entity tags for even more options)");

        knowingEssenceHighlightBosses = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight bosses in purple. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.config.knowingEssenceHighlightBosses")
                .define("knowingEssenceHighlightBosses", true);


        knowingEssenceHighlightMonsters = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight monsters in red. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.config.knowingessencehighlightmonsters")
                .define("knowingEssenceHighlightMonsters", true);

        knowingEssenceHighlightTamed = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight mobs you tamed in green. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.config.knowingessencehighlighttamed")
                .define("knowingEssenceHighlightTamed", true);

        knowingEssenceHighlightLivingEntities = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight any other mobs in orange. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.config.knowingessencehighlightlivingentities")
                .define("knowingEssenceHighlightLivingEntities", true);

        knowingEssenceHighlightCommonItems = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight common items in world in white color.\n")
                .translation("the_bumblezone.config.knowingessencehighlightcommonitems")
                .define("knowingEssenceHighlightCommonItems", true);

        knowingEssenceHighlightUncommonItems = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight uncommon items in world in yellow color.\n")
                .translation("the_bumblezone.config.knowingessencehighlightuncommonitems")
                .define("knowingEssenceHighlightUncommonItems", true);

        knowingEssenceHighlightRareItems = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight rare items in world in cyan color.\n")
                .translation("the_bumblezone.config.knowingessencehighlightrareitems")
                .define("knowingEssenceHighlightRareItems", true);

        knowingEssenceHighlightEpicItems = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will highlight epic items in world in purple color.\n")
                .translation("the_bumblezone.config.knowingessencehighlightepicitems")
                .define("knowingEssenceHighlightEpicItems", true);

        knowingEssenceStructureNameClient = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Knowing Essence will tell you the name of structures you are in.\n")
                .translation("the_bumblezone.config.knowingessencestructurenameclient")
                .define("knowingEssenceStructureNameClient", true);

        builder.pop();

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
        BzClientConfigs.playWrathOfHiveEffectMusic = playWrathOfHiveEffectMusic.get();
        BzClientConfigs.renderBeeQueenBonusTradeItem = renderBeeQueenBonusTradeItem.get();
        BzClientConfigs.knowingEssenceHighlightBosses = knowingEssenceHighlightBosses.get();
        BzClientConfigs.knowingEssenceHighlightMonsters = knowingEssenceHighlightMonsters.get();
        BzClientConfigs.knowingEssenceHighlightTamed = knowingEssenceHighlightTamed.get();
        BzClientConfigs.knowingEssenceHighlightLivingEntities = knowingEssenceHighlightLivingEntities.get();
        BzClientConfigs.knowingEssenceHighlightCommonItems = knowingEssenceHighlightCommonItems.get();
        BzClientConfigs.knowingEssenceHighlightUncommonItems = knowingEssenceHighlightUncommonItems.get();
        BzClientConfigs.knowingEssenceHighlightRareItems = knowingEssenceHighlightRareItems.get();
        BzClientConfigs.knowingEssenceHighlightEpicItems = knowingEssenceHighlightEpicItems.get();
        BzClientConfigs.knowingEssenceStructureNameClient = knowingEssenceStructureNameClient.get();
    }
}