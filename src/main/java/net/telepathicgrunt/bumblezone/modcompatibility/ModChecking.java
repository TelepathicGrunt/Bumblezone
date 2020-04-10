package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.ModList;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class ModChecking
{
	public static boolean buzzierBeesPresent = false;
	public static boolean potionOfBeesPresent = false;
	

	public static void setupModCompat() 
	{
		try
		{
			runIfModIsLoaded("buzzierbees", () -> () -> BuzzierBeesCompat.setupBuzzierBees());
			runIfModIsLoaded("potionofbees", () -> () -> PotionOfBeesCompat.setupPotionOfBees());
		}
		catch (Exception e)
		{
			Bumblezone.LOGGER.log(Level.INFO, "ERROR: Tried calling another mod's code when mod isn't present!!!");
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
