package com.telepathicgrunt.bumblezone.configs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "block_mechanics")
public class BzBlockMechanicsConfig implements ConfigData {

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Should Dispensers always drop the Glass Bottle when using specific
            bottle items on certain The Bumblezone blocks?

            Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and
            drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.""")
    public boolean dispensersDropGlassBottles = false;

    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip(count = 0)
    @Comment(value = """

            Brood Blocks will automatically spawn bees until the number of active bees is the value below.
            Set this higher to allow Brood Blocks to spawn more bees in a smaller area or set it to 0 to turn
            off automatic Brood Block bee spawning.""")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1000)
    public int broodBlocksBeeSpawnCapacity = 80;
}
