package com.telepathicgrunt.the_bumblezone.configs;

import java.util.List;

public class BzModCompatibilityConfigs {

	public static String alternativeFluidToReplaceHoneyFluid = "";

	public static boolean spawnTropicraftBeesMob = true;
	public static double spawnrateOfTropicraftBeesMobs = 0.025D;
	public static boolean allowTropicraftSpawnFromDispenserFedBroodBlock = true;

	public static boolean spawnResourcefulBeesBeesMob = true;
	public static double spawnrateOfResourcefulBeesMobsBrood = 0.1D;
	public static double spawnrateOfResourcefulBeesMobsOther = 0.1D;
	public static double RBOreHoneycombSpawnRateBeeDungeon = 0.045D;
	public static double RBOreHoneycombSpawnRateSpiderBeeDungeon = 0.10D;
	public static boolean spawnResourcefulBeesHoneycombVeins = true;
	public static boolean allowResourcefulBeesBeeJarRevivingEmptyBroodBlock = true;
	public static boolean allowResourcefulBeesSpawnFromDispenserFedBroodBlock = true;

	public static boolean spawnProductiveBeesBeesMob = true;
	public static double spawnrateOfProductiveBeesMobs = 0.1D;
	public static boolean allowProductiveBeesBeeCageRevivingEmptyBroodBlock = true;
	public static boolean allowProductiveBeesSpawnFromDispenserFedBroodBlock = true;
	public static boolean spawnProductiveBeesHoneycombVariants = true;
	public static double PBOreHoneycombSpawnRateBeeDungeon = 0.125D;
	public static double PBOreHoneycombSpawnRateSpiderBeeDungeon = 0.25D;
	public static List<String> allowedCombsForDungeons = List.of(
			"productivebees:butcher",
			"productivebees:coal",
			"productivebees:dusts/blazing",
			"productivebees:dusts/glowing",
			"productivebees:dusts/redstone",
			"productivebees:ender",
			"productivebees:experience",
			"productivebees:frosty",
			"productivebees:gems/amethyst",
			"productivebees:gems/crystalline",
			"productivebees:gems/diamond",
			"productivebees:gems/emerald",
			"productivebees:gems/lapis",
			"productivebees:lava",
			"productivebees:magmatic",
			"productivebees:obsidian",
			"productivebees:prismarine",
			"productivebees:raw_materials/copper",
			"productivebees:raw_materials/gold",
			"productivebees:raw_materials/iron",
			"productivebees:silky",
			"productivebees:skeletal",
			"productivebees:slimy",
			"productivebees:zombie"
	);
	public static List<String> allowedBees = List.of(
			"productivebees:butcher",
			"productivebees:coal",
			"productivebees:dusts/blazing",
			"productivebees:dusts/glowing",
			"productivebees:dusts/redstone",
			"productivebees:ender",
			"productivebees:experience",
			"productivebees:frosty",
			"productivebees:gems/amethyst",
			"productivebees:gems/crystalline",
			"productivebees:gems/diamond",
			"productivebees:gems/emerald",
			"productivebees:gems/lapis",
			"productivebees:lava",
			"productivebees:magmatic",
			"productivebees:obsidian",
			"productivebees:prismarine",
			"productivebees:raw_materials/copper",
			"productivebees:raw_materials/gold",
			"productivebees:raw_materials/iron",
			"productivebees:shroom/brown_shroom",
			"productivebees:shroom/red_shroom",
			"productivebees:silky",
			"productivebees:skeletal",
			"productivebees:slimy",
			"productivebees:sugarbag",
			"productivebees:zombie"
	);

	public static boolean allowBeeBottleRevivingEmptyBroodBlock = true;

	public static boolean allowBeeBucketRevivingEmptyBroodBlock = true;

	public static boolean allowPotionOfBeesRevivingEmptyBroodBlock = true;

	public static boolean allowGoodallBottledBeesRevivingEmptyBroodBlock = true;

	public static boolean allowLootrCompat = true;

	public static boolean allowCreateLimestoneForHoneyLavaCompat = true;

	public static boolean allowForestryCompat = true;
}
