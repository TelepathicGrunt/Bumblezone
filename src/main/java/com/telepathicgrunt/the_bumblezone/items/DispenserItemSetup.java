package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DispenserItemSetup {
    /**
     * Sets up Dispenser behaviors for Bumblezone's items
     */
    public static void setupDispenserBehaviors() {

        // Behavior for custom items

        IDispenseItemBehavior genericBucketDispenseBehavior = new DefaultDispenseItemBehavior() {
        	private final DefaultDispenseItemBehavior dispenserBehavior = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack execute(IBlockSource source, ItemStack stack) {
                BucketItem bucketitem = (BucketItem) stack.getItem();
                BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                World world = source.getLevel();
                BlockState blockstate = world.getBlockState(blockpos);

                if (bucketitem.emptyBucket(null, world, blockpos, null)) {

                    bucketitem.checkExtraContent(world, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                }
                else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL.get() && !blockstate.getValue(BlockStateProperties.WATERLOGGED)) {

                    world.setBlockAndUpdate(blockpos, BzBlocks.HONEY_CRYSTAL.get().defaultBlockState()
                            .setValue(BlockStateProperties.FACING, blockstate.getValue(BlockStateProperties.FACING))
                            .setValue(BlockStateProperties.WATERLOGGED, true));
                    return new ItemStack(Items.BUCKET);
                }
                else {
                    return this.dispenserBehavior.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BUCKET.get(), genericBucketDispenseBehavior); // adds compatibility with sugar water buckets in dispensers
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BOTTLE.get(), new SugarWaterBottleDispenseBehavior()); // adds compatibility with sugar water bottles in dispensers




        // Behavior chaining with vanilla items


        //grab the original bottle behaviors and set it as a fallback for our custom behavior
        //this is so we don't override another mod's Dispenser behavior that they set to the bottles.
        HoneyBottleDispenseBehavior.DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).bz_invokeGetDispenseMethod(new ItemStack(Items.HONEY_BOTTLE));

        GlassBottleDispenseBehavior.DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).bz_invokeGetDispenseMethod(new ItemStack(Items.GLASS_BOTTLE));

        EmptyBucketDispenseBehavior.DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).bz_invokeGetDispenseMethod(new ItemStack(Items.BUCKET));


        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new GlassBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.HONEY_BOTTLE, new HoneyBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.BUCKET, new EmptyBucketDispenseBehavior());
    }
}
