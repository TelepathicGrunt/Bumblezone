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
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BzBucketItem extends BucketItem {
    public BzBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> actionResult = useTemporary(world, playerEntity, hand);

        if(this.getFluid() == BzFluids.SUGAR_WATER_FLUID.get() && actionResult.getResult() == InteractionResult.CONSUME && playerEntity instanceof ServerPlayer) {
            BlockHitResult raytraceresult = getPlayerPOVHitResult(world, playerEntity, ClipContext.Fluid.NONE);
            if(raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                Direction direction = raytraceresult.getDirection();
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = world.getBlockState(blockpos);
                BlockPos blockpos2 = canBlockContainFluid(world, blockpos, blockstate) ? blockpos : blockpos1;

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

    protected boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer &&
                (((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, getFluid()) ||
                ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, Fluids.WATER));
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (!(this.getFluid() instanceof FlowingFluid)) {
            return false;
        }
        else {
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            Material material = blockState.getMaterial();
            boolean canBucketPlace = blockState.canBeReplaced(this.getFluid());
            boolean canPlaceFluid = blockState.isAir() || canBucketPlace;
            boolean feedVanillaWaterOverride = false;
            if (block instanceof LiquidBlockContainer) {
                if (((LiquidBlockContainer)block).canPlaceLiquid(world, pos, blockState, this.getFluid())) {
                    canPlaceFluid = true;
                }
                if (this.getFluid().is(FluidTags.WATER) && ((LiquidBlockContainer)block).canPlaceLiquid(world, pos, blockState, Fluids.WATER)) {
                    canPlaceFluid = true;
                    feedVanillaWaterOverride = true;
                }
            }

            if (!canPlaceFluid) {
                return hitResult != null && this.emptyContents(player, world, hitResult.getBlockPos().relative(hitResult.getDirection()), null);
            }
            else if (world.dimensionType().ultraWarm() && this.getFluid().is(FluidTags.WATER)) {
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
            else if (block instanceof LiquidBlockContainer && this.getFluid().is(FluidTags.WATER)) {
                if (feedVanillaWaterOverride) {
                    ((LiquidBlockContainer)block).placeLiquid(world, pos, blockState, Fluids.WATER.getSource(false));
                }
                else {
                    ((LiquidBlockContainer)block).placeLiquid(world, pos, blockState, ((FlowingFluid)this.getFluid()).getSource(false));
                }
                this.playEmptySound(player, world, pos);
                return true;
            }
            else {
                if (!world.isClientSide && canBucketPlace && !material.isLiquid()) {
                    world.destroyBlock(pos, true);
                }

                if (!world.setBlock(pos, this.getFluid().defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE) && !blockState.getFluidState().isSource()) {
                    return false;
                }
                else {
                    this.playEmptySound(player, world, pos);
                    return true;
                }
            }
        }
    }

    // Temporary till forge makes canBlockContainFluid protected
    public InteractionResultHolder<ItemStack> useTemporary(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, this.getFluid() == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
        if (ret != null) return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        }
        else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        }
        else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                if (this.getFluid() == Fluids.EMPTY) {
                    BlockState blockstate1 = pLevel.getBlockState(blockpos);
                    if (blockstate1.getBlock() instanceof BucketPickup bucketpickup) {
                        ItemStack itemstack1 = bucketpickup.pickupBlock(pLevel, blockpos, blockstate1);
                        if (!itemstack1.isEmpty()) {
                            pPlayer.awardStat(Stats.ITEM_USED.get(this));
                            bucketpickup.getPickupSound(blockstate1).ifPresent((p_150709_) -> {
                                pPlayer.playSound(p_150709_, 1.0F, 1.0F);
                            });
                            pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockpos);
                            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, pPlayer, itemstack1);
                            if (!pLevel.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)pPlayer, itemstack1);
                            }

                            return InteractionResultHolder.sidedSuccess(itemstack2, pLevel.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(itemstack);
                }
                else {
                    BlockState blockstate = pLevel.getBlockState(blockpos);
                    BlockPos blockpos2 = canBlockContainFluid(pLevel, blockpos, blockstate) ? blockpos : blockpos1;
                    if (this.emptyContents(pPlayer, pLevel, blockpos2, blockhitresult)) {
                        this.checkExtraContent(pPlayer, pLevel, itemstack, blockpos2);
                        if (pPlayer instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)pPlayer, blockpos2, itemstack);
                        }

                        pPlayer.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
                    }
                    else {
                        return InteractionResultHolder.fail(itemstack);
                    }
                }
            }
            else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}
