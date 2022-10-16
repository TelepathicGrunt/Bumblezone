package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.mixin.entities.LivingEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzDamageSources;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
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
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class CrystallineFlower extends BaseEntityBlock {
    public static final BooleanProperty FLOWER =  BooleanProperty.create("flower");

    private static final Component CONTAINER_TITLE = Component.translatable(Bumblezone.MODID + ".container.crystalline_flower");

    public CrystallineFlower() {
        super(Properties.of(BzBlocks.ORANGE_CRYSTAL_PLANT, MaterialColor.COLOR_ORANGE).noCollission().noOcclusion().strength(0.4F, 0.01F).lightLevel((blockState) -> blockState.getValue(FLOWER) ? 7 : 0));
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
        if (entity instanceof LivingEntity livingEntity && !BeeAggression.isBeelikeEntity(livingEntity)) {
            livingEntity.makeStuckInBlock(state, new Vec3(0.95F, 2, 0.95F));
            if (!level.isClientSide && (livingEntity.xOld != livingEntity.getX() || livingEntity.zOld != livingEntity.getZ())) {
                double xDiff = Math.abs(livingEntity.getX() - livingEntity.xOld);
                double zDiff = Math.abs(livingEntity.getZ() - livingEntity.zOld);
                if (xDiff >= (double)0.001F || zDiff >= (double)0.001F) {
                    livingEntity.hurt(BzDamageSources.CRYSTALLINE_FLOWER, 1.5f);

                    if (livingEntity.isDeadOrDying() &&
                        !livingEntity.wasExperienceConsumed() &&
                        !((LivingEntityAccessor)livingEntity).callIsAlwaysExperienceDropper() &&
                        livingEntity.shouldDropExperience() &&
                        level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
                    {
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity && !crystallineFlowerBlockEntity.isMaxTier()) {
                            int reward = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(livingEntity, null, livingEntity.getExperienceReward());
                            ExperienceOrb.award((ServerLevel) level, livingEntity.position(), reward);
                        }
                    }
                }
            }
        }
        else if (entity instanceof ItemEntity itemEntity && BzGeneralConfigs.crystallineFlowerConsumeItemEntities.get()) {
            ItemStack stack = itemEntity.getItem();
            if (stack.is(BzTags.CAN_BE_ENCHANTED_ITEMS) || stack.is(BzTags.CANNOT_CONSUMED_ITEMS)) {
                return;
            }

            int bottomY = CrystallineFlower.flowerHeightBelow(level, pos);
            BlockPos bottomPos = pos.below(bottomY);
            BlockEntity blockEntity = level.getBlockEntity(bottomPos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity && !crystallineFlowerBlockEntity.isMaxTier()) {
                int tiersToMax = 7 - crystallineFlowerBlockEntity.getXpTier();
                int topBlock = CrystallineFlower.flowerHeightAbove(level, crystallineFlowerBlockEntity.getBlockPos());
                List<Boolean> obstructedAbove = CrystallineFlower.getObstructions(tiersToMax, level, crystallineFlowerBlockEntity.getBlockPos().above(topBlock + 1));

                int xpPerCount = getXPPerItem(stack);
                int itemCount = stack.getCount();
                int xpForStack = itemCount * xpPerCount;

                int xpToHighestAvailableTier = getXpToHighestAvailableTier(crystallineFlowerBlockEntity, tiersToMax, obstructedAbove);
                int xpGranted = Math.min(xpToHighestAvailableTier, xpForStack);
                int consumedItemCount = (int) Math.ceil(xpGranted / (float)xpPerCount);
                if (consumedItemCount == 0) {
                    return;
                }

                crystallineFlowerBlockEntity.addXpAndTier(xpGranted);
                stack.shrink(consumedItemCount);
                if (consumedItemCount >= itemCount) {
                    itemEntity.discard();
                }

                if (level instanceof ServerLevel serverLevel) {
                    spawnSparkleParticles(serverLevel, itemEntity.position(), level.random, (consumedItemCount / 3) + 5);
                }
            }
        }
        else if (entity instanceof ExperienceOrb experienceOrb && BzGeneralConfigs.crystallineFlowerConsumeExperienceOrbEntities.get()) {
            int bottomY = CrystallineFlower.flowerHeightBelow(level, pos);
            BlockPos bottomPos = pos.below(bottomY);
            BlockEntity blockEntity = level.getBlockEntity(bottomPos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity && !crystallineFlowerBlockEntity.isMaxTier()) {
                int tiersToMax = 7 - crystallineFlowerBlockEntity.getXpTier();
                int topBlock = CrystallineFlower.flowerHeightAbove(level, crystallineFlowerBlockEntity.getBlockPos());
                List<Boolean> obstructedAbove = CrystallineFlower.getObstructions(tiersToMax, level, crystallineFlowerBlockEntity.getBlockPos().above(topBlock + 1));

                int xpToHighestAvailableTier = getXpToHighestAvailableTier(crystallineFlowerBlockEntity, tiersToMax, obstructedAbove);
                int xpGranted = Math.min(xpToHighestAvailableTier, experienceOrb.value);

                crystallineFlowerBlockEntity.addXpAndTier(xpGranted);
                experienceOrb.value -= xpGranted;
                if (experienceOrb.value <= 0) {
                    experienceOrb.discard();
                }

                if (level instanceof ServerLevel serverLevel) {
                    spawnSparkleParticles(serverLevel, experienceOrb.position(), level.random, 3);
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
    public void neighborChanged(BlockState blockstate, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (canSurvive(blockstate, level, pos)) {
            boolean flowerSpot = isFlowerSpot(level, pos);
            if (flowerSpot != blockstate.getValue(FLOWER)) {
                level.setBlock(pos, blockstate.setValue(FLOWER, flowerSpot), 3);
            }
        }
        else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
                destroyEntirePlant(level, pos, null, !crystallineFlowerBlockEntity.getIsBreaking());
            }
            else {
                destroyEntirePlant(level, pos, null, true);
            }
        }

        super.neighborChanged(blockstate, level, pos, block, fromPos, notify);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        // Sync data to entire stack so they can update properly and do drops properly
        int flowerBlockAbove = flowerHeightAbove(level, pos);
        int flowerBlockBelow = flowerHeightBelow(level, pos);
        BlockEntity operatingBE = level.getBlockEntity(pos.below(flowerBlockBelow));
        if (operatingBE instanceof CrystallineFlowerBlockEntity) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int i = (-flowerBlockBelow) + 1; i <= flowerBlockAbove; i++) {
                mutableBlockPos.set(pos).move(Direction.UP, i);
                BlockEntity blockEntity = level.getBlockEntity(mutableBlockPos);
                if (blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                    crystallineFlowerBlockEntity2.load(operatingBE.getUpdateTag());
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
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

            if (flowerBlockBelow != 0) {
                BlockEntity targetBlockEntity = level.getBlockEntity(pos.below(flowerBlockBelow));
                if (targetBlockEntity instanceof CrystallineFlowerBlockEntity && originalFlowerBlockEntity instanceof CrystallineFlowerBlockEntity) {
                    targetBlockEntity.load(originalFlowerBlockEntity.getUpdateTag());
                }
            }

            for (int i = 1; i < Math.abs(tierChange); i++) {
                BlockPos currentPos = tickedPos.above((int) (Math.signum(tierChange) * i));

                level.setBlock(
                        currentPos,
                        upward ? BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState() : Blocks.AIR.defaultBlockState(),
                        3);

                if (upward) {
                    BlockEntity blockEntity2 = level.getBlockEntity(currentPos);
                    if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                        crystallineFlowerBlockEntity2.setGUID(crystallineFlowerBlockEntity.getGUID());
                    }
                }
            }

            tickedPos = tickedPos.above(tierChange);

            level.setBlock(
                    tickedPos,
                    BzBlocks.CRYSTALLINE_FLOWER.get().defaultBlockState().setValue(CrystallineFlower.FLOWER, true),
                    3);

            BlockEntity blockEntity2 = level.getBlockEntity(tickedPos);
            if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity2) {
                crystallineFlowerBlockEntity2.setGUID(crystallineFlowerBlockEntity.getGUID());
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return (belowState.is(BzBlocks.GLISTERING_HONEY_CRYSTAL.get()) || belowState.is(BzBlocks.CRYSTALLINE_FLOWER.get())) && flowerHeightBelow(level, pos) + flowerHeightAbove(level, pos) + 1 <= 7;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        destroyEntirePlant(level, pos, player, true);
    }

    private void destroyEntirePlant(Level level, BlockPos pos, Player player, boolean dropItem) {
        int flowerBlockAbove = flowerHeightAbove(level, pos);
        BlockPos flowerPos = pos.above(flowerBlockAbove);
        int flowerBlockBelow = flowerHeightBelow(level, flowerPos);
        BlockPos bottomPos = flowerPos.below(flowerBlockBelow);
        BlockEntity blockEntity = level.getBlockEntity(bottomPos);

        // Tells entire stack of the plant that they are in destroy phase so neighbor change doesn't cuase multiple drops
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= flowerBlockBelow; i++) {
            mutableBlockPos.set(flowerPos).move(Direction.DOWN, i);
            BlockEntity blockEntity2 = level.getBlockEntity(mutableBlockPos);
            if (blockEntity2 instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
                crystallineFlowerBlockEntity.setIsBreaking(true);
            }
        }

        for (int i = 1; i <= flowerBlockBelow; i++) {
            mutableBlockPos.set(flowerPos).move(Direction.DOWN, i);
            level.destroyBlock(mutableBlockPos, false, player, 0);
        }

        if (player != null && player.getAbilities().instabuild) {
            super.playerWillDestroy(level, flowerPos, level.getBlockState(flowerPos), player);
            return;
        }

        if (dropItem && blockEntity instanceof CrystallineFlowerBlockEntity crystallineFlowerBlockEntity) {
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

    public static int getXpToHighestAvailableTier(CrystallineFlowerBlockEntity crystallineFlowerBlockEntity, int tiersToMax, List<Boolean> obstructedAbove) {
        int xpToHighestAvailableTier = 0;
        for (int i = 1; i <= tiersToMax; i++) {
            int xpToDesiredTier = crystallineFlowerBlockEntity.getXpForNextTiers(i);
            if (i - 1 < obstructedAbove.size() && obstructedAbove.get(i - 1)) {
                xpToHighestAvailableTier = xpToDesiredTier - 1;
                break;
            }
            xpToHighestAvailableTier = xpToDesiredTier;
        }
        return xpToHighestAvailableTier;
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

    private void spawnSparkleParticles(Level world, Vec3 position, RandomSource random, int particleCount) {
        ((ServerLevel)world).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                position.x(),
                position.y() + 0.5d,
                position.z(),
                particleCount,
                random.nextDouble() / 8 + 0.2d,
                random.nextDouble() / 8 + 0.2d,
                random.nextDouble() / 8 + 0.2d,
                0.1D);
    }
}
