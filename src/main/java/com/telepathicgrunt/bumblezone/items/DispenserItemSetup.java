package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.mixin.blocks.DispenserAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DispenserItemSetup {
    /**
     * Sets up Dispenser behaviors for Bumblezone's items
     */
    public static void setupDispenserBehaviors() {

        // Behavior for custom items

        DispenserBehavior genericBucketDispenseBehavior = new ItemDispenserBehavior() {
        	private final ItemDispenserBehavior dispenserBehavior = new ItemDispenserBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
                BucketItem bucketitem = (BucketItem) stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                World world = source.getWorld();
                BlockState blockstate = world.getBlockState(blockpos);

                if (bucketitem.placeFluid(null, world, blockpos, null)) {

                    bucketitem.onEmptied(null, world, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                }
                else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && !blockstate.get(Properties.WATERLOGGED)) {

                    world.setBlockState(blockpos, BzBlocks.HONEY_CRYSTAL.getDefaultState()
                            .with(Properties.FACING, blockstate.get(Properties.FACING))
                            .with(Properties.WATERLOGGED, true));
                    return new ItemStack(Items.BUCKET);
                }
                else {
                    return this.dispenserBehavior.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BUCKET, genericBucketDispenseBehavior); // adds compatibility with sugar water buckets in dispensers
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BOTTLE, new SugarWaterBottleDispenseBehavior()); // adds compatibility with sugar water bottles in dispensers




        // Behavior chaining with vanilla items


        //grab the original bottle behaviors and set it as a fallback for our custom behavior
        //this is so we don't override another mod's Dispenser behavior that they set to the bottles.
        HoneyBottleDispenseBehavior.DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserAccessor) Blocks.DISPENSER).thebumblezone_invokeGetBehaviorForItem(new ItemStack(Items.HONEY_BOTTLE));

        GlassBottleDispenseBehavior.DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserAccessor) Blocks.DISPENSER).thebumblezone_invokeGetBehaviorForItem(new ItemStack(Items.GLASS_BOTTLE));

        EmptyBucketDispenseBehavior.DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR =
                ((DispenserAccessor) Blocks.DISPENSER).thebumblezone_invokeGetBehaviorForItem(new ItemStack(Items.BUCKET));


        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new GlassBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.HONEY_BOTTLE, new HoneyBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.BUCKET, new EmptyBucketDispenseBehavior());
    }
}
