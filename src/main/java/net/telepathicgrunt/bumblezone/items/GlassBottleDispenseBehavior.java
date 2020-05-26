package net.telepathicgrunt.bumblezone.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;


public class GlassBottleDispenseBehavior extends DefaultDispenseItemBehavior
{
    public static IDispenseItemBehavior DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
	World world = source.getWorld();
	IPosition iposition = DispenserBlock.getDispensePosition(source);
	BlockPos position = new BlockPos(iposition);
	BlockState blockstate = world.getBlockState(position);

	if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD.get()) {
	    // spawn bee if at final stage and front isn't blocked off
	    int stage = blockstate.get(HoneycombBrood.STAGE);
	    if (stage == 3) {
		// the front of the block
		BlockPos.Mutable blockpos = new BlockPos.Mutable(position);
		blockpos.move(blockstate.get(HoneycombBrood.FACING).getOpposite());

		// do nothing if front is blocked off
		if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
		    MobEntity beeEntity = EntityType.BEE.create(world);
		    if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(beeEntity, world, blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, null, SpawnReason.TRIGGERED) != -1) {
			beeEntity.setLocationAndAngles(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
			ILivingEntityData ilivingentitydata = null;
			ilivingentitydata = beeEntity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(beeEntity)), SpawnReason.TRIGGERED, ilivingentitydata, (CompoundNBT) null);
			world.addEntity(beeEntity);
		    }

		}
	    }

	    // kill the brood block
	    world.setBlockState(position, BzBlocks.DEAD_HONEYCOMB_BROOD.get().getDefaultState().with(BlockStateProperties.FACING, blockstate.get(BlockStateProperties.FACING)));
	    stack.shrink(1);
	    
	    if(!stack.isEmpty())
		addHoneyBottleToDispenser(source);
	    else 
		stack = new ItemStack(Items.HONEY_BOTTLE);
	}
	// remove honey
	else if (blockstate.getBlock() == BzBlocks.FILLED_POROUS_HONEYCOMB.get()) {
	    world.setBlockState(position, BzBlocks.POROUS_HONEYCOMB.get().getDefaultState());
	    stack.shrink(1);
	    
	    if(!stack.isEmpty())
		addHoneyBottleToDispenser(source);
	    else 
		stack = new ItemStack(Items.HONEY_BOTTLE);
	}
	else {
	    return DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR.dispense(source, stack);
	}

	return stack;
    }


    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playDispenseSound(IBlockSource source) {
	source.getWorld().playEvent(1002, source.getBlockPos(), 0);
    }


    /**
     * Adds honey bottle to dispenser or if no room, dispense it
     */
    private static void addHoneyBottleToDispenser(IBlockSource source) {
	if(source.getBlockTileEntity() instanceof DispenserTileEntity) {
	    DispenserTileEntity dispenser = (DispenserTileEntity)source.getBlockTileEntity();
	    ItemStack honeyBottle = new ItemStack(Items.HONEY_BOTTLE);
	    if(!HopperTileEntity.putStackInInventoryAllSlots(null, dispenser, honeyBottle, null).isEmpty()) {
		DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
	    }
	}
    }
}
