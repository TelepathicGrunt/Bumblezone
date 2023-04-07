package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.List;
import java.util.Optional;

public class ResourcefulBeesCompat {

	public static final TagKey<Block> SPAWNS_IN_BEE_DUNGEONS_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawns_in_bee_dungeons"));
	public static final TagKey<Block> SPAWNS_IN_SPIDER_INFESTED_BEE_DUNGEONS_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawns_in_spider_infested_bee_dungeons"));
	public static final TagKey<EntityType<?>> SPAWNABLE_FROM_BROOD_BLOCK_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawnable_from_brood_block"));
	public static final TagKey<EntityType<?>> SPAWNABLE_FROM_CHUNK_CREATION_TAG = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawnable_from_chunk_creation"));

	private static Item BEE_JAR;

	public static void setupCompat() {
		BEE_JAR = Registry.ITEM.get(new ResourceLocation("resourcefulbees", "bee_jar"));

		if (BEE_JAR != Items.AIR && BzModCompatibilityConfigs.allowResourcefulBeesBeeJarRevivingEmptyBroodBlock.get()) {
			ResourcefulBeesDispenseBehavior.DEFAULT_BEE_JAR_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(BEE_JAR));
			DispenserBlock.registerBehavior(BEE_JAR, new ResourcefulBeesDispenseBehavior()); // adds compatibility with bottled bee in dispensers
		}

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.resourcefulBeesPresent = true;
	}

	public static boolean RBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event, boolean isChild, MobSpawnType spawnReason) {
		Mob entity = event.getEntity();
        LevelAccessor world = event.getLevel();

		Registry<EntityType<?>> entityTypes = world.registryAccess().registryOrThrow(Registry.ENTITY_TYPE_REGISTRY);
		Optional<HolderSet.Named<EntityType<?>>> optionalNamed = entityTypes.getTag(
				spawnReason == MobSpawnType.CHUNK_GENERATION ?
						SPAWNABLE_FROM_CHUNK_CREATION_TAG :
						SPAWNABLE_FROM_BROOD_BLOCK_TAG);
		if(optionalNamed.isEmpty()) return false;

		HolderSet.Named<EntityType<?>> holders = optionalNamed.get();
		if (holders.size() == 0) return false;

		EntityType<?> rbBeeType = holders.get(entity.getRandom().nextInt(holders.size())).get();
		Entity rbBeeUnchecked = rbBeeType.create(entity.getLevel());

		if (rbBeeUnchecked instanceof Bee rbBee) {
			BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
			rbBee.moveTo(
					blockpos.getX() + 0.5f,
					blockpos.getY() + 0.5f,
					blockpos.getZ() + 0.5f,
					rbBee.getRandom().nextFloat() * 360.0F,
					0.0F);

			rbBee.setBaby(isChild);

			rbBee.finalizeSpawn(
					(ServerLevelAccessor)world,
					world.getCurrentDifficultyAt(rbBee.blockPosition()),
					event.getSpawnReason(),
					null,
					null);

			world.addFreshEntity(rbBee);
			return true;
		}
		return false;
	}

	public static StructureTemplate.StructureBlockInfo RBGetSpiderHoneycomb(BlockPos worldPos, RandomSource random, LevelReader worldView) {
		return getRandomCombFromTag(worldPos, random, worldView, SPAWNS_IN_SPIDER_INFESTED_BEE_DUNGEONS_TAG);
	}

	public static StructureTemplate.StructureBlockInfo RBGetRandomBeeHoneycomb(BlockPos worldPos, RandomSource random, LevelReader worldView) {
		return getRandomCombFromTag(worldPos, random, worldView, SPAWNS_IN_BEE_DUNGEONS_TAG);
	}

	private static StructureTemplate.StructureBlockInfo getRandomCombFromTag(BlockPos worldPos, RandomSource random, LevelReader worldView, TagKey<Block> spawnsInBeeDungeonsTag) {
		if (worldView instanceof CommonLevelAccessor world) {
			Registry<Block> blockRegistry = world.registryAccess().registryOrThrow(Registry.BLOCK_REGISTRY);
			Optional<HolderSet.Named<Block>> optionalNamed = blockRegistry.getTag(spawnsInBeeDungeonsTag);
			if(optionalNamed.isEmpty()) return null;

			List<Block> holders = GeneralUtils.getListOfNonDummyBlocks(optionalNamed);
			if (holders.size() == 0) return null;

			Block rbComb = holders.get(random.nextInt(random.nextInt(holders.size()) + 1));
			return new StructureTemplate.StructureBlockInfo(worldPos, rbComb.defaultBlockState(), null);
		}
		return null;
	}

	public static boolean isFilledBeeJarItem(ItemStack stack) {
		return stack.is(BEE_JAR) && !stack.isEmpty() && stack.hasTag() && stack.getOrCreateTag().contains("Entity")
				&& stack.getOrCreateTag().getCompound("Entity").contains("id");
	}

	public static InteractionResult beeJarInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
		if (isFilledBeeJarItem(itemstack)) {
			if (!playerEntity.isCrouching()) {
				if (!playerEntity.isCreative()) {
					itemstack.getOrCreateTag().remove("Entity"); // Remove bee.
				}

				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.FAIL;
	}
}
