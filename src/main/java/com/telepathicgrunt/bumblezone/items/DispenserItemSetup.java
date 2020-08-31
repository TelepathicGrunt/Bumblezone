package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.mixin.DispenserBlockInvoker;
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
import com.telepathicgrunt.bumblezone.blocks.BzBlocks;

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
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                BucketItem bucketitem = (BucketItem) stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                World world = source.getWorld();
                BlockState blockstate = world.getBlockState(blockpos);

                if (bucketitem.tryPlaceContainedLiquid(null, world, blockpos, null)) {

                    bucketitem.onLiquidPlaced(world, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                }
                else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && !blockstate.get(BlockStateProperties.WATERLOGGED)) {

                    world.setBlockState(blockpos, BzBlocks.HONEY_CRYSTAL.getDefaultState()
                            .with(BlockStateProperties.FACING, blockstate.get(BlockStateProperties.FACING))
                            .with(BlockStateProperties.WATERLOGGED, true));
                    return new ItemStack(Items.BUCKET);
                }
                else {
                    return this.dispenserBehavior.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerDispenseBehavior(BzItems.SUGAR_WATER_BUCKET, genericBucketDispenseBehavior); // adds compatibility with sugar water buckets in dispensers
        DispenserBlock.registerDispenseBehavior(BzItems.SUGAR_WATER_BOTTLE, new SugarWaterBottleDispenseBehavior()); // adds compatibility with sugar water bottles in dispensers




        // Behavior chaining with vanilla items


        //grab the original bottle behaviors and set it as a fallback for our custom behavior
        //this is so we don't override another mod's Dispenser behavior that they set to the bottles.
        HoneyBottleDispenseBehavior.DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.HONEY_BOTTLE));

        GlassBottleDispenseBehavior.DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.GLASS_BOTTLE));

        EmptyBucketDispenseBehavior.DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR =
                ((DispenserBlockInvoker) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.BUCKET));


        DispenserBlock.registerDispenseBehavior(Items.GLASS_BOTTLE, new GlassBottleDispenseBehavior());
        DispenserBlock.registerDispenseBehavior(Items.HONEY_BOTTLE, new HoneyBottleDispenseBehavior());
        DispenserBlock.registerDispenseBehavior(Items.BUCKET, new EmptyBucketDispenseBehavior());
    }
}
