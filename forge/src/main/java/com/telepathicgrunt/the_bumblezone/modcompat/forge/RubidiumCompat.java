
package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;

public class RubidiumCompat implements ModCompat {

    public RubidiumCompat() {
        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.rubidiumPresent = true;
    }
}
