package com.telepathicgrunt.the_bumblezone.modcompat;

public class ForestryCompat implements ModCompat {

    public ForestryCompat() {

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.forestryPresent = true;
    }
}
