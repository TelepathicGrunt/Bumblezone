package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.Level;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class ModChecker {

	public static boolean productiveBeesPresent = false;
	public static boolean resourcefulBeesPresent = false;
	public static boolean buzzierBeesPresent = false;
	public static boolean pokecubePresent = false;
	public static boolean friendsAndFoesPresent = false;
	public static boolean beekeeperPresent = false;
	public static boolean quarkPresent = false;
	public static boolean potionOfBeesPresent = false;
	public static boolean arsNouveauPresent = false;
	public static boolean arsElementalPresent = false;
	public static boolean twilightForestPresent = false;
	public static boolean dragonEnchantsPresent = false;

	/*
	 * -- DO NOT TURN THE LAMBDAS INTO METHOD REFS. Method refs are not classloading safe. --
	 *
	 * This will run the mod compat setup stuff. If it blows up, it attempts to catch the issue,
	 * print it to the console, and then move on to the next mod instead of fully crashing. The compat
	 * is basically optional and not necessary for Bumblezone to function. If a classloading issue occurs
	 * somehow, we catch and print it so Forge doesn't silently swallow it. If this happens even with the
	 * lambdas, at least it will be much easier to find and debug now although this breaks all mod compat
	 * after the problematic mod line.
	 */
    public static void setupModCompat() {
		String modid = "";
		try {

			modid = "pokecube_mobs";
			loadupModCompat(modid, () -> PokecubeCompat.setupCompat());

			modid = "productivebees";
			loadupModCompat(modid, () -> ProductiveBeesCompat.setupCompat());

			modid = "friendsandfoes";
			loadupModCompat(modid, () -> FriendsAndFoesCompat.setupCompat());

			modid = "bk";
			loadupModCompat(modid, () -> BeekeeperCompat.setupCompat());

			modid = "quark";
			loadupModCompat(modid, () -> QuarkCompat.setupCompat());

			modid = "buzzier_bees";
			loadupModCompat(modid, () -> BuzzierBeesCompat.setupCompat());

			modid = "resourcefulbees";
			loadupModCompat(modid, () -> ResourcefulBeesCompat.setupCompat());

			modid = "potionofbees";
			loadupModCompat(modid, () -> PotionOfBeesCompat.setupCompat());

			modid = "ars_nouveau";
			loadupModCompat(modid, () -> ArsNouveauCompat.setupCompat());

			modid = "ars_elemental";
			loadupModCompat(modid, () -> ArsElementalCompat.setupCompat());

			modid = "twilightforest";
			loadupModCompat(modid, () -> TwilightForestCompat.setupCompat());

			modid = "dragon_enchants";
			loadupModCompat(modid, () -> DragonEnchantsCompat.setupCompat());
		}
		catch (Throwable e) {
			printErrorToLogs("classloading " + modid + " and so, mod compat done afterwards broke");
			e.printStackTrace();
		}
    }

    private static void loadupModCompat(String modid, Runnable runnable){
		try {
			if (ModList.get().isLoaded(modid)) {
				runnable.run();
			}
		}
		catch (Throwable e) {
			printErrorToLogs(modid);
			e.printStackTrace();
		}
	}

	private static void printErrorToLogs(String currentModID) {
		Bumblezone.LOGGER.log(Level.ERROR, """
		  ------------------------------------------------NOTICE-------------------------------------------------------------------------
		  
		  ERROR: Something broke when trying to add mod compatibility with %s. Please let The Bumblezone developer (TelepathicGrunt) know about this!
		  
		  ------------------------------------------------NOTICE-------------------------------------------------------------------------
		""".formatted(currentModID));
	}

    private static boolean isNotOutdated(String currentModID, String minVersion, boolean checkQualifierInstead) {
    	if(!ModList.get().isLoaded(currentModID)) return true;

		IModInfo modInfo = ModList.get().getModContainerById(currentModID).get().getModInfo();
		ArtifactVersion modVersion = modInfo.getVersion();

		// some people do 1.16.4-0.5.0.5 and we have to parse the second half instead.
		// if someone does 0.5.0.5-1.16.4, well, we are screwed lmao. WE HAVE STANDARDS FOR A REASON PEOPLE! lmao
		if(checkQualifierInstead && modVersion.getQualifier() != null){
			modVersion = new DefaultArtifactVersion(modVersion.getQualifier());
		}

		if (modVersion.compareTo(new DefaultArtifactVersion(minVersion)) < 0) {
			Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
			Bumblezone.LOGGER.log(Level.INFO, " ");
			Bumblezone.LOGGER.log(Level.INFO, "BUMBLEZONE: You're using a version of " + modInfo.getDisplayName() + " that is outdated. Please update " + modInfo.getDisplayName() + " to the latest version of that mod to enable compat with Bumblezone again.");
			Bumblezone.LOGGER.log(Level.INFO, " ");
			Bumblezone.LOGGER.log(Level.INFO, "------------------------------------------------NOTICE-------------------------------------------------------------------------");
			return false;
		}

		return true;
	}

}
