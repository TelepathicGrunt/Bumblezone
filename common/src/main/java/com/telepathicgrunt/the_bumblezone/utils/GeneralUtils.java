package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureTemplateAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class GeneralUtils {

    private static int ACTIVE_ENTITIES = 0;
    private static final Set<Bee> BEE_SET = new HashSet<>();

    public static void updateEntityCount(ServerLevel world) {
        BEE_SET.clear();
        int counter = 0;
        for (Entity entity : world.getAllEntities()) {
            if (entity.isAlive() && entity instanceof LivingEntity) {
                counter++;
            }

            if(entity instanceof Bee) {
                BEE_SET.add((Bee)entity);
            }
        }

        ACTIVE_ENTITIES = counter;
        BEE_SET.removeIf(bee ->
                bee.isPersistenceRequired()
                || bee.hasHive()
                || bee.hasCustomName()
                || bee.isLeashed()
                || bee.isVehicle()
                || bee.isNoAi());
    }

    public static int getNearbyActiveEntitiesInDimension(ServerLevel level, BlockPos position) {
        if (level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            return ACTIVE_ENTITIES;
        }
        else {
            return level.getEntitiesOfClass(
                Bee.class,
                new AABB(
                    Vec3.atLowerCornerOf(position.offset(-16, -16,-16)),
                    Vec3.atLowerCornerOf(position.offset(16, 16,16))
                )
            ).size();
        }
    }

    public static void adjustEntityCountInBz(int adjust) {
        ACTIVE_ENTITIES += adjust;
    }

    public static Set<Bee> getAllWildBees() {
        return BEE_SET;
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


    //////////////////////////////////////////

    /**
     * For doing basic trades.
     * Very short and barebone to what I want
     */
    public static class BasicItemTrade implements VillagerTrades.ItemListing  {
        private final Item itemToTrade;
        private final Item itemToReceive;
        private final int amountToGive;
        private final int amountToReceive;
        protected final int maxUses;
        protected final int experience;
        protected final float multiplier;

        public BasicItemTrade(Item itemToTrade, Item itemToReceive, int amountToGive,  int amountToReceive) {
            this(itemToReceive, itemToTrade, amountToGive, amountToReceive, 20, 2, 0.05F);
        }

        public BasicItemTrade(Item itemToTrade, Item itemToReceive, int amountToGive, int amountToReceive, int maxUses, int experience, float multiplier) {
            this.itemToTrade = itemToTrade;
            this.itemToReceive = itemToReceive;
            this.amountToGive = amountToGive;
            this.amountToReceive = amountToReceive;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemCost in = new ItemCost(this.itemToTrade, this.amountToGive);
            ItemStack out = new ItemStack(this.itemToReceive, this.amountToReceive);
            return new MerchantOffer(in, out, this.maxUses, this.experience, this.multiplier);
        }
    }

    ///////////////////////

    public static final List<BlockState> VANILLA_CANDLES = ImmutableList.of(
            Blocks.CANDLE.defaultBlockState(),
            Blocks.CYAN_CANDLE.defaultBlockState(),
            Blocks.BLACK_CANDLE.defaultBlockState(),
            Blocks.BLUE_CANDLE.defaultBlockState(),
            Blocks.BROWN_CANDLE.defaultBlockState(),
            Blocks.GRAY_CANDLE.defaultBlockState(),
            Blocks.GREEN_CANDLE.defaultBlockState(),
            Blocks.LIGHT_BLUE_CANDLE.defaultBlockState(),
            Blocks.LIGHT_GRAY_CANDLE.defaultBlockState(),
            Blocks.LIME_CANDLE.defaultBlockState(),
            Blocks.MAGENTA_CANDLE.defaultBlockState(),
            Blocks.ORANGE_CANDLE.defaultBlockState(),
            Blocks.PINK_CANDLE.defaultBlockState(),
            Blocks.PURPLE_CANDLE.defaultBlockState(),
            Blocks.RED_CANDLE.defaultBlockState(),
            Blocks.WHITE_CANDLE.defaultBlockState(),
            Blocks.YELLOW_CANDLE.defaultBlockState()
    );

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
        if(giveContainerItem && PlatformHooks.hasCraftingRemainder(copiedPlayerItem)) {
            ItemStack containerItem = PlatformHooks.getCraftingRemainder(copiedPlayerItem);
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
    public static boolean canJigsawsAttach(StructureTemplate.StructureBlockInfo jigsaw1, StructureTemplate.StructureBlockInfo jigsaw2) {
        FrontAndTop prop1 = jigsaw1.state().getValue(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = jigsaw2.state().getValue(JigsawBlock.ORIENTATION);

        return prop1.front() == prop2.front().getOpposite() &&
                (prop1.top() == prop2.top() || isRollableJoint(jigsaw1, prop1)) &&
                getStringMicroOptimised(jigsaw1.nbt(), "target").equals(getStringMicroOptimised(jigsaw2.nbt(), "name"));
    }

    private static boolean isRollableJoint(StructureTemplate.StructureBlockInfo jigsaw1, FrontAndTop prop1) {
        String joint = getStringMicroOptimised(jigsaw1.nbt(), "joint");
        if(!joint.equals("rollable") && !joint.equals("aligned")) {
            return !prop1.front().getAxis().isHorizontal();
        }
        else {
            return joint.equals("rollable");
        }
    }

    public static String getStringMicroOptimised(CompoundTag tag, String key) {
        return tag.get(key) instanceof StringTag stringTag ? stringTag.getAsString() : "";
    }

    //////////////////////////////////////////////

    public static int getFirstLandYFromPos(LevelReader worldView, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(pos);
        ChunkAccess currentChunk = worldView.getChunk(mutable);
        BlockState currentState = currentChunk.getBlockState(mutable);

        while(mutable.getY() >= worldView.getMinBuildHeight() && isReplaceableByStructures(currentState)) {
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
                    serverLevel.random.nextGaussian() * randomXZSpeed,
                    ySpeed,
                    serverLevel.random.nextGaussian() * randomXZSpeed));
            itemEntity.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(itemEntity);
        }
    }

    //////////////////////////////////////////////

    public static void centerAllPieces(BlockPos targetPos, List<? extends StructurePiece> pieces) {
        if(pieces.isEmpty()) return;

        Vec3i structureCenter = pieces.get(0).getBoundingBox().getCenter();
        int xOffset = targetPos.getX() - structureCenter.getX();
        int zOffset = targetPos.getZ() - structureCenter.getZ();

        for(StructurePiece structurePiece : pieces) {
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
        return registry.getHolder(registry.getId(value)).orElseThrow().is(key);
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

    public static boolean isBlockAllowedForSugarWaterWaterlogging(BlockState blockState) {
        return blockState.is(BzTags.WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID) && !blockState.is(BzTags.FORCED_DISALLOW_WATERLOGGING_BLOCKS_WHEN_PLACED_IN_FLUID);
    }

    //////////////////////////////////////////////

    public static boolean isPermissionAllowedAtSpot(Level level, Entity entity, BlockPos pos, boolean placingBlock) {
        if (entity instanceof Player player && !player.mayInteract(level, pos)) {
            return false;
        }
        return PlatformHooks.isPermissionAllowedAtSpot(level, entity, pos, placingBlock);
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
        if (((StructureTemplateAccessor)structureTemplate).getBlocks().isEmpty()) {
            return;
        }
        List<StructureTemplate.StructureBlockInfo> list = structurePlaceSettings.getRandomPalette(((StructureTemplateAccessor)structureTemplate).getBlocks(), blockPos).blocks();
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
        for (StructureTemplate.StructureBlockInfo structureBlockInfo : list5) {
            BlockEntity blockEntity;
            BlockPos blockPos3 = structureBlockInfo.pos();
            if (boundingBox != null && !boundingBox.isInside(blockPos3)) continue;
            FluidState fluidState = structurePlaceSettings.shouldApplyWaterlogging() ? serverLevelAccessor.getFluidState(blockPos3) : null;
            BlockState blockState = structureBlockInfo.state().mirror(structurePlaceSettings.getMirror()).rotate(structurePlaceSettings.getRotation());
            if (structureBlockInfo.nbt() != null) {
                blockEntity = serverLevelAccessor.getBlockEntity(blockPos3);
                Clearable.tryClear(blockEntity);
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
                blockEntity.loadWithComponents(structureBlockInfo.nbt(), serverLevelAccessor.registryAccess());
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
                    serverLevelAccessor.blockUpdated(blockPos7, blockState3.getBlock());
                }
                if (pair.getSecond() == null || (blockEntity = serverLevelAccessor.getBlockEntity(blockPos7)) == null) continue;
                blockEntity.setChanged();
            }
        }

        if (!structurePlaceSettings.isIgnoreEntities()) {
            placeEntities(serverLevelAccessor, structureTemplate, blockPos, structurePlaceSettings.getMirror(), structurePlaceSettings.getRotation(), structurePlaceSettings.getRotationPivot(), boundingBox, structurePlaceSettings.shouldFinalizeEntities());
        }
    }

    private static void placeEntities(ServerLevelAccessor serverLevelAccessor, StructureTemplate structureTemplate, BlockPos blockPos, Mirror mirror, Rotation rotation, BlockPos blockPos2, @Nullable BoundingBox boundingBox, boolean bl) {
        for (StructureTemplate.StructureEntityInfo structureEntityInfo : ((StructureTemplateAccessor)structureTemplate).getEntityInfoList()) {
            BlockPos blockPos3 = StructureTemplate.transform(structureEntityInfo.blockPos, mirror, rotation, blockPos2).offset(blockPos);
            if (boundingBox != null && !boundingBox.isInside(blockPos3)) continue;
            CompoundTag compoundTag = structureEntityInfo.nbt.copy();
            Vec3 vec3 = StructureTemplate.transform(structureEntityInfo.pos, mirror, rotation, blockPos2);
            Vec3 vec32 = vec3.add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            ListTag listTag = new ListTag();
            listTag.add(DoubleTag.valueOf(vec32.x));
            listTag.add(DoubleTag.valueOf(vec32.y));
            listTag.add(DoubleTag.valueOf(vec32.z));
            compoundTag.put("Pos", listTag);
            compoundTag.remove("UUID");
            createEntityIgnoreException(serverLevelAccessor, compoundTag).ifPresent(entity -> {
                float f = entity.rotate(rotation);
                entity.moveTo(vec32.x, vec32.y, vec32.z, f + (entity.mirror(mirror) - entity.getYRot()), entity.getXRot());
                if (bl && entity instanceof Mob) {
                    ((Mob)entity).finalizeSpawn(serverLevelAccessor, serverLevelAccessor.getCurrentDifficultyAt(BlockPos.containing(vec32)), MobSpawnType.STRUCTURE, null);
                }
                serverLevelAccessor.addFreshEntityWithPassengers(entity);
            });
        }
    }

    private static Optional<Entity> createEntityIgnoreException(ServerLevelAccessor serverLevelAccessor, CompoundTag compoundTag) {
        try {
            return EntityType.create(compoundTag, serverLevelAccessor.getLevel());
        }
        catch (Exception exception) {
            return Optional.empty();
        }
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
        for (int xOffset = minChunkPos.x; xOffset <= maxChunkPos.x; xOffset++) {
            for (int zOffset = minChunkPos.z; zOffset <= maxChunkPos.z; zOffset++) {
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
        for (int i = chunk.getMinSection(); i < chunk.getMaxSection(); ++i) {
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
                        SectionPos.sectionToBlockCoord(chunk.getPos().x),
                        sectionWorldY,
                        SectionPos.sectionToBlockCoord(chunk.getPos().z));

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

    public static List<StructureStart> startsForStructure(LevelReader level, StructureManager structureManager, SectionPos sectionPos, Structure structure) {
        ChunkAccess chunkAccess = level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES);
        LongSet references = chunkAccess.getReferencesForStructure(structure);
        ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
        fillStartsForStructure(level, structureManager, chunkAccess, structure, references, builder::add);
        return builder.build();
    }

    public static void fillStartsForStructure(LevelReader level, StructureManager structureManager, ChunkAccess chunkAccess, Structure structure, LongSet references, Consumer<StructureStart> consumer) {
        for (long ref : references) {
            SectionPos sectionPos = SectionPos.of(new ChunkPos(ref), level.getMinSection());
            StructureStart structureStart = structureManager.getStartForStructure(sectionPos, structure, chunkAccess);
            if (structureStart != null && structureStart.isValid()) {
                consumer.accept(structureStart);
            }
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
