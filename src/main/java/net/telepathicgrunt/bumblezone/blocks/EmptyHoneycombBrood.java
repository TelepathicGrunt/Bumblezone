package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.modcompatibility.BuzzierBeesRedirection;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;


public class EmptyHoneycombBrood extends DirectionalBlock
{

	public EmptyHoneycombBrood()
	{
		super(Block.Properties.create(Material.CLAY, MaterialColor.ADOBE).hardnessAndResistance(0.5F).sound(SoundType.CORAL));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}


	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getFace().getOpposite());
	}


	@Override
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}


	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}


	/**
	 * Allow player to harvest honey and put honey into this block using bottles or wands
	 */
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult)
	{
		ItemStack itemstack = playerEntity.getHeldItem(playerHand);

		//MOD COMPAT
		
		/*
		 * Buzzier Bees honey wand compat
		 */
		if (ModChecking.buzzierBeesPresent && Bumblezone.BzConfig.allowBottledBeeCompat.get())
		{
			/*
			 * Player is trying to revive the block
			 */
			ActionResultType action = BuzzierBeesRedirection.bottledBeeInteract(itemstack, thisBlockState, world, position, playerEntity, playerHand);
			if (action == ActionResultType.SUCCESS)
			{
				world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				world.setBlockState(position, BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
					.with(HoneycombBrood.STAGE, Integer.valueOf(0))
					.with(DirectionalBlock.FACING, thisBlockState.get(DirectionalBlock.FACING)));

				return action;
			}
		}

		return super.onBlockActivated(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
	}
}
