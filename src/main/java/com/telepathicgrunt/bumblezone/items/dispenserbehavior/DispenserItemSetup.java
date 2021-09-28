package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

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
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BUCKET, new SugarWaterBucketDispenseBehavior()); // adds compatibility with sugar water buckets in dispensers
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
