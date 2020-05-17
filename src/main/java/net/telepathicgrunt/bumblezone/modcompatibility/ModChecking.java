package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.ModList;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class ModChecking
{
	public static boolean buzzierBeesPresent = false;
	public static boolean potionOfBeesPresent = false;
	public static boolean beesourcefulPresent = false;
	public static boolean productiveBeesPresent = false;
	

	public static void setupModCompat() 
	{
	    	String currentModID = "";
	    	
		try{ 
		    	currentModID = "buzzierbees";
			if(ModList.get().isLoaded(currentModID)) {
				int majorVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion().getMajorVersion();
				int minorVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion().getMinorVersion();
				
				//only run setup for mod if it is v1.4 or greater
				if((majorVersion == 1 && minorVersion >= 4) || majorVersion > 1)
					runSetupForMod(() -> () -> BuzzierBeesCompat.setupBuzzierBees()); 
				
				//fix for v1.5.0 buzzier bees as the v killed the version checker rip
				if(majorVersion == 0 && minorVersion == 0)
					runSetupForMod(() -> () -> BuzzierBeesCompat.setupBuzzierBees()); 
			}


			currentModID = "beesourceful";
			if(ModList.get().isLoaded(currentModID)) {
				int majorVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion().getMajorVersion();
				int minorVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion().getMinorVersion();
				
				//only run setup for mod if it is v1.1 or greater
				if((majorVersion == 1 && minorVersion >= 1) || majorVersion > 1)
					runSetupForMod(() -> () -> BeesourcefulCompat.setupBeesourceful()); 
			}

			
			currentModID = "potionofbees";
			if(ModList.get().isLoaded(currentModID)) {
				runSetupForMod(() -> () -> PotionOfBeesCompat.setupPotionOfBees());
			}

			
			currentModID = "productivebees";
			if(ModList.get().isLoaded(currentModID)) {
				runSetupForMod(() -> () -> ProductiveBeesCompat.setupProductiveBees());
			}
			
		}
		catch (Exception e){
			Bumblezone.LOGGER.log(Level.INFO, "ERROR: Somehow tried calling "+currentModID+" code when it isn't on.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Hack to make Java not load the class beforehand when we don't have the mod installed.
	 * Basically: 
	 * 
	 * "java only loads the method body in 2 cases
	 * when it runs or when it needs to run the class verifier"
	 * 
	 * So by double wrapping, we prevent Java from loading a class with calls to a mod that isn't present
	 */
	public static void runSetupForMod(Callable<Runnable> toRun) throws Exception{
		toRun.call().run();
	}
}
