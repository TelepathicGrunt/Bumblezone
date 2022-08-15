package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class BzBlockItem extends BlockItem {
    private final boolean fitInContainers;
    private final boolean useBlockName;

    public BzBlockItem(Block block, Properties properties) {
        this(block, properties, true, true);
    }

    public BzBlockItem(Block block, Properties properties, boolean fitInContainers, boolean useBlockName) {
        super(block, properties);
        this.fitInContainers = fitInContainers;
        this.useBlockName = useBlockName;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return fitInContainers;
    }

    @Override
    public String getDescriptionId() {
        return this.useBlockName ? this.getBlock().getDescriptionId() : this.getOrCreateDescriptionId();
    }
}
