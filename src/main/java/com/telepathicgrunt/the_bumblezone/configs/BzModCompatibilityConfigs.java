package com.telepathicgrunt.the_bumblezone.configs;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class BzModCompatibilityConfigs {
	public static final ForgeConfigSpec GENERAL_SPEC;

	public static ForgeConfigSpec.BooleanValue allowHoneyFluidTanksFeedingCompat;
	
	public static ForgeConfigSpec.BooleanValue spawnPokecubeBeePokemon;
	public static ForgeConfigSpec.DoubleValue spawnrateOfPokecubeBeePokemon;

	public static ForgeConfigSpec.BooleanValue spawnProductiveBeesBeesMob;
	public static ForgeConfigSpec.DoubleValue spawnrateOfProductiveBeesMobs;
	public static ForgeConfigSpec.BooleanValue allowHoneyTreatCompat;
	public static ForgeConfigSpec.BooleanValue spawnProductiveBeesHoneycombVariants;
	public static ForgeConfigSpec.DoubleValue oreHoneycombSpawnRateBeeDungeon;
	public static ForgeConfigSpec.DoubleValue oreHoneycombSpawnRateSpiderBeeDungeon;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> allowedCombsForDungeons;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> allowedCombsAsOres;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> allowedBees;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		GENERAL_SPEC = configBuilder.build();
	}

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Mod Compatibility Options");
			builder.push("General Compat Options");

			allowHoneyFluidTanksFeedingCompat = builder
							.comment(" \n-----------------------------------------------------\n",
									" Will let you feed any item that has a Forge fluid capability attached and has ",
									"  any fluid that is tagged forge:fluid/honey inside. This works alongside the bee_feeding item tag.",
									"  An item can still be fed even if bee_feeding tag doesn't have the item as long as the ",
									" item fit the above conditions with the fluid capability and this config is set to true.\n")
							.translation("the_bumblezone.config.allowhoneyfluidtanksfeedingcompat")
							.define("allowHoneyFluidTanksFeedingCompat", true);

			builder.pop();

			builder.push("Pokecube Options");

			spawnPokecubeBeePokemon = builder
					.comment(" \n-----------------------------------------------------\n",
							" Spawn Pokecube's bee-like pokemon in The Bumblezone and from Honey Brood Blocks.\n")
					.translation("the_bumblezone.config.spawnpokecubebeepokemon")
					.define("spawnPokecubeBeePokemon", true);

			spawnrateOfPokecubeBeePokemon = builder
					.comment(" \n-----------------------------------------------------\n",
							" Chance of a Bee spawning in Bumblezone or from Honeycomb Brood Blocks being replaced by Pokecube's bee pokemon.",
							" 0 is no PC's mobs and 1 is max PC's mobs.\n")
					.translation("the_bumblezone.config.spawnrateofpokecubebeepokemon")
					.defineInRange("spawnrateOfPokecubeBeePokemon", 0.02D, 0D, 1D);

			builder.pop();

			builder.push("Productive Bees Options");

			spawnProductiveBeesBeesMob = builder
					.comment(" \n-----------------------------------------------------\n",
							" Spawn Productive Bees in The Bumblezone and from Honey Brood Blocks",
							" alongside regular bees at a 1/15th chance when spawning regular bees.\n")
					.translation("the_bumblezone.config.spawnproductivebeesbeesmob")
					.define("spawnProductiveBeesBeesMob", true);

			spawnrateOfProductiveBeesMobs = builder
					.comment(" \n-----------------------------------------------------\n",
							" Chance of a Bee spawning in Bumblezone or from Honeycomb Brood Blocks being replaced by Productive Bee's mob.",
							" 0 is no PB's mobs and 1 is max PB's mobs.\n")
					.translation("the_bumblezone.config.spawnrateofproductivebeesmobs")
					.defineInRange("spawnrateOfProductiveBeesMobs", 0.03D, 0D, 1D);

			spawnProductiveBeesHoneycombVariants = builder
					.comment(" \n-----------------------------------------------------\n",
							" Spawn Productive Bees's various honeycomb variants in The Bumblezone",
							" at all kinds of heights and height bands. Start exploring to find ",
							" where they spawn!",
							" ",
							" NOTE: Will require a restart of the world to take effect. \n")
					.translation("the_bumblezone.config.spawnproductivebeeshoneycombvariants")
					.define("spawnProductiveBeesHoneycombVariants", true);

			allowedCombsForDungeons = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow what Productive Bees combs should be able to spawn in Bumblezone dungeons.")
					.translation("the_bumblezone.config.allowedcombsfordungeons")
					.defineList("allowedCombsForDungeons",
							List.of("productivebees:diamond",
									"productivebees:iron",
									"productivebees:coal",
									"productivebees:redstone",
									"productivebees:copper",
									"productivebees:lapis",
									"productivebees:gold",
									"productivebees:emerald",
									"productivebees:obsidian",
									"productivebees:experience",
									"productivebees:magmatic",
									"productivebees:amethyst",
									"productivebees:prismarine",
									"productivebees:crystalline",
									"productivebees:sugarbag",
									"productivebees:glowing",
									"productivebees:frosty",
									"productivebees:slimy",
									"productivebees:silky",
									"productivebees:blazing",
									"productivebees:ender",
									"productivebees:skeletal",
									"productivebees:ghostly",
									"productivebees:zombie"
							), (t) -> true);

			allowedCombsAsOres = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow what Productive Bees combs should be able to spawn in Bumblezone as ores.")
					.translation("the_bumblezone.config.allowedcombsasores")
					.defineList("allowedCombsAsOres",
							List.of("productivebees:iron",
									"productivebees:redstone",
									"productivebees:copper",
									"productivebees:gold",
									"productivebees:magmatic",
									"productivebees:crystalline",
									"productivebees:glowing",
									"productivebees:blazing",
									"productivebees:skeletal",
									"productivebees:ghostly"
							), (t) -> true);

			allowedBees = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow what Productive Bees bees should be able to spawn in Bumblezone. ")
					.translation("the_bumblezone.config.blacklistedbees")
					.defineList("blacklistedBees",
							List.of("productivebees:diamond",
									"productivebees:iron",
									"productivebees:coal",
									"productivebees:redstone",
									"productivebees:copper",
									"productivebees:lapis",
									"productivebees:gold",
									"productivebees:emerald",
									"productivebees:obsidian",
									"productivebees:experience",
									"productivebees:magmatic",
									"productivebees:amethyst",
									"productivebees:prismarine",
									"productivebees:crystalline",
									"productivebees:sugarbag",
									"productivebees:glowing",
									"productivebees:frosty",
									"productivebees:slimy",
									"productivebees:silky",
									"productivebees:blazing",
									"productivebees:ender",
									"productivebees:skeletal",
									"productivebees:ghostly",
									"productivebees:zombie"
							), (t) -> true);

			allowHoneyTreatCompat = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow Honey Treat to be able to feed bees and Honeycomb Brood Blocks.\n")
					.translation("the_bumblezone.config.allowhoneytreatcompat")
					.define("allowHoneyTreatCompat", true);

			oreHoneycombSpawnRateBeeDungeon = builder
					.comment(" \n-----------------------------------------------------\n",
							" How much of Bee Dungeons is made of ore-based honeycombs.",
							" 0 is no PB's honeycombs and 1 is max PB's honeycombs.\n")
					.translation("the_bumblezone.config.orehoneycombspawnratebeedungeon")
					.defineInRange("PBOreHoneycombSpawnRateBeeDungeon", 0.06D, 0D, 1D);

			oreHoneycombSpawnRateSpiderBeeDungeon = builder
					.comment(" \n-----------------------------------------------------\n",
							" How much of Spider Infested Bee Dungeons is made of ore-based honeycombs.",
							" 0 is no PB's honeycombs and 1 is max PB's honeycombs.\n")
					.translation("the_bumblezone.config.orehoneycombspawnratespiderbeedungeon")
					.defineInRange("PBOreHoneycombSpawnRateSpiderBeeDungeon", 0.12D, 0D, 1D);

			builder.pop();


		builder.pop();
	}
}
