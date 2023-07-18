package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructureTemplateAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.ServerLevelAccessor;
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
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
                || bee.isVehicle());
    }

    public static int getNearbyActiveEntitiesInDimension(ServerLevel level, BlockPos position) {
        if (level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            return ACTIVE_ENTITIES;
        }
        else {
            return level.getEntitiesOfClass(
                    Bee.class,
                    new AABB(
                            position.offset(-16, -16,-16),
                            position.offset(16, 16,16)
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
            ItemStack in = new ItemStack(this.itemToTrade, this.amountToGive);
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
        String joint = jigsaw1.nbt().getString("joint");
        if(joint.isEmpty()) {
            joint = prop1.front().getAxis().isHorizontal() ? "aligned" : "rollable";
        }

        boolean isRollable = joint.equals("rollable");
        return prop1.front() == prop2.front().getOpposite() &&
                (isRollable || prop1.top() == prop2.top()) &&
                jigsaw1.nbt().getString("target").equals(jigsaw2.nbt().getString("name"));
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

    public static List<Block> getListOfNonDummyBlocks(Optional<HolderSet.Named<Block>> blockTagResult) {
        return blockTagResult.map(holders -> holders
                .stream()
                .map(Holder::value)
                .filter(block -> !block.defaultBlockState().isAir() && !block.getClass().getName().endsWith("BlockDummyAir"))
                .toList()
            ).orElseGet(ArrayList::new);
    }

    public static <B, T extends B> boolean isInTag(Registry<B> registry, TagKey<B> key, T value) {
        return registry.getTag(key)
                .stream()
                .flatMap(HolderSet.ListBacked::stream)
                .filter(Holder::isBound)
                .anyMatch(holder -> holder.value() == value);
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
        ArrayList<BlockPos> list2 = Lists.newArrayListWithCapacity(structurePlaceSettings.shouldKeepLiquids() ? list.size() : 0);
        ArrayList<BlockPos> list3 = Lists.newArrayListWithCapacity(structurePlaceSettings.shouldKeepLiquids() ? list.size() : 0);
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
            FluidState fluidState = structurePlaceSettings.shouldKeepLiquids() ? serverLevelAccessor.getFluidState(blockPos3) : null;
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
                blockEntity.load(structureBlockInfo.nbt());
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
                    ((Mob)entity).finalizeSpawn(serverLevelAccessor, serverLevelAccessor.getCurrentDifficultyAt(BlockPos.containing(vec32)), MobSpawnType.STRUCTURE, null, compoundTag);
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
}
