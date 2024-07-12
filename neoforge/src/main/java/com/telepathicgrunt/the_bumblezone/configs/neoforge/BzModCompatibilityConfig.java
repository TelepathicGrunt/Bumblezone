package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.stream.Collectors;

public class BzModCompatibilityConfig {
	public static final ModConfigSpec GENERAL_SPEC;

	public static ModConfigSpec.ConfigValue<String> alternativeFluidToReplaceHoneyFluid;

	public static ModConfigSpec.BooleanValue spawnPokecubeBeePokemon;
	public static ModConfigSpec.BooleanValue beePokemonGetsProtectionEffect;
	public static ModConfigSpec.DoubleValue spawnrateOfPokecubeBeePokemon;

	public static ModConfigSpec.BooleanValue spawnTropicraftBeesMob;
	public static ModConfigSpec.DoubleValue spawnrateOfTropicraftBeesMobs;
	public static ModConfigSpec.BooleanValue allowTropicraftSpawnFromDispenserFedBroodBlock;

	public static ModConfigSpec.BooleanValue spawnResourcefulBeesBeesMob;
	public static ModConfigSpec.DoubleValue spawnrateOfResourcefulBeesMobsBrood;
	public static ModConfigSpec.DoubleValue spawnrateOfResourcefulBeesMobsOther;
	public static ModConfigSpec.DoubleValue RBOreHoneycombSpawnRateBeeDungeon;
	public static ModConfigSpec.DoubleValue RBOreHoneycombSpawnRateSpiderBeeDungeon;
	public static ModConfigSpec.BooleanValue spawnResourcefulBeesHoneycombVeins;
	public static ModConfigSpec.BooleanValue allowResourcefulBeesBeeJarRevivingEmptyBroodBlock;
	public static ModConfigSpec.BooleanValue allowResourcefulBeesSpawnFromDispenserFedBroodBlock;

	public static ModConfigSpec.BooleanValue spawnProductiveBeesBeesMob;
	public static ModConfigSpec.DoubleValue spawnrateOfProductiveBeesMobs;
	public static ModConfigSpec.BooleanValue allowHoneyTreatCompat;
	public static ModConfigSpec.BooleanValue allowProductiveBeesBeeCageRevivingEmptyBroodBlock;
	public static ModConfigSpec.BooleanValue allowProductiveBeesSpawnFromDispenserFedBroodBlock;
	public static ModConfigSpec.BooleanValue spawnProductiveBeesHoneycombVariants;
	public static ModConfigSpec.DoubleValue PBOreHoneycombSpawnRateBeeDungeon;
	public static ModConfigSpec.DoubleValue PBOreHoneycombSpawnRateSpiderBeeDungeon;
	public static ModConfigSpec.ConfigValue<List<? extends String>> allowedCombsForDungeons;
	public static ModConfigSpec.ConfigValue<List<? extends String>> allowedBees;

	public static ModConfigSpec.BooleanValue allowFriendsAndFoesBeekeeperTradesCompat;
	public static ModConfigSpec.BooleanValue injectBzItemsIntoQuarkEnchantmentTooltipsCompat;

	public static ModConfigSpec.BooleanValue allowBeeBottleRevivingEmptyBroodBlock;

	public static ModConfigSpec.BooleanValue allowBeeBucketRevivingEmptyBroodBlock;

	public static ModConfigSpec.BooleanValue allowPotionOfBeesRevivingEmptyBroodBlock;

	public static ModConfigSpec.BooleanValue allowGoodallBottledBeesRevivingEmptyBroodBlock;

	public static ModConfigSpec.BooleanValue allowBeekeeperTradesCompat;

	public static ModConfigSpec.BooleanValue allowLootrCompat;

	static {
		ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
		setupConfig(configBuilder);
		GENERAL_SPEC = configBuilder.build();
	}

