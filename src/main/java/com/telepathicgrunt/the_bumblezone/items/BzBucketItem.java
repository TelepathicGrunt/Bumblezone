package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

public class BzBucketItem extends BucketItem {
    public BzBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> actionResult = super.use(world, playerEntity, hand);

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

    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, getFluid());
    }
}
