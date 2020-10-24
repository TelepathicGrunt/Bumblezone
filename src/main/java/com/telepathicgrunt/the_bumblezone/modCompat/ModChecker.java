package com.telepathicgrunt.the_bumblezone.modCompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.Level;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class ModChecker
{
    public static boolean potionOfBeesPresent = false;
	public static boolean productiveBeesPresent = false;
	public static boolean carrierBeesPresent = false;
	public static boolean resourcefulBeesPresent = false;


    public static void setupModCompat() {
		loadupModCompat("potionofbees", PotionOfBeesCompat::setupPotionOfBees);
		loadupModCompat("carrierbees", CarrierBeesCompat::setupProductiveBees);
		loadupModCompat("productivebees", ProductiveBeesCompat::setupProductiveBees);
		loadupModCompat("resourcefulbees", ResourcefulBeesCompat::setupResourcefulBees);
    }

    private static void loadupModCompat(String modid, Runnable runnable){
		String currentModID = "";
		try {
			currentModID = modid;
			if (ModList.get().isLoaded(currentModID)) {
				runnable.run();
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

}
