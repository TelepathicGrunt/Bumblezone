package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.modcompat.LootrCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HoneyCocoon extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<HoneyCocoon> CODEC = Block.simpleCodec(HoneyCocoon::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty IS_LOOT_CONTAINER = BooleanProperty.create("is_loot");

    protected final VoxelShape shape;
    public static final int waterDropDelay = 150;

    public HoneyCocoon() {
        this(Properties.of()
                .mapColor(MapColor.COLOR_YELLOW)
                .strength(0.3F, 0.3F)
                .randomTicks()
                .noOcclusion()
                .sound(SoundType.HONEY_BLOCK));
    }

    public HoneyCocoon(Properties properties) {
        super(properties);

        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE).setValue(IS_LOOT_CONTAINER, Boolean.FALSE));

        VoxelShape voxelshape = Block.box(1.0D, 3.0D, 1.0D, 15.0D, 13.0D, 15.0D);
        voxelshape = Shapes.joinUnoptimized(voxelshape, Block.box(3.0D, 0.0D, 3.0D, 13.0D, 3.0D, 13.0D), BooleanOp.OR);
        voxelshape = Shapes.joinUnoptimized(voxelshape, Block.box(3.0D, 13.0D, 3.0D, 13.0D, 16.0D, 13.0D), BooleanOp.OR);
        shape = voxelshape.optimize();
    }

    @Override
    public MapCodec<? extends HoneyCocoon> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState blockstate, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BzBlockEntities.HONEY_COCOON.get().create(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    /**
     * Setup properties
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, IS_LOOT_CONTAINER);
    }

    /**
     * Can be waterlogged so return sugar water fluid if so
     */
    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? BzFluids.SUGAR_WATER_FLUID.get().getSource(false) : super.getFluidState(state);
    }

    /**
     * begin fluid tick if waterlogged
     */
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState blockstate, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (blockstate.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(world));
            world.scheduleTick(currentPos, blockstate.getBlock(), waterDropDelay);
        }

        return super.updateShape(blockstate, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (blockstate.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(world));
            world.scheduleTick(pos, blockstate.getBlock(), waterDropDelay);
        }
        super.neighborChanged(blockstate, world, pos, block, fromPos, notify);
    }

    /**
     * sets waterlogging as well if replacing water
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return blockstate.setValue(WATERLOGGED, fluidstate.getType().is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && fluidstate.isSource());
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if (!blockState.getValue(WATERLOGGED)) {
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
            if (blockEntity instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
                if (!honeyCocoonBlockEntity.isUnpackedLoottable() || blockState.getValue(IS_LOOT_CONTAINER)) {
                    return;
                }

                BlockState aboveState = serverLevel.getBlockState(blockPos.above());
                if (!aboveState.getCollisionShape(serverLevel, blockPos).isEmpty()) {
                    return;
                }

                List<Pair<ItemStack, Integer>> emptyBroods = new ArrayList<>();
                List<Pair<ItemStack, Integer>> beeFeeding = new ArrayList<>();
                for (int i = 0; i < honeyCocoonBlockEntity.getContainerSize(); i++) {
                    ItemStack itemStack = honeyCocoonBlockEntity.getItem(i);
                    if (!itemStack.isEmpty()) {
                        if (itemStack.getItem() == BzItems.EMPTY_HONEYCOMB_BROOD.get()) {
                            emptyBroods.add(new Pair<>(itemStack, i));
                        }

                        if (itemStack.is(BzTags.BEE_FEEDING_ITEMS)) {
                            beeFeeding.add(new Pair<>(itemStack, i));
                        }
                    }
                }

                if (emptyBroods.isEmpty() || beeFeeding.isEmpty()) {
                    return;
                }

                honeyCocoonBlockEntity.removeItem(emptyBroods.get(random.nextInt(emptyBroods.size())).getSecond(), 1);
                ItemStack consumedItem = honeyCocoonBlockEntity.removeItem(beeFeeding.get(random.nextInt(beeFeeding.size())).getSecond(), 1);
                if (PlatformHooks.hasCraftingRemainder(consumedItem)) {
                    ItemStack ejectedItem = PlatformHooks.getCraftingRemainder(consumedItem);
                    if (ejectedItem.isEmpty()) {
                        ejectedItem = ContainerCraftingRecipe.HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.get(consumedItem.getItem()).getDefaultInstance();
                    }

                    if (!ejectedItem.isEmpty()) {
                        GeneralUtils.spawnItemEntity(serverLevel, blockPos, ejectedItem, 0, 0.2D);
                    }
                }

                boolean addedToInv = false;
                for (int i = 0; i < honeyCocoonBlockEntity.getContainerSize(); i++) {
                    ItemStack itemStack = honeyCocoonBlockEntity.getItem(i);
                    if (itemStack.isEmpty() || (itemStack.getItem() == BzItems.HONEYCOMB_BROOD.get() && itemStack.getCount() < 64)) {
                        if (itemStack.isEmpty()) {
                            honeyCocoonBlockEntity.setItem(i, BzItems.HONEYCOMB_BROOD.get().getDefaultInstance());
                        }
                        else {
                            itemStack.grow(1);
                        }

                        addedToInv = true;
                        break;
                    }
                }
                
                if (!addedToInv) {
                    GeneralUtils.spawnItemEntity(serverLevel, blockPos, BzItems.HONEYCOMB_BROOD.get().getDefaultInstance(), 0, 0.2D);
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(WATERLOGGED) && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
            if (blockEntity instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
                if (!honeyCocoonBlockEntity.isUnpackedLoottable() || blockState.getValue(IS_LOOT_CONTAINER)) {
                    return;
                }

                BlockState aboveState = serverLevel.getBlockState(blockPos.above());
                if (!(aboveState.getFluidState().is(FluidTags.WATER) && aboveState.getCollisionShape(serverLevel, blockPos).isEmpty())) {
                    return;
                }

                serverLevel.scheduleTick(blockPos, blockState.getBlock(), waterDropDelay);

                List<Pair<ItemStack, Integer>> itemStacks = new ArrayList<>();
                for (int i = 0; i < honeyCocoonBlockEntity.getContainerSize(); i++) {
                    ItemStack itemStack = honeyCocoonBlockEntity.getItem(i);
                    if (!itemStack.isEmpty()) {
                        itemStacks.add(new Pair<>(itemStack, i));
                    }
                }

                if (itemStacks.isEmpty()) {
                    return;
                }

                ItemStack takenItem = honeyCocoonBlockEntity.removeItem(itemStacks.get(random.nextInt(itemStacks.size())).getSecond(), 1);
                GeneralUtils.spawnItemEntity(serverLevel, blockPos, takenItem, 0, -0.2D);
            }
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockstate, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
         if (itemStack.getItem() == Items.GLASS_BOTTLE && blockstate.getValue(WATERLOGGED)) {

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);

            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false, true);
            return ItemInteractionResult.SUCCESS;
        }
        else if (world.isClientSide) {
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    BzSounds.HONEY_COCOON_OPEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            return ItemInteractionResult.SUCCESS;
        }
        else {
            MenuProvider menuprovider = null;
            if (ModChecker.lootrPresent && BzModCompatibilityConfigs.allowLootrCompat && blockstate.getValue(IS_LOOT_CONTAINER)) {
                if (world.getBlockEntity(position) instanceof HoneyCocoonBlockEntity blockEntity && blockEntity.getLootTable() != null) {
                    menuprovider = LootrCompat.getCocoonMenu((ServerPlayer) playerEntity, blockEntity);
                }
            }
            // IDE never realizes `ModChecker.lootrPresent` changes to true so it always thinks menuprovider is null.
            //noinspection ConstantValue
            if (menuprovider == null) {
                menuprovider = this.getMenuProvider(blockstate, world, position);
            }
            if (menuprovider != null) {
                playerEntity.openMenu(menuprovider);
            }

            return ItemInteractionResult.CONSUME;
        }
    }

    /**
     * Called by BlockItem after this block has been placed.
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockstate, LivingEntity livingEntity, ItemStack itemStack) {
       if (blockstate.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(level));
            level.scheduleTick(pos, blockstate.getBlock(), waterDropDelay);
        }
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return !blockState.getValue(WATERLOGGED) && fluid.is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && fluid.defaultFluidState().isSource();
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (!blockState.getValue(WATERLOGGED) && fluidState.getType().is(BzTags.CONVERTIBLE_TO_SUGAR_WATER) && fluidState.isSource()) {
            if (!world.isClientSide()) {
                world.setBlock(blockPos, blockState.setValue(WATERLOGGED, true), 3);
                world.scheduleTick(blockPos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(world));
            }
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public ItemStack pickupBlock(@Nullable Player player, LevelAccessor world, BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            world.setBlock(blockPos, blockState.setValue(WATERLOGGED, false), 3);
            return new ItemStack(BzItems.SUGAR_WATER_BUCKET.get());
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack itemStack) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0 && player instanceof ServerPlayer serverPlayer) {
            BzCriterias.HONEY_COCOON_SILK_TOUCH_TRIGGER.get().trigger(serverPlayer);
        }

        super.playerDestroy(level, player, pos, state, blockEntity, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        if (itemStack.has(DataComponents.CONTAINER_LOOT)) {
            return;
        }

        int i = 0;
        int j = 0;

        for(ItemStack itemStack2 : itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()) {
            ++j;
            if (i <= 4) {
                ++i;
                list.add(Component.translatable("container.the_bumblezone.honey_cocoon.item_count", itemStack2.getHoverName(), itemStack2.getCount()));
            }
        }

        if (j - i > 0) {
            list.add(Component.translatable("container.the_bumblezone.honey_cocoon.more", j - i).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    /**
     * Called periodically clientside on blocks near the player to show honey particles.
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, RandomSource random) {
        if (!blockState.getValue(WATERLOGGED)) {
            if (random.nextFloat() < 0.05F) {
                this.spawnHoneyParticles(world, position, random);
            }
        }
    }

    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle
     * method
     */
    private void spawnHoneyParticles(Level world, BlockPos position, RandomSource random) {
        double x = (random.nextDouble() * 14) + 1;
        double y = (random.nextDouble() * 6) + 5;
        double z = (random.nextDouble() * 14) + 1;

        if (random.nextBoolean()) {
            if (random.nextBoolean()) x = 0.8D;
            else x = 15.2;
        }
        else {
            if (random.nextBoolean()) z = 0.8D;
            else z = 15.2;
        }

        world.addParticle(ParticleTypes.FALLING_HONEY,
                (x / 16) + position.getX(),
                (y / 16) + position.getY(),
                (z / 16) + position.getZ(),
                0.0D,
                0.0D,
                0.0D);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return HoneyCocoon.createCocoonTicker(level, blockEntityType, BzBlockEntities.HONEY_COCOON.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createCocoonTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends HoneyCocoonBlockEntity> blockEntityType2) {
        return level.isClientSide ? null : HoneyCocoon.createTickerHelper(blockEntityType, blockEntityType2, HoneyCocoonBlockEntity::serverTick);
    }
}
