package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzBeeAggressionConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.Random;


public class HoneycombBrood extends ProperFacingBlock {
    private static final ResourceLocation HONEY_TREAT = new ResourceLocation("productivebees:honey_treat");
    public static final IntegerProperty STAGE = BlockStateProperties.AGE_3;

    public HoneycombBrood() {
        super(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).randomTicks().strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK).speedFactor(0.8F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(STAGE, 0));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }


    /**
     * Allow player to harvest honey and put honey into this block using bottles or wands
     */
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState thisBlockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
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
            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(Items.HONEY_BOTTLE), false, true);

            if ((playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                    BzBeeAggressionConfigs.allowWrathOfTheHiveOutsideBumblezone.get()) &&
                    !playerEntity.isCreative() &&
                    !playerEntity.isSpectator() &&
                    BzBeeAggressionConfigs.aggressiveBees.get())
            {
                if(playerEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE.get())) {
                    playerEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE.get());
                }
                else {
                    //Now all bees nearby in Bumblezone will get VERY angry!!!
                    playerEntity.addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE.get(), BzBeeAggressionConfigs.howLongWrathOfTheHiveLasts.get(), 2, false, BzBeeAggressionConfigs.showWrathOfTheHiveParticles.get(), true));
                }
            }

            return InteractionResult.SUCCESS;
        }
        else if (BzModCompatibilityConfigs.allowHoneyTreatCompat.get() && itemstack.getItem().getRegistryName().equals(HONEY_TREAT)) {
            if (!world.isClientSide()) {
                // spawn bee if at final stage and front isn't blocked off
                int stage = thisBlockState.getValue(STAGE);
                if (stage == 3) {
                    spawnBroodMob(world, thisBlockState, position, stage);
                }
                else {
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
                    SoundSource.NEUTRAL,
                    1.0F,
                    1.0F);

            // removes used item
            if (!playerEntity.isCreative()) {
                GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(itemstack.getItem()), true, true);
            }
            return InteractionResult.SUCCESS;
        }
        /*
         * Player is feeding larva
         */
        else if (itemstack.is(BzItemTags.BEE_FEEDING_ITEMS)) {
            if (!world.isClientSide()) {
                boolean successfulGrowth = false;

                //chance of growing the larva
                if (itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get()) {
                    if (world.random.nextFloat() < 0.30F)
                        successfulGrowth = true;
                } else {
                    successfulGrowth = true;
                }

                if (successfulGrowth && world.random.nextFloat() < 0.30F) {
                    if(!playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get())) {
                        playerEntity.addEffect(new MobEffectInstance(BzEffects.PROTECTION_OF_THE_HIVE.get(), (int) (BzBeeAggressionConfigs.howLongProtectionOfTheHiveLasts.get() * 0.75f), 1, false, false,  true));
                    }
                }

                //grows larva
                if (successfulGrowth) {
                    //spawn bee if at final stage and front isn't blocked off
                    int stage = thisBlockState.getValue(STAGE);
                    if (stage == 3) {
                        spawnBroodMob(world, thisBlockState, position, stage);
                    }
                    else {
                        int newStage = stage + 1;
                        if (itemstack.is(BzItemTags.HONEY_BUCKETS)) {
                            newStage = 3;
                            if (!world.isClientSide()) {
                                Direction facing = thisBlockState.getValue(FACING).getOpposite();
                                Vec3 centerFacePos = new Vec3(
                                        position.getX() + Math.max(-0.2D, facing.getStepX() == 0 ? 0.5D : facing.getStepX() * 1.2D),
                                        position.getY() + Math.max(-0.2D, facing.getStepY() == 0 ? 0.5D : facing.getStepY() * 1.2D),
                                        position.getZ() + Math.max(-0.2D, facing.getStepZ() == 0 ? 0.5D : facing.getStepZ() * 1.2D)
                                );

                                ((ServerLevel) world).sendParticles(
                                        ParticleTypes.HEART,
                                        centerFacePos.x(),
                                        centerFacePos.y(),
                                        centerFacePos.z(),
                                        3,
                                        world.getRandom().nextFloat() * 0.5 - 0.25f,
                                        world.getRandom().nextFloat() * 0.2f + 0.2f,
                                        world.getRandom().nextFloat() * 0.5 - 0.25f,
                                        world.getRandom().nextFloat() * 0.4 + 0.2f);
                            }
                        }

                        if(playerEntity instanceof ServerPlayer) {
                            BzCriterias.HONEY_BUCKET_BROOD_TRIGGER.trigger((ServerPlayer) playerEntity);
                        }

                        world.setBlockAndUpdate(position, thisBlockState.setValue(STAGE, newStage));
                    }
                }
            }

            //block grew one stage or bee was spawned
            world.playSound(playerEntity,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    SoundEvents.BOTTLE_EMPTY,
                    SoundSource.NEUTRAL,
                    1.0F,
                    1.0F);

            //removes used item
            if (!playerEntity.isCreative()) {
                GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(itemstack.getItem()), true, true);
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos position, Random rand) {
        super.tick(state, world, position, rand);
        if (!world.hasChunksAt(position, position))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(position).inflate(WrathOfTheHiveEffect.NEARBY_WRATH_EFFECT_RADIUS),
                entity -> entity.hasEffect(BzEffects.WRATH_OF_THE_HIVE.get()));

        int stage = state.getValue(STAGE);
        if (stage < 3) {
            if (!nearbyEntities.isEmpty() || (world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ? rand.nextInt(10) == 0 : rand.nextInt(22) == 0)) {
                world.setBlock(position, state.setValue(STAGE, stage + 1), 2);
            }
        }
        else if(BzGeneralConfigs.broodBlocksBeeSpawnCapacity.get() != 0) {
            if(!nearbyEntities.isEmpty() && GeneralUtils.getEntityCountInBz() < BzGeneralConfigs.broodBlocksBeeSpawnCapacity.get() * 1.75f) {
                spawnBroodMob(world, state, position, stage);
            }
            else if(GeneralUtils.getEntityCountInBz() < BzGeneralConfigs.broodBlocksBeeSpawnCapacity.get()) {
                spawnBroodMob(world, state, position, stage);
            }
        }
    }


    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @Override
    public void playerWillDestroy(Level world, BlockPos position, BlockState state, Player playerEntity) {
        ListTag listOfEnchants = playerEntity.getMainHandItem().getEnchantmentTags();
        if (listOfEnchants.stream().noneMatch(enchant -> enchant.getAsString().contains("minecraft:silk_touch"))) {
            BlockState blockState = world.getBlockState(position);
            int stage = blockState.getValue(STAGE);
            if (stage == 3) {
                spawnBroodMob(world, blockState, position, stage);
            }
        }

        super.playerWillDestroy(world, position, state, playerEntity);
    }


    private static void spawnBroodMob(Level world, BlockState state, BlockPos position, int stage) {
        //the front of the block
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(position);
        blockpos.move(state.getValue(FACING).getOpposite());

        if (stage == 3 && !world.getBlockState(blockpos).getMaterial().isSolid()) {
            Mob beeMob = EntityType.BEE.create(world);
            beeMob.setBaby(true);
            spawnMob(world, blockpos, beeMob, beeMob);

            if(world.random.nextFloat() < 0.1f) {
                Mob honeySlimeMob = BzEntities.HONEY_SLIME.get().create(world);
                honeySlimeMob.setBaby(true);
                spawnMob(world, blockpos, beeMob, honeySlimeMob);
            }

            world.setBlockAndUpdate(position, state.setValue(STAGE, 0));
        }
    }

    private static void spawnMob(Level world, BlockPos.MutableBlockPos blockpos, Mob beeMob, Mob entity) {
        if(entity == null || world.isClientSide()) return;
        entity.moveTo(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, world.getRandom().nextFloat() * 360.0F, 0.0F);
        entity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(new BlockPos(beeMob.position())), MobSpawnType.TRIGGERED, null, null);

        if(ForgeHooks.canEntitySpawn(entity, world, entity.position().x(), entity.position().y(), entity.position().z(), null, MobSpawnType.SPAWNER) != -1) {
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
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return blockState.getValue(STAGE) + 1;
    }


    /**
     * Called periodically clientside on blocks near the player to show honey particles. 50% of attempting to spawn a
     * particle. Also will buzz too based on stage
     */
    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos position, Random random) {
        //number of particles in this tick
        for (int i = 0; i < random.nextInt(2); ++i) {
            this.spawnHoneyParticles(world, position, blockState);
        }

        int stage = blockState.getValue(STAGE);
        float soundVolume = 0.05F + stage * 0.1F;
        if (world.random.nextInt(20) == 0)
            world.playLocalSound(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, SoundEvents.BEE_LOOP, SoundSource.BLOCKS, soundVolume, 1.0F, true);
    }


    /**
     * Starts checking if the block can take the particle and if so and it passes another rng to reduce spawnrate, it then
     * takes the block's dimensions and passes into methods to spawn the actual particle
     */
    private void spawnHoneyParticles(Level world, BlockPos position, BlockState blockState) {
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
    private void addHoneyParticle(Level world, BlockPos blockPos, VoxelShape blockShape, double height) {
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
    private void addHoneyParticle(Level world, double xMin, double xMax, double zMax, double zMin, double yHeight) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(world.random.nextDouble(), xMin, xMax), yHeight, Mth.lerp(world.random.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
    }
}
