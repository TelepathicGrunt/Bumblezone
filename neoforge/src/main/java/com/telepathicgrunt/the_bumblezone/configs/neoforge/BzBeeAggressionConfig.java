package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BzBeeAggressionConfig {
    public static final ModConfigSpec GENERAL_SPEC;

    // bee aggression
    public static ModConfigSpec.BooleanValue beehemothTriggersWrath;
    public static ModConfigSpec.BooleanValue allowWrathOfTheHiveOutsideBumblezone;
    public static ModConfigSpec.BooleanValue showWrathOfTheHiveParticles;
    public static ModConfigSpec.BooleanValue allowWrath;
    public static ModConfigSpec.IntValue aggressionTriggerRadius;
    public static ModConfigSpec.IntValue howLongWrathOfTheHiveLasts;
    public static ModConfigSpec.IntValue howLongProtectionOfTheHiveLasts;
    public static ModConfigSpec.IntValue speedBoostLevel;
    public static ModConfigSpec.IntValue absorptionBoostLevel;
    public static ModConfigSpec.IntValue strengthBoostLevel;

    static {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ModConfigSpec.Builder builder) {
        builder.translation("the_bumblezone.configuration.wrathofthehiveoptions").push("Wrath of the Hive Options");

            builder.translation("the_bumblezone.configuration.beesaggressionoptions").push("Bees Aggression Options");

                allowWrath = builder
                        .comment("----------------------------\n",
                                " Turn off or on the ability to get Wrath of the Hive effect.",
                                " ",
                                " The bees can see you through walls and will have ",
                                " speed, absorption, and strength effects applied to them.",
                                " ",
                                " Will also affect the bee's aggression toward other mobs in the dimension.",
                                " Note: Peaceful mode will always override the bee aggressive setting.\n")
                        .translation("the_bumblezone.configuration.allowwrath")
                        .define("allowWrath", true);

                aggressionTriggerRadius = builder
                        .comment("----------------------------\n",
                                " How far away the bee can be to become angry and hunt you down if",
                                " you get Wrath of the Hive effect in the Bumblezone dimension.",
                                " ",
                                " Will also affect the bee's aggression range toward other mobs in the dimension.\n")
                        .translation("the_bumblezone.configuration.aggressiontriggerradius")
                        .defineInRange("aggressionTriggerRadius", 64, 1, 200);

                howLongWrathOfTheHiveLasts = builder
                        .comment("----------------------------\n",
                                " How long Wrath of the Hive lasts which is basically how long",
                                " angry bees will keep their effects for (speed, absorption, strength).",
                                " Note: This is in ticks. 20 ticks = 1 second. And bee's normal anger will remain.\n")
                        .translation("the_bumblezone.configuration.howlongwrathofthehivelasts")
                        .defineInRange("howLongWrathOfTheHiveLasts", 1680, 1, Integer.MAX_VALUE);

                allowWrathOfTheHiveOutsideBumblezone = builder
                    .comment("----------------------------\n",
                   " Determines if Wrath of the Hive can be applied to players outside",
                   " the Bumblezone dimension when they pick up Honey blocks, take honey",
                   " from Filled Porous Honey blocks, or drink Honey Bottles.\n")
                    .translation("the_bumblezone.configuration.allowwrathofthehiveoutsidebumblezone")
                    .define("allowWrathOfTheHiveOutsideBumblezone", false);


                beehemothTriggersWrath = builder
                        .comment("----------------------------\n",
                                " If set to true, any entity that harms a beehemoth and is not owner of it,",
                                " that entity will get Wrath of the Hive effect.")
                        .translation("the_bumblezone.configuration.beehemothtriggerswrath")
                        .define("beehemothTriggersWrath", false);

                showWrathOfTheHiveParticles = builder
                    .comment("----------------------------\n",
                   " Show the orange-ish particles when you get Wrath of the Hive",
                   " after you angered the bees in the Bumblezone dimension.\n")
                    .translation("the_bumblezone.configuration.showwrathofthehiveparticles")
                    .define("showWrathOfTheHiveParticles", true);


        builder.pop();

            builder.translation("the_bumblezone.configuration.beebuffsfromwrath").push("Bee Buffs From Wrath");


                speedBoostLevel = builder
                    .comment("----------------------------\n",
                   " How fast bees move along the ground (Not while flying).",
                   " You will see this a lot when bees are about to attack",
                   " you, they tend to touch the floor and the speed boost",
                   " makes them dash forward at you. Set this to higher for",
                   " faster dash attacks from bees.\n")
                    .translation("the_bumblezone.configuration.speedboostlevel")
                    .defineInRange("speedBoostLevel", 2, 1, Integer.MAX_VALUE);


                absorptionBoostLevel = builder
                    .comment("----------------------------\n",
                   " How much extra health bees get that always instantly regenerates.",
                   " This means you need to deal more damage than the extra health gives",
                   " order to actually damage the bee's real health bar.",
                   " ",
                   " For example, Absorption 1 here makes bees get 4 extra padding of health (2 full hearts).",
                   " Your attacks need to deal 4 1/2 or more damage to actually be able to",
                   " kill the bee. This means using Bane of Arthropod 5 is needed to kill bees",
                   " if you set the absorption to a higher value like 2 or 3.",
                   " If you set this to like 5 or something, bees may be invincible!\n")
                    .translation("the_bumblezone.configuration.absorptionboostlevel")
                    .defineInRange("absorptionBoostLevel", 1, 1, Integer.MAX_VALUE);


                strengthBoostLevel = builder
                    .comment("----------------------------\n",
                   " How strong the bees attacks become. ",
                   " (5 or higher will instant kill you without armor).\n")
                    .translation("the_bumblezone.configuration.strengthboostlevel")
                    .defineInRange("strengthBoostLevel", 1, 1, Integer.MAX_VALUE);

            builder.pop();
        builder.pop();

        builder.translation("the_bumblezone.configuration.protectionofthehiveoptions").push("Protection of the Hive Options");

            howLongProtectionOfTheHiveLasts = builder
                    .comment("----------------------------\n",
                            " How long entities will keep Protection of the Hive effect after feeding bees",
                            " or Brood Blocks. Bees will attack anyone that damages someone with the effect.\n")
                    .translation("the_bumblezone.configuration.howlongprotectionofthehivelasts")
                    .defineInRange("howLongProtectionOfTheHiveLasts", 1680, 1, Integer.MAX_VALUE);

        builder.pop();
    }

    public static void copyToCommon() {
        BzBeeAggressionConfigs.beehemothTriggersWrath = beehemothTriggersWrath.get();
        BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone = allowWrathOfTheHiveOutsideBumblezone.get();
        BzBeeAggressionConfigs.showWrathOfTheHiveParticles = showWrathOfTheHiveParticles.get();
        BzBeeAggressionConfigs.aggressiveBees = allowWrath.get();
        BzBeeAggressionConfigs.aggressionTriggerRadius = aggressionTriggerRadius.get();
        BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts = howLongWrathOfTheHiveLasts.get();
        BzBeeAggressionConfigs.howLongProtectionOfTheHiveLasts = howLongProtectionOfTheHiveLasts.get();
        BzBeeAggressionConfigs.speedBoostLevel = speedBoostLevel.get();
        BzBeeAggressionConfigs.absorptionBoostLevel = absorptionBoostLevel.get();
        BzBeeAggressionConfigs.strengthBoostLevel = strengthBoostLevel.get();

    }
}