package com.telepathicgrunt.bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "the_bumblezone-main")
public class BzConfig implements ConfigData {

    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public BzGeneralConfig BZGeneralConfig = new BzGeneralConfig();

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public BzClientConfig BZClientConfig = new BzClientConfig();

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.TransitiveObject
    public BzDungeonsConfig BZDungeonsConfig = new BzDungeonsConfig();

    @ConfigEntry.Category("bee_aggression")
    @ConfigEntry.Gui.TransitiveObject
    public BzBeeAggressionConfig BZBeeAggressionConfig = new BzBeeAggressionConfig();

    @ConfigEntry.Category("dimension")
    @ConfigEntry.Gui.TransitiveObject
    public BzDimensionConfig BZDimensionConfig = new BzDimensionConfig();

    @ConfigEntry.Category("block_mechanics")
    @ConfigEntry.Gui.TransitiveObject
    public BzBlockMechanicsConfig BZBlockMechanicsConfig = new BzBlockMechanicsConfig();

    @ConfigEntry.Category("mod_compatibility")
    @ConfigEntry.Gui.TransitiveObject
    public BzModCompatibilityConfig BZModCompatibilityConfig = new BzModCompatibilityConfig();
}
