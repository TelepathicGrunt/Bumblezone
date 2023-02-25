package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModChecker {

	public static final List<ModCompat> SPAWNING_COMPATS = new ArrayList<>();
	public static final List<ModCompat> BROOD_EMPTY_COMPATS = new ArrayList<>();
	public static final List<ModCompat> DUNGEON_COMB_COMPATS = new ArrayList<>();
	public static final List<ModCompat> DIM_SPAWN_COMPATS = new ArrayList<>();
	public static final List<ModCompat> BLOCK_TELEPORT_COMPATS = new ArrayList<>();
	public static final List<ModCompat> COMB_ORE_COMPATS = new ArrayList<>();
	public static final List<ModCompat> HOST_BEE_COMPATS = new ArrayList<>();
	public static final List<ModCompat> PROJECTILE_IMPACT_HANDLED_COMPATS = new ArrayList<>();

	public static boolean productiveBeesPresent = false;
	public static boolean resourcefulBeesPresent = false;
	public static boolean buzzierBeesPresent = false;
	public static boolean pokecubePresent = false;
	public static boolean friendsAndFoesPresent = false;
	public static boolean beekeeperPresent = false;
	public static boolean quarkPresent = false;
	public static boolean potionOfBeesPresent = false;
	public static boolean requiemPresent = false;
	public static boolean twilightForestPresent = false;
	public static boolean dragonEnchantPresent = false;

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

			modid = "friendsandfoes";
			loadupModCompat(modid, () -> new FriendsAndFoesCompat());

			modid = "resourcefulbees";
			loadupModCompat(modid, () -> new ResourcefulBeesCompat());

			modid = "twilightforest";
			loadupModCompat(modid, () -> new TwilightForestCompat());
		}
		catch (Throwable e) {
			printErrorToLogs("classloading " + modid + " and so, mod compat done afterwards broke");
			e.printStackTrace();
		}
    }

	@ApiStatus.Internal
    public static void loadupModCompat(String modid, Supplier<ModCompat> loader){
		try {
			if (PlatformHooks.isModLoaded(modid)) {
				ModCompat compat = loader.get();
				if (compat.compatTypes().contains(ModCompat.Type.SPAWNS)) SPAWNING_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.EMPTY_BROOD)) BROOD_EMPTY_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.COMBS)) DUNGEON_COMB_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.DIMENSION_SPAWN)) DIM_SPAWN_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.BLOCK_TELEPORT)) BLOCK_TELEPORT_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.COMB_ORE)) COMB_ORE_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.HAS_HOST_BEES)) HOST_BEE_COMPATS.add(compat);
				if (compat.compatTypes().contains(ModCompat.Type.PROJECTILE_IMPACT_HANDLED)) PROJECTILE_IMPACT_HANDLED_COMPATS.add(compat);
			}
		}
		catch (Throwable e) {
			printErrorToLogs(modid);
			e.printStackTrace();
		}
	}

	@ApiStatus.Internal
	public static void printErrorToLogs(String currentModID) {
		Bumblezone.LOGGER.error("""
		  ------------------------------------------------NOTICE-------------------------------------------------------------------------
		  
		  ERROR: Something broke when trying to add mod compatibility with %s. Please let The Bumblezone developer (TelepathicGrunt) know about this!
		  
		  ------------------------------------------------NOTICE-------------------------------------------------------------------------
		""".formatted(currentModID));
	}

    private static boolean isNotOutdated(String currentModID, String minVersion, boolean checkQualifierInstead) {
		if(!PlatformHooks.isModLoaded(currentModID)) return true;

		ModInfo info = PlatformHooks.getModInfo(currentModID, checkQualifierInstead);

		if (info != null && info.compare(minVersion) < 0) {
			Bumblezone.LOGGER.info("------------------------------------------------NOTICE-------------------------------------------------------------------------");
			Bumblezone.LOGGER.info(" ");
			Bumblezone.LOGGER.info("BUMBLEZONE: You're using a version of " + info.displayName() + " that is outdated. Please update " + info.displayName() + " to the latest version of that mod to enable compat with Bumblezone again.");
			Bumblezone.LOGGER.info(" ");
			Bumblezone.LOGGER.info("------------------------------------------------NOTICE-------------------------------------------------------------------------");
			return false;
		}

		return true;
	}

}
