package net.telepathicgrunt.bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "the_bumblezone-main")
public class BzConfig implements ConfigData {

    @ConfigEntry.Category("dungeons")
    @ConfigEntry.Gui.TransitiveObject
    public BZDungeonsConfig BZDungeonsConfig = new BZDungeonsConfig();

    @ConfigEntry.Category("bee_aggression")
    @ConfigEntry.Gui.TransitiveObject
    public BZBeeAggressionConfig BZBeeAggressionConfig = new BZBeeAggressionConfig();

    @ConfigEntry.Category("dimension")
    @ConfigEntry.Gui.TransitiveObject
    public BZDimensionConfig BZDimensionConfig = new BZDimensionConfig();

    @ConfigEntry.Category("block_mechanics")
    @ConfigEntry.Gui.TransitiveObject
    public BZBlockMechanicsConfig BZBlockMechanicsConfig = new BZBlockMechanicsConfig();

    @ConfigEntry.Category("mod_compatibility")
    @ConfigEntry.Gui.TransitiveObject
    public BZModCompatibilityConfig BZModCompatibilityConfig = new BZModCompatibilityConfig();
}
