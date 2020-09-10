package net.telepathicgrunt.bumblezone.configs;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "mod_compatibility")
public class BZModCompatibilityConfig implements ConfigData {

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
