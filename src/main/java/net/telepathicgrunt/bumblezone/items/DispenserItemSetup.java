package net.telepathicgrunt.bumblezone.items;

import java.lang.reflect.Method;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class DispenserItemSetup
{
    /**
     * Sets up Dispenser behaviors for Bumblezone's items
     */
    public static void setupDispenserBehaviors() {
	IDispenseItemBehavior genericBucketDispenseBehavior = new DefaultDispenseItemBehavior() {
	    private final DefaultDispenseItemBehavior dispenserBehavior = new DefaultDispenseItemBehavior();

	    /**
	     * Dispense the specified stack, play the dispense sound and spawn particles.
	     */
	    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		BucketItem bucketitem = (BucketItem) stack.getItem();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
		World world = source.getWorld();

		if (bucketitem.tryPlaceContainedLiquid((PlayerEntity) null, world, blockpos, (BlockRayTraceResult) null)) {
		    bucketitem.onLiquidPlaced(world, stack, blockpos);
		    return new ItemStack(Items.BUCKET);
		}
		else {
		    return this.dispenserBehavior.dispense(source, stack);
		}
	    }
	};
	DispenserBlock.registerDispenseBehavior(BzItems.SUGAR_WATER_BUCKET.get(), genericBucketDispenseBehavior); // adds compatibility with sugar water buckets in dispensers
	DispenserBlock.registerDispenseBehavior(BzItems.SUGAR_WATER_BOTTLE.get(), new SugarWaterBottleDispenseBehavior()); // adds compatibility with sugar water bottles in dispensers
    }


    /**
     * For setting up Dispenser behaviors for items that other mods could modify as well.
     */
    public static void lateSetupDespenserBehavior() {
	//grab the original bottle behaviors and set it as a fallback for our custom behavior
	//this is so we don't override another mod's Dispenser behavior that they set to the bottles.
	try {
	    Method method = ObfuscationReflectionHelper.findMethod(DispenserBlock.class, "func_149940_a", ItemStack.class);
	    HoneyBottleDispenseBehavior.DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR = (IDispenseItemBehavior) (method.invoke(Blocks.DISPENSER, new ItemStack(Items.HONEY_BOTTLE)));
	    GlassBottleDispenseBehavior.DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR = (IDispenseItemBehavior) (method.invoke(Blocks.DISPENSER, new ItemStack(Items.GLASS_BOTTLE)));
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	
	DispenserBlock.registerDispenseBehavior(Items.GLASS_BOTTLE, new GlassBottleDispenseBehavior());
	DispenserBlock.registerDispenseBehavior(Items.HONEY_BOTTLE, new HoneyBottleDispenseBehavior());
    }
}
