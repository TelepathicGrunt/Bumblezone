package com.telepathicgrunt.the_bumblezone.configs;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "Bee Aggression")
public class BzBeeAggressionConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            If set to true, any entity that harms a beehemoth and is not owner of it,
            that entity will get Wrath of the Hive effect.""")
    public boolean beehemothTriggersWrath = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Determines if Wrath of the Hive can be applied to players outside
            the Bumblezone dimension when they pick up Honey blocks, take honey
             from Filled Porous Honey blocks, or drink Honey Bottles.""")
    public boolean allowWrathOfTheHiveOutsideBumblezone = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Show the orangish particles when you get Wrath of the Hive
            after you angered the bees in the Bumblezone dimension.""")
    public boolean showWrathOfTheHiveParticles = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Turn off or on the ability to get Wrath of the Hive effect from certain actions.

            The bees can see you through walls and will have
            speed, absorption, and strength effects applied to them.

            Will also affect the bee's aggression toward other mobs in the dimension.
            Note: Peaceful mode will always override the bee aggressive setting.""")
    public boolean aggressiveBees = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How far away the bee can be to become angry and hunt you down if
            you take their honey from the Bumblezone dimension.

            Will also affect the bee's aggression range toward bears in the dimension.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    public int aggressionTriggerRadius = 64;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How long bees will keep their effects for (speed, absorption, strength).

            Note: This is in ticks. 20 ticks = 1 second. And bee's anger will remain.
            Only the boosts given to the bees will be gone.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int howLongWrathOfTheHiveLasts = 1680;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How long entities will keep Protection of the Hive effect after feeding bees
            or Brood Blocks. Bees will attack anyone that damages someone with the effect.

            Note: This is in ticks. 20 ticks = 1 second.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
    public int howLongProtectionOfTheHiveLasts = 1680;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How fast bees move along the ground (Not while flying).
            You will see this a lot when bees are about to attack
            you, they tend to touch the floor and the speed boost
            makes them dash forward at you. Set this to higher for
            faster dash attacks from bees.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int speedBoostLevel = 2;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How much extra health bees get that always instantly regenerates.
            This means you need to deal more damage than the extra health gives
            order to actually damage the bee's real health bar.

            For example, Absorption 1 here makes bees get 4 extra padding of health (2 heart icons).
            Your attacks need to deal 4 1/2 or more damage to actually be able to
            kill the bee. This means using Bane of Arthropod 5 is needed to kill bees
            if you set the absorption to a higher value like 2 or 3.
            If you set this to like 5 or something, bees may be invicible! Game over.
            """)
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int absorptionBoostLevel = 1;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How strong the bees attacks become.
            (6 or higher will instant kill you without armor).""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int strengthBoostLevel = 2;
}
