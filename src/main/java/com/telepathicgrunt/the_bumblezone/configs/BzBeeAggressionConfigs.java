package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import com.telepathicgrunt.the_bumblezone.utils.ConfigHelper;

@Mod.EventBusSubscriber
public class BzBeeAggressionConfigs {

    public static class BzBeeAggressionConfigValues {
        // bee aggression
        public ConfigHelper.ConfigValueListener<Boolean> allowWrathOfTheHiveOutsideBumblezone;
        public ConfigHelper.ConfigValueListener<Boolean> showWrathOfTheHiveParticles;
        public ConfigHelper.ConfigValueListener<Boolean> aggressiveBees;
        public ConfigHelper.ConfigValueListener<Integer> aggressionTriggerRadius;
        public ConfigHelper.ConfigValueListener<Integer> howLongWrathOfTheHiveLasts;
        public ConfigHelper.ConfigValueListener<Integer> howLongProtectionOfTheHiveLasts;
        public ConfigHelper.ConfigValueListener<Integer> speedBoostLevel;
        public ConfigHelper.ConfigValueListener<Integer> absorptionBoostLevel;
        public ConfigHelper.ConfigValueListener<Integer> strengthBoostLevel;

        public BzBeeAggressionConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {

            builder.push("Wrath of the Hive Options");

	        	builder.push("Bees Aggression Options");

                    allowWrathOfTheHiveOutsideBumblezone = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" Determines if Wrath of the Hive can be applied to players outside\r\n"
                        +" the Bumblezone dimension when they pick up Honey blocks, take honey\r\n"
                        +" from Filled Porous Honey blocks, or drink Honey Bottles.\r\n")
                        .translation("the_bumblezone.config.bees.allowwrathofthehiveoutsidebumblezone")
                        .define("allowWrathOfTheHiveOutsideBumblezone", false));


                    showWrathOfTheHiveParticles = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" Show the orangish particles when you get Wrath of the Hive\r\n"
                        +" after you angered the bees in the Bumblezone dimension.\r\n")
                        .translation("the_bumblezone.config.bees.showwrathofthehiveparticles")
                        .define("showWrathOfTheHiveParticles", true));


                    aggressiveBees = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" Turn off or on the ability to get Wrath of the Hive effect.\r\n"
                        +" \r\n"
                        +" The bees can see you through walls and will have \r\n"
                        +" speed, absorption, and strength effects applied to them.\r\n"
                        +" \r\n"
                        +" Will also affect the bee's aggression toward other mobs in the dimension.\r\n"
                        +" Note: Peaceful mode will always override the bee aggressive setting.\r\n")
                        .translation("the_bumblezone.config.bees.aggressivebees")
                        .define("aggressiveBees", true));


                    aggressionTriggerRadius = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" How far away the bee can be to become angry and hunt you down if\r\n "
                        +" you get Wrath of the Hive effect in the Bumblezone dimension.\r\n"
                        +" \r\n"
                        +" Will also affect the bee's aggression range toward other mobs in the dimension.\r\n")
                        .translation("the_bumblezone.config.bees.aggressiontriggerradius")
                        .defineInRange("aggressionTriggerRadius", 64, 1, 200));


                    howLongWrathOfTheHiveLasts = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" How long bees will keep their effects for (speed, absorption, strength).\r\n"
                        +" Note: This is in ticks. 20 ticks = 1 second. And bee's normal anger will remain.\r\n")
                        .translation("the_bumblezone.config.bees.howlongwrathofthehivelasts")
                        .defineInRange("howLongWrathOfTheHiveLasts", 350, 1, Integer.MAX_VALUE));


                    howLongWrathOfTheHiveLasts = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" How long entities will keep Protection of the Hive effect after feeding bees\r\n"
                        +" or Brood Blocks. Bees will attack anyone that damages someone with the effect.\r\n")
                        .translation("the_bumblezone.config.bees.howLongProtectionOfTheHiveLasts")
                        .defineInRange("howLongProtectionOfTheHiveLasts", 350, 1, Integer.MAX_VALUE));



            builder.pop();

                builder.push("Bees Effects Options");


                    speedBoostLevel = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" How fast bees move along the ground (Not while flying).\r\n"
                        +" You will see this a lot when bees are about to attack\r\n"
                        +" you, they tend to touch the floor and the speed boost\r\n"
                        +" makes them dash forward at you. Set this to higher for\r\n"
                        +" faster dash attacks from bees.\r\n")
                        .translation("the_bumblezone.config.bees.speedboostlevel")
                        .defineInRange("speedBoostLevel", 1, 0, Integer.MAX_VALUE));


                    absorptionBoostLevel = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" How much extra health bees get that always instantly regenerates.\r\n"
                        +" This means you need to deal more damage than the extra health gives\r\n"
                        +" order to actually damage the bee's real health bar.\r\n"
                        +" \r\n"
                        +" For example, Absorption 1 here makes bees get 4 extra padding of hearts.\r\n"
                        +" Your attacks need to deal 4 1/2 or more damage to actually be able to\r\n"
                        +" kill the bee. This means using Bane of Arthropod 5 is needed to kill bees\r\n"
                        +" if you set the absorption to a higher value like 2 or 3.\r\n"
                        +" If you set this to like 5 or something, bees may be invicible! Game over.\r\n")
                        .translation("the_bumblezone.config.bees.absorptionboostlevel")
                        .defineInRange("absorptionBoostLevel", 1, 0, Integer.MAX_VALUE));


                    strengthBoostLevel = subscriber.subscribe(builder
                        .comment(" \r\n-----------------------------------------------------\r\n\r\n"
                        +" How strong the bees attacks become. \r\n"
                        +" (5 or higher will instant kill you without armor).\r\n")
                        .translation("the_bumblezone.config.bees.strengthboostlevel")
                        .defineInRange("strengthBoostLevel", 3, 0, Integer.MAX_VALUE));

                builder.pop();
            builder.pop();
        }

    }
}
