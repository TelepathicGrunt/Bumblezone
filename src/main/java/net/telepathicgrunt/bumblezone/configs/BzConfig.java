package net.telepathicgrunt.bumblezone.configs;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "the_bumblezone-main")
public class BzConfig implements ConfigData {

    @ConfigEntry.Category("Dungeons")
    @ConfigEntry.Gui.TransitiveObject
    public BZDungeonsConfig BZDungeonsConfig = new BZDungeonsConfig();

    @ConfigEntry.Category("Bee Aggression")
    @ConfigEntry.Gui.TransitiveObject
    public BZBeeAggressionConfig BZBeeAggressionConfig = new BZBeeAggressionConfig();

    @ConfigEntry.Category("Diemnsion")
    @ConfigEntry.Gui.TransitiveObject
    public BZDimensionConfig BZDimensionConfig = new BZDimensionConfig();

    @ConfigEntry.Category("Block Mechanics")
    @ConfigEntry.Gui.TransitiveObject
    public BZBlockMechanicsConfig BZBlockMechanicsConfig = new BZBlockMechanicsConfig();
}
