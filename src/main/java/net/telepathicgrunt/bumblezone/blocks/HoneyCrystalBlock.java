package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.telepathicgrunt.bumblezone.items.BzItems;


public class HoneyCrystalBlock extends FallingBlock
{
	protected static final VoxelShape AABB = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D);
	private Item item;

	public HoneyCrystalBlock()
	{
		super(Block.Properties.create(Material.GLASS, MaterialColor.ADOBE).lightValue(1).hardnessAndResistance(0.3F).notSolid());
	}


	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return AABB;
	}


	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockPos blockpos = pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return blockstate.isSolidSide(worldIn, blockpos, Direction.UP);
	}


	@Override
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}

	
	@Override
	public Item asItem()
	{
		if (this.item == null)
		{
			this.item = BzItems.HONEY_CRYSTAL_SHARDS.get();
		}

		return this.item.delegate.get();
	}


	@Deprecated
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isEmissiveRendering(BlockState p_225543_1_)
	{
		return true;
	}

}
