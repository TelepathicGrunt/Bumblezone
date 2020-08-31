package com.telepathicgrunt.the_bumblezone.modCompat;

import com.github.commoble.potionofbees.ResourceLocations;
import com.github.commoble.potionofbees.SplashPotionOfBeesEntity;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.DispenserBlockInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable;
import net.minecraftforge.registries.ForgeRegistries;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;

public class PotionOfBeesCompat
{
	private static final PotionOfBeesBeePotionDispenseBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new PotionOfBeesBeePotionDispenseBehavior();
	private static Item POTION_OF_BEES;
	private static Item SPLASH_POTION_OF_BEES;
	
	public static void setupPotionOfBees() 
	{
		ModChecker.potionOfBeesPresent = true;

		/*
		 * Sets up our custom behavior for Potion of Bees items withour overriding their default behavior completely
		 */
		POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(ResourceLocations.POTION_OF_BEES);
		SPLASH_POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(ResourceLocations.SPLASH_POTION_OF_BEES);

		if (POTION_OF_BEES != null && Bumblezone.BzModCompatibilityConfig.allowPotionOfBeesCompat.get()) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(POTION_OF_BEES));
			DispenserBlock.registerDispenseBehavior(POTION_OF_BEES, BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee potions in dispensers
		}
		if (SPLASH_POTION_OF_BEES != null && Bumblezone.BzModCompatibilityConfig.allowSplashPotionOfBeesCompat.get()) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(SPLASH_POTION_OF_BEES));
			DispenserBlock.registerDispenseBehavior(SPLASH_POTION_OF_BEES, BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee splash potion in dispensers
		}
	}
	
	public static boolean POBIsPotionOfBeesItem(Item item) {
	    return item == POTION_OF_BEES;
	}
	
	public static boolean POBIsSplashPotionOfBeesItem(Item item) {
	    return item == SPLASH_POTION_OF_BEES;
	}

	public static void POBReviveLarvaBlockEvent(Throwable event)
	{
		Entity thrownEntity = event.getEntity(); 

		if(thrownEntity instanceof SplashPotionOfBeesEntity) {
			World world = thrownEntity.world; // world we threw in
			Vector3d hitBlockPos = event.getRayTraceResult().getHitVec(); //position of the collision
			BlockPos originalPosition = new BlockPos(hitBlockPos);
			BlockPos.Mutable position = new BlockPos.Mutable().setPos(originalPosition);
			BlockState block;
			
			//revive nearby larva blocks
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					for(int z = -1; z <= 1; z++) {
						position.setPos(originalPosition).move(x, y, z);
						block = world.getBlockState(position);
						
						if(block.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD)
						{
							reviveLarvaBlock(world, block, position);
						}
					}
				}
			}
		}
	}

	private static void reviveLarvaBlock(World world, BlockState state, BlockPos position) {
		world.setBlockState(position, 
				BzBlocks.HONEYCOMB_BROOD.getDefaultState()
				.with(BlockStateProperties.FACING, state.get(BlockStateProperties.FACING))
				.with(HoneycombBrood.STAGE, world.rand.nextInt(3)));
	}
}
