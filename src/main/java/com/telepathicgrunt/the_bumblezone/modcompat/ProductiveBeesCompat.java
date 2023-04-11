package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.common.block.ConfigurableCombBlock;
import cy.jdkdigital.productivebees.common.block.ExpansionBox;
import cy.jdkdigital.productivebees.common.block.entity.CombBlockBlockEntity;
import cy.jdkdigital.productivebees.common.entity.bee.ConfigurableBee;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.init.ModEntities;
import cy.jdkdigital.productivebees.setup.BeeReloadListener;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;

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

	public static final TagKey<Block> SOLITARY_OVERWORLD_NESTS_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("productivebees", "solitary_overworld_nests"));

	protected static Item BEE_CAGE;
	protected static Item STURDY_BEE_CAGE;

	public static void setupCompat() {

		BEE_CAGE = Registry.ITEM.get(new ResourceLocation("productivebees", "bee_cage"));
		STURDY_BEE_CAGE = Registry.ITEM.get(new ResourceLocation("productivebees", "sturdy_bee_cage"));

		if (BEE_CAGE != Items.AIR && BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock.get()) {
			ProductiveBeesDispenseBehavior.DEFAULT_BEE_CAGED_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(BEE_CAGE));
			DispenserBlock.registerBehavior(BEE_CAGE, new ProductiveBeesDispenseBehavior()); // adds compatibility with caged bee in dispensers
		}

		if (STURDY_BEE_CAGE != Items.AIR && BzModCompatibilityConfigs.allowProductiveBeesBeeCageRevivingEmptyBroodBlock.get()) {
			ProductiveBeesDispenseBehavior.DEFAULT_STURDY_BEE_CAGED_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(STURDY_BEE_CAGE));
			DispenserBlock.registerBehavior(STURDY_BEE_CAGE, new ProductiveBeesDispenseBehavior()); // adds compatibility with caged bee in dispensers
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
	/**
	 * Is block is a ProductiveBees nest or beenest block
	 */
	public static boolean PBIsExpandedBeehiveBlock(BlockState block) {

		if (block.getBlock() instanceof ExpansionBox && block.getValue(AdvancedBeehive.EXPANDED) != VerticalHive.NONE) {
			return true; // expansion boxes only count as beenest when they expand a hive.
		}
		else if(block.is(SOLITARY_OVERWORLD_NESTS_TAG)){
			// Solitary nests are technically AdvancedBeehiveAbstract and will pass the next check.
			// But this is still done in case they do change that in the future to extend something else or something.
			return true;
		}
		else {
			return block.getBlock() instanceof AdvancedBeehiveAbstract; // all other nests/hives we somehow missed here so return true
		}
	}

	public static boolean PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
		if (ALL_BEES.get().size() == 0 || (event.getSpawnReason() == MobSpawnType.DISPENSER && !BzModCompatibilityConfigs.allowProductiveBeesSpawnFromDispenserFedBroodBlock.get())) {
			return false;
		}

		Mob entity = event.getEntity();
        LevelAccessor world = event.getLevel();

		// randomly pick a productive bee (the nbt determines the bee)
		ConfigurableBee productiveBeeEntity = ModEntities.CONFIGURABLE_BEE.get().create(entity.level);
		if(productiveBeeEntity == null) return false;

		BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
		productiveBeeEntity.moveTo(
				blockpos.getX() + 0.5f,
				blockpos.getY() + 0.5f,
				blockpos.getZ() + 0.5f,
				productiveBeeEntity.getRandom().nextFloat() * 360.0F,
				0.0F);

		productiveBeeEntity.setBaby(isChild);

		CompoundTag newTag = new CompoundTag();
		newTag.putString("type", ALL_BEES.get().get(productiveBeeEntity.getRandom().nextInt(ALL_BEES.get().size())));
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

	public static boolean PBIsConfigurableComb(Block block) {
		return block instanceof ConfigurableCombBlock;
	}

	public static void placeConfigurableCombBlockEntity(BlockPos.MutableBlockPos blockposMutable, ChunkAccess cachedChunk, String nbt, OreConfiguration.TargetBlockState targetBlockState, Block combBlock) {
		if(nbt != null) {
			cachedChunk.setBlockState(blockposMutable, targetBlockState.state, false);
			CombBlockBlockEntity be = (CombBlockBlockEntity)((ConfigurableCombBlock)combBlock).newBlockEntity(blockposMutable, targetBlockState.state);
			be.setType(nbt);
			cachedChunk.setBlockEntity(be);
		}
	}

	/**
	 * Returns a random comb type to use
	 */
	public static String PBGetRandomCombType(RandomSource random) {
		if(!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get() || ORE_HONEYCOMBS.get().size() == 0) {
			return null;
		}
		return ORE_HONEYCOMBS.get().get(random.nextInt(ORE_HONEYCOMBS.get().size()));
	}

	/**
	 * Safely get Rottened Honeycomb. If Rottened Honeycomb wasn't found, return
	 * Vanilla's Honeycomb
	 */
	public static StructureTemplate.StructureBlockInfo PBGetRottenedHoneycomb(BlockPos worldPos, RandomSource random) {
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
	public static StructureTemplate.StructureBlockInfo PBGetRandomHoneycomb(BlockPos worldPos, RandomSource random) {
		if (!BzModCompatibilityConfigs.spawnProductiveBeesHoneycombVariants.get() || BEE_DUNGEON_HONEYCOMBS.get().size() == 0) {
			return null;
		}
		else {
			CompoundTag newTag = new CompoundTag();
			newTag.putString("type", BEE_DUNGEON_HONEYCOMBS.get().get(random.nextInt(BEE_DUNGEON_HONEYCOMBS.get().size())));
			return new StructureTemplate.StructureBlockInfo(worldPos, ModBlocks.CONFIGURABLE_COMB.get().defaultBlockState(), newTag);
		}
	}

	public static boolean isFilledBeeCageItem(ItemStack stack) {
		return !stack.isEmpty() && (stack.is(BEE_CAGE) || stack.is(STURDY_BEE_CAGE)) && !stack.isEmpty() && stack.hasTag() && stack.getOrCreateTag().contains("entity");
	}

	public static boolean isFilledBabyBeeCageItem(ItemStack stack) {
		return isFilledBeeCageItem(stack) && stack.getOrCreateTag().getInt("Age") < 0;
	}

	public static InteractionResult beeCageInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
		if (isFilledBeeCageItem(itemstack)) {
			if (!playerEntity.isCrouching()) {
				GeneralUtils.givePlayerItem(
						playerEntity,
						playerHand,
						itemstack.is(ProductiveBeesCompat.STURDY_BEE_CAGE) ? ProductiveBeesCompat.STURDY_BEE_CAGE.getDefaultInstance() : ProductiveBeesCompat.BEE_CAGE.getDefaultInstance(),
						true,
						true);

				return isFilledBabyBeeCageItem(itemstack) ? InteractionResult.CONSUME_PARTIAL : InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.FAIL;
	}
}
