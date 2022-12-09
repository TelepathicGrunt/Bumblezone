package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BzBlockItem extends BlockItem {
    private final boolean fitInContainers;
    private final boolean useBlockName;
    private final BlockState blockState;

    public BzBlockItem(BlockState blockState, Properties properties) {
        super(blockState.getBlock(), properties);
        this.blockState = blockState;
        this.fitInContainers = true;
        this.useBlockName = true;
    }

    public BzBlockItem(Block block, Properties properties) {
        this(block, properties, true, true);
    }

    public BzBlockItem(Block block, Properties properties, boolean fitInContainers, boolean useBlockName) {
        super(block, properties);
        this.fitInContainers = fitInContainers;
        this.useBlockName = useBlockName;
        this.blockState = null;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return fitInContainers;
    }

    @Override
    public String getDescriptionId() {
        return this.useBlockName ? this.getBlock().getDescriptionId() : this.getOrCreateDescriptionId();
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState placingState = this.blockState == null ? this.getBlock().getStateForPlacement(context) : this.blockState;
        if (placingState != null && placingState.is(BzBlocks.CARVABLE_WAX.get())) {
            placingState = CarvableWax.getFacingStateForPlacement(placingState, context);
        }
        return placingState != null && this.canPlace(context, placingState) ? placingState : null;
    }
}
