package net.telepathicgrunt.bumblezone.modCompat;

import net.minecraftforge.fml.ModList;
import net.telepathicgrunt.bumblezone.Bumblezone;
import org.apache.logging.log4j.Level;

import java.util.concurrent.Callable;

public class ModChecker
{
    public static boolean potionOfBeesPresent = false;
    public static boolean productiveBeesPresent = false;


    public static void setupModCompat() {
		String currentModID = "";

		try {
		    currentModID = "potionofbees";
		    if (ModList.get().isLoaded(currentModID)) {
		    	runSetupForMod(() -> PotionOfBeesCompat::setupPotionOfBees);
		    }
		}
		catch (Exception e) {
		    printErrorToLogs(currentModID);
		    e.printStackTrace();
		}
	
	
		try {
		    currentModID = "productivebees";
		    if (ModList.get().isLoaded(currentModID)) {
		    	runSetupForMod(() -> ProductiveBeesCompat::setupProductiveBees);
		    }
	
		}
		catch (Exception e) {
		    printErrorToLogs(currentModID);
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


    /**
     * Hack to make Java not load the class beforehand when we don't have the mod installed. Basically:
     * "java only loads the method body in 2 cases when it runs or when it needs to run the class verifier"
     * So by double wrapping, we prevent Java from loading a class with calls to a mod that isn't present
     */
    public static void runSetupForMod(Callable<Runnable> toRun) throws Exception {
    	toRun.call().run();
    }
}
