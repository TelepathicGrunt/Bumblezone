
package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public class SodiumCompatImpl implements ModCompat {

    public SodiumCompatImpl() {
        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.sodiumPresent = true;
    }
}
