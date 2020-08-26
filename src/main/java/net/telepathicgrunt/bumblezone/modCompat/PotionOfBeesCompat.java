package net.telepathicgrunt.bumblezone.modcompatibility;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import com.github.commoble.potionofbees.ResourceLocations;
import com.github.commoble.potionofbees.SplashPotionOfBeesEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;

public class PotionOfBeesCompat
{
	private static final PotionOfBeesBeePotionDispenseBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new PotionOfBeesBeePotionDispenseBehavior();
	private static Item POTION_OF_BEES;
	private static Item SPLASH_POTION_OF_BEES;
	
	public static void setupPotionOfBees() 
	{
		ModChecking.potionOfBeesPresent = true;
		
		/**
		 * Sets up our custom behavior for Potion of Bees items withour overriding their default behavior completely
		 */
		POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(ResourceLocations.POTION_OF_BEES);
		SPLASH_POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(ResourceLocations.SPLASH_POTION_OF_BEES);
		try {
		    Method method = ObfuscationReflectionHelper.findMethod(DispenserBlock.class, "func_149940_a", ItemStack.class);
		    
		    if (POTION_OF_BEES != null && Bumblezone.BzConfig.allowPotionOfBeesCompat.get()) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR = (IDispenseItemBehavior) (method.invoke(Blocks.DISPENSER, new ItemStack(POTION_OF_BEES)));
			DispenserBlock.registerDispenseBehavior(POTION_OF_BEES, BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee potions in dispensers
		    }
		    else {
			Bumblezone.LOGGER.log(Level.WARN, "Error trying to change the dispenser behavior for Potion of Bee's bee potion item. Please report this issue to Bumblezone author.");
		    }

		    if (SPLASH_POTION_OF_BEES != null && Bumblezone.BzConfig.allowSplashPotionOfBeesCompat.get()) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR = (IDispenseItemBehavior) (method.invoke(Blocks.DISPENSER, new ItemStack(SPLASH_POTION_OF_BEES)));
			DispenserBlock.registerDispenseBehavior(SPLASH_POTION_OF_BEES, BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee splash potion in dispensers
		    }
		    else {
			Bumblezone.LOGGER.log(Level.WARN, "Error trying to change the dispenser behavior for Potion of Bee's bee splash potion item. Please report this issue to Bumblezone author.");
		    }
		} catch (Exception e) {
		    e.printStackTrace();
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
			Vec3d hitBlockPos = event.getRayTraceResult().getHitVec(); //position of the collision
			BlockPos originalPosition = new BlockPos(hitBlockPos);
			BlockPos.Mutable position = new BlockPos.Mutable(originalPosition);
			BlockState block;
			
			//revive nearby larva blocks
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					for(int z = -1; z <= 1; z++) {
						position.setPos(originalPosition).move(x, y, z);
						block = world.getBlockState(position);
						
						if(block.getBlock() == BzBlocks.DEAD_HONEYCOMB_BROOD.get()) 
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
				BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
				.with(BlockStateProperties.FACING, state.get(BlockStateProperties.FACING))
				.with(HoneycombBrood.STAGE, Integer.valueOf(world.rand.nextInt(3))));
	}
}
