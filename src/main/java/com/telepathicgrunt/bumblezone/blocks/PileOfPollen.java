package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.modinit.BzParticles;
import com.telepathicgrunt.bumblezone.tags.BzEntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class PileOfPollen extends FallingBlock {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
            Shapes.empty(),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    private Item item;

    public PileOfPollen() {
        super(BlockBehaviour.Properties.of(BzBlocks.ORANGE_NOT_SOLID)
                .isViewBlocking((blockState, world, blockPos) -> true)
                .noOcclusion()
                .strength(0.1F)
                .sound(SoundType.SNOW));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LAYERS);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(BzItems.POLLEN_PUFF);
    }

    /**
     * Makes this block spawn Pollen Puff when broken by piston or falling block breaks
     */
    @Override
    public Item asItem() {
        if (this.item == null) {
            this.item = BzItems.POLLEN_PUFF;
        }

        return this.item;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter world, BlockPos blockPos, PathComputationType pathType) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter world, BlockPos blockPos, CollisionContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter world, BlockPos blockPos, CollisionContext selectionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter world, BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter world, BlockPos blockPos, CollisionContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader world, BlockPos blockPos) {
        BlockState blockstate = world.getBlockState(blockPos.below());
        if(blockstate.is(Blocks.ICE) || blockstate.is(Blocks.PACKED_ICE) || blockstate.is(Blocks.BARRIER) || !world.getBlockState(blockPos).getFluidState().isEmpty()) {
            return false;
        }
        else if(blockstate.isAir() || blockstate.is(BzBlocks.PILE_OF_POLLEN) || blockstate.is(Blocks.HONEY_BLOCK) || blockstate.is(Blocks.SOUL_SAND)) {
            return true;
        }
        else {
            return Block.isFaceFull(blockstate.getCollisionShape(world, blockPos.below()), Direction.UP);
        }
    }

    @Override
    public BlockState updateShape(BlockState oldBlockState, Direction direction, BlockState newBlockState, LevelAccessor world, BlockPos blockPos, BlockPos blockPos1) {
        return !oldBlockState.canSurvive(world, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(oldBlockState, direction, newBlockState, world, blockPos, blockPos1);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext itemPlacementContext) {
        int layerValue = blockState.getValue(LAYERS);
        if (itemPlacementContext.getItemInHand().getItem() == this.asItem() && layerValue < 8) {
            // Need check for AutomaticItemPlacementContext as otherwise, stack overflow as replacingClickedOnBlock for AutomaticItemPlacementContext will call this method again
            if (!(itemPlacementContext instanceof DirectionalPlaceContext) && itemPlacementContext.replacingClickedOnBlock()) {
                return itemPlacementContext.getClickedFace() == Direction.UP;
            }
            else {
                return true;
            }
        }
        else {
            return layerValue == 1;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
        BlockState blockState = itemPlacementContext.getLevel().getBlockState(itemPlacementContext.getClickedPos());
        if (blockState.is(this)) {
            int layerValue = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(8, layerValue + 1));
        } else {
            return super.getStateForPlacement(itemPlacementContext);
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
     * the power fed into comparator (1 - 8)
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return blockState.getValue(LAYERS);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverWorld, BlockPos blockPos, Random random) {
        BlockState stateBelow = serverWorld.getBlockState(blockPos.below());
        if(stateBelow.is(BzBlocks.PILE_OF_POLLEN)) {
            if(stateBelow.getValue(LAYERS) == 8) {
                return;
            }
            else {
                serverWorld.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                stackPollen(stateBelow, serverWorld, blockPos.below(), blockState);
            }
        }

        super.tick(blockState, serverWorld, blockPos, random);
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos blockPos, BlockState blockState) {
        if(world.isClientSide()) {
            for(int i = 0; i < 50; i++) {
                spawnParticles(blockState, world, blockPos, world.getRandom(), true);
                spawnParticles(world, Vec3.atCenterOf(blockPos), world.getRandom(), 0.055D, 0.0075D, 0);
            }
        }
    }

    /**
     * Slows all entities inside the block.
     */
    @Override
    public void entityInside(BlockState blockState, Level world, BlockPos blockPos, Entity entity) {

        // make falling block of this block stack the pollen or else destroy it
        if(entity instanceof FallingBlockEntity) {
            if(((FallingBlockEntity) entity).getBlockState().isAir())
                return;

            if(((FallingBlockEntity)entity).getBlockState().is(BzBlocks.PILE_OF_POLLEN)) {
                stackPollen(blockState, world, blockPos, ((FallingBlockEntity)entity).getBlockState());
                entity.remove(Entity.RemovalReason.DISCARDED);

                // Prevents the FallingBlock's checkInsideBlocks from triggering this
                // method again for the pollen block we just set above our collision block.
                ((FallingBlockEntityAccessor) entity).thebumblezone_setBlock(Blocks.AIR.defaultBlockState());
            }
            else {
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                if(world.isClientSide()) {
                    for(int i = 0; i < blockState.getValue(LAYERS) * 30; i++){
                        spawnParticles(blockState, world, blockPos, world.random, true);
                    }
                }
            }
        }

        // Make pollen puff entity grow pile of pollen
        else if(entity.getType().equals(BzEntities.POLLEN_PUFF_ENTITY)){
            if(((PollenPuffEntity)entity).isConsumed()) return; // do not run this code if a block already was set.

            stackPollen(blockState, world, blockPos, BzBlocks.PILE_OF_POLLEN.defaultBlockState());
            entity.remove(Entity.RemovalReason.DISCARDED);
            ((PollenPuffEntity)entity).consumed();

            if(world.isClientSide()){
                for(int i = 0; i < 50; i++){
                    spawnParticles(world, entity.position(), world.random, 0.055D, 0.0075D, 0);
                }
            }
        }

        // slows the entity and spawns particles
        else {
            int layerValueMinusOne = blockState.getValue(LAYERS) - 1;
            double speedReduction = (entity instanceof Projectile) ? 0.85f : 1 - layerValueMinusOne * 0.1D;
            double chance = 0.22f + layerValueMinusOne * 0.09f;


            Vec3 deltaMovement = entity.getDeltaMovement();
            double newYDelta = deltaMovement.y;

            if(entity instanceof ServerPlayer && entity.fallDistance > 18 && newYDelta < -0.9D && blockState.getValue(LAYERS) >= 7) {
                BzCriterias.FALLING_ON_POLLEN_BLOCK_TRIGGER.trigger((ServerPlayer) entity);
            }

            if(deltaMovement.y > 0) {
                newYDelta *= (1f - layerValueMinusOne * 0.01f);
            }
            else {
                newYDelta *= (0.84f - layerValueMinusOne * 0.03f);
            }

            entity.setDeltaMovement(new Vec3(
                    deltaMovement.x * speedReduction,
                    newYDelta,
                    deltaMovement.z * speedReduction));

            double entitySpeed = entity.getDeltaMovement().length();

            // Need to multiply speed to avoid issues where tiny movement is seen as zero.
            if(entitySpeed > 0.00001D && world.random.nextFloat() < chance){
                int particleNumber = (int) (entitySpeed / 0.0045D);
                int particleStrength = (entity instanceof ItemEntity) ? Math.min(10, particleNumber / 3) : Math.min(20, particleNumber);

                if(world.isClientSide()) {
                    for(int i = 0; i < particleNumber; i++) {
                        if(particleNumber > 5) spawnParticles(blockState, world, blockPos, world.random, true);

                        spawnParticles(
                                world,
                                entity.position()
                                        .add(entity.getDeltaMovement().multiply(2D, 2D, 2D))
                                        .add(0, 0.75D, 0),
                                world.random,
                                0.006D * particleStrength,
                                0.00075D * particleStrength,
                                0.006D * particleStrength);
                    }
                }
                // Player and item entity runs this method on client side already so do not run it on server to reduce particle packet spam
                else if (!(entity instanceof Player || entity instanceof ItemEntity)){
                    spawnParticlesServer(
                            world,
                            entity.position()
                                    .add(entity.getDeltaMovement().multiply(2D, 2D, 2D))
                                    .add(0, 0.75D, 0),
                            world.random,
                            0.006D * particleStrength,
                            0.00075D * particleStrength,
                            0.006D * particleStrength,
                            particleNumber);
                }
            }

            // reduce pile of pollen to pollinate bee
            if(entity instanceof Bee && !((Bee)entity).hasNectar() && BzEntityTags.POLLEN_PUFF_CAN_POLLINATE.contains(entity.getType())) {
                ((BeeEntityInvoker)entity).thebumblezone_callSetHasNectar(true);
                ((Bee)entity).resetTicksWithoutNectarSinceExitingHive();
                if(layerValueMinusOne == 0) {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                }
                else {
                    world.setBlock(blockPos, blockState.setValue(LAYERS, layerValueMinusOne), 3);
                }
            }
        }

        if(entity instanceof Panda pandaEntity) {
            pandaSneezing(pandaEntity);
        }
    }

    public static void stackPollen(BlockState blockState, Level world, BlockPos blockPos, BlockState pollonToStack) {
        BlockState lastSetState = null;
        int initialLayerValue = blockState.getValue(LAYERS);
        int layersToAdd = pollonToStack.getValue(LAYERS);

        // Fill up current pile
        if(initialLayerValue < 8){
            int layerToMax = (8 - initialLayerValue);
            lastSetState = blockState.setValue(LAYERS, initialLayerValue + Math.min(layerToMax, layersToAdd));
            if(!world.isClientSide()) world.setBlock(blockPos, lastSetState, 3);
            layersToAdd -= layerToMax;
        }

        BlockState aboveState = world.getBlockState(blockPos.above());
        if(layersToAdd > 0 && aboveState.is(BzBlocks.PILE_OF_POLLEN)) {
            stackPollen(aboveState, world, blockPos.above(), blockState.setValue(LAYERS, layersToAdd));
        }
        else {
            // Stack on top of this pile
            if(layersToAdd > 0 && aboveState.isAir()) {
                lastSetState = blockState.setValue(LAYERS, layersToAdd);
                if(!world.isClientSide()) world.setBlock(blockPos.above(), blockState.setValue(LAYERS, layersToAdd), 3);
            }

            // Particles!
            if(world.isClientSide() && lastSetState != null) {
                for(int i = 0; i < 40; i++) {
                    spawnParticles(lastSetState, world, blockPos, world.random, true);
                }
            }
        }
    }

    public static void pandaSneezing(Panda pandaEntity) {
        if(!pandaEntity.level.isClientSide()) {
            if(pandaEntity.getRandom().nextFloat() < 0.005f && pandaEntity.level.getBlockState(pandaEntity.blockPosition()).is(BzBlocks.PILE_OF_POLLEN)) {
                pandaEntity.sneeze(true);
            }
        }
    }

    // Rarely spawn particle on its own
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, Random random) {
        int layerValue = blockState.getValue(LAYERS);
        double chance = 0.015f + layerValue * 0.008f;
        if(random.nextFloat() < chance) spawnParticles(blockState, world, blockPos, random, false);
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter blockReader, BlockPos blockPos) {
        return 16755200;
    }

    public static void spawnParticles(BlockState blockState, LevelAccessor world, BlockPos blockPos, Random random, boolean disturbed) {
        for(Direction direction : Direction.values()) {
            BlockPos blockpos = blockPos.relative(direction);
            if (!world.getBlockState(blockpos).isSolidRender(world, blockpos)) {
                double speedYModifier = disturbed ? 0.05D : 0.005D;
                double speedXZModifier = disturbed ? 0.03D : 0.005D;
                VoxelShape currentShape = SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
                double yHeight = currentShape.max(Direction.Axis.Y) - currentShape.min(Direction.Axis.Y);

                Direction.Axis directionAxis = direction.getAxis();
                double xOffset = directionAxis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getStepX() : (double)random.nextFloat();
                double yOffset = directionAxis == Direction.Axis.Y ? yHeight * (double)direction.getStepY() : (double)random.nextFloat() * yHeight;
                double zOffset = directionAxis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getStepZ() : (double)random.nextFloat();

                world.addParticle(
                        BzParticles.POLLEN,
                        (double)blockPos.getX() + xOffset,
                        (double)blockPos.getY() + yOffset,
                        (double)blockPos.getZ() + zOffset,
                        random.nextGaussian() * speedXZModifier,
                        (random.nextGaussian() * speedYModifier) + (disturbed ? 0.01D : 0),
                        random.nextGaussian() * speedXZModifier);

                return;
            }
        }
    }

    public static void spawnParticles(LevelAccessor world, Vec3 location, Random random, double speedXZModifier, double speedYModifier, double initYSpeed) {
        double xOffset = (random.nextFloat() * 0.3) - 0.15;
        double yOffset = (random.nextFloat() * 0.3) - 0.15;
        double zOffset = (random.nextFloat() * 0.3) - 0.15;

        world.addParticle(
                BzParticles.POLLEN,
                location.x() + xOffset,
                location.y() + yOffset,
                location.z() + zOffset,
                random.nextGaussian() * speedXZModifier,
                (random.nextGaussian() * speedYModifier) + initYSpeed,
                random.nextGaussian() * speedXZModifier);
    }

    public static void spawnParticlesServer(LevelAccessor world, Vec3 location, Random random, double speedXZModifier, double speedYModifier, double initYSpeed, int numberOfParticles) {
        if(world.isClientSide()) return;

        double xOffset = (random.nextFloat() * 0.3) - 0.15;
        double yOffset = (random.nextFloat() * 0.3) - 0.15;
        double zOffset = (random.nextFloat() * 0.3) - 0.15;

        ((ServerLevel)world).sendParticles(
                BzParticles.POLLEN,
                location.x() + xOffset,
                location.y() + yOffset,
                location.z() + zOffset,
                numberOfParticles,
                random.nextGaussian() * speedXZModifier,
                (random.nextGaussian() * speedYModifier) + initYSpeed,
                random.nextGaussian() * speedXZModifier,
                0.02f
        );
    }
}
