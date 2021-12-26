package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.common.block.ExpansionBox;
import cy.jdkdigital.productivebees.common.entity.bee.ConfigurableBee;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.init.ModEntities;
import cy.jdkdigital.productivebees.setup.BeeReloadListener;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProductiveBeesCompat {

	private static final Lazy<List<String>> SPIDER_DUNGEON_HONEYCOMBS = Lazy.of(() ->
		BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(e -> {
			CompoundTag tag = e.getValue();
			int primary = tag.getInt("primaryColor");
			return BzModCompatibilityConfigs.allowedCombsForDungeons.get().contains(e.getKey()) &&
					tag.getBoolean("createComb") &&
					(colorsAreClose(new Color(106, 127, 0), new Color(primary), 150) ||
					colorsAreClose(new Color(129, 198, 0), new Color(primary), 150) ||
					colorsAreClose(new Color(34, 45, 0), new Color(primary), 150));
		}).map(Map.Entry::getKey).toList());

	private static final Lazy<List<String>> BEE_DUNGEON_HONEYCOMBS = Lazy.of(() ->
		BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(e -> {
			CompoundTag tag = e.getValue();
			return BzModCompatibilityConfigs.allowedCombsForDungeons.get().contains(e.getKey()) &&
					tag.getBoolean("createComb") &&
					!SPIDER_DUNGEON_HONEYCOMBS.get().contains(e.getKey());
		}).map(Map.Entry::getKey).toList());

	private static final Lazy<List<String>> ORE_HONEYCOMBS = Lazy.of(() ->
		BeeReloadListener.INSTANCE.getData().entrySet().stream().filter(
			e -> BzModCompatibilityConfigs.allowedCombsAsOres.get().contains(e.getKey()) &&
				 e.getValue().getBoolean("createComb")
		).map(Map.Entry::getKey).toList());

	private static final Lazy<List<String>> ALL_BEES = Lazy.of(() -> BeeReloadListener.INSTANCE.getData().keySet().stream().filter(e -> BzModCompatibilityConfigs.allowedBees.get().contains(e)).toList());

	private static PlacedFeature PB_PLACEDFEATURE;

	public static void setupProductiveBees() {

		// feature to add to biomes
		ConfiguredFeature<?, ?> PB_CONFIGUREFEATURE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
			new ResourceLocation(Bumblezone.MODID, "productivebees_be_comb_feature"),
			BzFeatures.BLOCKENTITY_COMBS_FEATURE.get().configured(
				new OreConfiguration(
						new TagMatchTest(BzBlockTags.HONEYCOMBS_THAT_FEATURES_CAN_CARVE),
						ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(),
						16
				)
			)
		);

		PB_PLACEDFEATURE = Registry.register(BuiltinRegistries.PLACED_FEATURE,
			new ResourceLocation(Bumblezone.MODID, "productivebees_be_comb_feature"),
			PB_CONFIGUREFEATURE.placed(
				RarityFilter.onAverageOnceEvery(2),
				InSquarePlacement.spread(),
				HeightRangePlacement.uniform(
						VerticalAnchor.aboveBottom(10),
						VerticalAnchor.belowTop(10)),
				BiomeFilter.biome()
			)
		);

		if(BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get()) {
			MinecraftForge.EVENT_BUS.addListener(ProductiveBeesCompat::PBAddWorldgen);
		}

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.productiveBeesPresent = true;
	}

	private static boolean colorsAreClose(Color a, Color z, int threshold) {
		int r = a.getRed() - z.getRed();
		int g = a.getGreen() - z.getGreen();
		int b = a.getBlue() - z.getBlue();
		return (r*r + g*g + b*b) <= threshold*threshold;
	}

	public static void PBAddWorldgen(final BiomeLoadingEvent event) {
		if(ModChecker.productiveBeesPresent && event.getName().getNamespace().equals(Bumblezone.MODID)) {
			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PB_PLACEDFEATURE);
		}
	}

	/**
	 * Is block is a ProductiveBees nest or beenest block
	 */
	public static boolean PBIsExpandedBeehiveBlock(BlockState block) {

		if (block.getBlock() instanceof ExpansionBox && block.getValue(AdvancedBeehive.EXPANDED) != VerticalHive.NONE) {
			return true; // expansion boxes only count as beenest when they expand a hive.
		}
		else if(BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation("productivebees", "solitary_overworld_nests")).contains(block.getBlock())){
			// Solitary nests are technically AdvancedBeehiveAbstract and will pass the next check.
			// But this is still done in case they do change that in the future to extend something else or something.
			return true;
		}
		else {
			return block.getBlock() instanceof AdvancedBeehiveAbstract; // all other nests/hives we somehow missed here so return true
		}
	}

	/**
	 * 1/15th of bees spawning will also spawn Productive Bees' bees
	 */
	public static boolean PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
		if (ALL_BEES.get().size() == 0) {
			return false;
		}

		Mob entity = (Mob) event.getEntity();
        LevelAccessor world = event.getWorld();

		// randomly pick a productive bee (the nbt determines the bee)
		ConfigurableBee productiveBeeEntity = ModEntities.CONFIGURABLE_BEE.get().create(entity.level);
		if(productiveBeeEntity == null) return false;

		BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
		productiveBeeEntity.moveTo(
				blockpos.getX() + 0.5f,
				blockpos.getY() + 0.5f,
				blockpos.getZ() + 0.5f,
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		productiveBeeEntity.setBaby(isChild);

		CompoundTag newTag = new CompoundTag();
		newTag.putString("type", ALL_BEES.get().get(world.getRandom().nextInt(ALL_BEES.get().size())));
		productiveBeeEntity.finalizeSpawn(
				(ServerLevelAccessor)world,
				world.getCurrentDifficultyAt(productiveBeeEntity.blockPosition()),
				event.getSpawnReason(),
				null,
				newTag);
		productiveBeeEntity.setBeeType(newTag.getString("type"));

		world.addFreshEntity(productiveBeeEntity);
		return true;
	}

	/**
	 * Returns a random comb type to use
	 */
	public static String PBGetRandomCombType(Random random) {
		if(!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get() || ORE_HONEYCOMBS.get().size() == 0) {
			return null;
		}
		return ORE_HONEYCOMBS.get().get(random.nextInt(ORE_HONEYCOMBS.get().size()));
	}

	/**
	 * Safely get Rottened Honeycomb. If Rottened Honeycomb wasn't found, return
	 * Vanilla's Honeycomb
	 */
	public static StructureTemplate.StructureBlockInfo PBGetRottenedHoneycomb(BlockPos worldPos, Random random) {
		if(!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get() || SPIDER_DUNGEON_HONEYCOMBS.get().size() == 0) {
			return null;
		}
		else {
			CompoundTag newTag = new CompoundTag();
			newTag.putString("type", SPIDER_DUNGEON_HONEYCOMBS.get().get(random.nextInt(SPIDER_DUNGEON_HONEYCOMBS.get().size())));
			return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
		}
	}

	/**
	 * Picks a random Productive Bees Honeycomb with lower index of
	 * ORE_BASED_HONEYCOMB_VARIANTS list being highly common
	 */
	public static StructureTemplate.StructureBlockInfo PBGetRandomHoneycomb(BlockPos worldPos, Random random) {
		if (!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get() || BEE_DUNGEON_HONEYCOMBS.get().size() == 0) {
			return null;
		}
		else {
			CompoundTag newTag = new CompoundTag();
			newTag.putString("type", BEE_DUNGEON_HONEYCOMBS.get().get(random.nextInt(BEE_DUNGEON_HONEYCOMBS.get().size())));
			return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
		}
	}
}
