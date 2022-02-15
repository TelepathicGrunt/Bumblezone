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
    private final boolean fitInContainers;

    public BzBlockItem(Block block, Properties properties) {
        this(block, properties, true);
    }

    public BzBlockItem(Block block, Properties properties, boolean fitInContainers) {
        super(block, properties);
        this.fitInContainers = fitInContainers;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return fitInContainers;
    }
}
