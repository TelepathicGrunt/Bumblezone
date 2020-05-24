package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;


public class EmptyHoneycombBrood extends FacingBlock
{

	public EmptyHoneycombBrood()
	{
		super(FabricBlockSettings.of(Material.CLAY, MaterialColor.ORANGE).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CORAL).build());
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.SOUTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}


	@Override
	public BlockState getPlacementState(ItemPlacementContext context)
	{
		return this.getDefaultState().with(FACING, context.getSide().getOpposite());
	}


	@Override
	public BlockState rotate(BlockState state, BlockRotation rot)
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}


	@Override
	public BlockState mirror(BlockState state, BlockMirror mirrorIn)
	{
		return state.rotate(mirrorIn.getRotation(state.get(FACING)));
	}
}
