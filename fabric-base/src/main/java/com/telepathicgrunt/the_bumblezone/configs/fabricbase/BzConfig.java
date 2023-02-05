package com.telepathicgrunt.the_bumblezone.configs.fabricbase;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import eu.midnightdust.lib.config.MidnightConfig;
import org.jetbrains.annotations.ApiStatus;

public class BzConfig extends MidnightConfig {

    @Comment
    public static Comment beeAggressionComment;

    @Entry
    public static boolean beehemothTriggersWrath = false;

    @Entry
    public static boolean allowWrathOfTheHiveOutsideBumblezone = false;

    @Entry
    public static boolean showWrathOfTheHiveParticles = true;

    @Entry
    public static boolean aggressiveBees = true;

    @Entry(min=1, max=10000)
    public static int aggressionTriggerRadius = 64;

    @Entry(min=1, max=10000)
    public static int howLongWrathOfTheHiveLasts = 1680;

    @Entry(min=1, max=10000)
    public static int howLongProtectionOfTheHiveLasts = 1680;

    @Entry(min=1, max=100)
    public static int speedBoostLevel = 2;

    @Entry(min=1, max=100)
    public static int absorptionBoostLevel = 1;

    @Entry(min=1, max=100)
    public static int strengthBoostLevel = 2;


    @Comment
    public static Comment blockMechanicsComment;

    @Entry
    public static boolean dispensersDropGlassBottles = false;

    @Entry(min=1, max=1000)
    public static int broodBlocksBeeSpawnCapacity = 80;

    @Entry
    public static boolean superCandlesBurnsMobs = true;

    @Comment
    public static Comment crystallineFlowerComment;

    @Entry
    public static boolean crystallineFlowerConsumeItemEntities = true;

    @Entry
    public static boolean crystallineFlowerConsumeExperienceOrbEntities = true;

    @Entry
    public static boolean crystallineFlowerConsumeExperienceUI = true;

    @Entry
    public static boolean crystallineFlowerConsumeItemUI = true;

    @Entry
    public static int crystallineFlowerEnchantingPowerAllowedPerTier = 8;

    @Entry
    public static int crystallineFlowerExtraTierCost = 0;

    @Entry
    public static int crystallineFlowerExtraXpNeededForTiers = 0;


    @Comment
    public static Comment dimensionComment;

    @Entry(min=0, max=100000)
    public static double fogBrightnessPercentage = 110;

    @Entry(min=0, max=100)
    public static double fogThickness = 4;

    @Entry
    public static boolean enableDimensionFog = true;

    @Entry
    public static boolean onlyOverworldHivesTeleports = false;

    @Entry
    public static boolean forceExitToOverworld = false;

    @Entry
    public static boolean seaLevelOrHigherExitTeleporting = true;

    @Entry
    public static boolean warnPlayersOfWrongBlockUnderHive = true;

    @Entry
    public static boolean generateBeenest = true;

    @Entry(min=1, max=3)
    public static int teleportationMode = 3;

    @Entry
    public static boolean enableExitTeleportation = true;

    @Entry
    public static boolean enableEntranceTeleportation = true;

    @Entry
    public static boolean forceBumblezoneOriginMobToOverworldCenter = true;

    @Entry
    public static String defaultDimension = "minecraft:overworld";


    @Comment
    public static Comment dungeonsComment;

    @Entry(min=0, max=1000)
    public static int beeDungeonRarity = 1;

    @Entry(min=0, max=1000)
    public static int spiderInfestedBeeDungeonRarity = 8;

    @Entry(min=0, max=1)
    public static float spawnerRateSpiderBeeDungeon = 0.2f;


    @Comment
    public static Comment generalComment;

    @Entry(min=0, max=100)
    public static double beehemothSpeed = 0.95;

    @Entry(min=0, max=256)
    public static int beeQueenSuperTradeRewardMultiplier = 3;

    @Entry(min=0, max=2000000)
    public static int beeQueenSuperTradeDurationInTicks = 24000;

    @Entry(min=0, max=1000000)
    public static int beeQueenSuperTradeAmountTillSatified = 24;

    @Entry
    public static boolean specialBeeSpawning = true;

    @Entry
    public static int nearbyBeesPerPlayerInBz = 25;

    @Entry
    public static boolean keepEssenceOfTheBeesOnRespawning = true;

    @Entry
    public static int musicDiscTimeLengthFlightOfTheBumblebee = 84;

    @Entry
    public static int musicDiscTimeLengthHoneyBee = 216;

    @Entry
    public static int musicDiscTimeLengthLaBeeDaLoca = 176;

    @Entry
    public static int musicDiscTimeLengthBeeLaxingWithTheHomBees = 300;


    @Comment
    public static Comment modCompatComment;

    @Entry
    public static boolean allowFriendsAndFoesBeekeeperTradesCompat = true;


    @Comment
    public static Comment clientComment;

    @Entry(min=0, max=1)
    public static double lgbtBeeRate = 0.013;

    @Entry(min=0, max=1)
    public static double ukraineBeeRate = 0.004;

    @Entry
    public static boolean enableAltBeeSkinRenderer = true;

    @Entry
    public static boolean playWrathOfHiveEffectMusic = true;

    @ApiStatus.Internal
    public static void setup() {
        MidnightConfig.init(Bumblezone.MODID, BzConfig.class);
        copyConfigsToCommon();
    }

