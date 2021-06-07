package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SugarWaterBlock extends FlowingFluidBlock {

    public SugarWaterBlock(java.util.function.Supplier<? extends FlowingFluid> supplier) {
        super(supplier, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F, 100.0F).noDrops().speedFactor(0.95F));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.receiveNeighborFluids(world, pos, state)) {
            world.getLiquidTicks().scheduleTick(pos, state.getFluidState().getType(), this.getFluid().getTickDelay(world));
        }
    }

    private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state)  {
        boolean flag = false;

        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN && world.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            FluidState ifluidstate = world.getFluidState(pos);
            if (ifluidstate.isSource()) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_STONE.get().defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }
        }

        return true;
    }


    /**
     * Heal bees slightly if they are in Sugar Water and aren't taking damage.
     */
    @Deprecated
    @Override
    public void entityInside(BlockState state, World world, BlockPos position, Entity entity) {
        if (entity instanceof BeeEntity) {
            BeeEntity beeEntity = ((BeeEntity) entity);
            if (beeEntity.hurtTime == 0)
                beeEntity.addEffect(new EffectInstance(
                        Effects.REGENERATION,
                        4,
                        0,
                        false,
                        false));
        }

        super.entityInside(state, world, position, entity);
    }

    private void triggerMixEffects(World world, BlockPos pos) {
        world.levelEvent(1501, pos, 0);
    }
}
