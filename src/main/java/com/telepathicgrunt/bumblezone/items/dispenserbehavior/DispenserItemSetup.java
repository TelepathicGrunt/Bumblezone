package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.bumblezone.mixin.blocks.DispenserBlockAccessor;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;

public class DispenserItemSetup {
    /**
     * Sets up Dispenser behaviors for Bumblezone's items
     */
    public static void setupDispenserBehaviors() {

        // Behavior for custom items
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BUCKET, new SugarWaterBucketDispenseBehavior()); // adds compatibility with sugar water buckets in dispensers
        DispenserBlock.registerBehavior(BzItems.SUGAR_WATER_BOTTLE, new SugarWaterBottleDispenseBehavior()); // adds compatibility with sugar water bottles in dispensers
        DispenserBlock.registerBehavior(BzItems.HONEY_BUCKET, new HoneyFluidBucketDispenseBehavior()); // adds compatibility with honey buckets in dispensers

        // Behavior chaining with vanilla items

        //grab the original bottle behaviors and set it as a fallback for our custom behavior
        //this is so we don't override another mod's Dispenser behavior that they set to the bottles.
        HoneyBottleDispenseBehavior.DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserBlockAccessor) Blocks.DISPENSER).thebumblezone_invokeGetBehaviorForItem(new ItemStack(Items.HONEY_BOTTLE));

        GlassBottleDispenseBehavior.DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR =
                ((DispenserBlockAccessor) Blocks.DISPENSER).thebumblezone_invokeGetBehaviorForItem(new ItemStack(Items.GLASS_BOTTLE));

        EmptyBucketDispenseBehavior.DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR =
                ((DispenserBlockAccessor) Blocks.DISPENSER).thebumblezone_invokeGetBehaviorForItem(new ItemStack(Items.BUCKET));


        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, new GlassBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.HONEY_BOTTLE, new HoneyBottleDispenseBehavior());
        DispenserBlock.registerBehavior(Items.BUCKET, new EmptyBucketDispenseBehavior());
    }
}
