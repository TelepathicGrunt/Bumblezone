package net.telepathicgrunt.bumblezone.modcompatibility;

import com.bagel.buzzierbees.common.dispenser.BeeBottleDispenseBehavior;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombLarvaBlock;
import net.telepathicgrunt.bumblezone.items.HoneyBottleDispenseBehavior;


public class BuzzierBeeBottledBeeDispenseBehavior extends DefaultDispenseItemBehavior
{
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
	{
		World world = source.getWorld();
		IPosition iposition = DispenserBlock.getDispensePosition(source);
		BlockPos position = new BlockPos(iposition);
		BlockState blockstate = world.getBlockState(position);

		if (blockstate.getBlock() == BzBlocks.DEAD_HONEYCOMB_LARVA.get())
		{
			world.setBlockState(position, blockstate.with(HoneycombLarvaBlock.STAGE, Integer.valueOf(0)));
			stack.shrink(1);
			return new ItemStack(Items.GLASS_BOTTLE);
		}
		else
		{
			return new BeeBottleDispenseBehavior().dispenseStack(source, stack);
		}
	}


	/**
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource source)
	{
		source.getWorld().playEvent(1002, source.getBlockPos(), 0);
	}
}
