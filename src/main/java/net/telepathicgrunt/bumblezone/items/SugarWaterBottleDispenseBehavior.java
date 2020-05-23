package net.telepathicgrunt.bumblezone.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;


public class SugarWaterBottleDispenseBehavior extends DefaultDispenseItemBehavior
{
	/**
	 * Dispense the specified stack, play the dispense sound and spawn particles.
	 */
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
	{
		World world = source.getWorld();
		IPosition iposition = DispenserBlock.getDispensePosition(source);
		BlockPos position = new BlockPos(iposition);
		BlockState blockstate = world.getBlockState(position);

		if (blockstate.getBlock() == BzBlocks.HONEYCOMB_LARVA.get())
		{
			float chance = world.rand.nextFloat();
			if(chance <= 0.3F) 
			{
				//spawn bee if at final stage and front isn't blocked off
				int stage = blockstate.get(HoneycombBrood.STAGE);
				if (stage == 3)
				{
					//the front of the block
					BlockPos.Mutable blockpos = new BlockPos.Mutable(position);
					blockpos.move(blockstate.get(HoneycombBrood.FACING).getOpposite());

					//do nothing if front is blocked off
					if (!world.getBlockState(blockpos).getMaterial().isSolid())
					{
						MobEntity beeEntity = EntityType.BEE.create(world);
						if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(beeEntity, world, blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, null, SpawnReason.TRIGGERED) != -1)
						{
							beeEntity.setLocationAndAngles(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
							ILivingEntityData ilivingentitydata = null;
							ilivingentitydata = beeEntity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(beeEntity)), SpawnReason.TRIGGERED, ilivingentitydata, (CompoundNBT) null);
							world.addEntity(beeEntity);
						}

						world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, Integer.valueOf(0)));
					}
				}
				else
				{
					world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, Integer.valueOf(stage + 1)));
				}
			}

			stack.shrink(1);
		}
		else
		{
			return super.dispenseStack(source, stack);
		}

		return stack;
	}


	/**
	 * Play the dispense sound from the specified block.
	 */
	@Override
	protected void playDispenseSound(IBlockSource source)
	{
		source.getWorld().playEvent(1002, source.getBlockPos(), 0);
	}
}
