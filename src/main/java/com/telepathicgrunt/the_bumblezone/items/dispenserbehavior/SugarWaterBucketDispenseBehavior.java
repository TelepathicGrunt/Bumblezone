package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;


public class SugarWaterBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private static final DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(BlockSource source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        ServerLevel world = source.getLevel();

        if (bucketitem.emptyContents(null, world, position, null)) {
            bucketitem.checkExtraContent(null, world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else {
            return DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }
}
