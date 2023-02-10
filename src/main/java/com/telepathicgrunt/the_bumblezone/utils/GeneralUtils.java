package com.telepathicgrunt.the_bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Doubles;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        newBeePos = new BlockPos(
                entity.getX() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1),
                Doubles.constrainToRange(entity.getY() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1), 1, 254),
                entity.getZ() + (entity.getRandom().nextInt(maxRadius) + minRadius) * (entity.getRandom().nextBoolean() ? 1 : -1));
        return newBeePos;
    }

    ////////////////

    // Source: https://dzone.com/articles/be-lazy-with-java-8
    public static final class Lazy<T> {

        private volatile T value;

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
        ItemStack playerItem = playerEntity.getItemInHand(hand);
        ItemStack copiedPlayerItem = playerItem.copy();

        if(shrinkCurrentItem) {
            playerItem.shrink(1);
        }

        // Give item itself to users
        if(!itemstackToGive.isEmpty()) {
            if (playerItem.isEmpty()) {
                // places result item in hand
                playerEntity.setItemInHand(hand, itemstackToGive);
            }
            // places result item in inventory
            else if (!playerEntity.getInventory().add(itemstackToGive)) {
                // drops result item if inventory is full
                playerEntity.drop(itemstackToGive, false);
            }
        }

        // give container item of player's item if specified
        if(giveContainerItem && copiedPlayerItem.getItem().hasCraftingRemainingItem()) {
            ItemStack containerItem = copiedPlayerItem.getItem().getCraftingRemainingItem().getDefaultInstance();
            if (playerEntity.getItemInHand(hand).isEmpty()) {
                // places result item in hand
                playerEntity.setItemInHand(hand, containerItem);
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
        FrontAndTop prop1 = jigsaw1.state.getValue(JigsawBlock.ORIENTATION);
        FrontAndTop prop2 = jigsaw2.state.getValue(JigsawBlock.ORIENTATION);
        String joint = jigsaw1.nbt.getString("joint");
        if(joint.isEmpty()) {
            joint = prop1.front().getAxis().isHorizontal() ? "aligned" : "rollable";
        }

        boolean isRollable = joint.equals("rollable");
        return prop1.front() == prop2.front().getOpposite() &&
                (isRollable || prop1.top() == prop2.top()) &&
                jigsaw1.nbt.getString("target").equals(jigsaw2.nbt.getString("name"));
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
        return blockState.isAir() || blockState.getMaterial().isLiquid() || blockState.getMaterial().isReplaceable() || blockState.is(BzBlocks.HONEY_CRYSTAL);
    }

    //////////////////////////////////////////////

    public static BlockPos getLowestLand(ChunkGenerator chunkGenerator, RandomState randomState, BlockPos center, LevelHeightAccessor heightLimitView, boolean canBeOnLiquid, boolean canBeInLiquid) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(center);
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

}
