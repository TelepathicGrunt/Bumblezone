package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.the_bumblezone.mixin.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.tags.BzEntityTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import javax.annotation.Nullable;
import java.util.Random;

public class PileOfPollen extends FallingBlock {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
            VoxelShapes.empty(),
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
        super(AbstractBlock.Properties.of(BzBlocks.ORANGE_NOT_SOLID)
                .isViewBlocking((blockState, world, blockPos) -> true)
                .noOcclusion()
                .strength(0.1F)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.SNOW));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LAYERS);
    }

    @Override
    public ItemStack getCloneItemStack(IBlockReader p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return new ItemStack(BzItems.POLLEN_PUFF.get());
    }

    /**
     * Makes this block spawn Pollen Puff when broken by piston or falling block breaks
     */
    @Override
    public Item asItem() {
        if (this.item == null) {
            this.item = BzItems.POLLEN_PUFF.get();
        }

        return this.item;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, IBlockReader world, BlockPos blockPos, PathType pathType) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader world, BlockPos blockPos, ISelectionContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, IBlockReader world, BlockPos blockPos, ISelectionContext selectionContext) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, IBlockReader world, BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, IBlockReader world, BlockPos blockPos, ISelectionContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.getValue(LAYERS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState blockState, IWorldReader world, BlockPos blockPos) {
        BlockState blockstate = world.getBlockState(blockPos.below());
        if(blockstate.is(Blocks.ICE) || blockstate.is(Blocks.PACKED_ICE) || blockstate.is(Blocks.BARRIER) || !world.getBlockState(blockPos).getFluidState().isEmpty()) {
            return false;
        }
        else if(blockstate.isAir() || blockstate.is(BzBlocks.PILE_OF_POLLEN.get()) || blockstate.is(Blocks.HONEY_BLOCK) || blockstate.is(Blocks.SOUL_SAND)) {
            return true;
        }
        else {
            return Block.isFaceFull(blockstate.getCollisionShape(world, blockPos.below()), Direction.UP);
        }
    }

    @Override
    public BlockState updateShape(BlockState oldBlockState, Direction direction, BlockState newBlockState, IWorld world, BlockPos blockPos, BlockPos blockPos1) {
        return !oldBlockState.canSurvive(world, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(oldBlockState, direction, newBlockState, world, blockPos, blockPos1);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockItemUseContext blockItemUseContext) {
        int layerValue = blockState.getValue(LAYERS);
        if (blockItemUseContext.getItemInHand().getItem() == this.asItem() && layerValue < 8) {
            // Need check for DirectionalPlaceContext as otherwise, stack overflow as replacingClickedOnBlock for DirectionalPlaceContext will call this method again
            if (!(blockItemUseContext instanceof DirectionalPlaceContext) && blockItemUseContext.replacingClickedOnBlock()) {
                return blockItemUseContext.getClickedFace() == Direction.UP;
            }
            else {
                return true;
            }
        }
        else {
            return layerValue == 1;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        BlockState blockState = blockItemUseContext.getLevel().getBlockState(blockItemUseContext.getClickedPos());
        if (blockState.is(this)) {
            int layerValue = blockState.getValue(LAYERS);
            return blockState.setValue(LAYERS, Math.min(8, layerValue + 1));
        } else {
            return super.getStateForPlacement(blockItemUseContext);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        BlockState stateBelow = serverWorld.getBlockState(blockPos.below());
        if(stateBelow.is(BzBlocks.PILE_OF_POLLEN.get())) {
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
    public void destroy(IWorld world, BlockPos blockPos, BlockState blockState) {
        if(world.isClientSide()) {
            for(int i = 0; i < 50; i++) {
                spawnParticles(blockState, world, blockPos, world.getRandom(), true);
                spawnParticles(world, Vector3d.atCenterOf(blockPos), world.getRandom(), 0.055D, 0.0075D, 0);
            }
        }
    }

    /**
     * Slows all entities inside the block.
     */
    @Override
    public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity) {

        // make falling block of this block stack the pollen or else destroy it
        if(entity instanceof FallingBlockEntity) {
            if(((FallingBlockEntity) entity).getBlockState().isAir())
                return;

            if(((FallingBlockEntity)entity).getBlockState().is(BzBlocks.PILE_OF_POLLEN.get())) {
                stackPollen(blockState, world, blockPos, ((FallingBlockEntity)entity).getBlockState());
                entity.remove();

                // Prevents the FallingBlock's checkInsideBlocks from triggering this
                // method again for the pollen block we just set above our collision block.
                ((FallingBlockEntityAccessor) entity).bumblezone_setBlockState(Blocks.AIR.defaultBlockState());
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
        else if(entity.getType().equals(BzEntities.POLLEN_PUFF_ENTITY.get())){
            if(((PollenPuffEntity)entity).isConsumed()) return; // do not run this code if a block already was set.

            stackPollen(blockState, world, blockPos, BzBlocks.PILE_OF_POLLEN.get().defaultBlockState());
            entity.remove();
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
            double speedReduction = (entity instanceof ProjectileEntity) ? 0.85f : 1 - layerValueMinusOne * 0.1D;
            double chance = 0.22f + layerValueMinusOne * 0.09f;


            Vector3d deltaMovement = entity.getDeltaMovement();
            double newYDelta = deltaMovement.y;
            if(deltaMovement.y > 0) {
                newYDelta *= (1f - layerValueMinusOne * 0.01f);
            }
            else {
                newYDelta *= (0.84f - layerValueMinusOne * 0.03f);
            }

            entity.setDeltaMovement(new Vector3d(
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
                else if (!(entity instanceof PlayerEntity || entity instanceof ItemEntity)){
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
            if(entity instanceof BeeEntity && !((BeeEntity)entity).hasNectar() && BzEntityTags.POLLEN_PUFF_CAN_POLLINATE.contains(entity.getType())) {
                ((BeeEntity)entity).setFlag(8, true);
                ((BeeEntity)entity).resetTicksWithoutNectarSinceExitingHive();
                if(layerValueMinusOne == 0) {
                    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                }
                else {
                    world.setBlock(blockPos, blockState.setValue(LAYERS, layerValueMinusOne), 3);
                }
            }
        }
    }

    public static void stackPollen(BlockState blockState, World world, BlockPos blockPos, BlockState pollonToStack) {
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
        if(aboveState.is(BzBlocks.PILE_OF_POLLEN.get())) {
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

    public static void pandaSneezing(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if(!livingEntity.level.isClientSide() && livingEntity instanceof PandaEntity) {
            if(livingEntity.level.random.nextFloat() < 0.005f && livingEntity.level.getBlockState(livingEntity.blockPosition()).is(BzBlocks.PILE_OF_POLLEN.get())) {
                ((PandaEntity)livingEntity).sneeze(true);
            }
        }
    }

    // Rarely spawn particle on its own
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        int layerValue = blockState.getValue(LAYERS);
        double chance = 0.015f + layerValue * 0.008f;
        if(random.nextFloat() < chance) spawnParticles(blockState, world, blockPos, random, false);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getDustColor(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        return 16755200;
    }

    public static void spawnParticles(BlockState blockState, IWorld world, BlockPos blockPos, Random random, boolean disturbed) {
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
                        BzParticles.POLLEN.get(),
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

    public static void spawnParticles(IWorld world, Vector3d location, Random random, double speedXZModifier, double speedYModifier, double initYSpeed) {
        double xOffset = (random.nextFloat() * 0.3) - 0.15;
        double yOffset = (random.nextFloat() * 0.3) - 0.15;
        double zOffset = (random.nextFloat() * 0.3) - 0.15;

        world.addParticle(
                BzParticles.POLLEN.get(),
                location.x() + xOffset,
                location.y() + yOffset,
                location.z() + zOffset,
                random.nextGaussian() * speedXZModifier,
                (random.nextGaussian() * speedYModifier) + initYSpeed,
                random.nextGaussian() * speedXZModifier);
    }

    public static void spawnParticlesServer(IWorld world, Vector3d location, Random random, double speedXZModifier, double speedYModifier, double initYSpeed, int numberOfParticles) {
        if(world.isClientSide()) return;

        double xOffset = (random.nextFloat() * 0.3) - 0.15;
        double yOffset = (random.nextFloat() * 0.3) - 0.15;
        double zOffset = (random.nextFloat() * 0.3) - 0.15;

        ((ServerWorld)world).sendParticles(
                BzParticles.POLLEN.get(),
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
