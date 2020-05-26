package net.telepathicgrunt.bumblezone.modcompatibility;

import com.bagel.buzzierbees.common.dispenser.BeeBottleDispenseBehavior;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.EmptyHoneycombBrood;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;


public class BuzzierBeeBottledBeeDispenseBehavior extends DefaultDispenseItemBehavior
{
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();
    
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
	World world = source.getWorld();
	IPosition iposition = DispenserBlock.getDispensePosition(source);
	BlockPos position = new BlockPos(iposition);
	BlockState blockstate = world.getBlockState(position);

	if (blockstate.getBlock() == BzBlocks.DEAD_HONEYCOMB_BROOD.get()) {
	    world.setBlockState(position, BzBlocks.HONEYCOMB_BROOD.get().getDefaultState().with(HoneycombBrood.FACING, blockstate.get(EmptyHoneycombBrood.FACING)).with(HoneycombBrood.STAGE, Integer.valueOf(0)));
	    stack.shrink(1);
	    if(Bumblezone.BzConfig.dispensersDropGlassBottles.get()) {
        	    if (!stack.isEmpty())
        		addGlassBottleToDispenser(source);
        	    else
        		stack = new ItemStack(Items.GLASS_BOTTLE);
	    }
	    else {
		DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
	    }

	    return new ItemStack(Items.GLASS_BOTTLE);
	}
	else {
	    return new BeeBottleDispenseBehavior().dispenseStack(source, stack);
	}
    }


    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource source) {
	source.getWorld().playEvent(1002, source.getBlockPos(), 0);
    }
    
    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(IBlockSource source) {
	if (source.getBlockTileEntity() instanceof DispenserTileEntity) {
	    DispenserTileEntity dispenser = (DispenserTileEntity) source.getBlockTileEntity();
	    ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
	    if (!HopperTileEntity.putStackInInventoryAllSlots(null, dispenser, honeyBottle, null).isEmpty()) {
		DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
	    }
	}
    }
}
