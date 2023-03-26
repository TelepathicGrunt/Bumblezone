package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BzSmartBucket extends BucketItem {
    public final Fluid fluid;

    public BzSmartBucket(Fluid fluid, Properties settings) {
        super(fluid, settings);
        this.fluid = fluid;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> actionResult = performItemUse(world, playerEntity, hand);

        if(fluid == BzFluids.SUGAR_WATER_FLUID && actionResult.getResult() == InteractionResult.CONSUME && playerEntity instanceof ServerPlayer) {
            BlockHitResult raytraceresult = getPlayerPOVHitResult(world, playerEntity, ClipContext.Fluid.NONE);
            if(raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                Direction direction = raytraceresult.getDirection();
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = world.getBlockState(blockpos);
                BlockPos blockpos2 = blockstate.getBlock() instanceof LiquidBlockContainer && this.fluid.is(FluidTags.WATER) ? blockpos : blockpos1;

                boolean isNextToSugarCane = false;
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for(Direction directionOffset : Direction.Plane.HORIZONTAL) {
                    mutable.set(blockpos2).move(directionOffset).move(Direction.UP);
                    BlockState state = world.getBlockState(mutable);
                    if(state.is(Blocks.SUGAR_CANE)) {
                        isNextToSugarCane = true;
                        break;
                    }
                }

                if(isNextToSugarCane) {
                    BzCriterias.SUGAR_WATER_NEXT_TO_SUGAR_CANE_TRIGGER.trigger((ServerPlayer) playerEntity);
                }
            }
        }

        return actionResult;
    }

    @NotNull
    private InteractionResultHolder<ItemStack> performItemUse(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, user, this.fluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }
        else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        }
        else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            if (world.mayInteract(user, blockPos) && user.mayUseItemAt(blockPos2, direction, itemStack)) {
                BlockState blockState;
                if (this.fluid == Fluids.EMPTY) {
                    blockState = world.getBlockState(blockPos);
                    if (blockState.getBlock() instanceof BucketPickup fluidDrainable) {
                        ItemStack itemStack2 = fluidDrainable.pickupBlock(world, blockPos, blockState);
                        if (!itemStack2.isEmpty()) {
                            user.awardStat(Stats.ITEM_USED.get(this));
                            fluidDrainable.getPickupSound().ifPresent((sound) -> user.playSound(sound, 1.0F, 1.0F));
                            world.gameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, user, itemStack2);
                            if (!world.isClientSide()) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) user, itemStack2);
                            }

                            return InteractionResultHolder.sidedSuccess(itemStack3, world.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(itemStack);
                }
                else {
                    blockState = world.getBlockState(blockPos);
                    BlockPos blockPos3 = blockState.getBlock() instanceof LiquidBlockContainer && this.fluid.is(FluidTags.WATER) ? blockPos : blockPos2;
                    if (this.emptyContents(user, world, blockPos3, blockHitResult)) {
                        this.checkExtraContent(user, world, itemStack, blockPos3);
                        if (user instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) user, blockPos3, itemStack);
                        }

                        user.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemStack, user), world.isClientSide());
                    }
                    else {
                        return InteractionResultHolder.fail(itemStack);
                    }
                }
            }
            else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (!(this.fluid instanceof FlowingFluid)) {
            return false;
        }
        else {
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            Material material = blockState.getMaterial();
            boolean canBucketPlace = blockState.canBeReplaced(this.fluid);
            boolean canFillBlock = blockState.isAir() || canBucketPlace;
            boolean feedVanillaWaterOverride = false;
            if (!canFillBlock && block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(world, pos, blockState, this.fluid)) {
                canFillBlock = true;
            }
            else if (this.fluid.is(FluidTags.WATER) && !canFillBlock && block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(world, pos, blockState, Fluids.WATER)) {
                canFillBlock = true;
                feedVanillaWaterOverride = true;
            }

            if (!canFillBlock) {
                return hitResult != null && this.emptyContents(player, world, hitResult.getBlockPos().relative(hitResult.getDirection()), null);
            }
            else if (world.dimensionType().ultraWarm() && this.fluid.is(FluidTags.WATER)) {
                double x = pos.getX();
                double y = pos.getY();
                double z = pos.getZ();
                world.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    world.addParticle(ParticleTypes.LARGE_SMOKE, x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                x = hitResult != null ? hitResult.getLocation().x() : pos.getX();
                y = hitResult != null ? hitResult.getLocation().y() : pos.getY();
                z = hitResult != null ? hitResult.getLocation().z() : pos.getZ();
                ItemEntity itementity = new ItemEntity(world, x, y, z, Items.SUGAR.getDefaultInstance());
                itementity.setDefaultPickUpDelay();
                world.addFreshEntity(itementity);

                return true;
            }
            else if (block instanceof LiquidBlockContainer && this.fluid.is(FluidTags.WATER)) {
                if (feedVanillaWaterOverride) {
                    ((LiquidBlockContainer)block).placeLiquid(world, pos, blockState, Fluids.WATER.getSource(false));
                }
                else {
                    ((LiquidBlockContainer)block).placeLiquid(world, pos, blockState, ((FlowingFluid)this.fluid).getSource(false));
                }
                this.playEmptySound(player, world, pos);
                return true;
            }
            else {
                if (!world.isClientSide && canBucketPlace && !material.isLiquid()) {
                    world.destroyBlock(pos, true);
                }

                if (!world.setBlock(pos, this.fluid.defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE) && !blockState.getFluidState().isSource()) {
                    return false;
                }
                else {
                    this.playEmptySound(player, world, pos);
                    return true;
                }
            }
        }
    }
}
