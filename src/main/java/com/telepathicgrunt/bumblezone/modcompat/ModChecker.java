package com.telepathicgrunt.bumblezone.modcompat;


import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.apache.logging.log4j.Level;

public class ModChecker {

    public static boolean potionOfBeesPresent = false;
    public static boolean beeBetterPresent = false;
    public static boolean charmPresent = false;

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
            currentModID = "potionofbees";
            loadupModCompat(currentModID, () -> PotionOfBeesCompat.setupPotionOfBees());

            currentModID = "beebetter";
            loadupModCompat(currentModID, () -> BeeBetterCompat.setupBeeBetter());

            currentModID = "charm";
            if(isNotOutdated(currentModID, "2.3.2"))
                loadupModCompat(currentModID, () -> CharmCompat.setupCharm());
        }
        catch (Exception e) {
            printErrorToLogs("classloading " + currentModID + " and so, mod compat done afterwards broke");
            e.printStackTrace();
        }
    }

    private static void loadupModCompat(String modid, Runnable runnable){
        try {
            if (FabricLoader.getInstance().isModLoaded(modid)) {
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


    private static boolean isNotOutdated(String currentModID, String minVersion) throws VersionParsingException {
        if(!FabricLoader.getInstance().isModLoaded(currentModID)) return false;

        ModContainer modContainer = FabricLoader.getInstance().getModContainer(currentModID).orElse(null);
        if(modContainer == null) return false;

        Version modVersion = modContainer.getMetadata().getVersion();

        // some people do 1.16.4-0.5.0.5 and we have to parse the second half instead.
        // if someone does 0.5.0.5-1.16.4, well, we are screwed lmao. WE HAVE STANDARDS FOR A REASON PEOPLE! lmao
        boolean validVersion;
        if(modVersion instanceof SemanticVersion){
            validVersion = ((SemanticVersion)modVersion).compareTo(SemanticVersion.parse(minVersion)) < 0;
        }
        else{
            validVersion = modVersion.getFriendlyString().split("-")[0].compareTo(minVersion) < 0;
        }

        if (validVersion) {
            Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
            Bumblezone.LOGGER.log(Level.INFO, " ");
            Bumblezone.LOGGER.log(Level.INFO, "BUMBLEZONE: You're using a version of " + modContainer.getMetadata().getName() + " that is outdated. Please update " + modContainer.getMetadata().getName() + " to the latest version of that mod to enable compat with Bumblezone again.");
            Bumblezone.LOGGER.log(Level.INFO, " ");
            Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
            return false;
        }

        return true;
    }
}
