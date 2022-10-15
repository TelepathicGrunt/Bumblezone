package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CrystallineFlower extends BaseEntityBlock {
    public static final BooleanProperty FLOWER =  BooleanProperty.create("flower");

    private static final Component CONTAINER_TITLE = Component.translatable(Bumblezone.MODID + ".container.crystalline_flower");

    public CrystallineFlower() {
        super(Properties.of(BzBlocks.ORANGE_CRYSTAL_PLANT, MaterialColor.COLOR_ORANGE).noOcclusion().strength(0.4F, 0.01F).lightLevel((blockState) -> blockState.getValue(FLOWER) ? 7 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(FLOWER, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add().add(FLOWER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BzBlockEntities.CRYSTALLINE_FLOWER.get().create(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && !BeeAggression.isBeelikeEntity(entity)) {
            entity.makeStuckInBlock(state, new Vec3(0.9F, 0.8D, 0.9F));
            if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= (double)0.003F || d1 >= (double)0.003F) {
                    //TODO: custom damage source
                    entity.hurt(DamageSource.SWEET_BERRY_BUSH, 1.0F);
                }
            }
        }
        else if (entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity && !crystallineFlowerBlockEntity.isMaxTier()) {
                int tiersToMax = 7 - crystallineFlowerBlockEntity.getXpTier();
                int topBlock = CrystallineFlower.flowerHeightAbove(level, crystallineFlowerBlockEntity.getBlockPos());
                List<Boolean> obstructedAbove = CrystallineFlower.getObstructions(tiersToMax, level, crystallineFlowerBlockEntity.getBlockPos().above(topBlock + 1));

                int xpPerCount = getXPPerItem(stack);
                int itemCount = stack.getCount();
                int xpForStack = itemCount * xpPerCount;

                int xpToMaxTierAllowed = 0;
                for (int i = 1; i <= tiersToMax; i++) {
                    int xpToDesiredTier = crystallineFlowerBlockEntity.getXpForNextTiers(i);
                    if (i - 1 < obstructedAbove.size() && obstructedAbove.get(i - 1)) {
                        xpToMaxTierAllowed = xpToDesiredTier - 1;
                        break;
                    }
                    xpToMaxTierAllowed = xpToDesiredTier;
                }

                int xpGranted = Math.min(xpToMaxTierAllowed, xpForStack);
                int consumedItemCount = (int) Math.ceil(xpGranted / (float)xpPerCount);
                if (consumedItemCount == 0) {
                    return;
                }

                crystallineFlowerBlockEntity.addXpAndTier(xpGranted);
                stack.shrink(consumedItemCount);
                if (consumedItemCount >= itemCount) {
                    itemEntity.discard();
                }
            }
        }
        else if (entity instanceof ExperienceOrb experienceOrb) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity && !crystallineFlowerBlockEntity.isMaxTier()) {
                int tiersToMax = 7 - crystallineFlowerBlockEntity.getXpTier();
                int topBlock = CrystallineFlower.flowerHeightAbove(level, crystallineFlowerBlockEntity.getBlockPos());
                List<Boolean> obstructedAbove = CrystallineFlower.getObstructions(tiersToMax, level, crystallineFlowerBlockEntity.getBlockPos().above(topBlock + 1));

                int xpToMaxTierAllowed = 0;
                for (int i = 1; i <= tiersToMax; i++) {
                    int xpToDesiredTier = crystallineFlowerBlockEntity.getXpForNextTiers(i);
                    if (i - 1 < obstructedAbove.size() && obstructedAbove.get(i - 1)) {
                        xpToMaxTierAllowed = xpToDesiredTier - 1;
                        break;
                    }
                    xpToMaxTierAllowed = xpToDesiredTier;
                }

                int xpGranted = Math.min(xpToMaxTierAllowed, experienceOrb.value);
                crystallineFlowerBlockEntity.addXpAndTier(xpGranted);
                experienceOrb.value -= xpGranted;
                if (experienceOrb.value <= 0) {
                    experienceOrb.discard();
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            if (level.players().stream().noneMatch(p ->
                p.containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu &&
                crystallineFlowerMenu.crystallineFlowerBlockEntity.getGUID().equals(crystallineFlowerBlockEntity.getGUID())))
            {
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                }

                player.openMenu(state.getMenuProvider(level, pos));
                player.awardStat(BzStats.INTERACT_WITH_CRYSTALLINE_FLOWER_RL.get());
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.scheduleTick(pos, state.getBlock(), 0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos().below()).is(BzBlocks.CRYSTALLINE_FLOWER.get())) {
            return null;
        }

        BlockState defaultState = super.getStateForPlacement(context);
        return defaultState == null ? null : defaultState.setValue(FLOWER, isFlowerSpot(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (canSurvive(blockstate, world, pos)) {
            boolean flowerSpot = isFlowerSpot(world, pos);
            if (flowerSpot != blockstate.getValue(FLOWER)) {
                world.setBlock(pos, blockstate.setValue(FLOWER, flowerSpot), 3);
            }
        }
        else {
            playerWillDestroy(world, pos, blockstate, null);
        }

        super.neighborChanged(blockstate, world, pos, block, fromPos, notify);
    }

    @Deprecated
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            int tier = crystallineFlowerBlockEntity.getXpTier();

            int flowerBlockAbove = flowerHeightAbove(level, pos);
            int flowerBlockBelow = flowerHeightBelow(level, pos);
            int height = flowerBlockAbove + flowerBlockBelow + 1;
            if (tier == height) {
                return;
            }

            int tierChange = tier - height;
            boolean upward = tierChange > 0;
            BlockPos tickedPos = pos.above(flowerBlockBelow);
            BlockEntity originalFlowerBlockEntity = level.getBlockEntity(tickedPos);
            for (int i = 1; i < Math.abs(tierChange); i++) {
                level.setBlock(
                        tickedPos.above((int) (Math.signum(tierChange) * i)),
                        upward ? BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState() : Blocks.AIR.defaultBlockState(),
                        3);
            }

            level.setBlock(
                    tickedPos.above(tierChange),
                    BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState().setValue(CrystallineFlower.FLOWER, true),
                    3);

            if (flowerBlockBelow != 0) {
                BlockEntity targetBlockEntity = level.getBlockEntity(pos.below(flowerBlockBelow));
                if (targetBlockEntity instanceof CrystallineFlowerBlockEntity && originalFlowerBlockEntity instanceof CrystallineFlowerBlockEntity) {
                    targetBlockEntity.load(originalFlowerBlockEntity.getUpdateTag());
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP) && flowerHeightBelow(level, pos) + flowerHeightAbove(level, pos) + 1 <= 7;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        int flowerBlockAbove = flowerHeightAbove(level, pos);
        int flowerBlockBelow = flowerHeightBelow(level, pos);
        BlockPos flowerPos = pos.above(flowerBlockAbove);
        BlockPos bottomPos = pos.below(flowerBlockBelow);
        BlockState bottomState = level.getBlockState(bottomPos);
        BlockEntity blockEntity = level.getBlockEntity(bottomPos);

        for (int i = 1; i <= flowerBlockBelow + flowerBlockAbove; i++) {
            level.destroyBlock(flowerPos.below(i), false, player, 0);
        }

//        if (player != null && player.getAbilities().instabuild) {
//            super.playerWillDestroy(level, flowerPos, flowerState, player);
//            return;
//        }

        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            ItemStack itemStack = BzItems.CRYSTALLINE_FLOWER.get().getDefaultInstance();
            crystallineFlowerBlockEntity.saveToItem(itemStack);
            ItemEntity itementity = new ItemEntity(level, (double) flowerPos.getX() + 0.5D, (double) flowerPos.getY() + 0.5D, (double) flowerPos.getZ() + 0.5D, itemStack);
            itementity.setDefaultPickUpDelay();
            level.addFreshEntity(itementity);
        }
        level.destroyBlock(flowerPos, false, player, 0);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return flowerTotalHeight(level, pos);
    }

    public static boolean isFlowerSpot(Level level, BlockPos pos) {
        return !level.getBlockState(pos.above()).is(BzBlocks.CRYSTALLINE_FLOWER.get());
    }

    public static boolean isBottomSpot(Level level, BlockPos pos) {
        return !level.getBlockState(pos.below()).is(BzBlocks.CRYSTALLINE_FLOWER.get());
    }

    public static int flowerTotalHeight(LevelReader level, BlockPos pos) {
        return flowerHeightBelow(level, pos) + flowerHeightAbove(level, pos) + 1;
    }

    public static int flowerHeightBelow(LevelReader level, BlockPos pos) {
        int i = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockState currentState = level.getBlockState(mutable.set(pos.below()));
        while (currentState.is(BzBlocks.CRYSTALLINE_FLOWER.get()) && mutable.getY() >= level.getMinBuildHeight()) {
            i++;
            mutable.move(Direction.DOWN);
            currentState = level.getBlockState(mutable);
        }
        return i;
    }

    public static int flowerHeightAbove(LevelReader level, BlockPos pos) {
        int i = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockState currentState = level.getBlockState(mutable.set(pos.above()));
        while (currentState.is(BzBlocks.CRYSTALLINE_FLOWER.get()) && mutable.getY() <= level.getMaxBuildHeight()) {
            i++;
            mutable.move(Direction.UP);
            currentState = level.getBlockState(mutable);
        }
        return i;
    }

    public static List<Boolean> getObstructions(int scanArea, Level level, BlockPos pos) {
        List<Boolean> obstructions = new ArrayList<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(pos);

        for(int i = 0; i < scanArea; i++) {
            BlockState currentState = level.getBlockState(mutable);
            if (!currentState.isAir() && !currentState.is(BzBlocks.CRYSTALLINE_FLOWER.get())) {
                obstructions.add(true);
            }
            else {
                obstructions.add(false);
            }
            mutable.move(Direction.UP);
        }

        return obstructions;
    }

    public static int getXPPerItem(ItemStack stack) {
        if (stack.is(BzTags.XP_2_WHEN_CONSUMED_ITEMS)) {
            return 2;
        }
        else if (stack.is(BzTags.XP_5_WHEN_CONSUMED_ITEMS)) {
            return 5;
        }
        else if (stack.is(BzTags.XP_25_WHEN_CONSUMED_ITEMS)) {
            return 25;
        }
        else if (stack.is(BzTags.XP_100_WHEN_CONSUMED_ITEMS)) {
            return 100;
        }

        return 1;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        int searchLevel = 0;
        int searchTreasure = 0;

        int flowerBlockBelow = flowerHeightBelow(level, pos);
        BlockPos bottomPlant = pos.below(flowerBlockBelow);

        BlockEntity blockEntity = level.getBlockEntity(bottomPlant);
        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
            searchLevel = (crystallineFlowerBlockEntity.getXpTier() * 2) + 1;
            if (crystallineFlowerBlockEntity.getXpTier() > 5) {
                searchTreasure = 1;
            }
        }

        int finalSearchTreasure = searchTreasure;
        int finalSearchLevel = searchLevel;
        CrystallineFlowerBlockEntity finalCrystallineFlowerBlockEntity = blockEntity instanceof CrystallineFlowerBlockEntity ? (CrystallineFlowerBlockEntity) blockEntity : null;
        return new SimpleMenuProvider(
                (containerId, inventory, player) -> new CrystallineFlowerMenu(
                        containerId,
                        inventory,
                        ContainerLevelAccess.create(level, pos.below(flowerBlockBelow)),
                        finalSearchLevel,
                        finalSearchTreasure,
                        finalCrystallineFlowerBlockEntity
                ), CONTAINER_TITLE);
    }
}
