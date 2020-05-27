package net.telepathicgrunt.bumblezone.items;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.mixin.DispenserAccessor;

public class DispenserItemSetup {
    /**
     * Sets up Dispenser behaviors for Bumblezone's items
     */
    public static void setupDispenserBehaviors() {
        DispenserBehavior genericBucketDispenseBehavior = new ItemDispenserBehavior() {
        	private final ItemDispenserBehavior dispenserBehavior = new ItemDispenserBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            public ItemStack dispenseStack(BlockPointer source, ItemStack stack) {
                BucketItem bucketitem = (BucketItem) stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                World world = source.getWorld();

                if (bucketitem.placeFluid((PlayerEntity) null, world, blockpos, (BlockHitResult) null)) {
                    bucketitem.onEmptied(world, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.dispenserBehavior.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BUCKET, genericBucketDispenseBehavior); // adds compatibility with sugar water buckets in dispensers
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BOTTLE, new SugarWaterBottleDispenseBehavior()); // adds compatibility with sugar water bottles in dispensers
    }


    /**
     * For setting up Dispenser behaviors for items that other mods could modify as well.
     */
    public static void lateSetupDespenserBehavior() {
        //grab the original bottle behaviors and set it as a fallback for our custom behavior
        //this is so we don't override another mod's Dispenser behavior that they set to the bottles.
        HoneyBottleDispenseBehavior.DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR =
                (DispenserBehavior) ((DispenserAccessor) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.HONEY_BOTTLE));

        GlassBottleDispenseBehavior.DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR =
                (DispenserBehavior) ((DispenserAccessor) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.GLASS_BOTTLE));

        EmptyBucketDispenseBehavior.DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR =
                (DispenserBehavior) ((DispenserAccessor) Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Items.BUCKET));


        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new GlassBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.HONEY_BOTTLE, new HoneyBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.BUCKET, new EmptyBucketDispenseBehavior());
    }
}
