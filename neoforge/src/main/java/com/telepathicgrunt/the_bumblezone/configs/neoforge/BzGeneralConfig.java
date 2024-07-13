package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BzGeneralConfig {
    public static final ModConfigSpec GENERAL_SPEC;

    public static ModConfigSpec.BooleanValue bzHoneyFluidFromHoneyBottles;
    public static ModConfigSpec.ConfigValue<List<? extends String>> variantBeeTypes;
    public static ModConfigSpec.DoubleValue beehemothSpeed;
    public static ModConfigSpec.BooleanValue beehemothFriendlyFire;
    public static ModConfigSpec.BooleanValue specialBeeSpawning;
    public static ModConfigSpec.BooleanValue beeLootInjection;
    public static ModConfigSpec.BooleanValue moddedBeeLootInjection;
    public static ModConfigSpec.BooleanValue glisteringHoneyBrewingRecipe;
    public static ModConfigSpec.BooleanValue beeStingerBrewingRecipe;
    public static ModConfigSpec.BooleanValue beeSoupBrewingRecipe;
    public static ModConfigSpec.IntValue nearbyBeesPerPlayerInBz;
    public static ModConfigSpec.BooleanValue dispensersDropGlassBottles;
    public static ModConfigSpec.IntValue broodBlocksBeeSpawnCapacity;
    public static ModConfigSpec.BooleanValue pileOfPollenHyperFireSpread;
    public static ModConfigSpec.BooleanValue superCandlesBurnsMobs;
    public static ModConfigSpec.IntValue neurotoxinMaxLevel;
    public static ModConfigSpec.IntValue paralyzedMaxTickDuration;
    public static ModConfigSpec.IntValue beeQueenBonusTradeRewardMultiplier;
    public static ModConfigSpec.IntValue beeQueenBonusTradeDurationInTicks;
    public static ModConfigSpec.IntValue beeQueenBonusTradeAmountTillSatified;
    public static ModConfigSpec.BooleanValue beeQueenSpecialDayTrades;
    public static ModConfigSpec.BooleanValue beeQueenRespawning;
    public static ModConfigSpec.BooleanValue allowWanderingTraderMusicDiscsTrades;
    public static ModConfigSpec.BooleanValue crystallineFlowerConsumeItemEntities;
    public static ModConfigSpec.BooleanValue crystallineFlowerConsumeExperienceOrbEntities;
    public static ModConfigSpec.BooleanValue crystallineFlowerConsumeItemUI;
    public static ModConfigSpec.BooleanValue crystallineFlowerConsumeExperienceUI;
    public static ModConfigSpec.IntValue crystallineFlowerEnchantingPowerAllowedPerTier;
    public static ModConfigSpec.IntValue crystallineFlowerExtraXpNeededForTiers;
    public static ModConfigSpec.IntValue crystallineFlowerExtraTierCost;
    public static ModConfigSpec.BooleanValue repeatableEssenceEvents;
    public static ModConfigSpec.IntValue cosmicCrystalHealth;
    public static ModConfigSpec.IntValue ragingEssenceAbilityUse;
    public static ModConfigSpec.IntValue ragingEssenceCooldown;
    public static ModConfigSpec.ConfigValue<List<? extends Integer>> ragingEssenceStrengthLevels;
    public static ModConfigSpec.IntValue knowingEssenceAbilityUse;
    public static ModConfigSpec.IntValue knowingEssenceCooldown;
    public static ModConfigSpec.BooleanValue knowingEssenceStructureNameServer;
    public static ModConfigSpec.IntValue calmingEssenceAbilityUse;
    public static ModConfigSpec.IntValue calmingEssenceCooldown;
    public static ModConfigSpec.IntValue lifeEssenceAbilityUse;
    public static ModConfigSpec.IntValue lifeEssenceCooldown;
    public static ModConfigSpec.IntValue radianceEssenceAbilityUse;
    public static ModConfigSpec.IntValue radianceEssenceCooldown;
    public static ModConfigSpec.IntValue continuityEssenceCooldown;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
        builder.translation("the_bumblezone.configuration.bumblezonefluidoptions").push("Bumblezone Fluid Options");

        bzHoneyFluidFromHoneyBottles = builder
                .comment("----------------------------\n",
                        " Whether extracting fluid from Honey Bottles will attempt to generate Bumblezone Honey Fluid. (Think extraction machines)\n")
                .translation("the_bumblezone.configuration.bzhoneyfluidfromhoneybottles")
                .define("bzHoneyFluidFromHoneyBottles", true);

        builder.pop();


        builder.translation("the_bumblezone.configuration.variantbeeoptions").push("Variant Bee Options");

        variantBeeTypes = builder
                .comment("----------------------------\n",
                        " What skin variations of Variant Bees can spawn! Add more bee textures to this folder" +
                        " `assets/the_bumblezone/textures/entity/bee_variants` and define the bee in this config!\n")
                .translation("the_bumblezone.configuration.variantbeetypes")
                .defineList("variantBeeTypes", Arrays.asList(
                        "redtail_bee",
                        "green_bee",
                        "blue_bee",
                        "white_bee",
                        "ukraine_bee",
                        "trans_bee",
                        "asexual_bee",
                        "agender_bee",
                        "aroace_bee",
                        "aromantic_bee",
                        "bisexual_bee",
                        "pan_bee",
                        "enby_bee",
                        "reverse_bee",
                        "neapolitan_bee",
                        "rainbow_bee"), () -> "", entry -> true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.beehemothoptions").push("Beehemoth Options");

        beehemothSpeed = builder
                .comment("----------------------------\n",
                        " Base speed for the Beehemoth when being rode by a player.\n")
                .translation("the_bumblezone.configuration.beehemothspeed")
                .defineInRange("beehemothSpeed", 0.95D, 0D, 100D);

        beehemothFriendlyFire = builder
                .comment("----------------------------\n",
                        " Allows tamed Beehemoths to take damage from owner.\n")
                .translation("the_bumblezone.configuration.beehemothfriendlyfire")
                .define("beehemothFriendlyFire", true);

        builder.pop();


        builder.translation("the_bumblezone.configuration.specialbeespawningoptions").push("Special Bee Spawning Options");

        specialBeeSpawning = builder
                .comment("----------------------------\n",
                        " Whether Bumblezone will handle spawning vanilla bees near players in the Bumblezone to make it feel full of Bees.",
                        " Bees too far will be despawned in Bumblezone unless the bee has a hive, is name tagged, or is set to persistent.\n")
                .translation("the_bumblezone.configuration.specialbeespawning")
                .define("specialBeeSpawning", true);


        nearbyBeesPerPlayerInBz = builder
                .comment("----------------------------\n",
                        " If specialBeeSpawning is set to true, this config controls how many vanilla bees should be",
                        " near each player in Bumblezone dimension. Higher numbers like 100 is insane lol. 25 is nice.\n")
                .translation("the_bumblezone.configuration.nearbybeesperplayerinbz")
                .defineInRange("nearbyBeesPerPlayerInBz", 25, 0, 1000);

        builder.pop();


        builder.translation("the_bumblezone.configuration.beelootinjectionoption").push("Bee Loot Injection Options");

            beeLootInjection = builder
                    .comment("----------------------------\n",
                            " Whether Bee Stingers should drop from adult Bees that die while still having their stinger.",
                            " This pulls from this loot table for the drops: `the_bumblezone:entities/bee_stinger_drops`\n")
                    .translation("the_bumblezone.configuration.beelootinjection")
                    .define("beeLootInjection", true);

            moddedBeeLootInjection = builder
                    .comment("----------------------------\n",
                            " Whether Bee Stingers should drop from adult modded Bees that die while still having their stinger.",
                            " This pulls from this loot table for the drops: `the_bumblezone:entities/bee_stinger_drops`\n")
                    .translation("the_bumblezone.configuration.moddedbeelootinjection")
                    .define("moddedBeeLootInjection", true);

        builder.pop();


        builder.translation("the_bumblezone.configuration.brewingrecipeoptions").push("Brewing Recipe Options");

        glisteringHoneyBrewingRecipe = builder
                .comment("----------------------------\n",
                        " Whether Glistering Honey Crystals can be used to make Potions of Luck.")
                .translation("the_bumblezone.configuration.glisteringhoneybrewingrecipe")
                .define("glisteringHoneyBrewingRecipe", true);

        beeStingerBrewingRecipe = builder
                .comment("----------------------------\n",
                        " Whether Bee Stingers can be used to make Potions of Long Poison.")
                .translation("the_bumblezone.configuration.beestingerbrewingrecipe")
                .define("beeStingerBrewingRecipe", true);

        beeSoupBrewingRecipe = builder
                .comment("----------------------------\n",
                        " Whether Bee Soup can be used to make Potions of Neurotoxin.")
                .translation("the_bumblezone.configuration.beesoupbrewingrecipe")
                .define("beeSoupBrewingRecipe", true);

        builder.pop();


        builder.translation("the_bumblezone.configuration.enchantmentsoptions").push("Enchantments Options");

        neurotoxinMaxLevel = builder
                .comment("----------------------------\n",
                        " Maximum level that Neurotoxin enchantment's strength can be regardless of the enchantment's level.",
                        " Note, changing this won't change enchantment's own max level. Just that this config set to 2 will make level 3 enchant behave like level 2.",
                        " To change an enchantment's max level display, datapack replace data/the_bumblezone/enchantment/neurotoxin.json file.\n")
                .translation("the_bumblezone.configuration.neurotoxinmaxlevel")
                .defineInRange("neurotoxinMaxLevel", 2, 1, 255);


        paralyzedMaxTickDuration = builder
                .comment("----------------------------\n",
                        " Sets maximum tick duration the Paralyzed effect can be on an entity." +
                        " Actions that reapply Paralyzed can still reset the timer and keep entity stunned.\n")
                .translation("the_bumblezone.configuration.paralyzedmaxtickduration")
                .defineInRange("paralyzedMaxTickDuration", 600, 1, 1000000);

        builder.pop();

        builder.translation("the_bumblezone.configuration.beequeenoptions").push("Bee Queen Options");

        beeQueenBonusTradeRewardMultiplier = builder
                .comment("----------------------------\n",
                        " Bonus Trades will multiply the normal trade's quantity by this amount!",
                        " 0 or 1 set here disables Bonus Trades.\n")
                .translation("the_bumblezone.configuration.beequeenbonustraderewardmultiplier")
                .defineInRange("beeQueenBonusTradeRewardMultiplier", 3, 0, 256);

        beeQueenBonusTradeDurationInTicks = builder
                .comment("----------------------------\n",
                        " How long in ticks that bonus Trades will last for before the Bee Queen asks for a new item.",
                        " Setting this to 0 disables bonus Trades. Anything less than a minute (1200) will not broadcast request message to players.\n")
                .translation("the_bumblezone.configuration.beequeenbonustradedurationinticks")
                .defineInRange("beeQueenBonusTradeDurationInTicks", 24000, 0, 2000000);

        beeQueenBonusTradeAmountTillSatified = builder
                .comment("----------------------------\n",
                        " How many boosted trades are allowed until the Bonus Trade is depleted.",
                        " The queen will request a new item for Bonus Trades after some time to recharge.",
                        " Setting this to 0 disables bonus Trades.\n")
                .translation("the_bumblezone.configuration.beequeenbonustradeamounttillsatified")
                .defineInRange("beeQueenBonusTradeAmountTillSatified", 24, 0, 1000000);

        beeQueenSpecialDayTrades = builder
                .comment("----------------------------\n",
                        " Whether to allow special trades to activate during certain days of the year.\n")
                .translation("the_bumblezone.configuration.beequeenspecialdaytrades")
                .define("beeQueenSpecialDayTrades", true);

        beeQueenRespawning = builder
                .comment("----------------------------\n",
                        " Allow Bee Queen to respawn in Throne Pillar when located with fresh Throne Honey Compass.\n")
                .translation("the_bumblezone.configuration.beequeenrespawning")
                .define("beeQueenRespawning", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.crystallinefloweroptions").push("Crystalline Flower Options");

        crystallineFlowerConsumeItemEntities = builder
                .comment("----------------------------\n",
                        " Whether the Crystalline Flower block will eat any item entity that touches the block's collision box in the world.\n")
                .translation("the_bumblezone.configuration.crystallineflowerconsumeitementities")
                .define("crystallineFlowerConsumeItemEntities", true);

        crystallineFlowerConsumeExperienceOrbEntities = builder
                .comment("----------------------------\n",
                        " Whether the Crystalline Flower block will pull in and eat any experience orb that touches it in the world.\n")
                .translation("the_bumblezone.configuration.crystallineflowerconsumeexperienceorbentities")
                .define("crystallineFlowerConsumeExperienceOrbEntities", true);

        crystallineFlowerConsumeItemUI = builder
                .comment("----------------------------\n",
                        " Whether the Crystalline Flower's GUI allows players to feed it items directly.\n")
                .translation("the_bumblezone.configuration.crystallineflowerconsumeitemui")
                .define("crystallineFlowerConsumeItemUI", true);

        crystallineFlowerConsumeExperienceUI = builder
                .comment("----------------------------\n",
                        " Whether the Crystalline Flower's GUI allows players to feed it the player's experience.\n")
                .translation("the_bumblezone.configuration.crystallineflowerconsumeexperienceui")
                .define("crystallineFlowerConsumeExperienceUI", true);

        crystallineFlowerExtraXpNeededForTiers = builder
                .comment("----------------------------\n",
                        " How much extra experience is required to reach the next tier for the Crystalline Flower.",
                        " Remember, item consuming is also affected as items are converted to experience when the flower consumes it.\n")
                .translation("the_bumblezone.configuration.crystallineflowerextraxpneededfortiers")
                .defineInRange("crystallineFlowerExtraXpNeededForTiers", 0, -1000000, 1000000);


        crystallineFlowerExtraTierCost = builder
                .comment("----------------------------\n",
                        " Increases or decreases the tier cost of all enchantments available by whatever value you set.",
                        " The enchantment's final tier cost will be capped between 1 and 6.\n")
                .translation("the_bumblezone.configuration.crystallineflowerextratiercost")
                .defineInRange("crystallineFlowerExtraTierCost", 0, -5, 5);

        crystallineFlowerEnchantingPowerAllowedPerTier = builder
                .comment("----------------------------\n",
                        " Controls how much \"enchanting power\" is used per tier to determine what enchantment shows up.",
                        " Enchantments of higher levels or rarity requires more \"enchanting power\" before they show up in the UI.",
                        " Think of this like how Enchanting Tables only shows stronger or rarer enchantments when you have more bookshelves.",
                        " Except here, the flower's tier times this config value is used as the threshold to know what enchantment and level to show.\n")
                .translation("the_bumblezone.configuration.crystallineflowerenchantingpowerallowedpertier")
                .defineInRange("crystallineFlowerEnchantingPowerAllowedPerTier", 8, 0, 1000);

        builder.pop();

        builder.translation("the_bumblezone.configuration.generalmechanicsoptions").push("General Mechanics Options");

        dispensersDropGlassBottles = builder
                .comment("----------------------------\n",
                        " Should Dispensers always drop the Glass Bottle when using specific",
                        " bottle items on certain The Bumblezone blocks?",
                        " ",
                        " Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and",
                        " drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.\n")
                .translation("the_bumblezone.configuration.dispensersdropglassbottles")
                .define("dispensersDropGlassBottles", false);

        broodBlocksBeeSpawnCapacity = builder
                .comment("----------------------------\n",
                        " Brood Blocks will automatically spawn bees until the number of active bees is the value below.",
                        " Set this higher to allow Brood Blocks to spawn more bees in a smaller area or set it to 0 to turn",
                        " off automatic Brood Block bee spawning.\n")
                .translation("the_bumblezone.configuration.broodblocksbeespawncapacity")
                .defineInRange("broodBlocksBeeSpawnCapacity", 40, 0, 1000);

        pileOfPollenHyperFireSpread = builder
                .comment("----------------------------\n",
                        " Whether Pile of Pollen should spread fire super quickly to burn away huge swathes of pollen.\n")
                .translation("the_bumblezone.configuration.pileofpollenhyperfirespread")
                .define("pileOfPollenHyperFireSpread", false);

        superCandlesBurnsMobs = builder
                .comment("----------------------------\n",
                        " Allows lit Super Candles/Potion Candle to burn living entities in its flame.\n")
                .translation("the_bumblezone.configuration.supercandlesburnsmobs")
                .define("superCandlesBurnsMobs", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.musicdiscsoptions").push("Music Disc Options");

        allowWanderingTraderMusicDiscsTrades = builder
                .comment("----------------------------\n",
                        " Whether Wandering Traders can have a rare trade for Bumblezone Music Discs.\n")
                .translation("the_bumblezone.configuration.allowwanderingtradermusicdiscstrades")
                .define("allowWanderingTraderMusicDiscsTrades", true);

        builder.pop();

        builder.translation("the_bumblezone.configuration.essenceoptions").push("Essence Item And Events Options");

        repeatableEssenceEvents = builder
                .comment("----------------------------\n",
                        " Whether the Essence events in Sempiternal Sanctums can be repeated after beating it once.\n")
                .translation("the_bumblezone.configuration.repeatableessenceevents")
                .define("repeatableEssenceEvents", true);


        cosmicCrystalHealth = builder
                .comment("----------------------------\n",
                        " How much max health Cosmic Crystal entity has. (For Continuity Essence event in White Sempiternal Sanctum)\n")
                .translation("the_bumblezone.configuration.cosmiccrystalhealth")
                .defineInRange("cosmicCrystalHealth", 60, 1, 1000000);


        ragingEssenceAbilityUse = builder
                .comment("----------------------------\n",
                        " How much ability use this item has before depleted.\n")
                .translation("the_bumblezone.configuration.ragingessenceabilityuse")
                .defineInRange("ragingEssenceAbilityUse", 28, 0, 1000000);

        ragingEssenceCooldown = builder
                .comment("----------------------------\n",
                        " How long the cooldown is in ticks before recharged for this item.\n")
                .translation("the_bumblezone.configuration.ragingessencecooldown")
                .defineInRange("ragingEssenceCooldown", 36000, 0, 1000000);

        ragingEssenceStrengthLevels = builder
                .comment("----------------------------\n",
                        " How many rage levels there are and what level Strength effect each rage level gives.",
                        " The list is in order of the effect levels that the player will get as they make highlighted kills.",
                        " Adding or removing numbers will change the maximum number of rage tiers too.\n")
                .translation("the_bumblezone.configuration.ragingessencestrengthlevels")
                .defineList("ragingEssenceStrengthLevels", Arrays.asList(1, 2, 3, 5, 8, 14, 20), () -> 1, (entry) -> {
                    if (entry instanceof Integer integer) {
                        return integer > 0;
                    }
                    return false;
                });


        knowingEssenceAbilityUse = builder
                .comment("----------------------------\n",
                        " How much ability use (per second) this item has before depleted.\n")
                .translation("the_bumblezone.configuration.knowingessenceabilityuse")
                .defineInRange("knowingEssenceAbilityUse", 1200, 0, 1000000);

        knowingEssenceCooldown = builder
                .comment("----------------------------\n",
                        " How long the cooldown is in ticks before recharged for this item.\n")
                .translation("the_bumblezone.configuration.knowingessencecooldown")
                .defineInRange("knowingEssenceCooldown", 18000, 0, 1000000);

        knowingEssenceStructureNameServer = builder
                .comment("----------------------------\n",
                        " Whether clients should be allowed to see names of structures they are in with this item.\n")
                .translation("the_bumblezone.configuration.knowingEssenceStructureNameServer")
                .define("knowingEssenceStructureNameServer", true);


        calmingEssenceAbilityUse = builder
                .comment("----------------------------\n",
                        " How much ability use (per second) this item has before depleted.\n")
                .translation("the_bumblezone.configuration.calmingessenceabilityuse")
                .defineInRange("calmingEssenceAbilityUse", 600, 0, 1000000);

        calmingEssenceCooldown = builder
                .comment("----------------------------\n",
                        " How long the cooldown is in ticks before recharged for this item.\n")
                .translation("the_bumblezone.configuration.calmingessencecooldown")
                .defineInRange("calmingEssenceCooldown", 12000, 0, 1000000);


        lifeEssenceAbilityUse = builder
                .comment("----------------------------\n",
                        " How much ability use this item has before depleted.\n")
                .translation("the_bumblezone.configuration.lifeessenceabilityuse")
                .defineInRange("lifeEssenceAbilityUse", 1000, 0, 1000000);

        lifeEssenceCooldown = builder
                .comment("----------------------------\n",
                        " How long the cooldown is in ticks before recharged for this item.\n")
                .translation("the_bumblezone.configuration.lifeessencecooldown")
                .defineInRange("lifeEssenceCooldown", 12000, 0, 1000000);


        radianceEssenceAbilityUse = builder
                .comment("----------------------------\n",
                        " How much ability use (per 25 ticks) this item has before depleted.\n")
                .translation("the_bumblezone.configuration.radianceessenceabilityuse")
                .defineInRange("radianceEssenceAbilityUse", 4800, 0, 1000000);

        radianceEssenceCooldown = builder
                .comment("----------------------------\n",
                        " How long the cooldown is in ticks before recharged for this item.\n")
                .translation("the_bumblezone.configuration.radianceessencecooldown")
                .defineInRange("radianceEssenceCooldown", 12000, 0, 1000000);


        continuityEssenceCooldown = builder
                .comment("----------------------------\n",
                        " How long the cooldown is in ticks before recharged for this item.\n")
                .translation("the_bumblezone.configuration.continuityessencecooldown")
                .defineInRange("continuityEssenceCooldown", 48000, 0, 1000000);

        builder.pop();
    }

    public static void copyToCommon() {
        BzGeneralConfigs.variantBeeTypes = new ArrayList<>(variantBeeTypes.get());
        BzGeneralConfigs.beehemothSpeed = beehemothSpeed.get();
        BzGeneralConfigs.beehemothFriendlyFire = beehemothFriendlyFire.get();
        BzGeneralConfigs.specialBeeSpawning = specialBeeSpawning.get();
        BzGeneralConfigs.beeLootInjection = beeLootInjection.get();
        BzGeneralConfigs.moddedBeeLootInjection = moddedBeeLootInjection.get();
        BzGeneralConfigs.glisteringHoneyBrewingRecipe = glisteringHoneyBrewingRecipe.get();
        BzGeneralConfigs.beeStingerBrewingRecipe = beeStingerBrewingRecipe.get();
        BzGeneralConfigs.nearbyBeesPerPlayerInBz = nearbyBeesPerPlayerInBz.get();
        BzGeneralConfigs.dispensersDropGlassBottles = dispensersDropGlassBottles.get();
        BzGeneralConfigs.broodBlocksBeeSpawnCapacity = broodBlocksBeeSpawnCapacity.get();
        BzGeneralConfigs.pileOfPollenHyperFireSpread = pileOfPollenHyperFireSpread.get();
        BzGeneralConfigs.superCandlesBurnsMobs = superCandlesBurnsMobs.get();
        BzGeneralConfigs.neurotoxinMaxLevel = neurotoxinMaxLevel.get();
        BzGeneralConfigs.paralyzedMaxTickDuration = paralyzedMaxTickDuration.get();
        BzGeneralConfigs.beeQueenBonusTradeRewardMultiplier = beeQueenBonusTradeRewardMultiplier.get();
        BzGeneralConfigs.beeQueenBonusTradeDurationInTicks = beeQueenBonusTradeDurationInTicks.get();
        BzGeneralConfigs.beeQueenBonusTradeAmountTillSatified = beeQueenBonusTradeAmountTillSatified.get();
        BzGeneralConfigs.beeQueenSpecialDayTrades = beeQueenSpecialDayTrades.get();
        BzGeneralConfigs.beeQueenRespawning = beeQueenRespawning.get();
        BzGeneralConfigs.allowWanderingTraderMusicDiscsTrades = allowWanderingTraderMusicDiscsTrades.get();
        BzGeneralConfigs.crystallineFlowerConsumeItemEntities = crystallineFlowerConsumeItemEntities.get();
        BzGeneralConfigs.crystallineFlowerConsumeExperienceOrbEntities = crystallineFlowerConsumeExperienceOrbEntities.get();
        BzGeneralConfigs.crystallineFlowerConsumeItemUI = crystallineFlowerConsumeItemUI.get();
        BzGeneralConfigs.crystallineFlowerConsumeExperienceUI = crystallineFlowerConsumeExperienceUI.get();
        BzGeneralConfigs.crystallineFlowerEnchantingPowerAllowedPerTier = crystallineFlowerEnchantingPowerAllowedPerTier.get();
        BzGeneralConfigs.crystallineFlowerExtraXpNeededForTiers = crystallineFlowerExtraXpNeededForTiers.get();
        BzGeneralConfigs.crystallineFlowerExtraTierCost = crystallineFlowerExtraTierCost.get();
        BzGeneralConfigs.repeatableEssenceEvents = repeatableEssenceEvents.get();
        BzGeneralConfigs.cosmicCrystalHealth = cosmicCrystalHealth.get();
        BzGeneralConfigs.ragingEssenceAbilityUse = ragingEssenceAbilityUse.get();
        BzGeneralConfigs.ragingEssenceCooldown = ragingEssenceCooldown.get();
        BzGeneralConfigs.ragingEssenceStrengthLevels = ragingEssenceStrengthLevels.get().stream().mapToInt(i->i).toArray();
        BzGeneralConfigs.knowingEssenceAbilityUse = knowingEssenceAbilityUse.get();
        BzGeneralConfigs.knowingEssenceCooldown = knowingEssenceCooldown.get();
        BzGeneralConfigs.knowingEssenceStructureNameServer = knowingEssenceStructureNameServer.get();
        BzGeneralConfigs.calmingEssenceAbilityUse = calmingEssenceAbilityUse.get();
        BzGeneralConfigs.calmingEssenceCooldown = calmingEssenceCooldown.get();
        BzGeneralConfigs.lifeEssenceAbilityUse = lifeEssenceAbilityUse.get();
        BzGeneralConfigs.lifeEssenceCooldown = lifeEssenceCooldown.get();
        BzGeneralConfigs.radianceEssenceAbilityUse = radianceEssenceAbilityUse.get();
        BzGeneralConfigs.radianceEssenceCooldown = radianceEssenceCooldown.get();
        BzGeneralConfigs.continuityEssenceCooldown = continuityEssenceCooldown.get();
    }
}