	private static void setupConfig(ModConfigSpec.Builder builder) {
		builder.translation("the_bumblezone.configuration.misccompatoptions").push("Misc Compat Options");

		alternativeFluidToReplaceHoneyFluid = builder
				.comment("----------------------------\n",
						" The fluid to replace Bumblezone's Honey Fluid in the dimension. Requires game restart.",
						" Note, this will not replace already placed Bumblezone Honey Fluid.\n")
				.translation("the_bumblezone.configuration.alternativefluidtoreplacehoneyfluid")
				.define("alternativeFluidToReplaceHoneyFluid", "");

		builder.pop();

		builder.translation("the_bumblezone.configuration.pokecubecompat").push("Pokecube Compat");

		spawnPokecubeBeePokemon = builder
				.comment("----------------------------\n",
						" Spawn Pokecube's bee-like pokemon in The Bumblezone and from Honey Brood Blocks.\n")
				.translation("the_bumblezone.configuration.spawnpokecubebeepokemon")
				.define("spawnPokecubeBeePokemon", true);

		beePokemonGetsProtectionEffect = builder
				.comment("----------------------------\n",
						" Pokecube's bee-like pokemon that spawn in The Bumblezone will get",
						" Protection of the Hive effect. Attacking these pokemon in bumblezone will",
						" give you Wrath of the Hive effect and swarmed of angry bees.\n")
				.translation("the_bumblezone.configuration.beepokemongetsprotectioneffect")
				.define("beePokemonGetsProtectionEffect", true);

		spawnrateOfPokecubeBeePokemon = builder
				.comment("----------------------------\n",
						" Chance of a Bee spawning from Honeycomb Brood Blocks being replaced by Pokecube's bee Pokemon.",
						" 0 is no Pokemon mobs and 1 is max Pokemon mobs.\n")
				.translation("the_bumblezone.configuration.spawnrateofpokecubebeepokemon")
				.defineInRange("spawnrateOfPokecubeBeePokemon", 0.05D, 0D, 1D);

		builder.pop();

		builder.translation("the_bumblezone.configuration.tropicraftcompat").push("Tropicraft Compat");

		spawnTropicraftBeesMob = builder
				.comment("----------------------------\n",
						" Spawn Tropicraft's Tropibee in The Bumblezone and from Honey Brood Blocks alongside",
						" regular bees at a spawnrateOfTropicraftBeesMobs chance when spawning regular bees.\n")
				.translation("the_bumblezone.configuration.spawntropicraftbeesmob")
				.define("spawnTropicraftBeesMob", true);

		spawnrateOfTropicraftBeesMobs = builder
				.comment("----------------------------\n",
						" Chance of a Bee spawning from Honeycomb Brood Blocks in Bumblezone dimension being replaced by Tropicraft's Tropibee.",
						" 0 is no Tropicraft's mobs and 1 is max Tropicraft's mobs.\n")
				.translation("the_bumblezone.configuration.spawnrateoftropicraftbeesmobs")
				.defineInRange("spawnrateOfTropicraftBeesMobs", 0.025D, 0D, 1D);

		allowTropicraftSpawnFromDispenserFedBroodBlock = builder
				.comment("----------------------------\n",
						" Allow Honeycomb Brood blocks fed by Dispenser to be able to have chance of spawning Tropicraft's Tropibee.\n")
				.translation("the_bumblezone.configuration.allowtropicraftspawnfromdispenserfedbroodblock")
				.define("allowTropicraftSpawnFromDispenserFedBroodBlock", true);

		builder.pop();

		builder.translation("the_bumblezone.configuration.resourcefulbeescompat").push("Resourceful Bees Compat");

		spawnResourcefulBeesBeesMob = builder
				.comment("----------------------------\n",
						" Spawn Resourceful Bees in The Bumblezone and from Honey Brood Blocks alongside",
						" regular bees at a spawnrateOfResourcefulBeesMobs chance.",
						" You can datapack `the_bumblezone:resourcefulbees/spawnable_from_brood_block` entity tag",
						" and/or `the_bumblezone:resourcefulbees/spawnable_from_chunk_creation` entity tag",
						" for more control of what kinds of bees spawns.\n")
				.translation("the_bumblezone.configuration.spawnresourcefulbeesbeesmob")
				.define("spawnResourcefulBeesBeesMob", true);

		spawnrateOfResourcefulBeesMobsBrood = builder
				.comment("----------------------------\n",
						" Chance of a Bee spawning from Honeycomb Brood Blocks in Bumblezone dimension being replaced by Resourceful Bee's mob.",
						" 0 is no Resourceful Bees mobs and 1 is max Resourceful Bees mobs.\n")
				.translation("the_bumblezone.configuration.spawnrateofresourcefulbeesmobsbrood")
				.defineInRange("spawnrateOfResourcefulBeesMobsBrood", 0.03D, 0D, 1D);

		spawnrateOfResourcefulBeesMobsOther = builder
				.comment("----------------------------\n",
						" Chance of a regular Bee spawning in Bumblezone being replaced by Resourceful Bee's mob.",
						" 0 is no Resourceful Bees mobs and 1 is max Resourceful Bees mobs.\n")
				.translation("the_bumblezone.configuration.spawnrateofresourcefulbeesmobsother")
				.defineInRange("spawnrateOfResourcefulBeesMobsOther", 0.008D, 0D, 1D);

		RBOreHoneycombSpawnRateBeeDungeon = builder
				.comment("----------------------------\n",
						" How much of Bee Dungeons is made of honeycombs from `the_bumblezone:resourcefulbees/spawns_in_bee_dungeons` block tag.",
						" 0 is no Resourceful Bees honeycombs and 1 is max Resourceful Bees honeycombs.\n")
				.translation("the_bumblezone.configuration.rborehoneycombspawnratebeedungeon")
				.defineInRange("RBOreHoneycombSpawnRateBeeDungeon", 0.06D, 0D, 1D);

		RBOreHoneycombSpawnRateSpiderBeeDungeon = builder
				.comment("----------------------------\n",
						" How much of Spider Infested Bee Dungeons is made of honeycombs from `the_bumblezone:resourcefulbees/spawns_in_spider_infested_bee_dungeons` block tag.",
						" 0 is no Resourceful Bees honeycombs and 1 is max Resourceful Bees honeycombs.\n")
				.translation("the_bumblezone.configuration.rborehoneycombspawnratespiderbeedungeon")
				.defineInRange("RBOreHoneycombSpawnRateSpiderBeeDungeon", 0.12D, 0D, 1D);

		spawnResourcefulBeesHoneycombVeins = builder
				.comment("----------------------------\n",
						" Spawn Resourceful Bees's various honeycomb variants in The Bumblezone at all",
						" kinds of heights and height bands. Start exploring to find where they spawn!",
						" ",
						" NOTE: Will require a restart of the world to take effect.\n")
				.translation("the_bumblezone.configuration.spawnresourcefulbeeshoneycombveins")
				.define("spawnResourcefulBeesHoneycombVeins", true);


		allowResourcefulBeesBeeJarRevivingEmptyBroodBlock = builder
			.comment("----------------------------\n",
					" Allow Bee Jars with bees inside to turn Empty Honeycomb Brood blocks into",
					" a regular Honeycomb Brood Block with a larva inside!\n")
			.translation("the_bumblezone.configuration.allowresourcefulbeesbeejarrevivingemptybroodblock")
			.define("allowResourcefulBeesBeeJarRevivingEmptyBroodBlock", true);

		allowResourcefulBeesSpawnFromDispenserFedBroodBlock = builder
			.comment("----------------------------\n",
					" Allow Honeycomb Brood blocks fed by Dispenser to be able to have chance of spawning Resourceful Bees's bees.\n")
			.translation("the_bumblezone.configuration.allowresourcefulbeesspawnfromdispenserfedbroodblock")
			.define("allowResourcefulBeesSpawnFromDispenserFedBroodBlock", true);

		builder.pop();

		builder.translation("the_bumblezone.configuration.productivebeescompat").push("Productive Bees Compat");

		spawnProductiveBeesBeesMob = builder
				.comment("----------------------------\n",
						" Spawn Productive Bees in The Bumblezone and from Honey Brood Blocks alongside",
						" regular bees at a spawnrateOfProductiveBeesMobs chance when spawning regular bees.\n")
				.translation("the_bumblezone.configuration.spawnproductivebeesbeesmob")
				.define("spawnProductiveBeesBeesMob", true);

		allowedBees = builder
				.comment("----------------------------\n",
						" Allow what Productive Bees bees should be able to spawn in Bumblezone.\n")
				.translation("the_bumblezone.configuration.allowedbees")
				.defineList("allowedBees",
						List.of("productivebees:iron",
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
						), () -> "productivebees:", (t) -> true);

		spawnrateOfProductiveBeesMobs = builder
				.comment("----------------------------\n",
						" Chance of a Bee spawning in Bumblezone or from Honeycomb Brood Blocks being replaced by Productive Bees mob.",
						" 0 is no Productive Bees mobs and 1 is max Productive Bees mobs.\n")
				.translation("the_bumblezone.configuration.spawnrateofproductivebeesmobs")
				.defineInRange("spawnrateOfProductiveBeesMobs", 0.03D, 0D, 1D);

		spawnProductiveBeesHoneycombVariants = builder
				.comment("----------------------------\n",
						" Spawn Productive Bees various honeycomb variants in The Bumblezone at all",
						" kinds of heights and height bands. Start exploring to find where they spawn!",
						" Disabling this config will make all Productive Bees comb blocks not spawn in Bumblezone dimension.",
						" ",
						" To add or remove specific combs from spawning, datapack replace this placed feature tag file:",
						"`data/the_bumblezone/tags/worldgen/placed_feature/productive_bees_combs.json`",
						" ",
						" NOTE: This config will require a restart of the world to take effect.\n")
				.translation("the_bumblezone.configuration.spawnproductivebeeshoneycombvariants")
				.define("spawnProductiveBeesHoneycombVariants", true);

		allowedCombsForDungeons = builder
				.comment("----------------------------\n",
						" Allow what Productive Bees combs should be able to spawn in Bumblezone dungeons.")
				.translation("the_bumblezone.configuration.allowedcombsfordungeons")
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
						),  () -> "productivebees:", (t) -> true);

		allowProductiveBeesBeeCageRevivingEmptyBroodBlock = builder
			.comment("----------------------------\n",
					" Allow Bee Cages with bees inside to turn Empty Honeycomb Brood blocks into",
					" a regular Honeycomb Brood Block with a larva inside!\n")
			.translation("the_bumblezone.configuration.allowproductivebeesbeecagerevivingemptybroodblock")
			.define("allowProductiveBeesBeeCageRevivingEmptyBroodBlock", true);

		allowProductiveBeesSpawnFromDispenserFedBroodBlock = builder
			.comment("----------------------------\n",
					" Allow Honeycomb Brood blocks fed by Dispenser to be able to have chance of spawning Productive Bees bees.\n")
			.translation("the_bumblezone.configuration.allowproductivebeesspawnfromdispenserfedbroodblock")
			.define("allowProductiveBeesSpawnFromDispenserFedBroodBlock", true);

		allowHoneyTreatCompat = builder
				.comment("----------------------------\n",
						" Allow Honey Treat to be able to feed bees and Honeycomb Brood Blocks.\n")
				.translation("the_bumblezone.configuration.allowhoneytreatcompat")
				.define("allowHoneyTreatCompat", true);

		PBOreHoneycombSpawnRateBeeDungeon = builder
				.comment("----------------------------\n",
						" How much of Bee Dungeons is made of ore-based honeycombs.",
						" 0 is no Productive Bees honeycombs and 1 is max Productive Bees honeycombs.\n")
				.translation("the_bumblezone.configuration.pborehoneycombspawnratebeedungeon")
				.defineInRange("PBOreHoneycombSpawnRateBeeDungeon", 0.125D, 0D, 1D);

		PBOreHoneycombSpawnRateSpiderBeeDungeon = builder
				.comment("----------------------------\n",
						" How much of Spider Infested Bee Dungeons is made of ore-based honeycombs.",
						" 0 is no Productive Bees honeycombs and 1 is max Productive Bees honeycombs.\n")
				.translation("the_bumblezone.configuration.pborehoneycombspawnratespiderbeedungeon")
				.defineInRange("PBOreHoneycombSpawnRateSpiderBeeDungeon", 0.25D, 0D, 1D);

		builder.pop();

		builder.translation("the_bumblezone.configuration.friendsandfoescompat").push("Friends and Foes Compat");

		allowFriendsAndFoesBeekeeperTradesCompat = builder
				.comment("----------------------------\n",
						" Adds Bumblezone items to Friends and Foes's Beekeeper trades!\n")
				.translation("the_bumblezone.configuration.allowfriendsandfoesbeekeepertradescompat")
				.define("allowFriendsAndFoesBeekeeperTradesCompat", true);

		builder.pop();

		builder.translation("the_bumblezone.configuration.quarkcompat").push("Quark Compat");

		injectBzItemsIntoQuarkEnchantmentTooltipsCompat = builder
				.comment("----------------------------\n",
						" Adds Bumblezone items symbols to Quark's enchantment tooltips!\n")
				.translation("the_bumblezone.configuration.injectbzitemsintoquarkenchantmenttooltipscompat")
				.define("injectBzItemsIntoQuarkEnchantmentTooltipsCompat", true);

		builder.pop();

		builder.translation("the_bumblezone.configuration.buzzierbeescompat").push("Buzzier Bees Compat");

		allowBeeBottleRevivingEmptyBroodBlock = builder
				.comment("----------------------------\n",
						" Allow Bee Bottle to turn Empty Honeycomb Brood blocks into a regular Honeycomb Brood Block with a larva inside!\n")
				.translation("the_bumblezone.configuration.allowbeebottlerevivingemptybroodblock")
				.define("allowBeeBottleRevivingEmptyBroodBlock", true);

		builder.pop();

		builder.translation("the_bumblezone.configuration.forbiddenarcanuscompat").push("Forbidden Arcanus Compat");

		allowBeeBucketRevivingEmptyBroodBlock = builder
				.comment("----------------------------\n",
						" Allow Bee Bucket to turn Empty Honeycomb Brood blocks into a regular Honeycomb Brood Block with a larva inside!\n")
				.translation("the_bumblezone.configuration.allowbeebucketrevivingemptybroodblock")
				.define("allowBeeBucketRevivingEmptyBroodBlock", true);

		builder.pop();


		builder.translation("the_bumblezone.configuration.potionofbeescompat").push("Potion of Bees Compat");

		allowPotionOfBeesRevivingEmptyBroodBlock = builder
				.comment("----------------------------\n",
						" Allow Potion of Bees to turn Empty Honeycomb Brood blocks into ",
						" a regular Honeycomb Brood Block with a larva inside! \n")
				.translation("the_bumblezone.configuration.allowpotionofbeesrevivingemptybroodblock")
				.define("allowPotionOfBeesRevivingEmptyBroodBlock", true);

		builder.pop();


		builder.translation("the_bumblezone.configuration.goodallcompat").push("Goodall Compat");

		allowGoodallBottledBeesRevivingEmptyBroodBlock = builder
				.comment("----------------------------\n",
						" Allow Bottled Bees to turn Empty Honeycomb Brood blocks into",
						" a regular Honeycomb Brood Block with a larva inside!\n")
				.translation("the_bumblezone.configuration.allowgoodallbottledbeesrevivingemptybroodblock")
				.define("allowGoodallBottledBeesRevivingEmptyBroodBlock", true);

		builder.pop();


		builder.translation("the_bumblezone.configuration.beekeepercompat").push("Beekeeper Compat");

		allowBeekeeperTradesCompat = builder
				.comment("----------------------------\n",
						" Adds Bumblezone items to Beekeeper mod's Beekeeper trades!\n")
				.translation("the_bumblezone.configuration.allowbeekeepertradescompat")
				.define("allowBeekeeperTradesCompat", true);

		builder.pop();


		builder.translation("the_bumblezone.configuration.lootrcompat").push("Lootr Compat");

		allowLootrCompat = builder
				.comment("----------------------------\n",
						" Allow loot Cocoons to have compat with Lootr\n")
				.translation("the_bumblezone.configuration.allowlootrcompat")
				.define("allowLootrCompat", true);

		builder.pop();
	}

	public static void copyToCommon() {
		BzModCompatibilityConfigs.alternativeFluidToReplaceHoneyFluid = alternativeFluidToReplaceHoneyFluid.get();

		BzModCompatibilityConfigs.allowPotionOfBeesRevivingEmptyBroodBlock = allowPotionOfBeesRevivingEmptyBroodBlock.get();

		BzModCompatibilityConfigs.allowBeekeeperTradesCompat = allowBeekeeperTradesCompat.get();

		BzModCompatibilityConfigs.allowFriendsAndFoesBeekeeperTradesCompat = allowFriendsAndFoesBeekeeperTradesCompat.get();

		BzModCompatibilityConfigs.injectBzItemsIntoQuarkEnchantmentTooltipsCompat = injectBzItemsIntoQuarkEnchantmentTooltipsCompat.get();

		BzModCompatibilityConfigs.spawnTropicraftBeesMob = spawnTropicraftBeesMob.get();
		BzModCompatibilityConfigs.spawnrateOfTropicraftBeesMobs = spawnrateOfTropicraftBeesMobs.get();
		BzModCompatibilityConfigs.allowTropicraftSpawnFromDispenserFedBroodBlock = allowTropicraftSpawnFromDispenserFedBroodBlock.get();

		BzModCompatibilityConfigs.spawnProductiveBeesBeesMob = spawnProductiveBeesBeesMob.get();
		BzModCompatibilityConfigs.spawnrateOfProductiveBeesMobs = spawnrateOfProductiveBeesMobs.get();
		BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants = spawnProductiveBeesHoneycombVariants.get();
		BzModCompatibilityConfigs.allowedCombsForDungeons = allowedCombsForDungeons.get().stream().map(String::toString).collect(Collectors.toList());
		BzModCompatibilityConfigs.allowedBees = allowedBees.get().stream().map(String::toString).collect(Collectors.toList());
		BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock = allowProductiveBeesBeeCageRevivingEmptyBroodBlock.get();
		BzModCompatibilityConfigs.allowProductiveBeesSpawnFromDispenserFedBroodBlock = allowProductiveBeesSpawnFromDispenserFedBroodBlock.get();
		BzModCompatibilityConfigs.allowHoneyTreatCompat = allowHoneyTreatCompat.get();
		BzModCompatibilityConfigs.PBOreHoneycombSpawnRateBeeDungeon = PBOreHoneycombSpawnRateBeeDungeon.get();
		BzModCompatibilityConfigs.PBOreHoneycombSpawnRateSpiderBeeDungeon = PBOreHoneycombSpawnRateSpiderBeeDungeon.get();

		BzModCompatibilityConfigs.allowBeeBottleRevivingEmptyBroodBlock = allowBeeBottleRevivingEmptyBroodBlock.get();

		BzModCompatibilityConfigs.allowBeeBucketRevivingEmptyBroodBlock = allowBeeBucketRevivingEmptyBroodBlock.get();

		BzModCompatibilityConfigs.allowGoodallBottledBeesRevivingEmptyBroodBlock = allowGoodallBottledBeesRevivingEmptyBroodBlock.get();

		BzModCompatibilityConfigs.spawnPokecubeBeePokemon = spawnPokecubeBeePokemon.get();
		BzModCompatibilityConfigs.beePokemonGetsProtectionEffect = beePokemonGetsProtectionEffect.get();
		BzModCompatibilityConfigs.spawnrateOfPokecubeBeePokemon = spawnrateOfPokecubeBeePokemon.get();

		BzModCompatibilityConfigs.spawnResourcefulBeesBeesMob = spawnResourcefulBeesBeesMob.get();
		BzModCompatibilityConfigs.spawnrateOfResourcefulBeesMobsBrood = spawnrateOfResourcefulBeesMobsBrood.get();
		BzModCompatibilityConfigs.spawnrateOfResourcefulBeesMobsOther = spawnrateOfResourcefulBeesMobsOther.get();
		BzModCompatibilityConfigs.RBOreHoneycombSpawnRateBeeDungeon = RBOreHoneycombSpawnRateBeeDungeon.get();
		BzModCompatibilityConfigs.RBOreHoneycombSpawnRateSpiderBeeDungeon = RBOreHoneycombSpawnRateSpiderBeeDungeon.get();
		BzModCompatibilityConfigs.spawnResourcefulBeesHoneycombVeins = spawnResourcefulBeesHoneycombVeins.get();
		BzModCompatibilityConfigs.allowResourcefulBeesBeeJarRevivingEmptyBroodBlock = allowResourcefulBeesBeeJarRevivingEmptyBroodBlock.get();
		BzModCompatibilityConfigs.allowResourcefulBeesSpawnFromDispenserFedBroodBlock = allowResourcefulBeesSpawnFromDispenserFedBroodBlock.get();

		BzModCompatibilityConfigs.allowLootrCompat = allowLootrCompat.get();
	}
}