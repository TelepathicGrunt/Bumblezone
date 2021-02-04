package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.effects.BzEffects;
import com.telepathicgrunt.the_bumblezone.entities.BzEntities;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.modCompat.BuzzierBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;


public class HoneycombBrood extends DirectionalBlock {
    private static final ResourceLocation HONEY_TREAT = new ResourceLocation("productivebees:honey_treat");
    public static final IntegerProperty STAGE = BlockStateProperties.AGE_0_3;

    public HoneycombBrood() {
        super(Block.Properties.create(Material.CLAY, MaterialColor.ADOBE).tickRandomly().hardnessAndResistance(0.5F, 0.5F).sound(SoundType.CORAL).speedFactor(0.9F));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.SOUTH).with(STAGE, 0));
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace().getOpposite());
    }


    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }


    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }


    /**
     * Called when the given entity walks on this Block
     */
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        double yMagnitude = Math.abs(entityIn.getMotion().y);
        if (yMagnitude < 0.1D) {
            double slowFactor = 0.85D;
            entityIn.setMotion(entityIn.getMotion().mul(slowFactor, 1.0D, slowFactor));
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }


    /**
     * Allow player to harvest honey and put honey into this block using bottles or wands
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {
        ItemStack itemstack = playerEntity.getHeldItem(playerHand);

        //VANILLA COMPAT
        /*
         * Player is taking honey and killing larva
         */
        if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            world.setBlockState(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState().with(BlockStateProperties.FACING, thisBlockState.get(BlockStateProperties.FACING)), 3); // removed honey from this block

            //spawn angry bee if at final stage and front isn't blocked off
            int stage = thisBlockState.get(STAGE);
            spawnBroodMob(world, thisBlockState, position, stage);
            world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                itemstack.shrink(1); // remove current empty bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(Items.HONEY_BOTTLE)); // places honey bottle in hand
                } else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.HONEY_BOTTLE))) // places honey bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.HONEY_BOTTLE), false); // drops honey bottle if inventory is full
                }
            }

            if ((playerEntity.getEntityWorld().getDimensionKey().getLocation().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BzBeeAggressionConfig.aggressiveBees.get())
            {
                if(playerEntity.isPotionActive(BzEffects.PROTECTION_OF_THE_HIVE.get())){
                    playerEntity.removePotionEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                }
                else {
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addPotionEffect(new EffectInstance(
                            BzEffects.WRATH_OF_THE_HIVE.get(),
                            Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(),
                            2,
                            false,
                            Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(),
                            true));
                }
            }

            return ActionResultType.SUCCESS;
        }

        /*
         * Player is feeding larva
         */
        else if (itemstack.getItem() == Items.HONEY_BOTTLE || itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get()) {
            if (!world.isRemote) {
                boolean successfulGrowth = false;

                //chance of growing the larva
                if (itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get()) {
                    if (world.rand.nextFloat() < 0.30F)
                        successfulGrowth = true;
                } else {
                    successfulGrowth = true;
                }

                if (successfulGrowth && world.rand.nextFloat() < 0.30F) {
                    if(!playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE.get())){
                        playerEntity.addPotionEffect(new EffectInstance(BzEffects.PROTECTION_OF_THE_HIVE.get(), (int) (Bumblezone.BzBeeAggressionConfig.howLongProtectionOfTheHiveLasts.get() * 0.75f), 1, false, false,  true));
                    }
                }

                //grows larva
                if (successfulGrowth) {
                    //spawn bee if at final stage and front isn't blocked off
                    int stage = thisBlockState.get(STAGE);
                    if (stage == 3) {
                        spawnBroodMob(world, thisBlockState, position, stage);
                    } else {
                        world.setBlockState(position, thisBlockState.with(STAGE, stage + 1));
                    }
                }
            }

            //block grew one stage or bee was spawned
            world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            //removes used item
            if (!playerEntity.isCreative()) {
                itemstack.shrink(1); // remove current honey bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places empty bottle in hand
                } else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) // places empty bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops empty bottle if inventory is full
                }
            }

            return ActionResultType.SUCCESS;
        }

        // makes honey treat have a slight chance of growing the larva 2 stages instead of 1
        else if (ModChecker.productiveBeesPresent && Bumblezone.BzModCompatibilityConfig.allowHoneyTreatCompat.get()
                && itemstack.getItem().getRegistryName().equals(HONEY_TREAT))
        {
            if (!world.isRemote) {
                // spawn bee if at final stage and front isn't blocked off
                int stage = thisBlockState.get(STAGE);
                if (stage == 3) {
                    spawnBroodMob(world, thisBlockState, position, stage);
                } else {
                    int stageIncrease = world.rand.nextFloat() < 0.2f ? 2 : 1;
                    world.setBlockState(position, thisBlockState.with(STAGE, Math.min(3, stage + stageIncrease)));
                }
            }

            // block grew one stage or bee was spawned
            world.playSound(
                    playerEntity,
                    playerEntity.getPosX(),
                    playerEntity.getPosY(),
                    playerEntity.getPosZ(),
                    SoundEvents.ITEM_BOTTLE_EMPTY,
                    SoundCategory.NEUTRAL,
                    1.0F,
                    1.0F);

            // removes used item
            if (!playerEntity.isCreative()) {
                itemstack.shrink(1); // item was consumed
            }
        }

        // Buzzier Bees honey wand compat
        else if (ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowHoneyWandCompat.get()) {

            // Player is taking honey and killing larva/
            ActionResultType action = BuzzierBeesRedirection.honeyWandTakingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS) {

                // removed honey from this block
                world.setBlockState(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState().with(BlockStateProperties.FACING, thisBlockState.get(BlockStateProperties.FACING)), 3);

                // spawn angry bee if at final stage and front isn't blocked off
                int stage = thisBlockState.get(STAGE);
                spawnBroodMob(world, thisBlockState, position, stage);

                world.playSound(
                        playerEntity,
                        playerEntity.getPosX(),
                        playerEntity.getPosY(),
                        playerEntity.getPosZ(),
                        SoundEvents.BLOCK_HONEY_BLOCK_BREAK,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                if ((world.getDimensionKey().equals(BzDimension.BZ_WORLD_KEY) ||
                        Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                        !playerEntity.isCreative() &&
                        !playerEntity.isSpectator() &&
                        Bumblezone.BzBeeAggressionConfig.aggressiveBees.get()) {

                    // Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(),
                            Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), 2, false,
                            Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(), true));
                }

                return action;
            }

            // Player is feeding larva
            action = BuzzierBeesRedirection.honeyWandGivingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS) {
                world.playSound(
                        playerEntity,
                        playerEntity.getPosX(),
                        playerEntity.getPosY(),
                        playerEntity.getPosZ(),
                        SoundEvents.BLOCK_HONEY_BLOCK_BREAK,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                // spawn bee if at final stage and front isn't blocked off
                int stage = thisBlockState.get(STAGE);
                if (stage == 3) {
                    spawnBroodMob(world, thisBlockState, position, stage);
                } else {
                    world.setBlockState(position, thisBlockState.with(STAGE, stage + 1));
                }

                return action;
            }
        }

        return super.onBlockActivated(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos position, Random rand) {
        super.tick(state, world, position, rand);
        if (!world.isAreaLoaded(position, position))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        int stage = state.get(STAGE);
        if (stage < 3) {
            if (world.getDimensionKey().getLocation().equals(Bumblezone.MOD_DIMENSION_ID) ? rand.nextInt(10) == 0 : rand.nextInt(22) == 0) {
                world.setBlockState(position, state.with(STAGE, stage + 1), 2);
            }
        }
        else if(Bumblezone.BzBlockMechanicsConfig.broodBlocksBeeSpawnCapacity.get() != 0){
            List<Entity> beeList = world.getEntities(EntityType.BEE, (entity) -> true);
            if(beeList.size() < Bumblezone.BzBlockMechanicsConfig.broodBlocksBeeSpawnCapacity.get()){
                spawnBroodMob(world, state, position, stage);
            }
        }
    }


    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @Override
    public void onBlockHarvested(World world, BlockPos position, BlockState state, PlayerEntity playerEntity) {

        ListNBT listOfEnchants = playerEntity.getHeldItemMainhand().getEnchantmentTagList();
        if (listOfEnchants.stream().noneMatch(enchant -> enchant.getString().contains("minecraft:silk_touch"))) {
            BlockState blockState = world.getBlockState(position);
            int stage = blockState.get(STAGE);
            if (stage == 3) {
                spawnBroodMob(world, blockState, position, stage);
            }

            if ((playerEntity.getEntityWorld().getDimensionKey().getLocation().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BzBeeAggressionConfig.aggressiveBees.get()) {
                if (playerEntity.isPotionActive(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                    playerEntity.removePotionEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                }
                else {
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(), true));
                }
            }
        }

        super.onBlockHarvested(world, position, state, playerEntity);
    }


    private static void spawnBroodMob(World world, BlockState state, BlockPos position, int stage) {
        //the front of the block
        BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(position);
        blockpos.move(state.get(FACING).getOpposite());

        if (stage == 3 && !world.getBlockState(blockpos).getMaterial().isSolid()) {
            MobEntity beeMob = EntityType.BEE.create(world);
            beeMob.setChild(true);
            spawnMob(world, blockpos, beeMob, beeMob);

            if(world.rand.nextFloat() < 0.1f){
                MobEntity honeySlimeMob = BzEntities.HONEY_SLIME.get().create(world);
                honeySlimeMob.setChild(true);
                spawnMob(world, blockpos, beeMob, honeySlimeMob);
            }

            world.setBlockState(position, state.with(STAGE, 0));
        }
    }

    private static void spawnMob(World world, BlockPos.Mutable blockpos, MobEntity beeMob, MobEntity entity) {
        if(entity == null || world.isRemote) return;
        entity.setLocationAndAngles(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, world.getRandom().nextFloat() * 360.0F, 0.0F);

        if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(
                entity,
                world,
                blockpos.getX() + 0.5D,
                blockpos.getY() + 0.5D,
                blockpos.getZ() + 0.5D,
                null,
                SpawnReason.TRIGGERED) != -1) {

            entity.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(new BlockPos(beeMob.getPositionVec())), SpawnReason.TRIGGERED, null, null);
            world.addEntity(entity);
        }
    }


    /**
     * tell redstone that this can be use with comparator
     */
    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }


    /**
     * the power fed into comparator (1 - 4)
     */
    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(STAGE) + 1;
    }


    /**
     * Called periodically clientside on blocks near the player to show honey particles. 50% of attempting to spawn a
     * particle. Also will buzz too based on stage
     */
    @Override
    public void animateTick(BlockState blockState, World world, BlockPos position, Random random) {
        //number of particles in this tick
        for (int i = 0; i < random.nextInt(2); ++i) {
            this.spawnHoneyParticles(world, position, blockState);
        }

        int stage = blockState.get(STAGE);
        float soundVolume = 0.05F + stage * 0.1F;
        if (world.rand.nextInt(20) == 0)
            world.playSound(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, SoundEvents.ENTITY_BEE_LOOP, SoundCategory.BLOCKS, soundVolume, 1.0F, true);
    }


    /**
     * Starts checking if the block can take the particle and if so and it passes another rng to reduce spawnrate, it then
     * takes the block's dimensions and passes into methods to spawn the actual particle
     */
    private void spawnHoneyParticles(World world, BlockPos position, BlockState blockState) {
        if (blockState.getFluidState().isEmpty() && world.rand.nextFloat() < 0.08F) {
            VoxelShape currentBlockShape = blockState.getCollisionShape(world, position);
            double yEndHeight = currentBlockShape.getEnd(Direction.Axis.Y);
            if (yEndHeight >= 1.0D && !blockState.isIn(BlockTags.IMPERMEABLE)) {
                double yStartHeight = currentBlockShape.getStart(Direction.Axis.Y);
                if (yStartHeight > 0.0D) {
                    this.addHoneyParticle(world, position, currentBlockShape, position.getY() + yStartHeight - 0.05D);
                } else {
                    BlockPos belowBlockpos = position.down();
                    BlockState belowBlockstate = world.getBlockState(belowBlockpos);
                    VoxelShape belowBlockShape = belowBlockstate.getCollisionShape(world, belowBlockpos);
                    double yEndHeight2 = belowBlockShape.getEnd(Direction.Axis.Y);
                    if ((yEndHeight2 < 1.0D || !belowBlockstate.isOpaqueCube(world, belowBlockpos)) && belowBlockstate.getFluidState().isEmpty()) {
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
                blockPos.getX() + blockShape.getStart(Direction.Axis.X),
                blockPos.getX() + blockShape.getEnd(Direction.Axis.X),
                blockPos.getZ() + blockShape.getStart(Direction.Axis.Z),
                blockPos.getZ() + blockShape.getEnd(Direction.Axis.Z),
                height);
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addHoneyParticle(World world, double xMin, double xMax, double zMax, double zMin, double yHeight) {

        world.addParticle(
                ParticleTypes.DRIPPING_HONEY,
                MathHelper.lerp(world.rand.nextDouble(), xMin, xMax),
                yHeight,
                MathHelper.lerp(world.rand.nextDouble(), zMax, zMin),
                0.0D,
                0.0D,
                0.0D);
    }
}
