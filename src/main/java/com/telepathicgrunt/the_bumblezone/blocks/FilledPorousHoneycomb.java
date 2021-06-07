package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.block.AbstractBlock;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import java.util.Random;

public class FilledPorousHoneycomb extends Block {

    public FilledPorousHoneycomb() {
        super(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK).speedFactor(0.8f));
    }

    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        /*
         * Player is harvesting the honey from this block if it is filled with honey
         */
        if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            world.setBlock(position, BzBlocks.POROUS_HONEYCOMB.get().defaultBlockState(), 3); // removed honey from this block
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                itemstack.shrink(1); // remove current empty bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.HONEY_BOTTLE)); // places honey bottle in hand
                } else if (!playerEntity.inventory.add(new ItemStack(Items.HONEY_BOTTLE))) // places honey bottle in inventory
                {
                    playerEntity.drop(new ItemStack(Items.HONEY_BOTTLE), false); // drops honey bottle if inventory is full
                }
            }

            if ((playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BzBeeAggressionConfig.aggressiveBees.get())
            {
                if(playerEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())){
                    playerEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                }
                else{
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(), true));
                }
            }

            return ActionResultType.SUCCESS;
        }
        //allow compat with honey wand use
        else if (ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowHoneyWandCompat.get())
        {
            ActionResultType action = BuzzierBeesRedirection.honeyWandTakingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS)
            {
                world.setBlock(position, BzBlocks.POROUS_HONEYCOMB.get().defaultBlockState(), 3); // remove honey from this block
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                return action;
            }
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }


    /**
     * Called periodically clientside on blocks near the player to show honey particles. 50% of attempting to spawn a
     * particle
     */
    @Override
    public void animateTick(BlockState blockState, World world, BlockPos position, Random random) {
        //number of particles in this tick
        for (int i = 0; i < random.nextInt(2); ++i) {
            this.spawnHoneyParticles(world, position, blockState);
        }
    }


    /**
     * tell redstone that this can be use with comparator
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }


    /**
     * the power fed into comparator 1
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        return 1;
    }

    /**
     * Starts checking if the block can take the particle and if so and it passes another rng to reduce spawnrate, it then
     * takes the block's dimensions and passes into methods to spawn the actual particle
     */
    private void spawnHoneyParticles(World world, BlockPos position, BlockState blockState) {
        if (blockState.getFluidState().isEmpty() && world.random.nextFloat() < 0.08F) {
            VoxelShape currentBlockShape = blockState.getCollisionShape(world, position);
            double yEndHeight = currentBlockShape.max(Direction.Axis.Y);
            if (yEndHeight >= 1.0D && !blockState.is(BlockTags.IMPERMEABLE)) {
                double yStartHeight = currentBlockShape.min(Direction.Axis.Y);
                if (yStartHeight > 0.0D) {
                    this.addHoneyParticle(world, position, currentBlockShape, position.getY() + yStartHeight - 0.05D);
                } else {
                    BlockPos belowBlockpos = position.below();
                    BlockState belowBlockstate = world.getBlockState(belowBlockpos);
                    VoxelShape belowBlockShape = belowBlockstate.getCollisionShape(world, belowBlockpos);
                    double yEndHeight2 = belowBlockShape.max(Direction.Axis.Y);
                    if ((yEndHeight2 < 1.0D || !belowBlockstate.isSolidRender(world, belowBlockpos)) && belowBlockstate.getFluidState().isEmpty()) {
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
                blockPos.getX() + blockShape.min(Direction.Axis.X),
                blockPos.getX() + blockShape.max(Direction.Axis.X),
                blockPos.getZ() + blockShape.min(Direction.Axis.Z),
                blockPos.getZ() + blockShape.max(Direction.Axis.Z),
                height);
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addHoneyParticle(World world, double xMin, double xMax, double zMax, double zMin, double yHeight) {
        world.addParticle(
                ParticleTypes.DRIPPING_HONEY,
                MathHelper.lerp(world.random.nextDouble(), xMin, xMax),
                yHeight,
                MathHelper.lerp(world.random.nextDouble(), zMax, zMin),
                0.0D,
                0.0D,
                0.0D);
    }
}
