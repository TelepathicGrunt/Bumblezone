package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class PotionOfBeesBeePotionDispenseBehavior extends DefaultDispenseItemBehavior
{
    public static IDispenseItemBehavior DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR;
    public static IDispenseItemBehavior DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(IBlockSource source, ItemStack stack) {
		World world = source.getLevel();
		IPosition iposition = DispenserBlock.getDispensePosition(source);
		BlockPos position = new BlockPos(iposition);
		BlockState blockstate = world.getBlockState(position);

		if (blockstate.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD.get()) {
			world.setBlockAndUpdate(position, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState().setValue(HoneycombBrood.FACING, blockstate.getValue(EmptyHoneycombBrood.FACING)).setValue(HoneycombBrood.STAGE, Integer.valueOf(world.random.nextInt(3))));
			stack.shrink(1);

			if(!Bumblezone.BzBlockMechanicsConfig.dispensersDropGlassBottles.get()) {
					if (!stack.isEmpty())
						addGlassBottleToDispenser(source);
					else
						stack = new ItemStack(Items.GLASS_BOTTLE);
			}
			else {
				DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
			}

			return stack;
		}
		else {
			// Use whatever the regular Depenser behavior was
			if (PotionOfBeesCompat.POBIsSplashPotionOfBeesItem(stack.getItem())) {
				// If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
				// playing particles and sound twice due to dispense method having that by default.
				return GeneralUtils.dispenseStackProperly(source, stack, DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR);
			}
			else {
				return GeneralUtils.dispenseStackProperly(source, stack, DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR);
			}
		}
    }



	/**
     * Play the dispense sound from the specified block.
     */
    protected void playSound(IBlockSource source) {
	source.getLevel().levelEvent(1002, source.getPos(), 0);
    }
    
    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(IBlockSource source) {
		if (source.getEntity() instanceof DispenserTileEntity) {
			DispenserTileEntity dispenser = source.getEntity();
			ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
			if (!HopperTileEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
				DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
			}
		}
    }
}
