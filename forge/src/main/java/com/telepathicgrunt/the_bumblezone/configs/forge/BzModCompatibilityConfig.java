package com.telepathicgrunt.the_bumblezone.configs.forge;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class BzModCompatibilityConfig {
	public static final ForgeConfigSpec GENERAL_SPEC;

	public static ForgeConfigSpec.BooleanValue allowHoneyFluidTanksFeedingCompat;

	public static ForgeConfigSpec.BooleanValue spawnPokecubeBeePokemon;
	public static ForgeConfigSpec.BooleanValue beePokemonGetsProtectionEffect;
	public static ForgeConfigSpec.DoubleValue spawnrateOfPokecubeBeePokemon;

	public static ForgeConfigSpec.BooleanValue spawnResourcefulBeesBeesMob;
	public static ForgeConfigSpec.DoubleValue spawnrateOfResourcefulBeesMobsBrood;
	public static ForgeConfigSpec.DoubleValue spawnrateOfResourcefulBeesMobsOther;
	public static ForgeConfigSpec.DoubleValue RBOreHoneycombSpawnRateBeeDungeon;
	public static ForgeConfigSpec.DoubleValue RBOreHoneycombSpawnRateSpiderBeeDungeon;
	public static ForgeConfigSpec.BooleanValue spawnResourcefulBeesHoneycombVeins;

	public static ForgeConfigSpec.BooleanValue spawnProductiveBeesBeesMob;
	public static ForgeConfigSpec.DoubleValue spawnrateOfProductiveBeesMobs;
	public static ForgeConfigSpec.BooleanValue allowHoneyTreatCompat;
	public static ForgeConfigSpec.BooleanValue spawnProductiveBeesHoneycombVariants;
	public static ForgeConfigSpec.DoubleValue PBOreHoneycombSpawnRateBeeDungeon;
	public static ForgeConfigSpec.DoubleValue PBOreHoneycombSpawnRateSpiderBeeDungeon;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> allowedCombsForDungeons;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> allowedCombsAsOres;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> allowedBees;

	public static ForgeConfigSpec.BooleanValue allowFriendsAndFoesBeekeeperTradesCompat;
	public static ForgeConfigSpec.BooleanValue injectBzItemsIntoQuarkEnchantmentTooltipsCompat;

	public static ForgeConfigSpec.BooleanValue allowBeeBottleRevivingEmptyBroodBlock;
	public static ForgeConfigSpec.BooleanValue spawnCrystallizedHoneyInDimension;
	public static ForgeConfigSpec.BooleanValue spawnHoneyTilesInDimension;

	public static ForgeConfigSpec.BooleanValue allowPotionOfBeesRevivingEmptyBroodBlock;

	public static ForgeConfigSpec.BooleanValue allowBeekeeperTradesCompat;

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

			beePokemonGetsProtectionEffect = builder
					.comment(" \n-----------------------------------------------------\n",
							" Pokecube's bee-like pokemon that spawn in The Bumblezone will get",
							" Protection of the Hive effect. Attacking these pokemon in bumblezone will",
							" give you Wrath of the Hive effect and swarmed of angry bees.\n")
					.translation("the_bumblezone.config.beePokemonGetsProtectionEffect")
					.define("beePokemonGetsProtectionEffect", true);

			spawnrateOfPokecubeBeePokemon = builder
					.comment(" \n-----------------------------------------------------\n",
							" Chance of a Bee spawning from Honeycomb Brood Blocks being replaced by Pokecube's bee pokemon.",
							" 0 is no PC's mobs and 1 is max PC's mobs.\n")
					.translation("the_bumblezone.config.spawnrateofpokecubebeepokemon")
					.defineInRange("spawnrateOfPokecubeBeePokemon", 0.05D, 0D, 1D);

			builder.pop();

			builder.push("Resourceful Bees Options");

			spawnResourcefulBeesBeesMob = builder
					.comment(" \n-----------------------------------------------------\n",
							" Spawn Resourceful Bees in The Bumblezone and from Honey Brood Blocks alongside",
							" regular bees at a spawnrateOfResourcefulBeesMobs chance when spawning regular bees.",
							" You can datapack the_bumblezone:resourcefulbees/spawnable_from_brood_block entity tag",
							" and/or datapack the_bumblezone:resourcefulbees/spawnable_from_chunk_creation entity tag",
							" for more control of what kinds of bees spawns.\n")
					.translation("the_bumblezone.config.spawnresourcefulbeesbeesmob")
					.define("spawnResourcefulBeesBeesMob", true);

			spawnrateOfResourcefulBeesMobsBrood = builder
					.comment(" \n-----------------------------------------------------\n",
							" Chance of a Bee spawning from Honeycomb Brood Blocks in Bumblezone dimension being replaced by Resourceful Bee's mob.",
							" 0 is no RB's mobs and 1 is max EB's mobs.\n")
					.translation("the_bumblezone.config.spawnrateofresourcefulbeesmobsbrood")
					.defineInRange("spawnrateOfResourcefulBeesMobsBrood", 0.03D, 0D, 1D);

			spawnrateOfResourcefulBeesMobsOther = builder
					.comment(" \n-----------------------------------------------------\n",
							" Chance of a regular Bee spawning in Bumblezone being replaced by Resourceful Bee's mob.",
							" 0 is no RB's mobs and 1 is max RB's mobs.\n")
					.translation("the_bumblezone.config.spawnrateofresourcefulbeesmobsother")
					.defineInRange("spawnrateOfResourcefulBeesMobsOther", 0.008D, 0D, 1D);

			RBOreHoneycombSpawnRateBeeDungeon = builder
					.comment(" \n-----------------------------------------------------\n",
							" How much of Bee Dungeons is made of honeycombs from the_bumblezone:resourcefulbees/spawns_in_bee_dungeons block tag.",
							" 0 is no RB's honeycombs and 1 is max RB's honeycombs.\n")
					.translation("the_bumblezone.config.rborehoneycombspawnratebeedungeon")
					.defineInRange("RBOreHoneycombSpawnRateBeeDungeon", 0.06D, 0D, 1D);

			RBOreHoneycombSpawnRateSpiderBeeDungeon = builder
					.comment(" \n-----------------------------------------------------\n",
							" How much of Spider Infested Bee Dungeons is made of honeycombs from the_bumblezone:resourcefulbees/spawns_in_spider_infested_bee_dungeons block tag.",
							" 0 is no RB's honeycombs and 1 is max RB's honeycombs.\n")
					.translation("the_bumblezone.config.rborehoneycombspawnratespiderbeedungeon")
					.defineInRange("RBOreHoneycombSpawnRateSpiderBeeDungeon", 0.12D, 0D, 1D);

			spawnResourcefulBeesHoneycombVeins = builder
					.comment(" \n-----------------------------------------------------\n",
							" Spawn Resourceful Bees's various honeycomb variants in The Bumblezone at all",
							" kinds of heights and height bands. Start exploring to find where they spawn!",
							" ",
							" NOTE: Will require a restart of the world to take effect. \n")
					.translation("the_bumblezone.config.spawnresourcefulbeeshoneycombveins")
					.define("spawnResourcefulBeesHoneycombVeins", true);

			builder.pop();

			builder.push("Productive Bees Options");

			spawnProductiveBeesBeesMob = builder
					.comment(" \n-----------------------------------------------------\n",
							" Spawn Productive Bees in The Bumblezone and from Honey Brood Blocks alongside",
							" regular bees at a spawnrateOfProductiveBeesMobs chance when spawning regular bees.\n")
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
							" Spawn Productive Bees's various honeycomb variants in The Bumblezone at all",
							" kinds of heights and height bands. Start exploring to find where they spawn!",
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

			PBOreHoneycombSpawnRateBeeDungeon = builder
					.comment(" \n-----------------------------------------------------\n",
							" How much of Bee Dungeons is made of ore-based honeycombs.",
							" 0 is no PB's honeycombs and 1 is max PB's honeycombs.\n")
					.translation("the_bumblezone.config.pborehoneycombspawnratebeedungeon")
					.defineInRange("PBOreHoneycombSpawnRateBeeDungeon", 0.045D, 0D, 1D);

			PBOreHoneycombSpawnRateSpiderBeeDungeon = builder
					.comment(" \n-----------------------------------------------------\n",
							" How much of Spider Infested Bee Dungeons is made of ore-based honeycombs.",
							" 0 is no PB's honeycombs and 1 is max PB's honeycombs.\n")
					.translation("the_bumblezone.config.pborehoneycombspawnratespiderbeedungeon")
					.defineInRange("PBOreHoneycombSpawnRateSpiderBeeDungeon", 0.10D, 0D, 1D);

			builder.pop();

			builder.push("Friends and Foes Options");

			allowFriendsAndFoesBeekeeperTradesCompat = builder
					.comment(" \n-----------------------------------------------------\n",
							" Adds Bumblezone items to Friends and Foes's Beekeeper trades!\n")
					.translation("the_bumblezone.config.allowfriendsandfoesbeekeepertradescompat")
					.define("allowFriendsAndFoesBeekeeperTradesCompat", true);

			builder.pop();

			builder.push("Quark Options");

			injectBzItemsIntoQuarkEnchantmentTooltipsCompat = builder
					.comment(" \n-----------------------------------------------------\n",
							" Adds Bumblezone items symbols to Quark's enchantment tooltips!\n")
					.translation("the_bumblezone.config.injectbzitemsintoquarkenchantmenttooltipscompat")
					.define("injectBzItemsIntoQuarkEnchantmentTooltipsCompat", true);

			builder.pop();

			builder.push("Buzzier Bees Options");

			allowBeeBottleRevivingEmptyBroodBlock = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow Bee Bottle to turn Empty Honeycomb Brood blocks into ",
							" a regular Honeycomb Brood Block with a larva inside! \n")
					.translation("the_bumblezone.config.allowbeebottlerevivingemptybroodblock")
					.define("allowBeeBottleRevivingEmptyBroodBlock", true);

			spawnCrystallizedHoneyInDimension = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow Buzzier Bees's Crystallized honey block to spawn in patches on surfaces in Bumblezone Dimension. ",
							" Requires a game restart to take effect (close game and re-open) \n")
					.translation("the_bumblezone.config.spawncrystallizedhoneyindimension")
					.define("spawnCrystallizedHoneyInDimension", true);

			spawnHoneyTilesInDimension = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow Buzzier Bees's Honey Tile block to spawn mainly in caves inside Bumblezone Dimension. ",
							" Requires a game restart to take effect (close game and re-open) \n")
					.translation("the_bumblezone.config.spawnhoneytilesindimension")
					.define("spawnHoneyTilesInDimension", true);

			builder.pop();


			builder.push("Potion of Bees Options");

			allowPotionOfBeesRevivingEmptyBroodBlock = builder
					.comment(" \n-----------------------------------------------------\n",
							" Allow Potion of Bees to turn Empty Honeycomb Brood blocks into ",
							" a regular Honeycomb Brood Block with a larva inside! \n")
					.translation("the_bumblezone.config.allowpotionofbeesrevivingemptybroodblock")
					.define("allowPotionOfBeesRevivingEmptyBroodBlock", true);

			builder.pop();


			builder.push("Beekeeper Options");

			allowBeekeeperTradesCompat = builder
					.comment(" \n-----------------------------------------------------\n",
							" Adds Bumblezone items to Beekeeper mod's Beekeeper trades!\n")
					.translation("the_bumblezone.config.allowbeekeepertradescompat")
					.define("allowBeekeeperTradesCompat", true);

			builder.pop();

		builder.pop();
	}

	public static void copyToCommon() {
		BzModCompatibilityConfigs.allowBeeBottleRevivingEmptyBroodBlock = allowBeeBottleRevivingEmptyBroodBlock.get();
		BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock = allowPotionOfBeesRevivingEmptyBroodBlock.get();
		BzModCompatibilityConfigs.allowBeekeeperTradesCompat = allowBeekeeperTradesCompat.get();
		BzModCompatibilityConfigs.allowFriendsAndFoesBeekeeperTradesCompat = allowFriendsAndFoesBeekeeperTradesCompat.get();
		BzModCompatibilityConfigs.injectBzItemsIntoQuarkEnchantmentTooltipsCompat = injectBzItemsIntoQuarkEnchantmentTooltipsCompat.get();
		BzModCompatibilityConfigs.allowHoneyTreatCompat = allowHoneyTreatCompat.get();
		BzModCompatibilityConfigs.PBOreHoneycombSpawnRateBeeDungeon = PBOreHoneycombSpawnRateBeeDungeon.get();
		BzModCompatibilityConfigs.PBOreHoneycombSpawnRateSpiderBeeDungeon = PBOreHoneycombSpawnRateSpiderBeeDungeon.get();
		BzModCompatibilityConfigs.spawnCrystallizedHoneyInDimension = spawnCrystallizedHoneyInDimension.get();
		BzModCompatibilityConfigs.spawnHoneyTilesInDimension = spawnHoneyTilesInDimension.get();
	}
}