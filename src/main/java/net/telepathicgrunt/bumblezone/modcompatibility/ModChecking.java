package net.telepathicgrunt.bumblezone.modcompatibility;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.Level;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModChecking
{
    public static boolean buzzierBeesPresent = false;
    public static boolean AbnormalsCorePresent = false;
    public static boolean potionOfBeesPresent = false;
    public static boolean beesourcefulPresent = false;
    public static boolean productiveBeesPresent = false;


    public static void setupModCompat() {
	String currentModID = "";

	try {
	    currentModID = "buzzierbees";
	    if (ModList.get().isLoaded(currentModID)) {
		ArtifactVersion bbVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion();
		int majorVersion = bbVersion.getMajorVersion();
		int minorVersion = bbVersion.getMinorVersion();

		// DEPRECIATED - TO BE REMOVED IN FUTURE UPDATE
		// only run setup for mod if it is v1.4
		if (majorVersion == 1 && minorVersion == 4) {
		    runSetupForMod(() -> () -> BuzzierBeesCompat.setupBuzzierBees());
		    Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
		    Bumblezone.LOGGER.log(Level.INFO, " ");
		    Bumblezone.LOGGER.log(Level.INFO, "BUMBLEZONE: You're using a version of Buzzier Bees that is know to freeze worlds! Please update Buzzier Bees to v1.5 or greater!");
		    Bumblezone.LOGGER.log(Level.INFO, " ");
		    Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
		}


		// EntityJoinWorldEvent
		// only run for buzzier bees version 1.5 or greater
		// fix for v1.5.0 buzzier bees as the v killed the version checker rip
		if (((majorVersion == 1 && minorVersion >= 5) || majorVersion > 1) || bbVersion.toString().equals("v1.5.0")) {
		    runSetupForMod(() -> () -> BuzzierBeesCompat.setupBuzzierBees());

		    // 1.5+ buzzier bees uses abnormals core which we will need to reference for boats
		    currentModID = "abnormals_core";
		    if (buzzierBeesPresent && ModList.get().isLoaded(currentModID)) {
			runSetupForMod(() -> () -> AbnormalsCoreCompat.setupAbnormalsCore());
		    }
		}
	    }

	}
	catch (Exception e) {
	    printErrorToLogs(currentModID);
	    e.printStackTrace();
	}


	try {
	    currentModID = "beesourceful";
	    if (ModList.get().isLoaded(currentModID)) {
		int majorVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion().getMajorVersion();
		int minorVersion = ModList.get().getModContainerById(currentModID).get().getModInfo().getVersion().getMinorVersion();

		// only run setup for mod if it is v1.1 or greater
		if ((majorVersion == 1 && minorVersion >= 1) || majorVersion > 1) runSetupForMod(() -> () -> BeesourcefulCompat.setupBeesourceful());
	    }

	}
	catch (Exception e) {
	    printErrorToLogs(currentModID);
	    e.printStackTrace();
	}


	try {
	    currentModID = "potionofbees";
	    if (ModList.get().isLoaded(currentModID)) {
		runSetupForMod(() -> () -> PotionOfBeesCompat.setupPotionOfBees());
	    }
	}
	catch (Exception e) {
	    printErrorToLogs(currentModID);
	    e.printStackTrace();
	}


	try {
	    currentModID = "productivebees";
	    if (ModList.get().isLoaded(currentModID)) {
		runSetupForMod(() -> () -> ProductiveBeesCompat.setupProductiveBees());
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
	Bumblezone.LOGGER.log(Level.INFO, "ERROR: Something broke when trying to add mod compatibility with" + currentModID + ". Please let The Bumblezone developer know about this!");
	Bumblezone.LOGGER.log(Level.INFO, " ");
	Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
    }


    /**
     * Hack to make Java not load the class beforehand when we don't have the mod installed. Basically:
     * 
     * "java only loads the method body in 2 cases when it runs or when it needs to run the class verifier"
     * 
     * So by double wrapping, we prevent Java from loading a class with calls to a mod that isn't present
     */
    public static void runSetupForMod(Callable<Runnable> toRun) throws Exception {
	toRun.call().run();
    }


    @Mod.EventBusSubscriber(modid = Bumblezone.MODID)
    private static class ForgeEvents
    {
	/*
	 * Manual spawning of Beesourceful Bees so it can be disabled real time by config. works by making 1/15th of bees spawning also spawn beesourceful bees
	 */
	@SubscribeEvent
	public static void MobSpawnEvent(PlayerEvent.PlayerLoggedInEvent event) {
	    if (ModChecking.buzzierBeesPresent) {

		ArtifactVersion bbVersion = ModList.get().getModContainerById("buzzierbees").get().getModInfo().getVersion();
		int majorVersion = bbVersion.getMajorVersion();
		int minorVersion = bbVersion.getMinorVersion();

		// warn players their buzzier bees version is out of date and will cause issues
		if (majorVersion == 1 && minorVersion <= 4) {
		    ((PlayerEntity) event.getEntity()).sendMessage(new StringTextComponent("§cBumblezone Warning: The version of Buzzier Bees you have on is too old and can cause the game to freeze with Bumblezone. Please update Buzzier Bees to v1.5 or greater!"));
		}
	    }
	}
    }
}
