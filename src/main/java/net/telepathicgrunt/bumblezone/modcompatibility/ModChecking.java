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
	

	public static void setupModCompat() 
	{
		try{ runIfModIsLoaded("buzzierbees", () -> () -> BuzzierBeesCompat.setupBuzzierBees()); }
		catch (Exception e){
			Bumblezone.LOGGER.log(Level.INFO, "ERROR: Somehow tried calling buzzierbees code when it isn't on.");
			e.printStackTrace();
		}
		
		
		try{ runIfModIsLoaded("potionofbees", () -> () -> PotionOfBeesCompat.setupPotionOfBees()); }
		catch (Exception e){
			Bumblezone.LOGGER.log(Level.INFO, "ERROR: Somehow tried calling potionofbees code when it isn't on.");
			e.printStackTrace();
		}
		
		
		try{ runIfModIsLoaded("beesourceful", () -> () -> BeesourcefulCompat.setupBeesourceful()); }
		catch (Exception e){
			Bumblezone.LOGGER.log(Level.INFO, "ERROR: Somehow tried calling beesourceful code when it isn't on.");
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
	public static void runIfModIsLoaded(String modid, Callable<Runnable> toRun) throws Exception{
		if(ModList.get().isLoaded(modid)) toRun.call().run();
	}
}
