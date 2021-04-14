package net.telepathicgrunt.bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "mod_compatibility")
public class BZModCompatibilityConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = "\nAllow Bee Better compat which adds more blocks to Bumblezone's worldgen!")
    public boolean allowBeeBetterCompat = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = "\nAllow Potion of Bees item to turn Empty Honeycomb Brood blocks \r\n"
            + " back into Honeycomb Brood Blocks with a larva in it. (affects Dispenser too)")
    public boolean allowPotionOfBeesCompat = true;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = "\nAllow Splash Potion of Bees item to turn Empty Honeycomb Brood \r\n"
            +" blocks back into Honeycomb Brood Blocks with a larva in it when \r\n"
            +" the potion is thrown and splashed near the block. (affects Dispenser too)")
    public boolean allowSplashPotionOfBeesCompat = true;
}
