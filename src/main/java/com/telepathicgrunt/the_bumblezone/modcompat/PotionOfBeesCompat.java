package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionOfBeesCompat
{
	private static final PotionOfBeesBeePotionDispenseBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new PotionOfBeesBeePotionDispenseBehavior();
	private static EntityType<?> SPLASH_POTION_OF_BEES_ENTITY;
	private static Item POTION_OF_BEES;
	private static Item SPLASH_POTION_OF_BEES;
	
	public static void setupPotionOfBees() {

		/*
		 * Sets up our custom behavior for Potion of Bees items withour overriding their default behavior completely
		 */
		POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation("potionofbees:potion_of_bees"));
		SPLASH_POTION_OF_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation("potionofbees:splash_potion_of_bees"));
		SPLASH_POTION_OF_BEES_ENTITY = ForgeRegistries.ENTITIES.getValue(new ResourceLocation("potionofbees:splash_potion_of_bees"));

		if (POTION_OF_BEES != null && BzModCompatibilityConfigs.allowPotionOfBeesCompat.get()) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).thebumblezone_invokeGetDispenseMethod(new ItemStack(POTION_OF_BEES));
			DispenserBlock.registerBehavior(POTION_OF_BEES, BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee potions in dispensers
		}
		if (SPLASH_POTION_OF_BEES != null && BzModCompatibilityConfigs.allowSplashPotionOfBeesCompat.get()) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker)Blocks.DISPENSER).thebumblezone_invokeGetDispenseMethod(new ItemStack(SPLASH_POTION_OF_BEES));
			DispenserBlock.registerBehavior(SPLASH_POTION_OF_BEES, BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee splash potion in dispensers
		}

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.potionOfBeesPresent = true;
	}
	
	public static boolean POBIsPotionOfBeesItem(Item item) {
	    return item.equals(POTION_OF_BEES);
	}
	
	public static boolean POBIsSplashPotionOfBeesItem(Item item) {
	    return item.equals(SPLASH_POTION_OF_BEES);
	}

	public static void POBReviveLarvaBlockEvent(Throwable event)
	{
		Entity thrownEntity = event.getEntity(); 

		if(thrownEntity.getType().equals(SPLASH_POTION_OF_BEES_ENTITY)) {
			World world = thrownEntity.level; // world we threw in
			Vector3d hitBlockPos = event.getRayTraceResult().getLocation(); //position of the collision
			BlockPos originalPosition = new BlockPos(hitBlockPos);
			BlockPos.Mutable position = new BlockPos.Mutable().set(originalPosition);
			BlockState block;
			
			//revive nearby larva blocks
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					for(int z = -1; z <= 1; z++) {
						position.set(originalPosition).move(x, y, z);
						block = world.getBlockState(position);
						
						if(block.getBlock().equals(BzBlocks.EMPTY_HONEYCOMB_BROOD.get())) {
							reviveLarvaBlock(world, block, position);
						}
					}
				}
			}
		}
	}

	private static void reviveLarvaBlock(World world, BlockState state, BlockPos position) {
		world.setBlockAndUpdate(position, 
				BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
				.setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING))
				.setValue(HoneycombBrood.STAGE, world.random.nextInt(3)));
	}
}
