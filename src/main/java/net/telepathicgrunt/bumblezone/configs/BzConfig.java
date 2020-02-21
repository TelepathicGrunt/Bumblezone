package net.telepathicgrunt.bumblezone.configs;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name="TheBumblezoneConfig")
public class BzConfig {

    //////////////////////////////////////////////////////////////////////////////
    //bee aggression

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "Determines if Wrath of the Hive can be applied to players outside\n" +
            "the Bumblezone dimension when they pick up Honey blocks, take honey\n" +
            " from Filled Porous Honey blocks, or drink Honey Bottles.")
    public boolean allowWrathOfTheHiveOutsideBumblezone = false;
    
    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "Show the orangish particles when you get Wrath of the Hive\n" +
            "after you angered the bees in the Bumblezone dimension.")
    public boolean showWrathOfTheHiveParticles = true;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "Turn off or on the ability to get Wrath of the Hive effect.\n" +
            "\n" +
            "The effect gets applied when you pick up Honey blocks, take\n" +
            "honey from Filled Porous Honeycomb Blocks, or drink a\n" +
            "Honey Bottle inside the Bumblezone dimension.\n" +
            "Basically, bees become REALLY angry.\n" +
            "\n" +
            "In addition, the bees can see you through walls and will have \n" +
            "speed, absorption, and strength effects applied to them.\n" +
            "\n" +
            "Will also affect the bee's aggression toward bears in the dimension.\n" +
            "Note: Peaceful mode will always override the bee aggressive setting.")
    public boolean aggressiveBees = true;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "How far away the bee can be to become angry and hunt you down if\n" +
            "you take their honey from the Bumblezone dimension.\n" +
            "\n" +
            "Will also affect the bee's aggression range toward bears in the dimension.")
    public int aggressionTriggerRadius = 64;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "How long bees will keep their effects for (speed, absorption, strength).\n" +
            "\n" +
            "Note: This is not in seconds at all. And bee's anger will remain.\n" +
            "Only the boosts given to the bees will be gone.")
    public int howLongWrathOfTheHiveLasts = 350;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "How fast bees move along the ground (Not while flying).\n" +
            "You will see this a lot when bees are about to attack\n" +
            "you, they tend to touch the floor and the speed boost\n" +
            "makes them dash forward at you. Set this to higher for\n" +
            "faster dash attacks from bees.")
    public int speedBoostLevel = 1;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "How much extra unrecoverable health boost the bees gets.")
    public int absorptionBoostLevel = 2;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "How strong the bees attacks become.\n" +
            "(5 or higher will instant kill you without armor).")
    public int strengthBoostLevel = 3;



    //////////////////////////////////////////////////////////////////////////////////////
    //dimension

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            " Determines how the coordinates gets translated when entering\n" +
            "and leaving the Bumblezone. The default ratio is 10 which means\n" +
            "for every block you traverse in the dimension, it is equal to\n" +
            "traveling 10 blocks in the Overworld. For comparison, the Nether\n" +
            "has a 8 to 1 ratio with the Overworld. \n" +
            "\n" +
            "The scaling of coordinates will take into account other dimension's\n" +
            "coordinate ratios so it'll work for any dimension correctly.\n" +
            "\n" +
            "Note: Changing this in an already made world will change where Bee Nests will\n" +
            "take you in the dimension and exiting will place you in a different spot too.")
    public int movementFactor = 10;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "Determines if the day/night cycle active in the Bumblezone dimension.\n" +
            "The cycle will be visible by the change in color of the fog. \n" +
            "If kept on, the day/night cycle will match the Overworld's \n" +
            "cycle even when players sleep to skip night in the Overworld.\n" +
            "\n" +
            "If this setting is set to false, the cycle \n" +
            "will be stuck at \"noon\" for the dimension.")
    public boolean dayNightCycle = true;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "How bright the fog is in the Bumblezone dimension. \n" +
            "This will always affect the fog whether you have the \n" +
            "day/night cycle on or off.\n" +
            "\n" +
            "The brightness is represented as a percentage so if the \n" +
            "cycle is off, 0 will be pitch black, 50 will be half as \n" +
            "bright, 100 will be normal orange brightness, and \n" +
            "100000 will be white. When the cycle is on, 0 will be \n" +
            "but will not be completely black during daytime.")
    public double fogBrightnessPercentage = 100;

    @Comment(value="##############################################################\n" +
            "##############################################################\n" +
            "\n" +
            "Makes leaving The Bumblezone dimension always places you back\n "
            +"at the Overworld regardless of which dimension you originally \n"
            +"came from. Use this option if this dimension becomes locked in  \n"
            +"with another dimension so you are stuck teleporting between the \n"
            +"two and cannot get back to the Overworld")
    public boolean forceExitToOverworld = false;
}
