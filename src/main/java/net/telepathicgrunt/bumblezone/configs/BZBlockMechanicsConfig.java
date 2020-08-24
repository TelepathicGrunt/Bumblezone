package net.telepathicgrunt.bumblezone.configs;

import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "Block Mechanics")
public class BZBlockMechanicsConfig {

    @ConfigEntry.Gui.Tooltip(count = 5)
    @Comment(value = "\nShould Dispensers always drop the Glass Bottle when using specific \n"
            +"bottle items on certain The Bumblezone blocks? \n"
            +"\n"
            +"Example: Using Honey Bottle to feed Honeycomb Brood Blocks will grow the larva and \n"
            +"drop the Glass Bottle instead of putting it back into Dispenser if this is set to true.")
    public boolean dispensersDropGlassBottles = false;
}
