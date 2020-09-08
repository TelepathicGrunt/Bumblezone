package net.telepathicgrunt.bumblezone.modCompat;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.EmptyHoneycombBrood;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;



public class PotionOfBeesBeePotionDispenseBehavior extends ItemDispenserBehavior
{
    public static DispenserBehavior DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR;
    public static DispenserBehavior DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR;
    public static ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
		World world = source.getWorld();
		Position iposition = DispenserBlock.getOutputLocation(source);
		BlockPos position = new BlockPos(iposition);
		BlockState blockstate = world.getBlockState(position);

		if (blockstate.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD) {
			world.setBlockState(position, BzBlocks.HONEYCOMB_BROOD.getDefaultState().with(HoneycombBrood.FACING, blockstate.get(EmptyHoneycombBrood.FACING)).with(HoneycombBrood.STAGE, world.random.nextInt(3)));
			stack.decrement(1);

			if(!Bumblezone.BZ_CONFIG.BZBlockMechanicsConfig.dispensersDropGlassBottles) {
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
			// Use whatever the regular Dispenser behavior was
			if (PotionOfBeesCompat.POBIsSplashPotionOfBeesItem(stack.getItem())) {
				return DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR.dispense(source, stack);
			}
			else {
				return DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR.dispense(source, stack);
			}
		}
    }


    /**
     * Play the dispense sound from the specified block.
     */
    protected void playSound(BlockPointer source) {
	source.getWorld().syncWorldEvent(1002, source.getBlockPos(), 0);
    }
    
    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(BlockPointer source) {
		if (source.getBlockEntity() instanceof DispenserBlockEntity) {
			DispenserBlockEntity dispenser = source.getBlockEntity();
			ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
			if (!HopperBlockEntity.transfer(null, dispenser, honeyBottle, null).isEmpty()) {
				DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
			}
		}
    }
}
