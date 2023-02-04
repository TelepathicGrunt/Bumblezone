package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.BeekeeperCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;

import static com.telepathicgrunt.the_bumblezone.modcompat.ModChecker.loadupModCompat;
import static com.telepathicgrunt.the_bumblezone.modcompat.ModChecker.printErrorToLogs;

public class ForgeModChecker {

    /**
     * -- DO NOT TURN THE LAMBDAS INTO METHOD REFS. Method refs are not classloading safe. --
     * <p>
     * This will run the mod compat setup stuff. If it blows up, it attempts to catch the issue,
     * print it to the console, and then move on to the next mod instead of fully crashing. The compat
     * is basically optional and not necessary for Bumblezone to function. If a classloading issue occurs
     * somehow, we catch and print it so Forge doesn't silently swallow it. If this happens even with the
     * lambdas, at least it will be much easier to find and debug now although this breaks all mod compat
     * after the problematic mod line.
     * <p>
     * {@link ModChecker}
     */
    public static void setupModCompat() {
        String modid = "";
        try {

            modid = "pokecube_mobs";
            loadupModCompat(modid, () -> new PokecubeCompat());

            modid = "quark";
            loadupModCompat(modid, () -> new QuarkCompat());

            modid = "productivebees";
            loadupModCompat(modid, () -> new ProductiveBeesCompat());

            modid = "bk";
            loadupModCompat(modid, () -> new BeekeeperCompat());

            modid = "buzzier_bees";
            loadupModCompat(modid, () -> new BuzzierBeesCompat());

            modid = "potionofbees";
            loadupModCompat(modid, () -> new PotionOfBeesCompat());
        }
        catch (Throwable e) {
            printErrorToLogs("classloading " + modid + " and so, mod compat done afterwards broke");
            e.printStackTrace();
        }
        ModChecker.setupModCompat();
    }
}
