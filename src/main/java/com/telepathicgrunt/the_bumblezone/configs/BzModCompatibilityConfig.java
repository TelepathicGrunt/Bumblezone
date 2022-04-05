package com.telepathicgrunt.the_bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "mod_compatibility")
public class BzModCompatibilityConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Adds Bumblezone items to Friends and Foes's Beekeeper trades!""")
    public boolean allowFriendsAndFoesBeekeeperTradesCompat = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = "\nAllow Bee Better compat which adds more blocks to Bumblezone's worldgen!")
    public boolean allowBeeBetterCompat = true;
}
