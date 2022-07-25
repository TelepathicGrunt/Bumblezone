package com.telepathicgrunt.the_bumblezone.fluids;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;


public class SugarWaterBlock extends LiquidBlock {

    public SugarWaterBlock(FlowingFluid baseFluid) {
        super(baseFluid, QuiltBlockSettings.of(Material.WATER).noCollission().strength(100.0F, 100.0F).noLootTable().speedFactor(0.95F));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.shouldSpreadLiquid(world, pos, state)) {
            world.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(world));
        }
    }

    private boolean shouldSpreadLiquid(Level world, BlockPos pos, BlockState state)  {
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
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_STONE.defaultBlockState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockAndUpdate(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.defaultBlockState());
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
    public void entityInside(BlockState state, Level world, BlockPos position, Entity entity) {
        if (entity instanceof Bee beeEntity) {
            if (beeEntity.hurtMarked) beeEntity.heal(1);
        }

        super.entityInside(state, world, position, entity);
    }

    private void triggerMixEffects(Level world, BlockPos pos) {
        world.levelEvent(1501, pos, 0);
    }
}
