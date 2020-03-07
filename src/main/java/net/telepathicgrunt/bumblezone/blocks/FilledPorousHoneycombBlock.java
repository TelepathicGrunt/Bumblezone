package net.telepathicgrunt.bumblezone.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.effects.BzEffects;


public class FilledPorousHoneycombBlock extends Block
{

	public FilledPorousHoneycombBlock()
	{
		super(Block.Properties.create(Material.CLAY, MaterialColor.ADOBE).hardnessAndResistance(0.5F).sound(SoundType.CORAL));
		setRegistryName("filled_porous_honeycomb_block");
	}


	/**
	 * Allow player to harvest honey and put honey into this block using bottles
	 */
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onUse(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult)
	{
		ItemStack itemstack = playerEntity.getHeldItem(playerHand);

		/*
		 * Player is harvesting the honey from this block if it is filled with honey
		 */
		if (itemstack.getItem() == Items.GLASS_BOTTLE)
		{
			if (!world.isRemote)
			{
				world.setBlockState(position, BzBlocksInit.POROUS_HONEYCOMB.get().getDefaultState(), 3); // removed honey from this block
				world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				
				if(!playerEntity.isCreative())
				{
					itemstack.shrink(1); // remove current empty bottle
	
					if (itemstack.isEmpty())
					{
						playerEntity.setHeldItem(playerHand, new ItemStack(Items.field_226638_pX_)); // places honey bottle in hand
					}
					else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.field_226638_pX_))) // places honey bottle in inventory
					{
						playerEntity.dropItem(new ItemStack(Items.field_226638_pX_), false); // drops honey bottle if inventory is full
					}
				}
				
				
				if((playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
					!playerEntity.isCreative() && 
					!playerEntity.isSpectator() && 
					BzConfig.aggressiveBees) 
				{
					//Now all bees nearby in Bumblezone will get VERY angry!!!
					playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
				}
			}

			return ActionResultType.SUCCESS;
		}
		else
		{
			return super.onUse(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
		}
	}


	/**
	 * Called periodically clientside on blocks near the player to show honey particles.
	 * 50% of attempting to spawn a particle
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState blockState, World world, BlockPos position, Random random)
	{
		//number of particles in this tick
		for (int i = 0; i < random.nextInt(2); ++i)
		{
			this.spawnHoneyParticles(world, position, blockState);
		}
	}


	/**
	 * Starts checking if the block can take the particle and if so and it passes another rng to reduce spawnrate,
	 * it then takes the block's dimensions and passes into methods to spawn the actual particle
	 * 
	 */
	@OnlyIn(Dist.CLIENT)
	private void spawnHoneyParticles(World world, BlockPos position, BlockState blockState)
	{
		if (blockState.getFluidState().isEmpty() && world.rand.nextFloat() < 0.08F)
		{
			VoxelShape currentBlockShape = blockState.getCollisionShape(world, position);
			double yEndHeight = currentBlockShape.getEnd(Direction.Axis.Y);
			if (yEndHeight >= 1.0D && !blockState.isIn(BlockTags.IMPERMEABLE))
			{
				double yStartHeight = currentBlockShape.getStart(Direction.Axis.Y);
				if (yStartHeight > 0.0D)
				{
					this.addHoneyParticle(world, position, currentBlockShape, position.getY() + yStartHeight - 0.05D);
				}
				else
				{
					BlockPos belowBlockpos = position.down();
					BlockState belowBlockstate = world.getBlockState(belowBlockpos);
					VoxelShape belowBlockShape = belowBlockstate.getCollisionShape(world, belowBlockpos);
					double yEndHeight2 = belowBlockShape.getEnd(Direction.Axis.Y);
					if ((yEndHeight2 < 1.0D || !belowBlockstate.isFullCube(world, belowBlockpos)) && belowBlockstate.getFluidState().isEmpty())
					{
						this.addHoneyParticle(world, position, currentBlockShape, position.getY() - 0.05D);
					}
				}
			}

		}
	}


	/**
	 * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle method
	 */
	@OnlyIn(Dist.CLIENT)
	private void addHoneyParticle(World world, BlockPos blockPos, VoxelShape blockShape, double height)
	{
		this.addHoneyParticle(world, blockPos.getX() + blockShape.getStart(Direction.Axis.X), blockPos.getX() + blockShape.getEnd(Direction.Axis.X), blockPos.getZ() + blockShape.getStart(Direction.Axis.Z), blockPos.getZ() + blockShape.getEnd(Direction.Axis.Z), height);
	}


	/**
	 * Adds the actual honey particle into the world within the given range
	 */
	@OnlyIn(Dist.CLIENT)
	private void addHoneyParticle(World world, double xMin, double xMax, double zMax, double zMin, double yHeight)
	{
		world.addParticle(ParticleTypes.field_229427_ag_, MathHelper.lerp(world.rand.nextDouble(), xMin, xMax), yHeight, MathHelper.lerp(world.rand.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
	}
}
