package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class ResourcefulBeesCompat implements ModCompat {

    public static final TagKey<Block> SPAWNS_IN_BEE_DUNGEONS_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawns_in_bee_dungeons"));
    public static final TagKey<Block> SPAWNS_IN_SPIDER_INFESTED_BEE_DUNGEONS_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawns_in_spider_infested_bee_dungeons"));
    public static final TagKey<EntityType<?>> SPAWNABLE_FROM_BROOD_BLOCK_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawnable_from_brood_block"));
    public static final TagKey<EntityType<?>> SPAWNABLE_FROM_CHUNK_CREATION_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "resourcefulbees/spawnable_from_chunk_creation"));

    private static Optional<Item> BEE_JAR;

    public ResourcefulBeesCompat() {
        BEE_JAR = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("resourcefulbees", "bee_jar"));

        if (BEE_JAR.isPresent() && BzModCompatibilityConfigs.allowResourcefulBeesBeeJarRevivingEmptyBroodBlock) {
            ResourcefulBeesDispenseBehavior.DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetDispenseMethod(new ItemStack(BEE_JAR.get()));
            DispenserBlock.registerBehavior(BEE_JAR.get(), new ResourcefulBeesDispenseBehavior()); // adds compatibility with bottled bee in dispensers
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.resourcefulBeesPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.SPAWNS, Type.COMBS, Type.EMPTY_BROOD);
    }

    @Override
    public boolean onBeeSpawn(EntitySpawnEvent event, boolean isBaby) {
        if (!BzModCompatibilityConfigs.spawnResourcefulBeesBeesMob) return false;
        double spawnRate = event.spawnType() == MobSpawnType.SPAWNER ?
                BzModCompatibilityConfigs.spawnrateOfResourcefulBeesMobsBrood :
                BzModCompatibilityConfigs.spawnrateOfResourcefulBeesMobsOther;
        if (event.entity().getRandom().nextFloat() >= spawnRate) return false;
        Mob entity = event.entity();
        LevelAccessor world = event.level();

        Registry<EntityType<?>> entityTypes = world.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);
        Optional<HolderSet.Named<EntityType<?>>> optionalNamed = entityTypes.getTag(
                event.spawnType() == MobSpawnType.CHUNK_GENERATION ?
                        SPAWNABLE_FROM_CHUNK_CREATION_TAG :
                        SPAWNABLE_FROM_BROOD_BLOCK_TAG);
        if (optionalNamed.isEmpty()) return false;

        HolderSet.Named<EntityType<?>> holders = optionalNamed.get();
        if (holders.size() == 0) return false;

        EntityType<?> rbBeeType = holders.get(entity.getRandom().nextInt(holders.size())).value();
        Entity rbBeeUnchecked = rbBeeType.create(entity.getLevel());

        if (rbBeeUnchecked instanceof Bee rbBee) {
            BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(entity.blockPosition());
            rbBee.moveTo(
                    blockpos.getX() + 0.5f,
                    blockpos.getY() + 0.5f,
                    blockpos.getZ() + 0.5f,
                    rbBee.getRandom().nextFloat() * 360.0F,
                    0.0F);

            rbBee.setBaby(isBaby);

            rbBee.finalizeSpawn(
                    (ServerLevelAccessor) world,
                    world.getCurrentDifficultyAt(rbBee.blockPosition()),
                    event.spawnType(),
                    null,
                    null);

            world.addFreshEntity(rbBee);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCombSpawn(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
        if (spiderDungeon) {
            return random.nextFloat() < BzModCompatibilityConfigs.RBOreHoneycombSpawnRateSpiderBeeDungeon;
        }
        return random.nextFloat() < BzModCompatibilityConfigs.RBOreHoneycombSpawnRateBeeDungeon;
    }

    @Override
    public StructureTemplate.StructureBlockInfo getHoneycomb(BlockPos pos, RandomSource random, LevelReader level, boolean spiderDungeon) {
        if (spiderDungeon) {
            return getRandomCombFromTag(pos, random, level, SPAWNS_IN_SPIDER_INFESTED_BEE_DUNGEONS_TAG);
        }
        return getRandomCombFromTag(pos, random, level, SPAWNS_IN_BEE_DUNGEONS_TAG);
    }

    private static StructureTemplate.StructureBlockInfo getRandomCombFromTag(BlockPos worldPos, RandomSource random, LevelReader worldView, TagKey<Block> spawnsInBeeDungeonsTag) {
        if (worldView instanceof CommonLevelAccessor world) {
            Registry<Block> blockRegistry = world.registryAccess().registryOrThrow(Registries.BLOCK);
            Optional<HolderSet.Named<Block>> optionalNamed = blockRegistry.getTag(spawnsInBeeDungeonsTag);
            if (optionalNamed.isEmpty()) return null;

            List<Block> holders = GeneralUtils.getListOfNonDummyBlocks(optionalNamed);
            if (holders.size() == 0) return null;

            Block rbComb = holders.get(random.nextInt(random.nextInt(holders.size()) + 1));
            return new StructureTemplate.StructureBlockInfo(worldPos, rbComb.defaultBlockState(), null);
        }
        return null;
    }

    public static boolean isFilledBeeJarItem(ItemStack stack) {
        return BEE_JAR.isPresent() && stack.is(BEE_JAR.get()) && !stack.isEmpty() && stack.hasTag() && stack.getOrCreateTag().contains("Entity")
                && stack.getOrCreateTag().getCompound("Entity").contains("id");
    }

    @Override
    public InteractionResult onEmptyBroodInteract(ItemStack itemstack, Player playerEntity, InteractionHand playerHand) {
        if (!BzModCompatibilityConfigs.allowResourcefulBeesBeeJarRevivingEmptyBroodBlock) return InteractionResult.PASS;
        if (isFilledBeeJarItem(itemstack)) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    itemstack.getOrCreateTag().remove("Entity"); // Remove bee.
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
