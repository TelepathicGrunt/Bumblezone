package com.telepathicgrunt.bumblezone.configs;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "Client")
public class BzClientConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Rate for how often a bee will have an LGBT+ coat!""")
    public double lgbtBeeRate = 0.01;


    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """
            Enable replacing the bee renderer for LGBT+ skins.
            Set this to false if the render is messing with other mod's bee renderers.""")
    public boolean enableLgbtBeeRenderer = true;
}
