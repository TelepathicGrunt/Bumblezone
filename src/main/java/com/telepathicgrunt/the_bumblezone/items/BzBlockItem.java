package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BzBlockItem extends BlockItem {
    public BzBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext blockItemUseContext, BlockState blockState) {
        Player playerEntity = blockItemUseContext.getPlayer();
        if(playerEntity instanceof ServerPlayer && this.getBlock() == BzBlocks.HONEY_CRYSTAL.get()) {
            FluidState fluidState = blockItemUseContext.getLevel().getFluidState(blockItemUseContext.getClickedPos());
            if(!fluidState.isEmpty() && fluidState.is(FluidTags.WATER) && fluidState.getType() != BzFluids.SUGAR_WATER_FLUID.get() && fluidState.getType() != BzFluids.SUGAR_WATER_FLUID_FLOWING.get()) {
                BzCriterias.HONEY_CRYSTAL_IN_WATER_TRIGGER.trigger((ServerPlayer) playerEntity);
            }
        }
        return blockItemUseContext.getLevel().setBlock(blockItemUseContext.getClickedPos(), blockState, 11);
    }
}
