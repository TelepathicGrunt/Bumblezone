package com.telepathicgrunt.the_bumblezone.modcompat;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import org.apache.logging.log4j.Level;
import org.quiltmc.loader.api.QuiltLoader;

public class ModChecker {

    public static boolean potionOfBeesPresent = false;
    public static boolean strangePresent = false;
    public static boolean friendsAndFoesPresent = false;
    public static boolean beeBetterPresent = false;

    /*
     * -- DO NOT TURN THE LAMBDAS INTO METHOD REFS. Method refs are not classloading safe. --
     *
     * This will run the mod compat setup stuff. If it blows up, it attempts to catch the issue,
     * print it to the console, and then move on to the next mod instead of fully crashing. The compat
     * is basically optional and not necessary for Bumblezone to function. If a classloading issue occurs
     * somehow, we catch and print it so ~~Forge~~ Fabric doesn't silently swallow it. If this happens even
     * with the lambdas, at least it will be much easier to find and debug now although this breaks all
     * mod compat after the problematic mod line.
     */
    public static void setupModCompat() {
        String currentModID = "";

        try {
            currentModID = "friendsandfoes";
            loadupModCompat(currentModID, () -> FriendsAndFoesCompat.setupCompat());

            currentModID = "beebetter";
            loadupModCompat(currentModID, () -> BeeBetterCompat.setupCompat());
        }
        catch (Exception e) {
            printErrorToLogs("classloading " + currentModID + " and so, mod compat done afterwards broke");
            e.printStackTrace();
        }
    }

    private static void loadupModCompat(String modid, Runnable runnable) {
        try {
            if (QuiltLoader.isModLoaded(modid)) {
                runnable.run();
            }
        }
        catch (Throwable e) {
            printErrorToLogs(modid);
            e.printStackTrace();
        }
    }

    private static void printErrorToLogs(String currentModID) {
        Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
        Bumblezone.LOGGER.log(Level.INFO, " ");
        Bumblezone.LOGGER.log(Level.INFO, "ERROR: Something broke when trying to add mod compatibility with" + currentModID + ". Please let The Bumblezone developer (TelepathicGrunt) know about this!");
        Bumblezone.LOGGER.log(Level.INFO, " ");
        Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
    }
}
