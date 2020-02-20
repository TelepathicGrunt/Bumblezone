package net.telepathicgrunt.bumblezone.configs;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

import static net.telepathicgrunt.bumblezone.Bumblezone.MODID;

@ConfigFile(name="TheBumblezoneConfig")
public class BzConfig {

    @Comment(value="Determines if Wrath of the Hive can be applied to players outside\n" +
            "the Bumblezone dimension when they pick up Honey blocks, take honey\n" +
            " from Filled Porous Honey blocks, or drink Honey Bottles.")
    public static boolean allowWrathOfTheHiveOutsideBumblezone = false;
}
