package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;

public class BzBlockItem extends BlockItem {
    public BzBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext blockItemUseContext, BlockState blockState) {
        PlayerEntity playerEntity = blockItemUseContext.getPlayer();
        if(playerEntity instanceof ServerPlayerEntity && this.getBlock().is(BzBlocks.HONEY_CRYSTAL.get())) {
            FluidState fluidState = blockItemUseContext.getLevel().getFluidState(blockItemUseContext.getClickedPos());
            if(!fluidState.isEmpty() && fluidState.is(FluidTags.WATER) && fluidState.getType() != BzFluids.SUGAR_WATER_FLUID.get() && fluidState.getType() != BzFluids.SUGAR_WATER_FLUID_FLOWING.get()) {
                BzCriterias.HONEY_CRYSTAL_IN_WATER_TRIGGER.trigger((ServerPlayerEntity) playerEntity);
            }
        }
        return blockItemUseContext.getLevel().setBlock(blockItemUseContext.getClickedPos(), blockState, 11);
    }
}
