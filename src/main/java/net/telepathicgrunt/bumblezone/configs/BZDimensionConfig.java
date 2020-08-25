package net.telepathicgrunt.bumblezone.configs;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Dimension")
public class BZDimensionConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip(count = 6)
    @Comment(value = "\nHow bright the fog is in the Bumblezone dimension. \n" +
            "\n" +
            "The brightness is represented as a percentage so\n" +
            "0 will be pitch black, 50 will be half as \n" +
            "bright, 100 will be normal orange brightness, and \n" +
            "100000 will be white.")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100000)
    public double fogBrightnessPercentage = 110;

    @ConfigEntry.Gui.Tooltip(count = 5)
    @Comment(value = "\nMakes leaving The Bumblezone dimension always places you back\n " +
            "at the Overworld regardless of which dimension you originally \n" +
            "came from. Use this option if this dimension becomes locked in \n" +
            "with another dimension so you are stuck teleporting between the \n" +
            "two and cannot get back to the Overworld")
    public boolean forceExitToOverworld = false;

    @ConfigEntry.Gui.Tooltip(count = 5)
    @Comment(value = "\nShould exiting The Bumblezone always try and place you\n"
            +"above sealevel in the target dimension? (Will only look\n"
            +"for beehives above sealevel as well when placing you)"
            +"\n"
            +"ONLY FOR TELEPORTATION MODE 1 AND 3.")
    public boolean seaLevelOrHigherExitTeleporting = true;

    @ConfigEntry.Gui.Tooltip(count = 6)
    @Comment(value = "\nIf an identifier of a block is specified here,\n "
            +"then teleporting to Bumblezone will need that block under\n"
            +"the Bee Nest/Beehive you threw the Enderpearl at.\n"
            +"\n"
            +"By default, no identifier is specified so any block can\n"
            +"be under the Bee Nest/Beehive to teleport to the dimension.")
    public String requiredBlockUnderHive = "";

    @ConfigEntry.Gui.Tooltip(count = 5)
    @Comment(value = "\nIf requiredBlockUnderHive has a block specified and this config\n "
            +"is set to true, then player will get a warning if they throw\n"
            +"an Enderpearl at a Bee Nest/Beehive but the block under it is\n"
            +"not the correct required block. It will also tell the player what\n"
            +"block is needed under the Bee Nest/Beehive to teleport to the dimension.")
    public boolean warnPlayersOfWrongBlockUnderHive = true;

    @ConfigEntry.Gui.Tooltip(count = 4)
    @Comment(value = "\nWill a Beenest generate if no Beenest is \n"
            +"found when leaving The Bumblezone dimension.\n"
            +"\n"
            +"ONLY FOR TELEPORTATION MODE 1.")
    public boolean generateBeenest = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nWhich mode of teleportation should be used when\n"
            +"leaving The Bumblezone dimension.\n"
            +"\n"
            +"Mode 1: Coordinates will be converted to the other\n"
            +"dimension's coordinate scale and the game will look for\n"
            +"a Beenest/Beehive at the new spot to spawn players at.\n"
            +"If none is found, players will still be placed at the spot.\n"
            +"\n"
            +"Mode 2: Will always spawn players at the original spot\n"
            +"in the non-BZ dimension where they threw the Enderpearl\n"
            +"at a Beenest/Beehive."
            +"\n"
            +"Mode 3: Will use mode 1's teleportation method if Beehive/Beenest\n"
            +"is near the spot when exiting the dimension. If none is found, then\n"
            +"mode 2's teleportation method is used instead.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 3)
    public int teleportationMode = 1;

}
