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
}
