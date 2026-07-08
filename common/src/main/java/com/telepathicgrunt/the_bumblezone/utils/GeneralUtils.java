package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureCheckAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureManagerAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.SinglePoolElementAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureTemplateAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class GeneralUtils {

    public static <T> T loadService(Class<T> service) {
        return ServiceLoader.load(service, service.getClassLoader()).findFirst().orElseThrow(() -> new IllegalStateException("No platform implementation found for " + service.getName()));
    }

    /////////////////////////////

    // Weighted Random from: https://stackoverflow.com/a/6737362
    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, RandomSource random) {
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += pair.getSecond();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < rlList.size() - 1; ++index) {
            randomWeightPicked -= rlList.get(index).getSecond();
            if (randomWeightPicked <= 0.0) break;
        }

        return rlList.get(index).getFirst();
    }

    ////////////////

    public static BlockPos getRandomBlockposWithinRange(LivingEntity entity, int maxRadius, int minRadius) {
        BlockPos newBeePos;
        newBeePos = BlockPos.containing(
                entity.getX() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1),
                Doubles.constrainToRange(entity.getY() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1), 1, 254),
                entity.getZ() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1));
        return newBeePos;
    }


    ////////////////

    // Source: https://dzone.com/articles/be-lazy-with-java-8
    public static final class Lazy<T> {

        private volatile T value;
        private Supplier<T> supplierValue;

        public Lazy() {}

        public Lazy(Supplier<T> supplierValue) { this.supplierValue = supplierValue; }

        public T getOrCompute(Supplier<T> supplier) {
            final T result = value; // Just one volatile read
            return result == null ? maybeCompute(supplier) : result;
        }

        private synchronized T maybeCompute(Supplier<T> supplier) {
            if (value == null) {
                value = requireNonNull(supplier.get());
            }
            return value;
        }

        public T getOrFillFromInternal() {
            final T result = value; // Just one volatile read
            return result == null ? maybeCompute(supplierValue) : result;
        }
    }

    //////////////////////////////////////////////

    /**
     * For giving the player an item properly into their inventory
     */
    public static void givePlayerItem(Player playerEntity, InteractionHand hand, ItemStack itemstackToGive, boolean giveContainerItem, boolean shrinkCurrentItem) {
        if (playerEntity.level().isClientSide()) {
            return;
        }

        ItemStack playerItem = playerEntity.getItemInHand(hand);
        ItemStack copiedPlayerItem = playerItem.copy();
        boolean instabuild = playerEntity.getAbilities().instabuild;

        if (!playerItem.isEmpty()) {
            playerEntity.awardStat(Stats.ITEM_USED.get(playerItem.getItem()));
        }

        if(!instabuild && shrinkCurrentItem) {
            playerItem.shrink(1);
        }

        // Give item itself to users
        if(!itemstackToGive.isEmpty()) {
            if (playerItem.isEmpty()) {
                // places result item in hand
                playerEntity.setItemInHand(hand, itemstackToGive);
            }
            else if (instabuild) {
                if (!playerEntity.getInventory().contains(itemstackToGive)) {
                    playerEntity.getInventory().add(itemstackToGive);
                }
            }
            // places result item in inventory
            else if (!playerEntity.getInventory().add(itemstackToGive)) {
                // drops result item if inventory is full
                playerEntity.drop(itemstackToGive, false);
            }
        }

        // give container item of player's item if specified
        if(giveContainerItem && PlatformService.INSTANCE.hasCraftingRemainder(copiedPlayerItem)) {
            ItemStack containerItem = PlatformService.INSTANCE.getCraftingRemainder(copiedPlayerItem);
            if (playerItem.isEmpty()) {
                // places result item in hand
                playerEntity.setItemInHand(hand, containerItem);
            }
            else if (instabuild) {
                if (!playerEntity.getInventory().contains(containerItem)) {
                    playerEntity.getInventory().add(containerItem);
                }
            }
            // places result item in inventory
            else if (!playerEntity.getInventory().add(containerItem)) {
                // drops result item if inventory is full
                playerEntity.drop(containerItem, false);
            }
        }
    }

    //////////////////////////////////////////////

    // More optimized with checking if the jigsaw blocks can connect
    public static boolean canJigsawsAttach(StructureTemplate.JigsawBlockInfo parentJigsaw, StructureTemplate.JigsawBlockInfo childJigsaw) {
        if (!parentJigsaw.target().equals(childJigsaw.name())) {
            return false;
        }

        FrontAndTop prop1 = parentJigsaw.info().state().getValue(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = childJigsaw.info().state().getValue(JigsawBlock.ORIENTATION);

        return prop1.front() == prop2.front().getOpposite() &&
                (prop1.top() == prop2.top() || parentJigsaw.jointType() == JigsawBlockEntity.JointType.ROLLABLE);
    }

    //////////////////////////////////////////////

    public static int getFirstLandYFromPos(LevelReader worldView, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(pos);
        ChunkAccess currentChunk = worldView.getChunk(mutable);
        BlockState currentState = currentChunk.getBlockState(mutable);

        while(mutable.getY() >= worldView.getMinY() && isReplaceableByStructures(currentState)) {
            mutable.move(Direction.DOWN);
            currentState = currentChunk.getBlockState(mutable);
        }

        return mutable.getY();
    }

    private static boolean isReplaceableByStructures(BlockState blockState) {
        return blockState.isAir() || !blockState.getFluidState().isEmpty() || blockState.canBeReplaced() || blockState.is(BzBlocks.HONEY_CRYSTAL.get());
    }

    //////////////////////////////////////////////

    public static BlockPos getLowestLand(ChunkGenerator chunkGenerator, RandomState randomState, BlockPos centerPos, LevelHeightAccessor heightLimitView, boolean canBeOnLiquid, boolean canBeInLiquid) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(centerPos.getX(), 1, centerPos.getZ());
        NoiseColumn blockView = chunkGenerator.getBaseColumn(mutable.getX(), mutable.getZ(), heightLimitView, randomState);
        BlockState currentBlockstate = blockView.getBlock(mutable.getY());
        BlockState pastBlockstate = currentBlockstate;
        while (mutable.getY() <= getMaxTerrainLimit(chunkGenerator)) {
            if(canBeInLiquid && !currentBlockstate.getFluidState().isEmpty())
            {
                mutable.move(Direction.UP);
                return mutable;
            }
            else if((canBeOnLiquid || !pastBlockstate.getFluidState().isEmpty()) && currentBlockstate.isAir())
            {
                mutable.move(Direction.UP);
                return mutable;
            }

            mutable.move(Direction.UP);
            pastBlockstate = currentBlockstate;
            currentBlockstate = blockView.getBlock(mutable.getY());
        }

        return mutable;
    }

    public static int getMaxTerrainLimit(ChunkGenerator chunkGenerator) {
        return chunkGenerator.getMinY() + chunkGenerator.getGenDepth();
    }

    //////////////////////////////////////////////

    public static void spawnItemEntity(ServerLevel serverLevel, BlockPos blockPos, ItemStack itemToSpawn, double randomXZSpeed, double ySpeed) {
        if(!itemToSpawn.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(
                    serverLevel,
                    blockPos.getX() + 0.5D,
                    blockPos.getY() + 1D,
                    blockPos.getZ() + 0.5D,
                    itemToSpawn);
            itemEntity.setDeltaMovement(new Vec3(
                    serverLevel.getRandom().nextGaussian() * randomXZSpeed,
                    ySpeed,
                    serverLevel.getRandom().nextGaussian() * randomXZSpeed));
            itemEntity.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(itemEntity);
        }
    }

    //////////////////////////////////////////////

    public static void centerAllPieces(BlockPos targetPos, List<? extends StructurePiece> pieces) {
        if (pieces.isEmpty()) {
            return;
        }

        Vec3i structureCenter = pieces.get(0).getBoundingBox().getCenter();
        int xOffset = targetPos.getX() - structureCenter.getX();
        int zOffset = targetPos.getZ() - structureCenter.getZ();

        for (StructurePiece structurePiece : pieces) {
            structurePiece.move(xOffset, 0, zOffset);
        }
    }

    //////////////////////////////////////////////

    public static <T> List<T> convertHoldersetToList(Optional<HolderSet.Named<T>> blockTagResult) {
        return blockTagResult.map(holders -> holders
                .stream()
                .map(Holder::value)
                .collect(Collectors.toCollection(ArrayList::new))
        ).orElseGet(ArrayList::new);
    }

    public static <B, T extends B> boolean isInTag(Registry<B> registry, TagKey<B> key, T value) {
        return registry.get(registry.getId(value)).orElseThrow().is(key);
    }

    public static boolean isInTag(DefaultedRegistry<Item> item, TagKey<Item> itemSpecialDedicatedCompat, Optional<Holder.Reference<Item>> itemReference) {
        return isInTag(item, itemSpecialDedicatedCompat, itemReference.get().value());
    }

    /**
     * Matches each list item to a predicate and returns if all predicates are true.
     */
    public static <T> boolean listMatches(List<T> list, List<? extends Predicate<T>> predicates) {
        if(list.size() != predicates.size()) return false;
        List<Predicate<T>> copiedPredicates = new ArrayList<>(predicates);
        predicateCheck:
        for (int i = copiedPredicates.size() - 1; i >= 0; i--) {
            for (int k = list.size() - 1; k >= 0; k--) {
                if (copiedPredicates.get(i).test(list.get(k))) {
                    copiedPredicates.remove(i);
                    continue predicateCheck;
                }
            }
            return false;
        }
        return copiedPredicates.isEmpty();
    }

    //////////////////////////////////////////////
    // Source: https://github.com/Shadows-of-Fire/Placebo/blob/35bc107709970cd7f2f24cf73b2c4337ab00fb3b/src/main/java/shadows/placebo/container/ContainerUtil.java

    /**
     * IIntArray can only send shorts, so we need to split int values in two.
     * @param value The int to split
     * @param upper If sending the upper bits or not.
     * @return The appropriate half of the integer.
     */
    public static int split(int value, boolean upper) {
        return upper ? value >> 16 : value & 0xFFFF;
    }

    /**
     * IIntArray can only send shorts, so we need to split int values in two.
     * @param upper The current upper split bits, received from network
     * @param lower The current lower split bits, received from network
     * @return The updated value.
     */
    public static int merge(int upper, int lower) {
        return (upper << 16) + (lower & 0x0000FFFF);
    }

    //////////////////////////////////////////////

    public static boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock) {
        if (level instanceof ServerLevel serverLevel && entity instanceof Player player && !player.mayInteract(serverLevel, pos)) {
            return false;
        }
        return PlatformService.INSTANCE.isPermissionAllowedAtSpot(level, entity, pos, placingBlock);
    }

    ///////////////////////////////////////////////

    public static <T extends Comparable<T>> BlockState getStateWithProperty(BlockState state, BlockState stateToCopy, Property<T> property) {
        return state.setValue(property, stateToCopy.getValue(property));
    }

    ///////////////////////////////////////////////

    public static void placeInWorldWithoutNeighborUpdate(ServerLevelAccessor serverLevelAccessor,
                                                         StructureTemplate structureTemplate,
                                                         BlockPos blockPos,
                                                         BlockPos blockPos2,
                                                         StructurePlaceSettings structurePlaceSettings,
                                                         RandomSource randomSource,
                                                         int i)
    {
        if (((StructureTemplateAccessor)structureTemplate).bumblezone$getBlocks().isEmpty()) {
            return;
        }
        List<StructureTemplate.StructureBlockInfo> list = structurePlaceSettings.getRandomPalette(((StructureTemplateAccessor)structureTemplate).bumblezone$getBlocks(), blockPos).blocks();
        if (list.isEmpty() && structurePlaceSettings.isIgnoreEntities() || structureTemplate.getSize().getX() < 1 || structureTemplate.getSize().getY() < 1 || structureTemplate.getSize().getZ() < 1) {
            return;
        }
        BoundingBox boundingBox = structurePlaceSettings.getBoundingBox();
        ArrayList<BlockPos> list2 = Lists.newArrayListWithCapacity(structurePlaceSettings.shouldApplyWaterlogging() ? list.size() : 0);
        ArrayList<BlockPos> list3 = Lists.newArrayListWithCapacity(structurePlaceSettings.shouldApplyWaterlogging() ? list.size() : 0);
        ArrayList<Pair<BlockPos, CompoundTag>> list4 = Lists.newArrayListWithCapacity(list.size());
        int j = Integer.MAX_VALUE;
        int k = Integer.MAX_VALUE;
        int l = Integer.MAX_VALUE;
        int m = Integer.MIN_VALUE;
        int n = Integer.MIN_VALUE;
        int o = Integer.MIN_VALUE;
        List<StructureTemplate.StructureBlockInfo> list5 = StructureTemplate.processBlockInfos(serverLevelAccessor, blockPos, blockPos2, structurePlaceSettings, list);

        try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(Bumblezone.LOGGER)) {
            for (StructureTemplate.StructureBlockInfo structureBlockInfo : list5) {
                BlockEntity blockEntity;
                BlockPos blockPos3 = structureBlockInfo.pos();
                if (boundingBox != null && !boundingBox.isInside(blockPos3)) continue;
                FluidState fluidState = structurePlaceSettings.shouldApplyWaterlogging() ? serverLevelAccessor.getFluidState(blockPos3) : null;
                BlockState blockState = structureBlockInfo.state().mirror(structurePlaceSettings.getMirror()).rotate(structurePlaceSettings.getRotation());
                if (structureBlockInfo.nbt() != null) {
                    serverLevelAccessor.setBlock(blockPos3, Blocks.BARRIER.defaultBlockState(), 20);
                }
                if (!serverLevelAccessor.setBlock(blockPos3, blockState, i)) continue;
                j = Math.min(j, blockPos3.getX());
                k = Math.min(k, blockPos3.getY());
                l = Math.min(l, blockPos3.getZ());
                m = Math.max(m, blockPos3.getX());
                n = Math.max(n, blockPos3.getY());
                o = Math.max(o, blockPos3.getZ());
                list4.add(Pair.of(blockPos3, structureBlockInfo.nbt()));
                if (structureBlockInfo.nbt() != null && (blockEntity = serverLevelAccessor.getBlockEntity(blockPos3)) != null) {
                    if (blockEntity instanceof RandomizableContainerBlockEntity) {
                        structureBlockInfo.nbt().putLong("LootTableSeed", randomSource.nextLong());
                    }

                    blockEntity.loadWithComponents(
                            TagValueInput.create(
                                    problemreporter$scopedcollector.forChild(blockEntity.problemPath()),
                                    serverLevelAccessor.registryAccess(),
                                    structureBlockInfo.nbt()
                            )
                    );
                }
                if (fluidState == null) continue;
                if (blockState.getFluidState().isSource()) {
                    list3.add(blockPos3);
                    continue;
                }
                if (!(blockState.getBlock() instanceof LiquidBlockContainer)) continue;
                ((LiquidBlockContainer) blockState.getBlock()).placeLiquid(serverLevelAccessor, blockPos3, blockState, fluidState);
                if (fluidState.isSource()) continue;
                list2.add(blockPos3);
            }
            boolean bl = true;
            Direction[] directions = new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
            while (bl && !list2.isEmpty()) {
                bl = false;
                Iterator<BlockPos> iterator = list2.iterator();
                while (iterator.hasNext()) {
                    BlockState blockState2;
                    Object block;
                    BlockPos blockPos3 = iterator.next();
                    FluidState fluidState2 = serverLevelAccessor.getFluidState(blockPos3);
                    for (int p = 0; p < directions.length && !fluidState2.isSource(); ++p) {
                        BlockPos blockPos5 = blockPos3.relative(directions[p]);
                        FluidState fluidState = serverLevelAccessor.getFluidState(blockPos5);
                        if (!fluidState.isSource() || list3.contains(blockPos5)) continue;
                        fluidState2 = fluidState;
                    }
                    if (!fluidState2.isSource() || !((block = (blockState2 = serverLevelAccessor.getBlockState(blockPos3)).getBlock()) instanceof LiquidBlockContainer)) continue;
                    ((LiquidBlockContainer)block).placeLiquid(serverLevelAccessor, blockPos3, blockState2, fluidState2);
                    bl = true;
                    iterator.remove();
                }
            }
            if (j <= m) {
                if (!structurePlaceSettings.getKnownShape()) {
                    BitSetDiscreteVoxelShape discreteVoxelShape = new BitSetDiscreteVoxelShape(m - j + 1, n - k + 1, o - l + 1);
                    for (Pair<BlockPos, CompoundTag> pair : list4) {
                        BlockPos blockPos6 = pair.getFirst();
                        ((DiscreteVoxelShape)discreteVoxelShape).fill(blockPos6.getX() - j, blockPos6.getY() - k, blockPos6.getZ() - l);
                    }
                    StructureTemplate.updateShapeAtEdge(serverLevelAccessor, i, discreteVoxelShape, j, k, l);
                }
                for (Pair<BlockPos, CompoundTag> pair : list4) {
                    BlockEntity blockEntity;
                    BlockPos blockPos7 = pair.getFirst();
                    if (!structurePlaceSettings.getKnownShape()) {
                        BlockState blockState3;
                        BlockState blockState2 = serverLevelAccessor.getBlockState(blockPos7);
                        if (blockState2 != (blockState3 = Block.updateFromNeighbourShapes(blockState2, serverLevelAccessor, blockPos7))) {
                            serverLevelAccessor.setBlock(blockPos7, blockState3, i & 0xFFFFFFFE);
                        }
                        serverLevelAccessor.updateNeighborsAt(blockPos7, blockState3.getBlock());
                    }
                    if (pair.getSecond() == null || (blockEntity = serverLevelAccessor.getBlockEntity(blockPos7)) == null) continue;
                    blockEntity.setChanged();
                }
            }

            if (!structurePlaceSettings.isIgnoreEntities()) {
                ((StructureTemplateAccessor) structureTemplate).bumblezone$callPlaceEntities(
                        serverLevelAccessor,
                        blockPos,
                        structurePlaceSettings.getMirror(),
                        structurePlaceSettings.getRotation(),
                        structurePlaceSettings.getRotationPivot(),
                        boundingBox,
                        structurePlaceSettings.shouldFinalizeEntities(),
                        problemreporter$scopedcollector);
            }
        }
    }

    private static final Direction[] FLUID_CHECKING_DIRECTION = new Direction[]{ Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public static void placeInWorldWithChunkSectionCachingAndWithoutNeighborUpdate(
            ServerLevelAccessor serverLevel,
            StructureTemplate structureTemplate,
            BlockPos offset,
            BlockPos pos,
            StructurePlaceSettings settings,
            RandomSource random,
            int flags)
    {
        if (((StructureTemplateAccessor)structureTemplate).bumblezone$getBlocks().isEmpty()) {
            return;
        }

        List<StructureTemplate.StructureBlockInfo> list = settings.getRandomPalette(((StructureTemplateAccessor)structureTemplate).bumblezone$getBlocks(), offset).blocks();
        if ((!list.isEmpty() || !settings.isIgnoreEntities() && !((StructureTemplateAccessor)structureTemplate).bumblezone$getEntityInfoList().isEmpty())
                && structureTemplate.getSize().getX() >= 1
                && structureTemplate.getSize().getY() >= 1
                && structureTemplate.getSize().getZ() >= 1) {
            UnsafeBulkSectionAccess bulkSectionAccess = new UnsafeBulkSectionAccess(serverLevel);

            BoundingBox boundingbox = settings.getBoundingBox();
            List<BlockPos> list1 = Lists.newArrayListWithCapacity(settings.shouldApplyWaterlogging() ? list.size() : 0);
            List<BlockPos> list2 = Lists.newArrayListWithCapacity(settings.shouldApplyWaterlogging() ? list.size() : 0);
            List<Pair<BlockPos, CompoundTag>> list3 = Lists.newArrayListWithCapacity(list.size());
            int i = Integer.MAX_VALUE;
            int j = Integer.MAX_VALUE;
            int k = Integer.MAX_VALUE;
            int l = Integer.MIN_VALUE;
            int i1 = Integer.MIN_VALUE;
            int j1 = Integer.MIN_VALUE;
            List<StructureTemplate.StructureBlockInfo> list4 = StructureTemplate.processBlockInfos(serverLevel, offset, pos, settings, list);

            try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(Bumblezone.LOGGER)) {
                for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : list4) {
                    BlockPos blockpos = structuretemplate$structureblockinfo.pos();
                    if (boundingbox == null || boundingbox.isInside(blockpos)) {
                        FluidState fluidstate = settings.shouldApplyWaterlogging() ? bulkSectionAccess.getFluidState(blockpos) : null;
                        BlockState blockstate = structuretemplate$structureblockinfo.state().mirror(settings.getMirror()).rotate(settings.getRotation());
                        if (structuretemplate$structureblockinfo.nbt() != null) {
                            SetBlockWithChangeNotified(serverLevel, bulkSectionAccess,blockpos, Blocks.BARRIER.defaultBlockState());
                        }

                        if (SetBlockWithChangeNotified(serverLevel, bulkSectionAccess, blockpos, blockstate)) {
                            i = Math.min(i, blockpos.getX());
                            j = Math.min(j, blockpos.getY());
                            k = Math.min(k, blockpos.getZ());
                            l = Math.max(l, blockpos.getX());
                            i1 = Math.max(i1, blockpos.getY());
                            j1 = Math.max(j1, blockpos.getZ());
                            list3.add(Pair.of(blockpos, structuretemplate$structureblockinfo.nbt()));
                            if (structuretemplate$structureblockinfo.nbt() != null) {
                                BlockEntity blockentity = serverLevel.getBlockEntity(blockpos);
                                if (blockentity != null) {
                                    if (blockentity instanceof RandomizableContainer) {
                                        structuretemplate$structureblockinfo.nbt().putLong("LootTableSeed", random.nextLong());
                                    }

                                    blockentity.loadWithComponents(
                                            TagValueInput.create(
                                                    problemreporter$scopedcollector.forChild(blockentity.problemPath()),
                                                    serverLevel.registryAccess(),
                                                    structuretemplate$structureblockinfo.nbt()
                                            )
                                    );
                                }
                            }

                            if (fluidstate != null) {
                                if (blockstate.getFluidState().isSource()) {
                                    list2.add(blockpos);
                                } else if (blockstate.getBlock() instanceof LiquidBlockContainer) {
                                    ((LiquidBlockContainer) blockstate.getBlock()).placeLiquid(serverLevel, blockpos, blockstate, fluidstate);
                                    if (!fluidstate.isSource()) {
                                        list1.add(blockpos);
                                    }
                                }
                            }
                        }
                    }
                }

                boolean flag = true;

                while (flag && !list1.isEmpty()) {
                    flag = false;
                    Iterator<BlockPos> iterator = list1.iterator();

                    while (iterator.hasNext()) {
                        BlockPos blockpos3 = iterator.next();
                        FluidState fluidstate2 = bulkSectionAccess.getFluidState(blockpos3);

                        for (int i2 = 0; i2 < FLUID_CHECKING_DIRECTION.length && !fluidstate2.isSource(); i2++) {
                            BlockPos blockpos1 = blockpos3.relative(FLUID_CHECKING_DIRECTION[i2]);
                            FluidState fluidstate1 = bulkSectionAccess.getFluidState(blockpos1);
                            if (fluidstate1.isSource() && !list2.contains(blockpos1)) {
                                fluidstate2 = fluidstate1;
                            }
                        }

                        if (fluidstate2.isSource()) {
                            BlockState blockstate1 = bulkSectionAccess.getBlockState(blockpos3);
                            Block block = blockstate1.getBlock();
                            if (block instanceof LiquidBlockContainer) {
                                ((LiquidBlockContainer) block).placeLiquid(serverLevel, blockpos3, blockstate1, fluidstate2);
                                flag = true;
                                iterator.remove();
                            }
                        }
                    }
                }

                if (i <= l) {
                    if (!settings.getKnownShape()) {
                        DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(l - i + 1, i1 - j + 1, j1 - k + 1);

                        for (Pair<BlockPos, CompoundTag> pair1 : list3) {
                            BlockPos blockpos2 = pair1.getFirst();
                            discretevoxelshape.fill(blockpos2.getX() - i, blockpos2.getY() - j, blockpos2.getZ() - k);
                        }

                        StructureTemplate.updateShapeAtEdge(serverLevel, flags, discretevoxelshape, i, j, k);
                    }

                    for (Pair<BlockPos, CompoundTag> pair : list3) {
                        BlockPos blockpos4 = pair.getFirst();
                        if (!settings.getKnownShape()) {
                            BlockState blockstate2 = bulkSectionAccess.getBlockState(blockpos4);
                            BlockState blockstate3 = Block.updateFromNeighbourShapes(blockstate2, serverLevel, blockpos4);
                            if (blockstate2 != blockstate3) {
                                SetBlockWithChangeNotified(serverLevel, bulkSectionAccess, blockpos4, blockstate3);
                            }

                            serverLevel.updateNeighborsAt(blockpos4, blockstate3.getBlock());
                        }

                        if (pair.getSecond() != null) {
                            BlockEntity blockentity1 = serverLevel.getBlockEntity(blockpos4);
                            if (blockentity1 != null) {
                                blockentity1.setChanged();
                            }
                        }
                    }
                }

                if (!settings.isIgnoreEntities()) {
                    ((StructureTemplateAccessor)structureTemplate).bumblezone$callPlaceEntities(
                            serverLevel,
                            offset,
                            settings.getMirror(),
                            settings.getRotation(),
                            settings.getRotationPivot(),
                            boundingbox,
                            settings.shouldFinalizeEntities(),
                            problemreporter$scopedcollector
                    );
                }
            }
        }
    }

    private static boolean SetBlockWithChangeNotified(ServerLevelAccessor serverLevelAccessor, UnsafeBulkSectionAccess bulkSectionAccess, BlockPos blockPos3, BlockState newState) {
        BlockState oldState = bulkSectionAccess.setBlockStateAndGetOldState(blockPos3, newState, false);
        if (oldState != null) {
            serverLevelAccessor.getLevel().updatePOIOnBlockStateChange(blockPos3, oldState, newState);
            return true;
        }
        return false;
    }

    /////////////////////////////////////////////////


    public static boolean isSimilarInColor(int color1, int color2, int threshold) {
        return (Math.abs(getRed(color1) - getRed(color2)) +
                Math.abs(getGreen(color1) - getGreen(color2)) +
                Math.abs(getBlue(color1) - getBlue(color2))) < threshold;
    }

    public static boolean isSimilarInVisualColor(int color1, int color2, int hueThreshold, int valueThreshold) {
        double[] hue1 = ColorToHsv(color1);
        double[] hue2 = ColorToHsv(color2);

        double hueDiff = hue1[0] - hue2[0];
        if (hueDiff > 180) {
            hueDiff -= 360;
        }
        else if (hueDiff < -180) {
            hueDiff += 360;
        }
        double hueDistance = Math.sqrt(hueDiff * hueDiff);

        double valueDiff = Math.abs(hue1[2] - hue2[2]);

        return hueDistance < hueThreshold && valueDiff < valueThreshold;
    }

    // Source: http://www.java2s.com/example/csharp/system.drawing/calculate-the-difference-in-hue-between-two-s.html
    public static double[] ColorToHsv(int color) {
        int r = getRed(color);
        int g = getGreen(color);
        int b = getBlue(color);

        double h = 0, s, v;
        double min = Math.min(Math.min(r, g), b);
        v = Math.max(Math.max(r, g), b);
        double delta = v - min;

        if (v == 0.0) {
            s = 0;
        }
        else {
            s = delta / v;
        }

        if (s == 0) {
            h = 0.0;
        }
        else {
            if (r == v) {
                h = (g - b) / delta;
            }
            else if (g == v) {
                h = 2 + (b - r) / delta;
            }
            else if (b == v) {
                h = 4 + (r - g) / delta;
            }

            h *= 60;
            if (h < 0.0) {
                h = h + 360;
            }
        }

        var hsv = new double[3];
        hsv[0] = h; // 0 to 360
        hsv[1] = s * 360; // 0 to 360
        hsv[2] = v; // 0 to 360
        return hsv;
    }

    public static int getAlpha(int color) {
        return (color >> 24) & 0xFF;
    }

    public static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int getBlue(int color) {
        return color & 0xFF;
    }

    public static int colorToInt(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static double capBetween(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static List<BlockPos> matchingBlocksOfKindInRange(Level level, BlockPos centerPos, int radius, Predicate<BlockState> predicate) {
        List<BlockPos> validPos = new ObjectArrayList<>();

        // Figure out how many chunk radius we need to search outward to encompass the radius properly
        ChunkPos maxChunkPos = new ChunkPos(
                SectionPos.blockToSectionCoord(centerPos.getX() + radius),
                SectionPos.blockToSectionCoord(centerPos.getZ() + radius)
        );
        ChunkPos minChunkPos = new ChunkPos(
                SectionPos.blockToSectionCoord(centerPos.getX() - radius),
                SectionPos.blockToSectionCoord(centerPos.getZ() - radius)
        );

        // Get all the chunks in range
        for (int xOffset = minChunkPos.x(); xOffset <= maxChunkPos.x(); xOffset++) {
            for (int zOffset = minChunkPos.z(); zOffset <= maxChunkPos.z(); zOffset++) {
                ChunkAccess chunk = level.getChunk(xOffset, zOffset);

                // Find and store all matches
                scanChunkForMatchInRange(predicate, validPos, chunk, centerPos, radius);
            }
        }

        return validPos;
    }

    private static void scanChunkForMatchInRange(Predicate<BlockState> predicate, List<BlockPos> validPos, ChunkAccess chunk, BlockPos originalPos, int radius) {
        BlockPos.MutableBlockPos mutableSectionWorldOrigin = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos mutableSectionWorldBlockPos = new BlockPos.MutableBlockPos();
        int radiusSq = radius * radius;

        // Iterate over all sections in chunk. Note, sections can be negative if world extends to negative.
        for (int i = chunk.getMinSectionY(); i < chunk.getMaxSectionY(); ++i) {
            int sectionWorldY = SectionPos.sectionToBlockCoord(i);

            // Make sure this section is in range of the radius we want to check.
            if (sectionWorldY + 15 < originalPos.getY() - radius || sectionWorldY > originalPos.getY() + radius) {
                continue;
            }

            LevelChunkSection levelChunkSection = chunk.getSection(chunk.getSectionIndexFromSectionY(i));

            // Check if chunk section has match
            if (levelChunkSection.maybeHas(predicate)) {

                // Set to origin corner of chunk section
                mutableSectionWorldOrigin.set(
                        SectionPos.sectionToBlockCoord(chunk.getPos().x()),
                        sectionWorldY,
                        SectionPos.sectionToBlockCoord(chunk.getPos().z()));

                for (int yOffset = 0; yOffset < 16; yOffset++) {
                    for (int zOffset = 0; zOffset < 16; zOffset++) {
                        for (int xOffset = 0; xOffset < 16; xOffset++) {
                            // Go to spot in section in terms of world position.
                            mutableSectionWorldBlockPos.set(mutableSectionWorldOrigin).move(xOffset, yOffset, zOffset);

                            // Make sure spot is in radius
                            int xDiff = originalPos.getX() - mutableSectionWorldBlockPos.getX();
                            int yDiff = originalPos.getY() - mutableSectionWorldBlockPos.getY();
                            int zDiff = originalPos.getZ() - mutableSectionWorldBlockPos.getZ();
                            if ((xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) <= radiusSq) {

                                // Test block and add position found.
                                BlockState blockState = levelChunkSection.getBlockState(xOffset, yOffset, zOffset);
                                if (predicate.test(blockState)) {
                                    validPos.add(mutableSectionWorldBlockPos.immutable());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static String formatTickDurationNoMilliseconds(int tickDuration, float tickRate) {
        int j = Mth.floor((float)tickDuration / tickRate);
        int k = j / 60;
        j %= 60;
        k %= 60;
        return String.format(Locale.ROOT, "%02d:%02d", k, j);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static int constrainToRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static StructureStart getStructureAt(LevelReader level, StructureManager structureManager, BlockPos blockPos, Structure structure) {
        for(StructureStart structureStart : startsForStructure(level, structureManager, SectionPos.of(blockPos), structure)) {
            if (structureStart.getBoundingBox().isInside(blockPos)) {
                return structureStart;
            }
        }

        return StructureStart.INVALID_START;
    }

    public static List<StructureStart> startsForAllStructure(LevelReader level, StructureManager structureManager, SectionPos sectionPos, Predicate<Structure> structureMatch) {
        ChunkAccess chunkAccess = level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES);
        Map<Structure, LongSet> references = chunkAccess.getAllReferences();
        ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
        for(Map.Entry<Structure, LongSet> entry : references.entrySet()) {
            if (structureMatch.test(entry.getKey())) {
                fillStartsForStructure(level, structureManager, entry.getKey(), entry.getValue(), builder::add);
            }
        }
        return builder.build();
    }

    public static List<StructureStart> startsForStructure(LevelReader level, StructureManager structureManager, SectionPos sectionPos, Structure structure) {
        ChunkAccess chunkAccess = level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES);
        LongSet references = chunkAccess.getReferencesForStructure(structure);
        ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
        fillStartsForStructure(level, structureManager, structure, references, builder::add);
        return builder.build();
    }

    public static void fillStartsForStructure(LevelReader level, StructureManager structureManager, Structure structure, LongSet references, Consumer<StructureStart> consumer) {
        for (long ref : references) {
            SectionPos sectionPos = SectionPos.of(ChunkPos.unpack(ref), level.getMinSectionY());
            if (!level.hasChunk(sectionPos.x(), sectionPos.z())) {
                continue;
            }
            StructureStart structureStart = structureManager.getStartForStructure(sectionPos, structure, level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS));
            if (structureStart != null && structureStart.isValid()) {
                consumer.accept(structureStart);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////


    public static List<ItemStack> convertBlockTagsToItemStacks(TagKey<Block> baseTag, @Nullable TagKey<Block> disallowTag) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (Holder<Block> blockHolder : BuiltInRegistries.BLOCK.getTagOrEmpty(baseTag)) {
            if (disallowTag == null || !blockHolder.is(disallowTag)) {
                Item item = blockHolder.value().asItem();
                if (item == null) {
                    continue;
                }

                ItemStack itemStack = item.getDefaultInstance();
                if (!itemStack.isEmpty()) {
                    itemStacks.add(itemStack);
                }
            }
        }
        return itemStacks;
    }

    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Bumblezone structures do not use priority in Jigsaws, so we can skip the expensive priority sorting.
     */
    public static List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocksWithoutPriority(SinglePoolElement singlePoolElement, StructureTemplateManager structureTemplateManager, BlockPos blockPos, Rotation rotation, RandomSource randomSource) {
        StructureTemplate structureTemplate = ((SinglePoolElementAccessor)singlePoolElement).bumblezone$getTemplate().map(structureTemplateManager::getOrCreate, Function.identity());
        List<StructureTemplate.JigsawBlockInfo> objectArrayList = structureTemplate.getJigsaws(blockPos, rotation);
        Util.shuffle(objectArrayList, randomSource);
        return objectArrayList;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static boolean isOutsideStructureAllowedBounds(StructurePlaceSettings settings, BlockPos pos) {
        return settings.getBoundingBox() != null && !settings.getBoundingBox().isInside(pos);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static boolean isFaceFullFast(BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        BlockState blockstate = blockGetter.getBlockState(blockPos);
        VoxelShape overallShape = blockstate.getCollisionShape(blockGetter, blockPos);

        return isFaceFullFast(overallShape, direction);
    }

    public static boolean isFaceFullFast(VoxelShape overallShape, Direction direction) {
        if (overallShape == Shapes.block()) {
            return true;
        }

        if (overallShape == Shapes.empty()) {
            return false;
        }

        return Block.isFaceFull(overallShape, direction);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static Optional<Property<Integer>> getBlockCurrentAge(BlockState blockState) {
        Optional<Property<?>> propertyOptional = blockState.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase("age")).findAny();
        if (propertyOptional.isPresent()) {
            Property<?> property = propertyOptional.get();
            if (property.getValueClass() == Integer.class) {
                return (Optional<Property<Integer>>)(Object)propertyOptional;
            }
        }
        return Optional.empty();
    }

    public static Optional<Integer> getAgePropertyMaxAge(Property<Integer> ageProperty) {
        return ageProperty.getPossibleValues().stream().max(Comparable::compareTo);
    }

    public static @NotNull BlockState copyNonAgeProperties(BlockState oldState, BlockState newState) {
        for (Property<?> property : newState.getProperties()) {
            if (!property.getName().equalsIgnoreCase("age")) {
                newState = copyProperty(oldState, newState, property);
            }
        }
        return newState;
    }

    private static <T extends Comparable<T>> @NotNull BlockState copyProperty(BlockState state, BlockState newState, Property<T> propertyToCopy) {
        if (newState.hasProperty(propertyToCopy) && state.hasProperty(propertyToCopy)) {
            newState = newState.setValue(propertyToCopy, state.getValue(propertyToCopy));
        }
        return newState;
    }

    /////////////////////////////////////////////////////////////////////////////////


    public static <T extends LivingEntity> T getNearestEntity(
            ServerLevel serverLevel,
            Class<? extends T> entityClazz,
            TargetingConditions conditions,
            @javax.annotation.Nullable LivingEntity target,
            double x,
            double y,
            double z,
            AABB boundingBox
    ) {
        return GeneralUtils.getNearestEntity(
                serverLevel,
                serverLevel.getEntitiesOfClass(entityClazz, boundingBox, _ -> true),
                conditions,
                target,
                x,
                y,
                z);
    }

    public static <T extends LivingEntity> T getNearestEntity(
            ServerLevel serverLevel,
            List<? extends T> entities,
            TargetingConditions predicate,
            LivingEntity target,
            double x,
            double y,
            double z
    ) {
        double best = -1.0;
        T result = null;

        for (T entity : entities) {
            if (predicate.test(serverLevel, target, entity)) {
                double dist = entity.distanceToSqr(x, y, z);
                if (best == -1.0 || dist < best) {
                    best = dist;
                    result = entity;
                }
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////

    // Below copied from ChunkGenerator as I needed to make a new StructureCheck instance so that the structure search is threadsafe
    // So much freaking duplicate code

    public static Pair<BlockPos, Holder<Structure>> findNearestMapStructureAsyncSafe(
            ServerLevel level, HolderSet<Structure> structure, BlockPos pos, int searchRadius, boolean skipKnownStructures
    ) {
        ChunkGeneratorStructureState chunkgeneratorstructurestate = level.getChunkSource().getGeneratorState();
        Map<StructurePlacement, Set<Holder<Structure>>> map = new Object2ObjectArrayMap<>();

        for (Holder<Structure> holder : structure) {
            for (StructurePlacement structureplacement : chunkgeneratorstructurestate.getPlacementsForStructure(holder)) {
                map.computeIfAbsent(structureplacement, p_223127_ -> new ObjectArraySet<>()).add(holder);
            }
        }

        if (map.isEmpty()) {
            return null;
        } else {
            Pair<BlockPos, Holder<Structure>> pair2 = null;
            double d2 = Double.MAX_VALUE;
            StructureManager structuremanager = level.structureManager();
            List<Map.Entry<StructurePlacement, Set<Holder<Structure>>>> list = new ArrayList<>(map.size());

            for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : map.entrySet()) {
                StructurePlacement structureplacement1 = entry.getKey();
                if (structureplacement1 instanceof ConcentricRingsStructurePlacement concentricringsstructureplacement) {
                    Pair<BlockPos, Holder<Structure>> pair = getNearestGeneratedStructureAsyncSafe(
                            entry.getValue(), level, structuremanager, pos, skipKnownStructures, concentricringsstructureplacement
                    );
                    if (pair != null) {
                        BlockPos blockpos = pair.getFirst();
                        double d0 = pos.distSqr(blockpos);
                        if (d0 < d2) {
                            d2 = d0;
                            pair2 = pair;
                        }
                    }
                } else if (structureplacement1 instanceof RandomSpreadStructurePlacement) {
                    list.add(entry);
                }
            }

            if (!list.isEmpty()) {
                int i = SectionPos.blockToSectionCoord(pos.getX());
                int j = SectionPos.blockToSectionCoord(pos.getZ());

                for (int k = 0; k <= searchRadius; k++) {
                    boolean flag = false;

                    for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry1 : list) {
                        RandomSpreadStructurePlacement randomspreadstructureplacement = (RandomSpreadStructurePlacement)entry1.getKey();
                        Pair<BlockPos, Holder<Structure>> pair1 = getNearestGeneratedStructureAsyncSafe(
                                entry1.getValue(),
                                level,
                                structuremanager,
                                i,
                                j,
                                k,
                                skipKnownStructures,
                                chunkgeneratorstructurestate.getLevelSeed(),
                                randomspreadstructureplacement
                        );
                        if (pair1 != null) {
                            flag = true;
                            double d1 = pos.distSqr(pair1.getFirst());
                            if (d1 < d2) {
                                d2 = d1;
                                pair2 = pair1;
                            }
                        }
                    }

                    if (flag) {
                        return pair2;
                    }
                }
            }

            return pair2;
        }
    }

    private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructureAsyncSafe(
            Set<Holder<Structure>> structureHoldersSet,
            ServerLevel level,
            StructureManager structureManager,
            BlockPos pos,
            boolean skipKnownStructures,
            ConcentricRingsStructurePlacement placement
    ) {
        List<ChunkPos> list = level.getChunkSource().getGeneratorState().getRingPositionsFor(placement);
        if (list == null) {
            throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
        } else {
            Pair<BlockPos, Holder<Structure>> pair = null;
            double d0 = Double.MAX_VALUE;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (ChunkPos chunkpos : list) {
                blockpos$mutableblockpos.set(SectionPos.sectionToBlockCoord(chunkpos.x(), 8), 32, SectionPos.sectionToBlockCoord(chunkpos.z(), 8));
                double d1 = blockpos$mutableblockpos.distSqr(pos);
                boolean flag = pair == null || d1 < d0;
                if (flag) {
                    Pair<BlockPos, Holder<Structure>> pair1 = getStructureGeneratingAtAsyncSafe(structureHoldersSet, level, structureManager, skipKnownStructures, placement, chunkpos);
                    if (pair1 != null) {
                        pair = pair1;
                        d0 = d1;
                    }
                }
            }

            return pair;
        }
    }

    private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructureAsyncSafe(
            Set<Holder<Structure>> structureHoldersSet,
            LevelReader level,
            StructureManager structureManager,
            int x,
            int y,
            int z,
            boolean skipKnownStructures,
            long seed,
            RandomSpreadStructurePlacement spreadPlacement
    ) {
        int i = spreadPlacement.spacing();

        for (int j = -z; j <= z; j++) {
            boolean flag = j == -z || j == z;

            for (int k = -z; k <= z; k++) {
                boolean flag1 = k == -z || k == z;
                if (flag || flag1) {
                    int l = x + i * j;
                    int i1 = y + i * k;
                    ChunkPos chunkpos = spreadPlacement.getPotentialStructureChunk(seed, l, i1);
                    Pair<BlockPos, Holder<Structure>> pair = getStructureGeneratingAtAsyncSafe(structureHoldersSet, level, structureManager, skipKnownStructures, spreadPlacement, chunkpos);
                    if (pair != null) {
                        return pair;
                    }
                }
            }
        }


        return null;
    }

    private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAtAsyncSafe(
            Set<Holder<Structure>> structureHoldersSet,
            LevelReader level,
            StructureManager structureManager,
            boolean skipKnownStructures,
            StructurePlacement placement,
            ChunkPos chunkPos
    ) {

        // Need to create new instance as StructureCheck is not threadsafe.
        StructureCheckAccessor originalStructureCheck = ((StructureCheckAccessor)((StructureManagerAccessor)structureManager).bumblezone$getStructureCheck());
        StructureCheck tempStructureCheck = new StructureCheck(
                originalStructureCheck.bumblezone$getStorageAccess(),
                originalStructureCheck.bumblezone$getRegistryAccess(),
                originalStructureCheck.bumblezone$getStructureTemplateManager(),
                originalStructureCheck.bumblezone$getDimension(),
                originalStructureCheck.bumblezone$getChunkGenerator(),
                originalStructureCheck.bumblezone$getRandomState(),
                originalStructureCheck.bumblezone$getHeightAccessor(),
                originalStructureCheck.bumblezone$getBiomeSource(),
                originalStructureCheck.bumblezone$getSeed(),
                originalStructureCheck.bumblezone$getFixerUpper()
        );
        for (Holder<Structure> holder : structureHoldersSet) {
            StructureCheckResult structurecheckresult = tempStructureCheck.checkStart(chunkPos, holder.value(), placement, skipKnownStructures);
            if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
                if (!skipKnownStructures && structurecheckresult == StructureCheckResult.START_PRESENT) {
                    return Pair.of(placement.getLocatePos(chunkPos), holder);
                }

                ChunkAccess chunkaccess = level.getChunk(chunkPos.x(), chunkPos.z(), ChunkStatus.STRUCTURE_STARTS);
                StructureStart structurestart = structureManager.getStartForStructure(SectionPos.bottomOf(chunkaccess), holder.value(), chunkaccess);
                if (structurestart != null && structurestart.isValid() && (!skipKnownStructures || tryAddReferenceAsyncSafe(tempStructureCheck, structurestart))) {
                    return Pair.of(placement.getLocatePos(structurestart.getChunkPos()), holder);
                }
            }
        }

        return null;
    }

    private static boolean tryAddReferenceAsyncSafe(StructureCheck tempStructureCheck, StructureStart structureStart) {
        if (structureStart.canBeReferenced()) {
            structureStart.addReference();
            tempStructureCheck.incrementReference(structureStart.getChunkPos(), structureStart.getStructure());
            return true;
        } else {
            return false;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    // For comparison with POI
//            PoiManager poiManager = ((ServerLevel)world).getPoiManager();
//            for (int i = 0; i < 1000; i++) {
//                long time31 = System.nanoTime();
//                List<PoiRecord> poiInRange2 = poiManager.getInSquare(
//                                (pointOfInterestType) -> pointOfInterestType.value() == BzPOI.BROOD_BLOCK_POI.get(),
//                                entity.blockPosition(),
//                                NEARBY_WRATH_EFFECT_RADIUS,
//                                PoiManager.Occupancy.ANY)
//                        .collect(Collectors.toList());
//                long time32 = System.nanoTime();
//
//                long time41 = System.nanoTime();
//                List<BlockPos> blockPosList2 = GeneralUtils.matchingBlocksOfKindInRange(
//                        world,
//                        entity.blockPosition(),
//                        NEARBY_WRATH_EFFECT_RADIUS,
//                        (b) -> b.is(BzBlocks.HONEYCOMB_BROOD.get()));
//                long time42 = System.nanoTime();
//
//                if (i == 999) {
//                    Bumblezone.LOGGER.warn("--------------------------------");
//                    Bumblezone.LOGGER.warn("A: {}", time32 - time31);
//                    Bumblezone.LOGGER.warn("B: {}", time42 - time41);
//                }
//            }

    /////////////////////////////////////////////////////////////////////////////////
}
