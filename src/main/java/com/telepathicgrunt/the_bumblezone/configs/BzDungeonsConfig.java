package com.telepathicgrunt.the_bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "Dungeons")
public class BzDungeonsConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How rare Bee Dungeons are. Higher numbers means more rare.
            Default rate is 1. Setting to greater than 1000 will disable Bee Dungeons.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    public int beeDungeonRarity = 1;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How rare Spider Infested Bee Dungeons are. Higher numbers means more rare.
            Default rate is 8. Setting to greater than 1000 will disable Spider Infested Bee Dungeons.""")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    public int spiderInfestedBeeDungeonRarity = 8;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            How rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.
            0 is no spawners, 1 is maximum spawners, and default is 0.2f""")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
    public float spawnerRateSpiderBeeDungeon = 0.2f;

}
