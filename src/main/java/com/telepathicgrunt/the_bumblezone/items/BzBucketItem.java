package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BzBucketItem extends BucketItem {
    public BzBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ActionResult<ItemStack> actionResult = super.use(world, playerEntity, hand);

        if(this.getFluid() == BzFluids.SUGAR_WATER_FLUID.get() && actionResult.getResult() == ActionResultType.CONSUME && playerEntity instanceof ServerPlayerEntity) {
            BlockRayTraceResult raytraceresult = getPlayerPOVHitResult(world, playerEntity, RayTraceContext.FluidMode.NONE);
            if(raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();
                Direction direction = raytraceresult.getDirection();
                BlockPos blockpos1 = blockpos.relative(direction);
                BlockState blockstate = world.getBlockState(blockpos);
                BlockPos blockpos2 = canBlockContainFluid(world, blockpos, blockstate) ? blockpos : blockpos1;

                boolean isNextToSugarCane = false;
                BlockPos.Mutable mutable = new BlockPos.Mutable();
                for(Direction directionOffset : Direction.Plane.HORIZONTAL) {
                    mutable.set(blockpos2).move(directionOffset).move(Direction.UP);
                    BlockState state = world.getBlockState(mutable);
                    if(state.is(Blocks.SUGAR_CANE)) {
                        isNextToSugarCane = true;
                        break;
                    }
                }

                if(isNextToSugarCane) {
                    BzCriterias.SUGAR_WATER_NEXT_TO_SUGAR_CANE_TRIGGER.trigger((ServerPlayerEntity) playerEntity);
                }
            }
        }

        return actionResult;
    }

    private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, getFluid());
    }
}
