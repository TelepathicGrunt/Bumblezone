package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzDimension;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class HoneycombBrood extends ProperFacingBlock {
    private static final ResourceLocation HONEY_TREAT = new ResourceLocation("productivebees:honey_treat");
    public static final IntegerProperty STAGE = BlockStateProperties.AGE_3;

    public HoneycombBrood() {
        super(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).randomTicks().strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK).speedFactor(0.8F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(STAGE, 0));
    }


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    /**
     * Allow player to harvest honey and put honey into this block using bottles or wands
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        //VANILLA COMPAT
        /*
         * Player is taking honey and killing larva
         */
        if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            world.setBlock(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.get().defaultBlockState().setValue(BlockStateProperties.FACING, thisBlockState.getValue(BlockStateProperties.FACING)), 3); // removed honey from this block

            //spawn angry bee if at final stage and front isn't blocked off
            int stage = thisBlockState.getValue(STAGE);
            spawnBroodMob(world, thisBlockState, position, stage);
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
                else {
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addEffect(new EffectInstance(
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
        else if (itemstack.getItem().is(BzItemTags.BEE_FEEDING_ITEMS)) {
            if (!world.isClientSide) {
                boolean successfulGrowth = false;

                //chance of growing the larva
                if (itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get()) {
                    if (world.random.nextFloat() < 0.30F)
                        successfulGrowth = true;
                } else {
                    successfulGrowth = true;
                }

                if (successfulGrowth && world.random.nextFloat() < 0.30F) {
                    if(!playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get())){
                        playerEntity.addEffect(new EffectInstance(BzEffects.PROTECTION_OF_THE_HIVE.get(), (int) (Bumblezone.BzBeeAggressionConfig.howLongProtectionOfTheHiveLasts.get() * 0.75f), 1, false, false,  true));
                    }
                }

                //grows larva
                if (successfulGrowth) {
                    //spawn bee if at final stage and front isn't blocked off
                    int stage = thisBlockState.getValue(STAGE);
                    if (stage == 3) {
                        spawnBroodMob(world, thisBlockState, position, stage);
                    } else {
                        world.setBlockAndUpdate(position, thisBlockState.setValue(STAGE, stage + 1));
                    }
                }
            }

            //block grew one stage or bee was spawned
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            //removes used item
            if (!playerEntity.isCreative()) {
                itemstack.shrink(1); // remove current honey bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places empty bottle in hand
                } else if (!playerEntity.inventory.add(new ItemStack(Items.GLASS_BOTTLE))) // places empty bottle in inventory
                {
                    playerEntity.drop(new ItemStack(Items.GLASS_BOTTLE), false); // drops empty bottle if inventory is full
                }
            }

            return ActionResultType.SUCCESS;
        }

        // makes honey treat have a slight chance of growing the larva 2 stages instead of 1
        else if (ModChecker.productiveBeesPresent && Bumblezone.BzModCompatibilityConfig.allowHoneyTreatCompat.get()
                && itemstack.getItem().getRegistryName().equals(HONEY_TREAT))
        {
            if (!world.isClientSide) {
                // spawn bee if at final stage and front isn't blocked off
                int stage = thisBlockState.getValue(STAGE);
                if (stage == 3) {
                    spawnBroodMob(world, thisBlockState, position, stage);
                } else {
                    int stageIncrease = world.random.nextFloat() < 0.2f ? 2 : 1;
                    world.setBlockAndUpdate(position, thisBlockState.setValue(STAGE, Math.min(3, stage + stageIncrease)));
                }
            }

            // block grew one stage or bee was spawned
            world.playSound(
                    playerEntity,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    SoundEvents.BOTTLE_EMPTY,
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
            ActionResultType action = BuzzierBeesCompat.honeyWandTakingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS) {

                // removed honey from this block
                world.setBlock(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.get().defaultBlockState().setValue(BlockStateProperties.FACING, thisBlockState.getValue(BlockStateProperties.FACING)), 3);

                // spawn angry bee if at final stage and front isn't blocked off
                int stage = thisBlockState.getValue(STAGE);
                spawnBroodMob(world, thisBlockState, position, stage);

                world.playSound(
                        playerEntity,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.HONEY_BLOCK_BREAK,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                if ((world.dimension().equals(BzDimension.BZ_WORLD_KEY) ||
                        Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                        !playerEntity.isCreative() &&
                        !playerEntity.isSpectator() &&
                        Bumblezone.BzBeeAggressionConfig.aggressiveBees.get()) {

                    // Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(),
                            Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), 2, false,
                            Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(), true));
                }

                return action;
            }

            // Player is feeding larva
            action = BuzzierBeesCompat.honeyWandGivingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS) {
                world.playSound(
                        playerEntity,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.HONEY_BLOCK_BREAK,
                        SoundCategory.NEUTRAL,
                        1.0F,
                        1.0F);

                // spawn bee if at final stage and front isn't blocked off
                int stage = thisBlockState.getValue(STAGE);
                if (stage == 3) {
                    spawnBroodMob(world, thisBlockState, position, stage);
                } else {
                    world.setBlockAndUpdate(position, thisBlockState.setValue(STAGE, stage + 1));
                }

                return action;
            }
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos position, Random rand) {
        super.tick(state, world, position, rand);
        if (!world.hasChunksAt(position, position))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        List<Entity> nearbyEntities = world.getEntitiesOfClass(
                LivingEntity.class,
                new AxisAlignedBB(position).inflate(WrathOfTheHiveEffect.NEARBY_WRATH_EFFECT_RADIUS),
                entity -> ((LivingEntity)entity).hasEffect(BzEffects.WRATH_OF_THE_HIVE.get()));

        int stage = state.getValue(STAGE);
        if (stage < 3) {
            if (!nearbyEntities.isEmpty() || (world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ? rand.nextInt(10) == 0 : rand.nextInt(22) == 0)) {
                world.setBlock(position, state.setValue(STAGE, stage + 1), 2);
            }
        }
        else if(Bumblezone.BzBlockMechanicsConfig.broodBlocksBeeSpawnCapacity.get() != 0){
            if(!nearbyEntities.isEmpty() && GeneralUtils.getEntityCountInBz() < Bumblezone.BzBlockMechanicsConfig.broodBlocksBeeSpawnCapacity.get() * 1.75f){
                spawnBroodMob(world, state, position, stage);
            }
            else if (GeneralUtils.getEntityCountInBz() < Bumblezone.BzBlockMechanicsConfig.broodBlocksBeeSpawnCapacity.get()) {
                spawnBroodMob(world, state, position, stage);
            }
        }
    }


    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @Override
    public void playerWillDestroy(World world, BlockPos position, BlockState state, PlayerEntity playerEntity) {

        ListNBT listOfEnchants = playerEntity.getMainHandItem().getEnchantmentTags();
        if (listOfEnchants.stream().noneMatch(enchant -> enchant.getAsString().contains("minecraft:silk_touch"))) {
            BlockState blockState = world.getBlockState(position);
            int stage = blockState.getValue(STAGE);
            if (stage == 3) {
                spawnBroodMob(world, blockState, position, stage);
            }

            if ((playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    Bumblezone.BzBeeAggressionConfig.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    Bumblezone.BzBeeAggressionConfig.aggressiveBees.get()) {
                if (playerEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                    playerEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                }
                else {
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), Bumblezone.BzBeeAggressionConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzBeeAggressionConfig.showWrathOfTheHiveParticles.get(), true));
                }
            }
        }

        super.playerWillDestroy(world, position, state, playerEntity);
    }


    private static void spawnBroodMob(World world, BlockState state, BlockPos position, int stage) {
        //the front of the block
        BlockPos.Mutable blockpos = new BlockPos.Mutable().set(position);
        blockpos.move(state.getValue(FACING).getOpposite());

        if (stage == 3 && !world.getBlockState(blockpos).getMaterial().isSolid()) {
            MobEntity beeMob = EntityType.BEE.create(world);
            beeMob.setBaby(true);
            spawnMob(world, blockpos, beeMob, beeMob);

            if(world.random.nextFloat() < 0.1f){
                MobEntity honeySlimeMob = BzEntities.HONEY_SLIME.get().create(world);
                honeySlimeMob.setBaby(true);
                spawnMob(world, blockpos, beeMob, honeySlimeMob);
            }

            world.setBlockAndUpdate(position, state.setValue(STAGE, 0));
        }
    }

    private static void spawnMob(World world, BlockPos.Mutable blockpos, MobEntity beeMob, MobEntity entity) {
        if(entity == null || world.isClientSide) return;
        entity.moveTo(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, world.getRandom().nextFloat() * 360.0F, 0.0F);

        if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(
                entity,
                world,
                blockpos.getX() + 0.5D,
                blockpos.getY() + 0.5D,
                blockpos.getZ() + 0.5D,
                null,
                SpawnReason.TRIGGERED) != -1) {

            entity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(new BlockPos(beeMob.position())), SpawnReason.TRIGGERED, null, null);
            world.addFreshEntity(entity);
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
     * the power fed into comparator (1 - 4)
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(STAGE) + 1;
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

        int stage = blockState.getValue(STAGE);
        float soundVolume = 0.05F + stage * 0.1F;
        if (world.random.nextInt(20) == 0)
            world.playLocalSound(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, SoundEvents.BEE_LOOP, SoundCategory.BLOCKS, soundVolume, 1.0F, true);
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
