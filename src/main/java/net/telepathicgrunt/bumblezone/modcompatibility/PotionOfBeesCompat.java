package net.telepathicgrunt.bumblezone.modcompatibility;

import org.apache.logging.log4j.Level;

import com.github.commoble.potionofbees.ResourceLocations;
import com.github.commoble.potionofbees.SplashPotionOfBeesEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;

public class PotionOfBeesCompat
{
	private static final DefaultDispenseItemBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new PotionOfBeesBeePotionDispenseBehavior();
	
	public static void setupPotionOfBees() 
	{
		ModChecking.potionOfBeesPresent = true;
		
		if(ForgeRegistries.ITEMS.containsKey(ResourceLocations.POTION_OF_BEES)) {
			DispenserBlock.registerDispenseBehavior(ForgeRegistries.ITEMS.getValue(ResourceLocations.POTION_OF_BEES), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with bee potions in dispensers
		}
		else {
			Bumblezone.LOGGER.log(Level.WARN, "Error trying to change the dispenser behavior for Potion of Bee's bee potion item. Please report this issue to Bumblezone author.");
		}
		
		if(ForgeRegistries.ITEMS.containsKey(ResourceLocations.SPLASH_POTION_OF_BEES)) {
			DispenserBlock.registerDispenseBehavior(ForgeRegistries.ITEMS.getValue(ResourceLocations.SPLASH_POTION_OF_BEES), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with bee splash potion in dispensers
		}
		else {
			Bumblezone.LOGGER.log(Level.WARN, "Error trying to change the dispenser behavior for Potion of Bee's bee splash potion item. Please report this issue to Bumblezone author.");
		}
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
						
						if(block.getBlock() == BzBlocks.DEAD_HONEYCOMB_LARVA.get()) 
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
				BzBlocks.HONEYCOMB_LARVA.get().getDefaultState()
				.with(BlockStateProperties.FACING, state.get(BlockStateProperties.FACING))
				.with(HoneycombBrood.STAGE, Integer.valueOf(world.rand.nextInt(3))));
	}
}
