package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

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
