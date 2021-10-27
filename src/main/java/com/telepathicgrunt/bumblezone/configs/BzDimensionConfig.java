package com.telepathicgrunt.bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "Dimension")
public class BZDimensionConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How bright the fog is in the Bumblezone dimension.\s

            The brightness is represented as a percentage so
            0 will be pitch black, 50 will be half as\s
            bright, 100 will be normal orange brightness, and\s
            100000 will be white.""")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100000)
    public double fogBrightnessPercentage = 110;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Makes throwing Enderpearls at Bee Nests or Hives only
             work in the Overworld. What this means setting this to true makes it\s
            only possible to enter The Bumblezone dimension from the Overworld""")
    public boolean onlyOverworldHivesTeleports = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Makes leaving The Bumblezone dimension always places you back
             at the Overworld regardless of which dimension you originally\s
            came from. Use this option if this dimension becomes locked in\s
            with another dimension so you are stuck teleporting between the\s
            two and cannot get back to the Overworld""")
    public boolean forceExitToOverworld = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Should exiting The Bumblezone always try and place you
            above sealevel in the target dimension? (Will only look
            for beehives above sealevel as well when placing you)
            ONLY FOR TELEPORTATION MODE 1 AND 3.""")
    public boolean seaLevelOrHigherExitTeleporting = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            If requiredBlockUnderHive has a block specified and this config
             is set to true, then player will get a warning if they throw
            an Enderpearl at a Bee Nest/Beehive but the block under it is
            not the correct required block. It will also tell the player what
            block is needed under the Bee Nest/Beehive to teleport to the dimension.""")
    public boolean warnPlayersOfWrongBlockUnderHive = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Will a Beenest generate if no Beenest is\s
            found when leaving The Bumblezone dimension.

            ONLY FOR TELEPORTATION MODE 1.""")
    public boolean generateBeenest = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Which mode of teleportation should be used when
            leaving The Bumblezone dimension.

            Mode 1: Coordinates will be converted to the other
            dimension's coordinate scale and the game will look for
            a Beenest/Beehive at the new spot to spawn players at.
            If none is found, players will still be placed at the spot.

            Mode 2: Will always spawn players at the original spot
            in the non-BZ dimension where they threw the Enderpearl
            at a Beenest/Beehive.
            Mode 3: Will use mode 1's teleportation method if Beehive/Beenest
            is near the spot when exiting the dimension. If none is found, then
            mode 2's teleportation method is used instead.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 3)
    public int teleportationMode = 1;

}
