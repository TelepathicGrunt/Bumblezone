package net.telepathicgrunt.bumblezone.configs;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Dungeons")
public class BZDungeonsConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow rare Bee Dungeons are. Higher numbers means more rare.\n" +
            "Default rate is 1. Setting to greater than 1000 will disable Bee Dungeons.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    public int beeDungeonRarity = 1;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow rare Spider Infested Bee Dungeons are. Higher numbers means more rare.\n" +
            "Default rate is 8. Setting to greater than 1000 will disable Spider Infested Bee Dungeons.")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    public int spiderInfestedBeeDungeonRarity = 8;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip()
    @Comment(value = "\nHow rare are Spider/Cave Spider Spawners in Spider Infested Bee Dungeons.\n" +
            "0 is no spawners, 1 is maximum spawners, and default is 0.2f")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
    public float spawnerRateSpiderBeeDungeon = 0.2f;

}
