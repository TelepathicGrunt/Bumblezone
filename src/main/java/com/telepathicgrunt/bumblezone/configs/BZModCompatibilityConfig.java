package com.telepathicgrunt.bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.potion.Potion;

@Config(name = "mod_compatibility")
public class BZModCompatibilityConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
            
            Adds Bumblezone items to Charm's Beekeeper trades!""")
    public boolean allowCharmBeekeeperTradesCompat = true;
}
