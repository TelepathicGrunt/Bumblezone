package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import dev.architectury.injectables.annotations.PlatformOnly;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BzCustomBucketItem extends BzBucketItem {
    public final Fluid fluid;

    public BzCustomBucketItem(FluidInfo info, Properties builder) {
        super(info, builder);
        this.fluid = info.source().getSource();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> specialActionResult = PlatformHooks.performItemUse(world, playerEntity, hand, this.fluid, this);
        if (specialActionResult.getResult() != InteractionResult.PASS) {
            return specialActionResult;
        }

        InteractionResultHolder<ItemStack> actionResult = super.use(world, playerEntity, hand);

        if(getFluid() == BzFluids.SUGAR_WATER_FLUID.get() && actionResult.getResult() == InteractionResult.CONSUME && playerEntity instanceof ServerPlayer) {
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

    // Do not change signature of this method. It will automatically override Forge's canBlockContainFluid they patched into BucketItem.
    // We want to use our logic instead of Forge's.
    protected boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer &&
                (((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, getFluid()) ||
                ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, Fluids.WATER));
    }

    // Override and redirect forge patched method to our own.
    @PlatformOnly({"forge"})
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hitResult, @Nullable ItemStack container) {
        return emptyContents(player, world, pos, hitResult);
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (!(this.fluid instanceof FlowingFluid)) {
            return false;
        }
        else {
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            boolean canBucketPlace = blockState.canBeReplaced(this.fluid);
            boolean canPlaceFluid = blockState.isAir() || canBucketPlace;
            boolean feedVanillaWaterOverride = false;
            if (block instanceof LiquidBlockContainer) {
                if (((LiquidBlockContainer)block).canPlaceLiquid(world, pos, blockState, this.fluid)) {
                    canPlaceFluid = true;
                }
                if (this.fluid.is(FluidTags.WATER) && ((LiquidBlockContainer)block).canPlaceLiquid(world, pos, blockState, Fluids.WATER)) {
                    canPlaceFluid = true;
                    feedVanillaWaterOverride = true;
                }
            }

            if (!canPlaceFluid) {
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

                if (this.fluid.is(BzTags.SUGAR_WATER_FLUID) &&
                    world instanceof ServerLevel serverLevel &&
                    world.getServer() != null)
                {
                    Vec3 targetPos = hitResult != null ? hitResult.getLocation() : new Vec3(pos.getX(), pos.getY(), pos.getZ());

                    LootTable sugarWaterEvaporateLootTable = world.getServer().getLootData()
                            .getLootTable(new ResourceLocation(Bumblezone.MODID, "fluids/sugar_water_evaporates"));
                    LootParams lootParams = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.ORIGIN, targetPos)
                            .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                            .create(LootContextParamSets.COMMAND);
                    ObjectArrayList<ItemStack> evaporateItems = sugarWaterEvaporateLootTable.getRandomItems(lootParams);

                    for (ItemStack itemStackToSpawn : evaporateItems) {
                        ItemEntity itementity = new ItemEntity(world, targetPos.x(), targetPos.y(), targetPos.z(), itemStackToSpawn);
                        itementity.setDefaultPickUpDelay();
                        world.addFreshEntity(itementity);
                    }
                }

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
                if (!world.isClientSide && canBucketPlace && blockState.getFluidState().isEmpty()) {
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
