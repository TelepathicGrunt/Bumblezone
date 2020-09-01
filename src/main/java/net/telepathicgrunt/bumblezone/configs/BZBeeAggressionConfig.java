package net.telepathicgrunt.bumblezone.configs;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Bee Aggression")
public class BZBeeAggressionConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nDetermines if Wrath of the Hive can be applied to players outside\n" +
            "the Bumblezone dimension when they pick up Honey blocks, take honey\n" +
            " from Filled Porous Honey blocks, or drink Honey Bottles.")
    public boolean allowWrathOfTheHiveOutsideBumblezone = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nShow the orangish particles when you get Wrath of the Hive\n" +
            "after you angered the bees in the Bumblezone dimension.")
    public boolean showWrathOfTheHiveParticles = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nTurn off or on the ability to get Wrath of the Hive effect from certain actions.\n" +
            "\n" +
            "The bees can see you through walls and will have \n" +
            "speed, absorption, and strength effects applied to them.\n" +
            "\n" +
            "Will also affect the bee's aggression toward other mobs in the dimension.\n" +
            "Note: Peaceful mode will always override the bee aggressive setting.")
    public boolean aggressiveBees = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow far away the bee can be to become angry and hunt you down if\n" +
            "you take their honey from the Bumblezone dimension.\n" +
            "\n" +
            "Will also affect the bee's aggression range toward bears in the dimension.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    public int aggressionTriggerRadius = 64;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow long bees will keep their effects for (speed, absorption, strength).\n" +
            "\n" +
            "Note: This is in ticks. 20 ticks = 1 second. And bee's anger will remain.\n" +
            "Only the boosts given to the bees will be gone.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000000)
    public int howLongWrathOfTheHiveLasts = 350;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow long entities will keep Protection of the Hive effect after feeding bees\n" +
            "or Brood Blocks. Bees will attack anyone that damages someone with the effect.\n" +
            "\n" +
            "Note: This is in ticks. 20 ticks = 1 second.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000000)
    public int howLongProtectionOfTheHiveLasts = 500;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow fast bees move along the ground (Not while flying).\n" +
            "You will see this a lot when bees are about to attack\n" +
            "you, they tend to touch the floor and the speed boost\n" +
            "makes them dash forward at you. Set this to higher for\n" +
            "faster dash attacks from bees.")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1000000)
    public int speedBoostLevel = 1;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow much extra health bees get that always instantly regenerates.\n" +
            "This means you need to deal more damage than the extra health gives\n" +
            "order to actually damage the bee's real health bar.\n" +
            "\n" +
            "For example, Absorpton 1 here makes bees get 4 extra padding of hearts.\n" +
            "Your attacks need to deal 4 1/2 or more damage to actually be able to\n" +
            "kill the bee. This means using Bane of Arthropod 5 is needed to kill bees\n" +
            "if you set the absorption to a higher value like 2 or 3.\n" +
            "If you set this to like 5 or something, bees may be invicible! Game over.\n")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1000000)
    public int absorptionBoostLevel = 1;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow strong the bees attacks become.\n" +
            "(5 or higher will instant kill you without armor).")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1000000)
    public int strengthBoostLevel = 2;
}
