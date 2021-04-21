package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzEffects;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.Entity;
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

import java.util.Random;


public class FilledPorousHoneycomb extends Block {

    public FilledPorousHoneycomb() {
        super(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.ORANGE).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CORAL).build().velocityMultiplier(0.9F));
    }


    /**
     * Called when the given entity walks on this Block
     */
    @Override
    public void onSteppedOn(World worldIn, BlockPos pos, Entity entityIn) {
        double yMagnitude = Math.abs(entityIn.getVelocity().y);
        if (yMagnitude < 0.1D) {
            double slowFactor = 0.85D;
            entityIn.setVelocity(entityIn.getVelocity().multiply(slowFactor, 1.0D, slowFactor));
        }

        super.onSteppedOn(worldIn, pos, entityIn);
    }


    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getStackInHand(playerHand);

        /*
         * Player is harvesting the honey from this block if it is filled with honey
         */
        if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            world.setBlockState(position, BzBlocks.POROUS_HONEYCOMB.getDefaultState(), 3); // removed honey from this block
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                itemstack.decrement(1); // remove current empty bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setStackInHand(playerHand, new ItemStack(Items.HONEY_BOTTLE)); // places honey bottle in hand
                } else if (!playerEntity.inventory.insertStack(new ItemStack(Items.HONEY_BOTTLE))) // places honey bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.HONEY_BOTTLE), false); // drops honey bottle if inventory is full
                }
            }

            if ((playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.aggressiveBees)
            {
                if(playerEntity.hasStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE)){
                    playerEntity.removeStatusEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                }
                else{
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addStatusEffect(new StatusEffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.howLongWrathOfTheHiveLasts, 2, false, Bumblezone.BZ_CONFIG.BZBeeAggressionConfig.showWrathOfTheHiveParticles, true));
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }


    /**
     * Called periodically clientside on blocks near the player to show honey particles. 50% of attempting to spawn a
     * particle
     */
    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos position, Random random) {
        //number of particles in this tick
        for (int i = 0; i < random.nextInt(2); ++i) {
            this.spawnHoneyParticles(world, position, blockState);
        }
    }


    /**
     * tell redstone that this can be use with comparator
     */
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }


    /**
     * the power fed into comparator 1
     */
    @Override
    public int getComparatorOutput(BlockState blockState, World worldIn, BlockPos pos) {
        return 1;
    }

    /**
     * Starts checking if the block can take the particle and if so and it passes another rng to reduce spawnrate, it then
     * takes the block's dimensions and passes into methods to spawn the actual particle
     */
    private void spawnHoneyParticles(World world, BlockPos position, BlockState blockState) {
        if (blockState.getFluidState().isEmpty() && world.random.nextFloat() < 0.08F) {
            VoxelShape currentBlockShape = blockState.getCollisionShape(world, position);
            double yEndHeight = currentBlockShape.getMax(Direction.Axis.Y);
            if (yEndHeight >= 1.0D && !blockState.isIn(BlockTags.IMPERMEABLE)) {
                double yStartHeight = currentBlockShape.getMin(Direction.Axis.Y);
                if (yStartHeight > 0.0D) {
                    this.addHoneyParticle(world, position, currentBlockShape, position.getY() + yStartHeight - 0.05D);
                } else {
                    BlockPos belowBlockpos = position.down();
                    BlockState belowBlockstate = world.getBlockState(belowBlockpos);
                    VoxelShape belowBlockShape = belowBlockstate.getCollisionShape(world, belowBlockpos);
                    double yEndHeight2 = belowBlockShape.getMax(Direction.Axis.Y);
                    if ((yEndHeight2 < 1.0D || !belowBlockstate.isOpaqueFullCube(world, belowBlockpos)) && belowBlockstate.getFluidState().isEmpty()) {
                        this.addHoneyParticle(world, position, currentBlockShape, position.getY() - 0.05D);
                    }
                }
            }

        }
    }


    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle
     * method
     */
    private void addHoneyParticle(World world, BlockPos blockPos, VoxelShape blockShape, double height) {
        this.addHoneyParticle(
                world,
                blockPos.getX() + blockShape.getMin(Direction.Axis.X),
                blockPos.getX() + blockShape.getMax(Direction.Axis.X),
                blockPos.getZ() + blockShape.getMin(Direction.Axis.Z),
                blockPos.getZ() + blockShape.getMax(Direction.Axis.Z),
                height);
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addHoneyParticle(World world, double xMin, double xMax, double zMax, double zMin, double yHeight) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, MathHelper.lerp(world.random.nextDouble(), xMin, xMax), yHeight, MathHelper.lerp(world.random.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
    }
}
