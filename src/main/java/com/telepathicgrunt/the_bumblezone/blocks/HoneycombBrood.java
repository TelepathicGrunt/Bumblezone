package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.List;


public class HoneycombBrood extends ProperFacingBlock {
    private static final ResourceLocation HONEY_TREAT = new ResourceLocation("productivebees:honey_treat");
    public static final IntegerProperty STAGE = BlockStateProperties.AGE_3;

    public HoneycombBrood() {
        super(QuiltBlockSettings.of(Material.CLAY, MaterialColor.COLOR_ORANGE).randomTicks().strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK).speedFactor(0.8F));
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
        RandomSource random = playerEntity.getRandom();

        //VANILLA COMPAT
        /*
         * Player is taking honey and killing larva
         */
        if (itemstack.getItem() == Items.GLASS_BOTTLE) {
            GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(Items.HONEY_BOTTLE), false, true);

            //spawn angry bee if at final stage and front isn't blocked off
            int stage = thisBlockState.getValue(STAGE);
            spawnBroodMob(world, random, thisBlockState, position, stage);
            world.setBlock(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.defaultBlockState().setValue(BlockStateProperties.FACING, thisBlockState.getValue(BlockStateProperties.FACING)), 3); // removed honey from this block

            if ((playerEntity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ||
                BzConfig.allowWrathOfTheHiveOutsideBumblezone) &&
                !playerEntity.isCreative() &&
                !playerEntity.isSpectator() &&
                BzConfig.aggressiveBees &&
                world.getDifficulty() != Difficulty.PEACEFUL)
            {
                if (playerEntity instanceof ServerPlayer serverPlayer && !EssenceOfTheBees.hasEssence(serverPlayer)) {
                    if(playerEntity.hasEffect(BzEffects.PROTECTION_OF_THE_HIVE)) {
                        playerEntity.removeEffect(BzEffects.PROTECTION_OF_THE_HIVE);
                    }
                    else {
                        //Now all bees nearby in Bumblezone will get VERY angry!!!
                        playerEntity.addEffect(new MobEffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }
        /*
         * Player is feeding larva
         */
        else if (itemstack.is(BzTags.BEE_FEEDING_ITEMS)) {
            if (!world.isClientSide()) {
                boolean successfulGrowth = false;

                //chance of growing the larva
                if (itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE) {
                    if (random.nextFloat() < 0.30F)
                        successfulGrowth = true;
                } else {
                    successfulGrowth = true;
                }

                if (successfulGrowth && random.nextFloat() < 0.30F) {
                    if(!playerEntity.hasEffect(BzEffects.WRATH_OF_THE_HIVE)) {
                        playerEntity.addEffect(new MobEffectInstance(BzEffects.PROTECTION_OF_THE_HIVE, (int) (BzConfig.howLongProtectionOfTheHiveLasts * 0.75f), 1, false, false,  true));

                        if (playerEntity instanceof ServerPlayer serverPlayer) {
                            BzCriterias.GETTING_PROTECTION_TRIGGER.trigger(serverPlayer);
                        }
                    }
                }

                //grows larva
                if (successfulGrowth) {
                    //spawn bee if at final stage and front isn't blocked off
                    int stage = thisBlockState.getValue(STAGE);
                    if (stage == 3) {
                        spawnBroodMob(world, random, thisBlockState, position, stage);
                    }
                    else {
                        int newStage = stage + 1;
                        if (itemstack.is(BzTags.HONEY_BUCKETS) || itemstack.is(BzTags.ROYAL_JELLY_BUCKETS)) {
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
                                        random.nextFloat() * 0.5 - 0.25f,
                                        random.nextFloat() * 0.2f + 0.2f,
                                        random.nextFloat() * 0.5 - 0.25f,
                                        random.nextFloat() * 0.4 + 0.2f);
                            }

                            if(playerEntity instanceof ServerPlayer) {
                                BzCriterias.HONEY_BUCKET_BROOD_TRIGGER.trigger((ServerPlayer) playerEntity);
                            }
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
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F);

            //removes used item
            GeneralUtils.givePlayerItem(playerEntity, playerHand, ItemStack.EMPTY, true, true);

            return InteractionResult.SUCCESS;
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos position, RandomSource random) {
        super.tick(state, world, position, random);
        if (!world.hasChunksAt(position, position))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light

        List<LivingEntity> nearbyEntities = null;

        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            nearbyEntities = world.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(position).inflate(WrathOfTheHiveEffect.NEARBY_WRATH_EFFECT_RADIUS),
                entity -> entity.hasEffect(BzEffects.WRATH_OF_THE_HIVE));
        }

        int stage = state.getValue(STAGE);
        if (stage < 3) {
            if ((nearbyEntities != null && !nearbyEntities.isEmpty()) || (world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) ? random.nextInt(10) == 0 : random.nextInt(22) == 0)) {
                world.setBlock(position, state.setValue(STAGE, stage + 1), 2);
            }
        }
        else if(BzConfig.broodBlocksBeeSpawnCapacity != 0) {
            if((nearbyEntities != null && !nearbyEntities.isEmpty()) && GeneralUtils.getNearbyActiveEntitiesInDimension(world, position) < BzConfig.broodBlocksBeeSpawnCapacity * 1.75f) {
                spawnBroodMob(world, random, state, position, stage);
            }
            else if(GeneralUtils.getNearbyActiveEntitiesInDimension(world, position) < BzConfig.broodBlocksBeeSpawnCapacity) {
                spawnBroodMob(world, random, state, position, stage);
            }
        }
    }


    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
     * this block
     */
    @Override
    public void playerWillDestroy(Level world, BlockPos position, BlockState state, Player playerEntity) {
        if (world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ListTag listOfEnchants = playerEntity.getMainHandItem().getEnchantmentTags();
            if (listOfEnchants.stream().noneMatch(enchant -> enchant.getAsString().contains("minecraft:silk_touch"))) {
                BlockState blockState = world.getBlockState(position);
                int stage = blockState.getValue(STAGE);
                if (stage == 3) {
                    spawnBroodMob(world, playerEntity.getRandom(), blockState, position, stage);
                }
            }
        }

        super.playerWillDestroy(world, position, state, playerEntity);
    }


    private static void spawnBroodMob(Level world, RandomSource random, BlockState state, BlockPos position, int stage) {
        //the front of the block
        BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(position);
        blockpos.move(state.getValue(FACING).getOpposite());

        BlockState frontState = world.getBlockState(blockpos);
        if (stage == 3 && frontState.getFluidState().isEmpty() && !frontState.isCollisionShapeFullBlock(world, position)) {
            Mob beeMob = EntityType.BEE.create(world);
            beeMob.setBaby(true);
            spawnMob(world, blockpos, beeMob, beeMob);

            if(random.nextFloat() < 0.1f) {
                Mob honeySlimeMob = BzEntities.HONEY_SLIME.create(world);
                honeySlimeMob.setBaby(true);
                spawnMob(world, blockpos, beeMob, honeySlimeMob);
            }

            world.setBlockAndUpdate(position, state.setValue(STAGE, 0));
        }
    }

    private static void spawnMob(Level world, BlockPos.MutableBlockPos blockpos, Mob beeMob, Mob entity) {
        if(entity == null || world.isClientSide()) return;
        entity.moveTo(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, beeMob.getRandom().nextFloat() * 360.0F, 0.0F);
        entity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(new BlockPos(beeMob.position())), MobSpawnType.TRIGGERED, null, null);

        world.addFreshEntity(entity);
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
    public void animateTick(BlockState blockState, Level world, BlockPos position, RandomSource random) {
        //number of particles in this tick
        for (int i = 0; i < random.nextInt(2); ++i) {
            this.spawnHoneyParticles(world, random, position, blockState);
        }

        int stage = blockState.getValue(STAGE);
        float soundVolume = 0.05F + stage * 0.1F;
        if (random.nextInt(20) == 0) {
            world.playLocalSound(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D, SoundEvents.BEE_LOOP, SoundSource.BLOCKS, soundVolume, 1.0F, true);
        }
    }


    /**
     * Starts checking if the block can take the particle and if so and it passes another rng to reduce spawnrate, it then
     * takes the block's dimensions and passes into methods to spawn the actual particle
     */
    private void spawnHoneyParticles(Level world, RandomSource random, BlockPos position, BlockState blockState) {
        if (blockState.getFluidState().isEmpty() && random.nextFloat() < 0.08F) {
            VoxelShape currentBlockShape = blockState.getCollisionShape(world, position);
            double yEndHeight = currentBlockShape.max(Direction.Axis.Y);
            if (yEndHeight >= 1.0D && !blockState.is(BlockTags.IMPERMEABLE)) {
                double yStartHeight = currentBlockShape.min(Direction.Axis.Y);
                if (yStartHeight > 0.0D) {
                    this.addHoneyParticle(world, random, position, currentBlockShape, position.getY() + yStartHeight - 0.05D);
                } else {
                    BlockPos belowBlockpos = position.below();
                    BlockState belowBlockstate = world.getBlockState(belowBlockpos);
                    VoxelShape belowBlockShape = belowBlockstate.getCollisionShape(world, belowBlockpos);
                    double yEndHeight2 = belowBlockShape.max(Direction.Axis.Y);
                    if ((yEndHeight2 < 1.0D || !belowBlockstate.isSolidRender(world, belowBlockpos)) && belowBlockstate.getFluidState().isEmpty()) {
                        this.addHoneyParticle(world, random, position, currentBlockShape, position.getY() - 0.05D);
                    }
                }
            }

        }
    }


    /**
     * intermediary method to apply the blockshape and ranges that the particle can spawn in for the next addHoneyParticle
     * method
     */
    private void addHoneyParticle(Level world, RandomSource random, BlockPos blockPos, VoxelShape blockShape, double height) {
        this.addHoneyParticle(
                world,
                random,
                blockPos.getX() + blockShape.min(Direction.Axis.X),
                blockPos.getX() + blockShape.max(Direction.Axis.X),
                blockPos.getZ() + blockShape.min(Direction.Axis.Z),
                blockPos.getZ() + blockShape.max(Direction.Axis.Z),
                height);
    }


    /**
     * Adds the actual honey particle into the world within the given range
     */
    private void addHoneyParticle(Level world, RandomSource random, double xMin, double xMax, double zMax, double zMin, double yHeight) {
        world.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(random.nextDouble(), xMin, xMax), yHeight, Mth.lerp(random.nextDouble(), zMax, zMin), 0.0D, 0.0D, 0.0D);
    }
}
