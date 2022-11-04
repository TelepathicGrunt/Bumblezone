package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.common.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.common.block.ConfigurableCombBlock;
import cy.jdkdigital.productivebees.common.block.ExpansionBox;
import cy.jdkdigital.productivebees.common.block.entity.CombBlockBlockEntity;
import cy.jdkdigital.productivebees.common.entity.bee.ConfigurableBee;
import cy.jdkdigital.productivebees.common.item.BeeNestHelmet;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.init.ModEntities;
import cy.jdkdigital.productivebees.setup.BeeReloadListener;
import cy.jdkdigital.productivebees.state.properties.VerticalHive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	public static void setupProductiveBees() {
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

	/**
	 * 1/15th of bees spawning will also spawn Productive Bees' bees
	 */
	public static boolean PBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild) {
		if (ALL_BEES.get().size() == 0) {
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

	public static boolean runTeleportCodeIfBeeHelmentHitHigh(HitResult hitResult, Projectile pearlEntity) {
		Level world = pearlEntity.level; // world we threw in

		// Make sure we are on server by checking if thrower is ServerPlayer and that we are not in bumblezone.
		// If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
		if (!world.isClientSide() &&
				hitResult instanceof EntityHitResult entityHitResult &&
				hasBeeNestHelmet(entityHitResult.getEntity()) &&
				BzDimensionConfigs.enableEntranceTeleportation.get() &&
				pearlEntity.getOwner() instanceof ServerPlayer playerEntity &&
				!world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
				(!BzDimensionConfigs.onlyOverworldHivesTeleports.get() || world.dimension().equals(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(BzDimensionConfigs.defaultDimension.get())))))
		{
			Vec3 hitPos = pearlEntity.position();
			AABB boundBox = entityHitResult.getEntity().getBoundingBox();
			double minYThreshold = ((boundBox.maxY - boundBox.minY) * 0.66d) + boundBox.minY;

			if (hitPos.y() < minYThreshold) {
				return false;
			}

			BlockPos hivePos = entityHitResult.getEntity().blockPosition();

			//checks if block under hive is correct if config needs one
			boolean validBelowBlock = false;
			Optional<HolderSet.Named<Block>> blockTag = Registry.BLOCK.getTag(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT);
			if (blockTag.isPresent() && blockTag.get().size() != 0) {
				if (world.getBlockState(hivePos.below()).is(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT)) {
					validBelowBlock = true;
				}
				else if (BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive.get()) {
					//failed. Block below isn't the required block
					Bumblezone.LOGGER.log(org.apache.logging.log4j.Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
					net.minecraft.network.chat.Component message = Component.translatable("system.the_bumblezone.require_hive_blocks_failed");
					playerEntity.displayClientMessage(message, true);
					return false;
				}
			}
			else {
				validBelowBlock = true;
			}


			//if the pearl hit a beehive, begin the teleportation.
			if (validBelowBlock) {
				BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(playerEntity);
				BzWorldSavedData.queueEntityToTeleport(playerEntity, BzDimension.BZ_WORLD_KEY);
				pearlEntity.discard();
				return true;
			}
		}

		return false;
	}

	public static boolean hasBeeNestHelmet(Entity entity) {
		for(ItemStack armor : entity.getArmorSlots()) {
			if(armor.getItem() instanceof BeeNestHelmet) {
				return true;
			}
		}
		return false;
	}
}
