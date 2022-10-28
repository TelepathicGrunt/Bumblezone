package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoneyCocoon extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final VoxelShape shape;
    public static final int waterDropDelay = 150;

    public HoneyCocoon() {
        super(Properties.of(Material.EGG, MaterialColor.COLOR_YELLOW).strength(0.3F, 0.3F).randomTicks().noOcclusion().sound(SoundType.HONEY_BLOCK));
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));

        VoxelShape voxelshape = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 14.0D, 15.0D);
        voxelshape = Shapes.or(voxelshape, Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D));
        voxelshape = Shapes.or(voxelshape, Block.box(3.0D, 14.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        shape = voxelshape;
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
        builder.add(WATERLOGGED);
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
            world.scheduleTick(pos, blockstate.getFluidState().getType(), blockstate.getFluidState().getType().getTickDelay(world));
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
        if(!blockState.getValue(WATERLOGGED)) {
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
            if(blockEntity instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
                if (!honeyCocoonBlockEntity.isUnpackedLoottable()) {
                    return;
                }

                BlockState aboveState = serverLevel.getBlockState(blockPos.above());
                if (!aboveState.getCollisionShape(serverLevel, blockPos).isEmpty()) {
                    return;
                }

                List<Pair<ItemStack, Integer>> emptyBroods = new ArrayList<>();
                List<Pair<ItemStack, Integer>> beeFeeding = new ArrayList<>();
                for(int i = 0; i < honeyCocoonBlockEntity.getContainerSize(); i++) {
                    ItemStack itemStack = honeyCocoonBlockEntity.getItem(i);
                    if(!itemStack.isEmpty()) {
                        if(itemStack.getItem() == BzItems.EMPTY_HONEYCOMB_BROOD.get()) {
                            emptyBroods.add(new Pair<>(itemStack, i));
                        }

                        if(itemStack.is(BzTags.BEE_FEEDING_ITEMS)) {
                            beeFeeding.add(new Pair<>(itemStack, i));
                        }
                    }
                }

                if(emptyBroods.isEmpty() || beeFeeding.isEmpty()) {
                    return;
                }


                honeyCocoonBlockEntity.removeItem(emptyBroods.get(random.nextInt(emptyBroods.size())).getSecond(), 1);
                ItemStack consumedItem = honeyCocoonBlockEntity.removeItem(beeFeeding.get(random.nextInt(beeFeeding.size())).getSecond(), 1);
                if(consumedItem.hasCraftingRemainingItem()) {
                    ItemStack ejectedItem = consumedItem.getCraftingRemainingItem();
                    if(ejectedItem.isEmpty()) {
                        ejectedItem = ContainerCraftingRecipe.HARDCODED_EDGECASES_WITHOUT_CONTAINERS_SET.get(consumedItem.getItem()).getDefaultInstance();
                    }

                    if(!ejectedItem.isEmpty()) {
                        GeneralUtils.spawnItemEntity(serverLevel, blockPos, ejectedItem, 0, 0.2D);
                    }
                }

                boolean addedToInv = false;
                for(int i = 0; i < honeyCocoonBlockEntity.getContainerSize(); i++) {
                    ItemStack itemStack = honeyCocoonBlockEntity.getItem(i);
                    if (itemStack.isEmpty() || (itemStack.getItem() == BzItems.HONEYCOMB_BROOD.get() && itemStack.getCount() < 64)) {
                        if(itemStack.isEmpty()) {
                            honeyCocoonBlockEntity.setItem(i, BzItems.HONEYCOMB_BROOD.get().getDefaultInstance());
                        }
                        else {
                            itemStack.grow(1);
                        }

                        addedToInv = true;
                        break;
                    }
                }
                if(!addedToInv) {
                    GeneralUtils.spawnItemEntity(serverLevel, blockPos, BzItems.HONEYCOMB_BROOD.get().getDefaultInstance(), 0, 0.2D);
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(blockState.getValue(WATERLOGGED)) {
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
            if(blockEntity instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
                if (!honeyCocoonBlockEntity.isUnpackedLoottable()) {
                    return;
                }

                BlockState aboveState = serverLevel.getBlockState(blockPos.above());
                if (!(aboveState.getFluidState().is(FluidTags.WATER) && aboveState.getCollisionShape(serverLevel, blockPos).isEmpty())) {
                    return;
                }

                serverLevel.scheduleTick(blockPos, blockState.getBlock(), waterDropDelay);

                List<Pair<ItemStack, Integer>> itemStacks = new ArrayList<>();
                for(int i = 0; i < honeyCocoonBlockEntity.getContainerSize(); i++) {
                    ItemStack itemStack = honeyCocoonBlockEntity.getItem(i);
                    if(!itemStack.isEmpty()) {
                        itemStacks.add(new Pair<>(itemStack, i));
                    }
                }

                if(itemStacks.isEmpty()) {
                    return;
                }

                ItemStack takenItem = honeyCocoonBlockEntity.removeItem(itemStacks.get(random.nextInt(itemStacks.size())).getSecond(), 1);
                GeneralUtils.spawnItemEntity(serverLevel, blockPos, takenItem, 0, -0.2D);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world,
                                 BlockPos position, Player playerEntity,
                                 InteractionHand playerHand, BlockHitResult raytraceResult) {

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if (itemstack.getItem() == Items.GLASS_BOTTLE && blockstate.getValue(WATERLOGGED)) {

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);

            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get()), false, true);
            return InteractionResult.SUCCESS;
        }
        else if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        else {
            MenuProvider menuprovider = this.getMenuProvider(blockstate, world, position);
            if (menuprovider != null) {
                playerEntity.openMenu(menuprovider);
            }

            return InteractionResult.CONSUME;
        }
    }

    /**
     * Called by BlockItem after this block has been placed.
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockstate, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof HoneyCocoonBlockEntity) {
                ((HoneyCocoonBlockEntity)blockentity).setCustomName(itemStack.getHoverName());
            }
        }

        if (blockstate.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, BzFluids.SUGAR_WATER_FLUID.get(), BzFluids.SUGAR_WATER_FLUID.get().getTickDelay(level));
            level.scheduleTick(pos, blockstate.getBlock(), waterDropDelay);
        }
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter world, BlockPos blockPos, BlockState blockState, Fluid fluid) {
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
    public ItemStack pickupBlock(LevelAccessor world, BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(WATERLOGGED)) {
            world.setBlock(blockPos, blockState.setValue(WATERLOGGED, false), 3);
            return new ItemStack(BzItems.SUGAR_WATER_BUCKET.get());
        }
        else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof HoneyCocoonBlockEntity honeyCocoonBlockEntity) {
            if (!level.isClientSide() && player.isCreative() && !honeyCocoonBlockEntity.isEmpty()) {
                ItemStack itemStack = BzItems.HONEY_COCOON.get().getDefaultInstance();
                blockEntity.saveToItem(itemStack);
                if (honeyCocoonBlockEntity.hasCustomName()) {
                    itemStack.setHoverName(honeyCocoonBlockEntity.getCustomName());
                }

                CompoundTag parentTag = null;
                CompoundTag tag = itemStack.getTag();
                if(tag != null) {
                    if(tag.contains("BlockEntityTag")) {
                        parentTag = tag;
                        tag = tag.getCompound("BlockEntityTag");
                    }
                    if(!tag.contains("UUID")) {
                        if(parentTag != null) {
                            parentTag.putString("UUID", UUID.randomUUID().toString());
                        }
                        else {
                            tag.putString("UUID", UUID.randomUUID().toString());
                        }
                    }
                }

                ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemStack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            }
            else {
                honeyCocoonBlockEntity.unpackLootTable(player);
            }
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack itemStack) {
        if (itemStack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0 && player instanceof ServerPlayer serverPlayer) {
            BzCriterias.HONEY_COCOON_SILK_TOUCH_TRIGGER.trigger(serverPlayer);
        }

        super.playerDestroy(level, player, pos, state, blockEntity, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);
        CompoundTag compoundtag = BlockItem.getBlockEntityData(itemStack);
        if (compoundtag != null) {
            if (compoundtag.contains("LootTable", 8)) {
                tooltip.add(Component.literal("???????"));
            }

            if (compoundtag.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundtag, nonnulllist);
                int i = 0;
                int j = 0;

                for(ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableComponent mutablecomponent = itemstack.getHoverName().copy();
                            mutablecomponent.append(" x").append(String.valueOf(itemstack.getCount()));
                            tooltip.add(mutablecomponent);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add((Component.translatable("container.shulkerBox.more", j - i)).withStyle(ChatFormatting.ITALIC));
                }
            }
        }

    }

    /**
     * Called periodically clientside on blocks near the player to show honey particles.
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, RandomSource random) {
        if(!blockState.getValue(WATERLOGGED)) {
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
}
