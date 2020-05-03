package net.telepathicgrunt.bumblezone.utils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IClearable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class TemplateTG extends Template
{

	/**
	 * Adds blocks and entities from this structure to the given world.
	 */
	public boolean addBlocksToSolidWorld(IWorld worldIn, BlockPos pos, PlacementSettings placementIn, int flags)
	{
		if (this.blocks.isEmpty())
		{
			return false;
		}
		else
		{
			List<Template.BlockInfo> list = placementIn.func_227459_a_(this.blocks, pos);
			if ((!list.isEmpty() || !placementIn.getIgnoreEntities() && !this.entities.isEmpty()) && this.size.getX() >= 1 && this.size.getY() >= 1 && this.size.getZ() >= 1)
			{
				MutableBoundingBox mutableboundingbox = placementIn.getBoundingBox();
				List<BlockPos> list1 = Lists.newArrayListWithCapacity(placementIn.func_204763_l() ? list.size() : 0);
				List<Pair<BlockPos, CompoundNBT>> list2 = Lists.newArrayListWithCapacity(list.size());
				int i = Integer.MAX_VALUE;
				int j = Integer.MAX_VALUE;
				int k = Integer.MAX_VALUE;
				int l = Integer.MIN_VALUE;
				int i1 = Integer.MIN_VALUE;
				int j1 = Integer.MIN_VALUE;

				for (Template.BlockInfo template$blockinfo : processBlockInfos(this, worldIn, pos, placementIn, list))
				{
					BlockPos blockpos = template$blockinfo.pos;
					if (mutableboundingbox == null || mutableboundingbox.isVecInside(blockpos))
					{
						IFluidState ifluidstate = placementIn.func_204763_l() ? worldIn.getFluidState(blockpos) : null;
						BlockState blockstate = template$blockinfo.state.mirror(placementIn.getMirror()).rotate(placementIn.getRotation());
						if (template$blockinfo.nbt != null)
						{
							TileEntity tileentity = worldIn.getTileEntity(blockpos);
							IClearable.clearObj(tileentity);
							worldIn.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 20);
						}

						if (worldIn.getBlockState(blockpos).isSolid() && worldIn.setBlockState(blockpos, blockstate, flags))
						{
							i = Math.min(i, blockpos.getX());
							j = Math.min(j, blockpos.getY());
							k = Math.min(k, blockpos.getZ());
							l = Math.max(l, blockpos.getX());
							i1 = Math.max(i1, blockpos.getY());
							j1 = Math.max(j1, blockpos.getZ());
							list2.add(Pair.of(blockpos, template$blockinfo.nbt));
							if (template$blockinfo.nbt != null)
							{
								TileEntity tileentity1 = worldIn.getTileEntity(blockpos);
								if (tileentity1 != null)
								{
									template$blockinfo.nbt.putInt("x", blockpos.getX());
									template$blockinfo.nbt.putInt("y", blockpos.getY());
									template$blockinfo.nbt.putInt("z", blockpos.getZ());
									tileentity1.read(template$blockinfo.nbt);
									tileentity1.mirror(placementIn.getMirror());
									tileentity1.rotate(placementIn.getRotation());
								}
							}

							if (ifluidstate != null && blockstate.getBlock() instanceof ILiquidContainer)
							{
								((ILiquidContainer) blockstate.getBlock()).receiveFluid(worldIn, blockpos, blockstate, ifluidstate);
								if (!ifluidstate.isSource())
								{
									list1.add(blockpos);
								}
							}
						}
					}
				}

				boolean flag = true;
				Direction[] adirection = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

				while (flag && !list1.isEmpty())
				{
					flag = false;
					Iterator<BlockPos> iterator = list1.iterator();

					while (iterator.hasNext())
					{
						BlockPos blockpos2 = iterator.next();
						BlockPos blockpos3 = blockpos2;
						IFluidState ifluidstate2 = worldIn.getFluidState(blockpos2);

						for (int k1 = 0; k1 < adirection.length && !ifluidstate2.isSource(); ++k1)
						{
							BlockPos blockpos1 = blockpos3.offset(adirection[k1]);
							IFluidState ifluidstate1 = worldIn.getFluidState(blockpos1);
							if (ifluidstate1.getActualHeight(worldIn, blockpos1) > ifluidstate2.getActualHeight(worldIn, blockpos3) || ifluidstate1.isSource() && !ifluidstate2.isSource())
							{
								ifluidstate2 = ifluidstate1;
								blockpos3 = blockpos1;
							}
						}

						if (ifluidstate2.isSource())
						{
							BlockState blockstate2 = worldIn.getBlockState(blockpos2);
							Block block = blockstate2.getBlock();
							if (block instanceof ILiquidContainer)
							{
								((ILiquidContainer) block).receiveFluid(worldIn, blockpos2, blockstate2, ifluidstate2);
								flag = true;
								iterator.remove();
							}
						}
					}
				}

				if (i <= l)
				{
					if (!placementIn.func_215218_i())
					{
						VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(l - i + 1, i1 - j + 1, j1 - k + 1);
						int l1 = i;
						int i2 = j;
						int j2 = k;

						for (Pair<BlockPos, CompoundNBT> pair1 : list2)
						{
							BlockPos blockpos5 = pair1.getFirst();
							voxelshapepart.setFilled(blockpos5.getX() - l1, blockpos5.getY() - i2, blockpos5.getZ() - j2, true, true);
						}

						func_222857_a(worldIn, flags, voxelshapepart, l1, i2, j2);
					}

					for (Pair<BlockPos, CompoundNBT> pair : list2)
					{
						BlockPos blockpos4 = pair.getFirst();
						if (!placementIn.func_215218_i())
						{
							BlockState blockstate1 = worldIn.getBlockState(blockpos4);
							BlockState blockstate3 = Block.getValidBlockForPosition(blockstate1, worldIn, blockpos4);
							if (blockstate1 != blockstate3)
							{
								worldIn.setBlockState(blockpos4, blockstate3, flags & -2 | 16);
							}

							worldIn.notifyNeighbors(blockpos4, blockstate3.getBlock());
						}

						if (pair.getSecond() != null)
						{
							TileEntity tileentity2 = worldIn.getTileEntity(blockpos4);
							if (tileentity2 != null)
							{
								tileentity2.markDirty();
							}
						}
					}
				}

				
				if (!placementIn.getIgnoreEntities())
				{
					Method addEntitiesToWorldReflect = ObfuscationReflectionHelper.findMethod(
							Template.class, 
							"func_207668_a", 
							IWorld.class,
							BlockPos.class, 
							PlacementSettings.class,
							Mirror.class,
							Rotation.class,
							BlockPos.class,
							MutableBoundingBox.class);
					
					try
					{
						addEntitiesToWorldReflect.invoke(
								this, 
								worldIn, 
								pos,
								placementIn, 
								placementIn.getMirror(), 
								placementIn.getRotation(), 
								placementIn.getCenterOffset(), 
								placementIn.getBoundingBox());
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				return true;
			}
			else
			{
				return false;
			}
		}
	}
}
