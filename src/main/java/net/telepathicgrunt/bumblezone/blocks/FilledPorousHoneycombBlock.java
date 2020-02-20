package net.telepathicgrunt.bumblezone.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.configs.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;


public class FilledPorousHoneycombBlock extends Block
{

	public FilledPorousHoneycombBlock()
	{
		super(FabricBlockSettings.of(Material.CLAY, MaterialColor.ORANGE).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CORAL).build());
	}


	/**
	 * Allow player to harvest honey and put honey into this block using bottles
	 */
	public ActionResult onUse(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockHitResult raytraceResult)
	{
		ItemStack itemstack = playerEntity.getStackInHand(playerHand);

		/*
		 * Player is harvesting the honey from this block if it is filled with honey
		 */
		if (itemstack.getItem() == Items.GLASS_BOTTLE)
		{
			if (!world.isClient)
			{
				world.setBlockState(position, BzBlocksInit.POROUS_HONEYCOMB.getDefaultState(), 3); // removed honey from this block
				world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
				
				if(!playerEntity.isCreative())
				{
					itemstack.decrement(1); // remove current empty bottle

					if (itemstack.isEmpty())
					{
						playerEntity.setStackInHand(playerHand, new ItemStack(Items.HONEY_BOTTLE)); // places honey bottle in hand
					}
					else if (!playerEntity.inventory.insertStack(new ItemStack(Items.HONEY_BOTTLE))) // places honey bottle in inventory
					{
						playerEntity.dropItem(new ItemStack(Items.HONEY_BOTTLE), false); // drops honey bottle if inventory is full
					}
				}
				

				if((playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE || Bumblezone.BZ_CONFIG.allowWrathOfTheHiveOutsideBumblezone) &&
					!playerEntity.isCreative() &&
					!playerEntity.isSpectator())
					//&& BzConfig.aggressiveBees)
				{
					//Now all bees nearby in Bumblezone will get VERY angry!!!
//					playerEntity.addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
					playerEntity.addStatusEffect(new StatusEffectInstance(BzEffectsInit.WRATH_OF_THE_HIVE, 350, 2, false, true, true));
				}
			}

			return ActionResult.SUCCESS;
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

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState blockState, World world, BlockPos position, Random random)
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
	@Environment(EnvType.CLIENT)
	private void spawnHoneyParticles(World world, BlockPos position, BlockState blockState)
	{
		if (blockState.getFluidState().isEmpty() && world.random.nextFloat() < 0.08F)
		{
			VoxelShape currentBlockShape = blockState.getCollisionShape(world, position);
			double yEndHeight = currentBlockShape.getMaximum(Direction.Axis.Y);
			if (yEndHeight >= 1.0D && !blockState.matches(BlockTags.IMPERMEABLE))
			{
				double yStartHeight = currentBlockShape.getMinimum(Direction.Axis.Y);
				if (yStartHeight > 0.0D)
				{
					this.addHoneyParticle(world, position, currentBlockShape, (double) position.getY() + yStartHeight - 0.05D);
				}
				else
				{
					BlockPos belowBlockpos = position.down();
					BlockState belowBlockstate = world.getBlockState(belowBlockpos);
					VoxelShape belowBlockShape = belowBlockstate.getCollisionShape(world, belowBlockpos);
					double yEndHeight2 = belowBlockShape.getMaximum(Direction.Axis.Y);
					if ((yEndHeight2 < 1.0D || !belowBlockstate.isFullCube(world, belowBlockpos)) && belowBlockstate.getFluidState().isEmpty())
					{
						this.addHoneyParticle(world, position, currentBlockShape, (double) position.getY() - 0.05D);
					}
				}
			}

		}
	}


	/**
	 * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle method
	 */
	@Environment(EnvType.CLIENT)
	private void addHoneyParticle(World world, BlockPos blockPos, VoxelShape blockShape, double height)
	{
		this.addHoneyParticle(world, (double) blockPos.getX() + blockShape.getMinimum(Direction.Axis.X), (double) blockPos.getX() + blockShape.getMaximum(Direction.Axis.X), (double) blockPos.getZ() + blockShape.getMinimum(Direction.Axis.Z), (double) blockPos.getZ() + blockShape.getMaximum(Direction.Axis.Z), height);
	}


	/**
	 * Adds the actual honey particle into the world within the given range
	 */
	private void addHoneyParticle(World world, double xMin, double xMax, double zMax, double zMin, double yHeight)
	{
		world.addParticle(ParticleTypes.DRIPPING_HONEY, MathHelper.lerp(world.random.nextDouble(), xMin, xMax), yHeight, MathHelper.lerp(world.random.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
	}
}
