package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class BzGeneralConfigs {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.DoubleValue beehemothSpeed;
    public static ForgeConfigSpec.BooleanValue specialBeeSpawning;
    public static ForgeConfigSpec.BooleanValue beeLootInjection;
    public static ForgeConfigSpec.BooleanValue moddedBeeLootInjection;
    public static ForgeConfigSpec.IntValue nearbyBeesPerPlayerInBz;
    public static ForgeConfigSpec.BooleanValue dispensersDropGlassBottles;
    public static ForgeConfigSpec.IntValue broodBlocksBeeSpawnCapacity;
    public static ForgeConfigSpec.IntValue beeQueenBonusTradeRewardMultiplier;
    public static ForgeConfigSpec.IntValue beeQueenBonusTradeDurationInTicks;
    public static ForgeConfigSpec.IntValue beeQueenBonusTradeAmountTillSatified;
    public static ForgeConfigSpec.BooleanValue superCandlesBurnsMobs;
    public static ForgeConfigSpec.BooleanValue keepEssenceOfTheBeesOnRespawning;
    public static ForgeConfigSpec.IntValue musicDiscTimeLengthFlightOfTheBumblebee;
    public static ForgeConfigSpec.IntValue musicDiscTimeLengthHoneyBee;
    public static ForgeConfigSpec.IntValue musicDiscTimeLengthBeeLaxingWithTheHomBees;
    public static ForgeConfigSpec.IntValue musicDiscTimeLengthLaBeeDaLoca;
    public static ForgeConfigSpec.BooleanValue crystallineFlowerConsumeItemEntities;
    public static ForgeConfigSpec.BooleanValue crystallineFlowerConsumeExperienceOrbEntities;
    public static ForgeConfigSpec.BooleanValue crystallineFlowerConsumeItemUI;
    public static ForgeConfigSpec.BooleanValue crystallineFlowerConsumeExperienceUI;
    public static ForgeConfigSpec.IntValue crystallineFlowerEnchantingPowerAllowedPerTier;
    public static ForgeConfigSpec.IntValue crystallineFlowerExtraXpNeededForTiers;
    public static ForgeConfigSpec.IntValue crystallineFlowerExtraTierCost;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Beehemoth Options");

        beehemothSpeed = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Base speed for the Beehemoth when being rode by a player.\n")
                .translation("the_bumblezone.config.beehemothSpeed")
                .defineInRange("beehemothSpeed", 0.95D, 0D, 100D);

        builder.pop();


        builder.push("Special Bee Spawning Options");

        specialBeeSpawning = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Bumblezone will handle spawning vanilla bees near players in the Bumblezone to make it feel full of Bees.",
                        " Bees too far will be despawned in Bumblezone unless the bee has a hive, is name tagged, or is set to persistent.",
                        " Note: Modded bees will not be spawned through this system. Those will be spawned by normal biome spawning.\n")
                .translation("the_bumblezone.config.specialbeespawning")
                .define("specialBeeSpawning", true);


        nearbyBeesPerPlayerInBz = builder
                .comment(" \n-----------------------------------------------------\n",
                        " If specialBeeSpawning is set to true, this config controls how many vanilla bees should be",
                        " near each player in Bumblezone dimension. Higher numbers like 100 is insane lol. 25 is nice.\n")
                .translation("the_bumblezone.config.nearbybeesperplayerinbz")
                .defineInRange("nearbyBeesPerPlayerInBz", 25, 0, 1000);

        builder.pop();


        builder.push("Bee Loot Injection Options");

            beeLootInjection = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Bee Stingers should drop from adult Bees that die while still having their stinger.",
                        " This pulls from this loot table for the drops: `the_bumblezone:entities/bee_stinger_drops`\n")
                .translation("the_bumblezone.config.beelootinjection")
                .define("beeLootInjection", true);

            moddedBeeLootInjection = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Bee Stingers should drop from adult modded Bees that die while still having their stinger.",
                        " This pulls from this loot table for the drops: `the_bumblezone:entities/bee_stinger_drops`\n")
                .translation("the_bumblezone.config.moddedbeelootinjection")
                .define("moddedBeeLootInjection", true);

        builder.pop();


        builder.push("General Mechanics Options");

        dispensersDropGlassBottles = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Should Dispensers always drop the Glass Bottle when using specific ",
                        " bottle items on certain The Bumblezone blocks? ",
                        " ",
                        " Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and",
                        " drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.\n")
                .translation("the_bumblezone.config.dispensersdropglassbottles")
                .define("dispensersDropGlassBottles", false);

        broodBlocksBeeSpawnCapacity = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Brood Blocks will automatically spawn bees until the number of active bees is the value below. ",
                        " Set this higher to allow Brood Blocks to spawn more bees in a smaller area or set it to 0 to turn ",
                        " off automatic Brood Block bee spawning.\n")
                .translation("the_bumblezone.config.broodblocksbeespawncapacity")
                .defineInRange("broodBlocksBeeSpawnCapacity", 60, 0, 1000);

        beeQueenBonusTradeRewardMultiplier = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Multiplies the trade reward by this much for bonus Trades! 0 or 1 set here disables bonus Trades.\n")
                .translation("the_bumblezone.config.beequeenbonusTraderewardmultiplier")
                .defineInRange("beeQueenBonusTradeRewardMultiplier", 3, 0, 256);

        beeQueenBonusTradeDurationInTicks = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in ticks that bonus Trades will last for before the Bee Queen asks for a new item.",
                        " Setting this to 0 disables bonus Trades. Anything less than a minute (1200) will not broadcast request message to players\n")
                .translation("the_bumblezone.config.beequeenbonusTradedurationinticks")
                .defineInRange("beeQueenBonusTradeDurationInTicks", 24000, 0, 2000000);

        beeQueenBonusTradeAmountTillSatified = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How many boosted trades are allowed until the bonus Trade is depleted until the queen requests a new item",
                        " Setting this to 0 disables bonus Trades.\n")
                .translation("the_bumblezone.config.beequeenbonusTradeamounttillsatified")
                .defineInRange("beeQueenBonusTradeAmountTillSatified", 24, 0, 1000000);

        superCandlesBurnsMobs = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Allows lit Super Candles/Incense Candle to burn mobs in its flame.\n")
                .translation("the_bumblezone.config.supercandlesburnsmobs")
                .define("superCandlesBurnsMobs", true);

        keepEssenceOfTheBeesOnRespawning = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether Essence of the Bees's effect should stay on the player if they die and then respawn.\n")
                .translation("the_bumblezone.config.keepessenceofthebeesonrespawning")
                .define("keepEssenceOfTheBeesOnRespawning", true);


        musicDiscTimeLengthFlightOfTheBumblebee = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in seconds this music disc will be playing music.",
                        " This is used for the server to know when to make Allays stop dancing when Jukebox plays this music disc.\n")
                .translation("the_bumblezone.config.broodblocksbeespawncapacity")
                .defineInRange("broodBlocksBeeSpawnCapacity", 84, 0 , 1000000);

        musicDiscTimeLengthHoneyBee = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in seconds this music disc will be playing music.",
                        " This is used for the server to know when to make Allays stop dancing when Jukebox plays this music disc.\n")
                .translation("the_bumblezone.config.musicdisctimelengthhoneybee")
                .defineInRange("musicDiscTimeLengthHoneyBee", 216, 0 , 1000000);

        musicDiscTimeLengthBeeLaxingWithTheHomBees = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in seconds this music disc will be playing music.",
                        " This is used for the server to know when to make Allays stop dancing when Jukebox plays this music disc.\n")
                .translation("the_bumblezone.config.musicdisctimelengthbeelaxingwiththehombees")
                .defineInRange("musicDiscTimeLengthBeeLaxingWithTheHomBees", 300, 0 , 1000000);

        musicDiscTimeLengthLaBeeDaLoca = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How long in seconds this music disc will be playing music.",
                        " This is used for the server to know when to make Allays stop dancing when Jukebox plays this music disc.\n")
                .translation("the_bumblezone.config.musicdisctimelengthlabeedaloca")
                .defineInRange("musicDiscTimeLengthLaBeeDaLoca", 176, 0 , 1000000);

        crystallineFlowerConsumeItemEntities = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether the Crystalline Flower block will eat any item entity that touches the block's collision box in the world\n")
                .translation("the_bumblezone.config.crystallineflowerconsumeitementities")
                .define("crystallineFlowerConsumeItemEntities", true);

        crystallineFlowerConsumeExperienceOrbEntities = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether the Crystalline Flower block will pull in and eat any experience orb that touches it in the world\n")
                .translation("the_bumblezone.config.crystallineflowerconsumeexperienceorbentities")
                .define("crystallineFlowerConsumeExperienceOrbEntities", true);

        crystallineFlowerConsumeItemUI = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether the Crystalline Flower's GUI allows players to feed it items directly\n")
                .translation("the_bumblezone.config.crystallineflowerconsumeitemui")
                .define("crystallineFlowerConsumeItemUI", true);

        crystallineFlowerConsumeExperienceUI = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Whether the Crystalline Flower's GUI allows players to feed it the player's experience\n")
                .translation("the_bumblezone.config.crystallineflowerconsumeexperienceui")
                .define("crystallineFlowerConsumeExperienceUI", true);


        crystallineFlowerEnchantingPowerAllowedPerTier = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Controls how much \"enchanting power\" is used per tier to determine what enchantment shows up.",
                        " Enchantments of higher levels or rarity requires more \"enchanting power\" before they show up in the UI.",
                        " Think of this like how Enchanting Tables only shows stronger or rarer enchantments when you have more bookshelves.",
                        " Except here, the flower's tier times this config value is used as the threshold to know what enchantment and level to show.\n")
                .translation("the_bumblezone.config.crystallineflowerenchantingpowerallowedpertier")
                .defineInRange("crystallineFlowerEnchantingPowerAllowedPerTier", 8, 0, 1000);


        crystallineFlowerExtraXpNeededForTiers = builder
                .comment(" \n-----------------------------------------------------\n",
                        " How much extra experience is required to reach the next tier for the Crystalline Flower.",
                        " Remember, item consuming is also affected as items are converted to experience when the flower consumes it.\n")
                .translation("the_bumblezone.config.crystallineflowerextraxpneededfortiers")
                .defineInRange("crystallineFlowerExtraXpNeededForTiers", 0, -1000000, 1000000);


        crystallineFlowerExtraTierCost = builder
                .comment(" \n-----------------------------------------------------\n",
                        " Increases or decreases the tier cost of all enchantments available by whatever value you set.",
                        " The enchantment's tier cost will be capped between 1 and 6.\n")
                .translation("the_bumblezone.config.crystallineflowerextratiercost")
                .defineInRange("crystallineFlowerExtraTierCost", 0, -5, 5);

        builder.pop();
    }
}