    /**
     * This is used to have a 'common' config in the common project but custom configs on both sides.
     */
    @ApiStatus.Internal
    public static void copyConfigsToCommon() {

        //Aggression
        BzBeeAggressionConfigs.aggressiveBees = aggressiveBees;
        BzBeeAggressionConfigs.aggressionTriggerRadius = aggressionTriggerRadius;
        BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts = howLongWrathOfTheHiveLasts;
        BzBeeAggressionConfigs.howLongProtectionOfTheHiveLasts = howLongProtectionOfTheHiveLasts;
        BzBeeAggressionConfigs.speedBoostLevel = speedBoostLevel;
        BzBeeAggressionConfigs.absorptionBoostLevel = absorptionBoostLevel;
        BzBeeAggressionConfigs.strengthBoostLevel = strengthBoostLevel;
        BzBeeAggressionConfigs.beehemothTriggersWrath = beehemothTriggersWrath;
        BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone = allowWrathOfTheHiveOutsideBumblezone;
        BzBeeAggressionConfigs.showWrathOfTheHiveParticles = showWrathOfTheHiveParticles;

        //Block Mechanics
        BzGeneralConfigs.dispensersDropGlassBottles = dispensersDropGlassBottles;
        BzGeneralConfigs.broodBlocksBeeSpawnCapacity = broodBlocksBeeSpawnCapacity;
        BzGeneralConfigs.superCandlesBurnsMobs = superCandlesBurnsMobs;

        //Crystalline Flower
        BzGeneralConfigs.crystallineFlowerConsumeItemEntities = crystallineFlowerConsumeItemEntities;
        BzGeneralConfigs.crystallineFlowerConsumeExperienceOrbEntities = crystallineFlowerConsumeExperienceOrbEntities;
        BzGeneralConfigs.crystallineFlowerConsumeExperienceUI = crystallineFlowerConsumeExperienceUI;
        BzGeneralConfigs.crystallineFlowerConsumeItemUI = crystallineFlowerConsumeItemUI;
        BzGeneralConfigs.crystallineFlowerEnchantingPowerAllowedPerTier = crystallineFlowerEnchantingPowerAllowedPerTier;
        BzGeneralConfigs.crystallineFlowerExtraTierCost = crystallineFlowerExtraTierCost;
        BzGeneralConfigs.crystallineFlowerExtraXpNeededForTiers = crystallineFlowerExtraXpNeededForTiers;

        //Dimension
        BzDimensionConfigs.fogBrightnessPercentage = fogBrightnessPercentage;
        BzDimensionConfigs.fogThickness = fogThickness;
        BzDimensionConfigs.enableDimensionFog = enableDimensionFog;
        BzDimensionConfigs.onlyOverworldHivesTeleports = onlyOverworldHivesTeleports;
        BzDimensionConfigs.forceExitToOverworld = forceExitToOverworld;
        BzDimensionConfigs.seaLevelOrHigherExitTeleporting = seaLevelOrHigherExitTeleporting;
        BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive = warnPlayersOfWrongBlockUnderHive;
        BzDimensionConfigs.generateBeenest = generateBeenest;
        BzDimensionConfigs.teleportationMode = teleportationMode;
        BzDimensionConfigs.enableExitTeleportation = enableExitTeleportation;
        BzDimensionConfigs.enableEntranceTeleportation = enableEntranceTeleportation;
        BzDimensionConfigs.forceBumblezoneOriginMobToOverworldCenter = forceBumblezoneOriginMobToOverworldCenter;
        BzDimensionConfigs.defaultDimension = defaultDimension;

        //Dungeon Config
        BzWorldgenConfigs.beeDungeonRarity = beeDungeonRarity;
        BzWorldgenConfigs.spiderInfestedBeeDungeonRarity = spiderInfestedBeeDungeonRarity;
        BzWorldgenConfigs.spawnerRateSpiderBeeDungeon = spawnerRateSpiderBeeDungeon;

        //General
        BzGeneralConfigs.beehemothSpeed = beehemothSpeed;
        BzGeneralConfigs.beeQueenSuperTradeRewardMultiplier = beeQueenSuperTradeRewardMultiplier;
        BzGeneralConfigs.beeQueenSuperTradeDurationInTicks = beeQueenSuperTradeDurationInTicks;
        BzGeneralConfigs.beeQueenSuperTradeAmountTillSatified = beeQueenSuperTradeAmountTillSatified;
        BzGeneralConfigs.specialBeeSpawning = specialBeeSpawning;
        BzGeneralConfigs.nearbyBeesPerPlayerInBz = nearbyBeesPerPlayerInBz;
        BzGeneralConfigs.keepEssenceOfTheBeesOnRespawning = keepEssenceOfTheBeesOnRespawning;
        BzGeneralConfigs.musicDiscTimeLengthFlightOfTheBumblebee = musicDiscTimeLengthFlightOfTheBumblebee;
        BzGeneralConfigs.musicDiscTimeLengthHoneyBee = musicDiscTimeLengthHoneyBee;
        BzGeneralConfigs.musicDiscTimeLengthLaBeeDaLoca = musicDiscTimeLengthLaBeeDaLoca;
        BzGeneralConfigs.musicDiscTimeLengthBeeLaxingWithTheHomBees = musicDiscTimeLengthBeeLaxingWithTheHomBees;

        //Compat
        BzModCompatibilityConfigs.allowFriendsAndFoesBeekeeperTradesCompat = allowFriendsAndFoesBeekeeperTradesCompat;

        //Client
        BzClientConfigs.enableAltBeeSkinRenderer = enableAltBeeSkinRenderer;
        BzClientConfigs.lgbtBeeRate = lgbtBeeRate;
        BzClientConfigs.ukraineBeeRate = ukraineBeeRate;
        BzClientConfigs.playWrathOfHiveEffectMusic = playWrathOfHiveEffectMusic;
    }

}