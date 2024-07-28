package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BzClientConfig {
    public static final ModConfigSpec GENERAL_SPEC;

    public static ModConfigSpec.DoubleValue fogBrightnessPercentage;
    public static ModConfigSpec.DoubleValue fogThickness;
    public static ModConfigSpec.BooleanValue enableDimensionFog;
    public static ModConfigSpec.BooleanValue useBackupModelForVariantBee;
    public static ModConfigSpec.BooleanValue playWrathOfHiveEffectMusic;
    public static ModConfigSpec.BooleanValue playSempiternalSanctumMusic;
    public static ModConfigSpec.BooleanValue renderBeeQueenBonusTradeItem;
    public static ModConfigSpec.BooleanValue disableEssenceBlockShaders;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightBosses;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightMonsters;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightTamed;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightLivingEntities;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightCommonItems;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightUncommonItems;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightRareItems;
    public static ModConfigSpec.BooleanValue knowingEssenceHighlightEpicItems;
    public static ModConfigSpec.BooleanValue knowingEssenceStructureNameClient;
    public static ModConfigSpec.IntValue knowingEssenceStructureNameXCoord;
    public static ModConfigSpec.IntValue knowingEssenceStructureNameYCoord;
    public static ModConfigSpec.BooleanValue radianceEssenceArmorDurability;
    public static ModConfigSpec.IntValue radianceEssenceArmorDurabilityXCoord;
    public static ModConfigSpec.IntValue radianceEssenceArmorDurabilityYCoord;
    public static ModConfigSpec.IntValue essenceItemHUDVisualEffectLayers;
    public static ModConfigSpec.DoubleValue essenceItemHUDVisualEffectSpeed;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
        builder.translation("the_bumblezone.configuration.clientdimensionoptions").push("The Bumblezone Client Dimension Options");

        fogBrightnessPercentage = builder
                .comment("----------------------------\n",
                        " How bright the fog is in the Bumblezone dimension. ",
                        " ",
                        " The brightness is represented as a percentage",
                        " so 0 will be pitch black, 50 will be half",
                        " as bright, 100 will be normal orange brightness,",
                        " and 100000 will be white.\n")
                .translation("the_bumblezone.configuration.fogbrightnesspercentage")
                .defineInRange("fogBrightnessPercentage", 100D, 0D, 100000D);

        fogThickness = builder
                .comment("----------------------------\n",
                        " How thick the fog in Bumblezone is.",
                        " 2 is a little bit of fog and 50 is super thick fog. Decimal values are allowed.\n")
                .translation("the_bumblezone.configuration.fogthickness")
                .defineInRange("fogThickness", 4D, 0D, 100D);

        enableDimensionFog = builder
                .comment("----------------------------\n",
                        " Whether Bumblezone dimension has thick fog or not.\n")
                .translation("the_bumblezone.configuration.enabledimensionfog")
                .define("enableDimensionFog", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.essenceitemhudrenderingconfigs").push("Essence Item HUD Rendering Configs");

        essenceItemHUDVisualEffectLayers = builder
                .comment("----------------------------\n",
                        " How many of the rotating texture layers to show on HUD when Essence item is in offhand slot.",
                        " 0 to turn the rendering off.\n")
                .translation("the_bumblezone.configuration.essenceitemhudvisualeffectlayers")
                .defineInRange("essenceItemHUDVisualEffectLayers", 3, 0, 3);

        essenceItemHUDVisualEffectSpeed = builder
                .comment("----------------------------\n",
                        " How fast to spin the rotating texture layers on HUD when Essence item is in offhand slot.",
                        " 0.5 for half speed. 0 to turn the spinning off.\n")
                .translation("the_bumblezone.configuration.essenceitemhudvisualeffectspeed")
                .defineInRange("essenceItemHUDVisualEffectSpeed", 1.0D, 0, 100);

        builder.pop();

        builder.translation("the_bumblezone.configuration.knowingessencehighlightingconfigs").push("Knowing Essence Highlighting Configs (see block and entity tags for even more options)");

        knowingEssenceHighlightBosses = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight bosses in purple. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightbosses")
                .define("knowingEssenceHighlightBosses", true);


        knowingEssenceHighlightMonsters = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight monsters in red. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightmonsters")
                .define("knowingEssenceHighlightMonsters", true);

        knowingEssenceHighlightTamed = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight mobs you tamed in green. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.configuration.knowingessencehighlighttamed")
                .define("knowingEssenceHighlightTamed", true);

        knowingEssenceHighlightLivingEntities = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight any other mobs in orange. (Does not override the forced highlighting entity tag)\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightlivingentities")
                .define("knowingEssenceHighlightLivingEntities", true);

        knowingEssenceHighlightCommonItems = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight common items in world in white color.\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightcommonitems")
                .define("knowingEssenceHighlightCommonItems", true);

        knowingEssenceHighlightUncommonItems = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight uncommon items in world in yellow color.\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightuncommonitems")
                .define("knowingEssenceHighlightUncommonItems", true);

        knowingEssenceHighlightRareItems = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight rare items in world in cyan color.\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightrareitems")
                .define("knowingEssenceHighlightRareItems", true);

        knowingEssenceHighlightEpicItems = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will highlight epic items in world in purple color.\n")
                .translation("the_bumblezone.configuration.knowingessencehighlightepicitems")
                .define("knowingEssenceHighlightEpicItems", true);

        knowingEssenceStructureNameClient = builder
                .comment("----------------------------\n",
                        " Whether Knowing Essence will tell you the name of structures you are in. (Server config can disable this)\n")
                .translation("the_bumblezone.configuration.knowingessencestructurenameclient")
                .define("knowingEssenceStructureNameClient", true);

        knowingEssenceStructureNameXCoord = builder
                .comment("----------------------------\n",
                        " X coordinate for where to render the structure name on screen. (Based on bottom left corner)\n")
                .translation("the_bumblezone.configuration.knowingessencestructurenamexcoord")
                .defineInRange("knowingEssenceStructureNameXCoord", 4, 0, 10000);

        knowingEssenceStructureNameYCoord = builder
                .comment("----------------------------\n",
                        " Y coordinate for where to render the structure name on screen. (Based on bottom left corner)\n")
                .translation("the_bumblezone.configuration.knowingessencestructurenameycoord")
                .defineInRange("knowingEssenceStructureNameYCoord", 16, 0, 10000);

        builder.pop();

        builder.translation("the_bumblezone.configuration.radianceessenceconfigs").push("Radiance Essence configs");

        radianceEssenceArmorDurability = builder
                .comment("----------------------------\n",
                        " Whether Radiance Essence will show the durability of your armor.\n")
                .translation("the_bumblezone.configuration.radianceessencearmordurability")
                .define("radianceEssenceArmorDurability", true);

        radianceEssenceArmorDurabilityXCoord = builder
                .comment("----------------------------\n",
                        " X coordinate for where to render the armor durability on screen. (Based on bottom left corner)\n")
                .translation("the_bumblezone.configuration.radianceessencearmordurabilityxcoord")
                .defineInRange("radianceEssenceArmorDurabilityXCoord", 4, 0, 10000);

        radianceEssenceArmorDurabilityYCoord = builder
                .comment("----------------------------\n",
                        " Y coordinate for where to render the armor durability on screen. (Based on bottom left corner)\n")
                .translation("the_bumblezone.configuration.radianceessencearmordurabilityycoord")
                .defineInRange("radianceEssenceArmorDurabilityYCoord", 16, 0, 10000);

        builder.pop();

        builder.translation("the_bumblezone.configuration.entitymodelrendererconfigs").push("Entity Model/Renderer configs");

        useBackupModelForVariantBee = builder
                .gameRestart()
                .comment("----------------------------\n",
                        " Turn this on if Variant Bee's model is messed up by another mod or resourcepack. Requires restart.\n")
                .translation("the_bumblezone.configuration.usebackupmodelforvariantbee")
                .define("useBackupModelForVariantBee", false);

        renderBeeQueenBonusTradeItem = builder
                .comment("----------------------------\n",
                        " Whether to show the item on the Bee Queen that the Queen wants for Bonus Trades.\n")
                .translation("the_bumblezone.configuration.renderbeequeenbonustradeitem")
                .define("renderBeeQueenBonusTradeItem", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.musicconfigs").push("Music configs");

        playWrathOfHiveEffectMusic = builder
                .comment("----------------------------\n",
                        " If on, Flight of the Bumblebee by Rimsky Korsakov will play when you have Wrath of the Hive effect on.\n")
                .translation("the_bumblezone.configuration.playwrathofhiveeffectmusic")
                .define("playWrathOfHiveEffectMusic", true);

        playSempiternalSanctumMusic = builder
                .comment("----------------------------\n",
                        " If on, Bee-ware of the Temple by LudoCrypt will play when you are in Sempiternal Sanctum.\n")
                .translation("the_bumblezone.configuration.playsempiternalsanctummusic")
                .define("playSempiternalSanctumMusic", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.essenceblockconfigs").push("Essence Block configs");

        disableEssenceBlockShaders = builder
                .comment("----------------------------\n",
                        " Whether to not render the shader for Essence Blocks.\n",
                        " Will default to normal block textures instead if shader is disabled.")
                .translation("the_bumblezone.configuration.disableessenceblockshaders")
                .define("disableEssenceBlockShaders", false);

        builder.pop();
    }

    public static void copyToCommon() {
        BzDimensionConfigs.fogBrightnessPercentage = fogBrightnessPercentage.get();
        BzDimensionConfigs.fogThickness = fogThickness.get();
        BzDimensionConfigs.enableDimensionFog = enableDimensionFog.get();
        BzClientConfigs.useBackupModelForVariantBee = useBackupModelForVariantBee.get();
        BzClientConfigs.playWrathOfHiveEffectMusic = playWrathOfHiveEffectMusic.get();
        BzClientConfigs.playSempiternalSanctumMusic = playSempiternalSanctumMusic.get();
        BzClientConfigs.renderBeeQueenBonusTradeItem = renderBeeQueenBonusTradeItem.get();
        BzClientConfigs.disableEssenceBlockShaders = disableEssenceBlockShaders.get();
        BzClientConfigs.knowingEssenceHighlightBosses = knowingEssenceHighlightBosses.get();
        BzClientConfigs.knowingEssenceHighlightMonsters = knowingEssenceHighlightMonsters.get();
        BzClientConfigs.knowingEssenceHighlightTamed = knowingEssenceHighlightTamed.get();
        BzClientConfigs.knowingEssenceHighlightLivingEntities = knowingEssenceHighlightLivingEntities.get();
        BzClientConfigs.knowingEssenceHighlightCommonItems = knowingEssenceHighlightCommonItems.get();
        BzClientConfigs.knowingEssenceHighlightUncommonItems = knowingEssenceHighlightUncommonItems.get();
        BzClientConfigs.knowingEssenceHighlightRareItems = knowingEssenceHighlightRareItems.get();
        BzClientConfigs.knowingEssenceHighlightEpicItems = knowingEssenceHighlightEpicItems.get();
        BzClientConfigs.knowingEssenceStructureNameClient = knowingEssenceStructureNameClient.get();
        BzClientConfigs.knowingEssenceStructureNameXCoord = knowingEssenceStructureNameXCoord.get();
        BzClientConfigs.knowingEssenceStructureNameYCoord = knowingEssenceStructureNameYCoord.get();
        BzClientConfigs.radianceEssenceArmorDurability = radianceEssenceArmorDurability.get();
        BzClientConfigs.radianceEssenceArmorDurabilityXCoord = radianceEssenceArmorDurabilityXCoord.get();
        BzClientConfigs.radianceEssenceArmorDurabilityYCoord = radianceEssenceArmorDurabilityYCoord.get();
        BzClientConfigs.essenceItemHUDVisualEffectLayers = essenceItemHUDVisualEffectLayers.get();
        BzClientConfigs.essenceItemHUDVisualEffectSpeed = essenceItemHUDVisualEffectSpeed.get().floatValue();
    }
}