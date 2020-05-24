package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;


public class SugarWaterBlock extends FluidBlock {
    @SuppressWarnings("deprecation")
    public SugarWaterBlock(BaseFluid fluid, Block.Settings properties) {
        super(fluid, properties);
    }


    public SugarWaterBlock(BaseFluid baseFluid) {
        super(baseFluid, FabricBlockSettings.of(Material.WATER).noCollision().strength(100.0F, 100.0F).dropsNothing().build().velocityMultiplier(0.95F));
    }


    @Override
    public boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
        boolean flag = false;

        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN && world.getFluidState(pos.offset(direction)).matches(FluidTags.LAVA)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            FluidState ifluidstate = world.getFluidState(pos);
            if (ifluidstate.isStill()) {
                world.setBlockState(pos, BzBlocks.SUGAR_INFUSED_STONE.getDefaultState());
                this.triggerMixEffects(world, pos);
                return false;
            }

            if (ifluidstate.getHeight(world, pos) >= 0.44444445F) {
                world.setBlockState(pos, BzBlocks.SUGAR_INFUSED_COBBLESTONE.getDefaultState());
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
    public void onEntityCollision(BlockState state, World world, BlockPos position, Entity entity) {
        if (entity instanceof BeeEntity) {
            BeeEntity beeEntity = ((BeeEntity) entity);
            if (beeEntity.hurtTime == 0)
                beeEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 4, 0, false, false));
        }

        super.onEntityCollision(state, world, position, entity);
    }

    private void triggerMixEffects(IWorld world, BlockPos pos) {
        world.playLevelEvent(1501, pos, 0);
    }
}
