package net.telepathicgrunt.bumblezone.modCompat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.mixin.DispenserAccessor;

public class PotionOfBeesCompat
{
	private static final PotionOfBeesBeePotionDispenseBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new PotionOfBeesBeePotionDispenseBehavior();
	public static Identifier POB_POTION_OF_BEES_ITEM_ID = new Identifier("potionofbees:potion_of_bees");
	public static Item POB_POTION_OF_BEES_ITEM = null;

	public static Identifier POB_SPLASH_POTION_OF_BEES_ITEM_ID = new Identifier("potionofbees:splash_potion_of_bees");
	public static Item POB_SPLASH_POTION_OF_BEES_ITEM = null;

	public static Identifier POB_SPLASH_POTION_OF_BEES_ENTITY_ID = new Identifier("potionofbees:splash_potion_of_bees");
	public static EntityType<?> POB_SPLASH_POTION_OF_BEES_ENTITY = null;

	public static void setupPotionOfBees() 
	{
		ModChecker.potionOfBeesPresent = true;

		/*
		 * Sets up our custom behavior for Potion of Bees items withour overriding their default behavior completely
		 */
		if (Registry.ITEM.containsId(POB_POTION_OF_BEES_ITEM_ID) && Bumblezone.BZ_CONFIG.BZModCompatibilityConfig.allowPotionOfBeesCompat) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserAccessor)Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Registry.ITEM.get(POB_POTION_OF_BEES_ITEM_ID)));
			DispenserBlock.registerBehavior(Registry.ITEM.get(POB_POTION_OF_BEES_ITEM_ID), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee potions in dispensers
		}
		if (Registry.ITEM.containsId(POB_SPLASH_POTION_OF_BEES_ITEM_ID) && Bumblezone.BZ_CONFIG.BZModCompatibilityConfig.allowSplashPotionOfBeesCompat) {
			PotionOfBeesBeePotionDispenseBehavior.DEFAULT_SPLASH_POTION_BEE_DISPENSE_BEHAVIOR = ((DispenserAccessor)Blocks.DISPENSER).invokeGetBehaviorForItem(new ItemStack(Registry.ITEM.get(POB_SPLASH_POTION_OF_BEES_ITEM_ID)));
			DispenserBlock.registerBehavior(Registry.ITEM.get(POB_SPLASH_POTION_OF_BEES_ITEM_ID), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); // adds compatibility with bee splash potion in dispensers
		}

		POB_SPLASH_POTION_OF_BEES_ENTITY = Registry.ENTITY_TYPE.get(POB_SPLASH_POTION_OF_BEES_ENTITY_ID);
		POB_POTION_OF_BEES_ITEM = Registry.ITEM.get(POB_POTION_OF_BEES_ITEM_ID);
		POB_SPLASH_POTION_OF_BEES_ITEM = Registry.ITEM.get(POB_SPLASH_POTION_OF_BEES_ITEM_ID);
	}


	public static boolean POBIsPotionOfBeesItem(Item item) {
		return item.equals(POB_POTION_OF_BEES_ITEM);
	}

	public static boolean POBIsSplashPotionOfBeesItem(Item item) {
		return item.equals(POB_SPLASH_POTION_OF_BEES_ITEM);
	}

	public static boolean POBReviveLarvaBlockEvent(Entity thrownEntity, Vec3d hitBlockPos)
	{
		if(thrownEntity.getType().equals(POB_SPLASH_POTION_OF_BEES_ENTITY)) {
			World world = thrownEntity.getEntityWorld(); // world we threw in
			BlockPos originalPosition = new BlockPos(hitBlockPos);
			BlockPos.Mutable position = new BlockPos.Mutable().set(originalPosition);
			BlockState blockState;
			boolean revivedBrood = false;
			
			//revive nearby larva blocks
			for(int x = -1; x <= 1; x++) {
				for(int y = -1; y <= 1; y++) {
					for(int z = -1; z <= 1; z++) {
						position.set(originalPosition).move(x, y, z);
						blockState = world.getBlockState(position);
						
						if(blockState.getBlock().equals(BzBlocks.EMPTY_HONEYCOMB_BROOD)) {
							reviveLarvaBlock(world, blockState, position);
							revivedBrood = true;
						}
					}
				}
			}

			return revivedBrood;
		}

		return false;
	}

	private static void reviveLarvaBlock(World world, BlockState blockState, BlockPos position) {
		world.setBlockState(position, 
				BzBlocks.HONEYCOMB_BROOD.getDefaultState()
				.with(Properties.FACING, blockState.get(Properties.FACING))
				.with(HoneycombBrood.STAGE, world.random.nextInt(3)));
	}
}
