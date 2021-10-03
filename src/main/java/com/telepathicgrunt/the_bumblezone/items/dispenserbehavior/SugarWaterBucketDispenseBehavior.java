package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;


public class SugarWaterBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private static final DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        ServerWorld world = source.getLevel();
        BlockState blockstate = world.getBlockState(position);

        if (bucketitem.emptyBucket(null, world, position, null)) {

            bucketitem.checkExtraContent(world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL.get() && !blockstate.getValue(BlockStateProperties.WATERLOGGED)) {

            world.setBlockAndUpdate(position, BzBlocks.HONEY_CRYSTAL.get().defaultBlockState()
                    .setValue(BlockStateProperties.FACING, blockstate.getValue(BlockStateProperties.FACING))
                    .setValue(BlockStateProperties.WATERLOGGED, true));
            return new ItemStack(Items.BUCKET);
        }
        else {
            return this.DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }
}
