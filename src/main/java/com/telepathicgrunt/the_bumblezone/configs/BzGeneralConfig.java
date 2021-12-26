package com.telepathicgrunt.the_bumblezone.configs;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "General")
public class BzGeneralConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Base speed for the Beehemoth when being rode by a player.""")
    public double beehemothSpeed = 0.95;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Whether Bumblezone will handle spawning vanilla bees near players in the Bumblezone to make it feel full of Bees.
            Bees too far will be despawned in Bumblezone unless the bee has a hive, is name tagged, or is set to persistent.
            Note: Modded bees will not be spawned through this system. Those will be spawned by normal biome spawning.""")
    public boolean specialBeeSpawning = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            If specialBeeSpawning is set to true, this config controls how many vanilla bees should be
            near each player in Bumblezone dimension. Higher numbers like 100 is insane lol. 25 is nice.""")
    public int nearbyBeesPerPlayerInBz = 25;
}
