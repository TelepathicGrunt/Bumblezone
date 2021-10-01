package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.entities.nonliving.PollenPuffEntity;
import com.telepathicgrunt.bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.bumblezone.mixin.entities.PandaEntityInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.modinit.BzParticles;
import com.telepathicgrunt.bumblezone.tags.BzEntityTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class PileOfPollen extends FallingBlock {
    public static final IntProperty LAYERS = Properties.LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{
            VoxelShapes.empty(),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    private Item item;

    public PileOfPollen() {
        super(AbstractBlock.Settings.of(BzBlocks.ORANGE_NOT_SOLID)
                .blockVision((blockState, world, blockPos) -> true)
                .nonOpaque()
                .strength(0.1F)
                .sounds(BlockSoundGroup.SNOW));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(LAYERS);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos blockPos, BlockState blockState) {
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
    public boolean canPathfindThrough(BlockState blockState, BlockView world, BlockPos blockPos, NavigationType pathType) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView world, BlockPos blockPos, ShapeContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.get(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockView world, BlockPos blockPos, ShapeContext selectionContext) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getSidesShape(BlockState blockState, BlockView world, BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.get(LAYERS)];
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState blockState, BlockView world, BlockPos blockPos, ShapeContext selectionContext) {
        return SHAPE_BY_LAYER[blockState.get(LAYERS)];
    }

    @Override
    public boolean hasSidedTransparency(BlockState blockState) {
        return true;
    }

    @Override
    public boolean canPlaceAt(BlockState blockState, WorldView world, BlockPos blockPos) {
        BlockState blockstate = world.getBlockState(blockPos.down());
        if(blockstate.isOf(Blocks.ICE) || blockstate.isOf(Blocks.PACKED_ICE) || blockstate.isOf(Blocks.BARRIER) || !world.getBlockState(blockPos).getFluidState().isEmpty()) {
            return false;
        }
        else if(blockstate.isAir() || blockstate.isOf(BzBlocks.PILE_OF_POLLEN) || blockstate.isOf(Blocks.HONEY_BLOCK) || blockstate.isOf(Blocks.SOUL_SAND)) {
            return true;
        }
        else {
            return Block.isFaceFullSquare(blockstate.getCollisionShape(world, blockPos.down()), Direction.UP);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState oldBlockState, Direction direction, BlockState newBlockState, WorldAccess world, BlockPos blockPos, BlockPos blockPos1) {
        return !oldBlockState.canPlaceAt(world, blockPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(oldBlockState, direction, newBlockState, world, blockPos, blockPos1);
    }

    @Override
    public boolean canReplace(BlockState blockState, ItemPlacementContext itemPlacementContext) {
        int layerValue = blockState.get(LAYERS);
        if (itemPlacementContext.getStack().getItem() == this.asItem() && layerValue < 8) {
            // Need check for AutomaticItemPlacementContext as otherwise, stack overflow as replacingClickedOnBlock for AutomaticItemPlacementContext will call this method again
            if (!(itemPlacementContext instanceof AutomaticItemPlacementContext) && itemPlacementContext.canReplaceExisting()) {
                return itemPlacementContext.getSide() == Direction.UP;
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
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        BlockState blockState = itemPlacementContext.getWorld().getBlockState(itemPlacementContext.getBlockPos());
        if (blockState.isOf(this)) {
            int layerValue = blockState.get(LAYERS);
            return blockState.with(LAYERS, Math.min(8, layerValue + 1));
        } else {
            return super.getPlacementState(itemPlacementContext);
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
     * the power fed into comparator (1 - 8)
     */
    @Override
    public int getComparatorOutput(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(LAYERS);
    }

    @Override
    public void scheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        BlockState stateBelow = serverWorld.getBlockState(blockPos.down());
        if(stateBelow.isOf(BzBlocks.PILE_OF_POLLEN)) {
            if(stateBelow.get(LAYERS) == 8) {
                return;
            }
            else {
                serverWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);
                stackPollen(stateBelow, serverWorld, blockPos.down(), blockState);
            }
        }

        super.scheduledTick(blockState, serverWorld, blockPos, random);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos blockPos, BlockState blockState) {
        if(world.isClient()) {
            for(int i = 0; i < 50; i++) {
                spawnParticles(blockState, world, blockPos, world.getRandom(), true);
                spawnParticles(world, Vec3d.ofCenter(blockPos), world.getRandom(), 0.055D, 0.0075D, 0);
            }
        }
    }

    /**
     * Slows all entities inside the block.
     */
    @Override
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {

        // make falling block of this block stack the pollen or else destroy it
        if(entity instanceof FallingBlockEntity) {
            if(((FallingBlockEntity) entity).getBlockState().isAir())
                return;

            if(((FallingBlockEntity)entity).getBlockState().isOf(BzBlocks.PILE_OF_POLLEN)) {
                stackPollen(blockState, world, blockPos, ((FallingBlockEntity)entity).getBlockState());
                entity.remove(Entity.RemovalReason.DISCARDED);

                // Prevents the FallingBlock's checkInsideBlocks from triggering this
                // method again for the pollen block we just set above our collision block.
                ((FallingBlockEntityAccessor) entity).thebumblezone_setBlock(Blocks.AIR.getDefaultState());
            }
            else {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                if(world.isClient()) {
                    for(int i = 0; i < blockState.get(LAYERS) * 30; i++){
                        spawnParticles(blockState, world, blockPos, world.random, true);
                    }
                }
            }
        }

        // Make pollen puff entity grow pile of pollen
        else if(entity.getType().equals(BzEntities.POLLEN_PUFF_ENTITY)){
            if(((PollenPuffEntity)entity).isConsumed()) return; // do not run this code if a block already was set.

            stackPollen(blockState, world, blockPos, BzBlocks.PILE_OF_POLLEN.getDefaultState());
            entity.remove(Entity.RemovalReason.DISCARDED);
            ((PollenPuffEntity)entity).consumed();

            if(world.isClient()){
                for(int i = 0; i < 50; i++){
                    spawnParticles(world, entity.getPos(), world.random, 0.055D, 0.0075D, 0);
                }
            }
        }

        // slows the entity and spawns particles
        else {
            int layerValueMinusOne = blockState.get(LAYERS) - 1;
            double speedReduction = (entity instanceof ProjectileEntity) ? 0.85f : 1 - layerValueMinusOne * 0.1D;
            double chance = 0.22f + layerValueMinusOne * 0.09f;


            Vec3d deltaMovement = entity.getVelocity();
            double newYDelta = deltaMovement.y;
            if(deltaMovement.y > 0) {
                newYDelta *= (1f - layerValueMinusOne * 0.01f);
            }
            else {
                newYDelta *= (0.84f - layerValueMinusOne * 0.03f);
            }

            entity.setVelocity(new Vec3d(
                    deltaMovement.x * speedReduction,
                    newYDelta,
                    deltaMovement.z * speedReduction));

            double entitySpeed = entity.getVelocity().length();

            // Need to multiply speed to avoid issues where tiny movement is seen as zero.
            if(entitySpeed > 0.00001D && world.random.nextFloat() < chance){
                int particleNumber = (int) (entitySpeed / 0.0045D);
                int particleStrength = (entity instanceof ItemEntity) ? Math.min(10, particleNumber / 3) : Math.min(20, particleNumber);

                if(world.isClient()) {
                    for(int i = 0; i < particleNumber; i++) {
                        if(particleNumber > 5) spawnParticles(blockState, world, blockPos, world.random, true);

                        spawnParticles(
                                world,
                                entity.getPos()
                                        .add(entity.getVelocity().multiply(2D, 2D, 2D))
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
                            entity.getPos()
                                    .add(entity.getVelocity().multiply(2D, 2D, 2D))
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
                ((BeeEntityInvoker)entity).thebumblezone_callSetHasNectar(true);
                ((BeeEntity)entity).resetPollinationTicks();
                if(layerValueMinusOne == 0) {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                }
                else {
                    world.setBlockState(blockPos, blockState.with(LAYERS, layerValueMinusOne), 3);
                }
            }
        }

        if(entity instanceof PandaEntity pandaEntity) {
            pandaSneezing(pandaEntity);
        }
    }

    public static void stackPollen(BlockState blockState, World world, BlockPos blockPos, BlockState pollonToStack) {
        BlockState lastSetState = null;
        int initialLayerValue = blockState.get(LAYERS);
        int layersToAdd = pollonToStack.get(LAYERS);

        // Fill up current pile
        if(initialLayerValue < 8){
            int layerToMax = (8 - initialLayerValue);
            lastSetState = blockState.with(LAYERS, initialLayerValue + Math.min(layerToMax, layersToAdd));
            if(!world.isClient()) world.setBlockState(blockPos, lastSetState, 3);
            layersToAdd -= layerToMax;
        }

        BlockState aboveState = world.getBlockState(blockPos.up());
        if(layersToAdd > 0 && aboveState.isOf(BzBlocks.PILE_OF_POLLEN)) {
            stackPollen(aboveState, world, blockPos.up(), blockState.with(LAYERS, layersToAdd));
        }
        else {
            // Stack on top of this pile
            if(layersToAdd > 0 && aboveState.isAir()) {
                lastSetState = blockState.with(LAYERS, layersToAdd);
                if(!world.isClient()) world.setBlockState(blockPos.up(), blockState.with(LAYERS, layersToAdd), 3);
            }

            // Particles!
            if(world.isClient() && lastSetState != null) {
                for(int i = 0; i < 40; i++) {
                    spawnParticles(lastSetState, world, blockPos, world.random, true);
                }
            }
        }
    }

    public static void pandaSneezing(PandaEntity pandaEntity) {
        if(!pandaEntity.world.isClient()) {
            if(pandaEntity.getRandom().nextFloat() < 0.005f && pandaEntity.world.getBlockState(pandaEntity.getBlockPos()).isOf(BzBlocks.PILE_OF_POLLEN)) {
                ((PandaEntityInvoker)pandaEntity).thebumblezone_callSneeze();
            }
        }
    }

    // Rarely spawn particle on its own
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        int layerValue = blockState.get(LAYERS);
        double chance = 0.015f + layerValue * 0.008f;
        if(random.nextFloat() < chance) spawnParticles(blockState, world, blockPos, random, false);
    }

    @Override
    public int getColor(BlockState blockState, BlockView blockReader, BlockPos blockPos) {
        return 16755200;
    }

    public static void spawnParticles(BlockState blockState, WorldAccess world, BlockPos blockPos, Random random, boolean disturbed) {
        for(Direction direction : Direction.values()) {
            BlockPos blockpos = blockPos.offset(direction);
            if (!world.getBlockState(blockpos).isOpaqueFullCube(world, blockpos)) {
                double speedYModifier = disturbed ? 0.05D : 0.005D;
                double speedXZModifier = disturbed ? 0.03D : 0.005D;
                VoxelShape currentShape = SHAPE_BY_LAYER[blockState.get(LAYERS)];
                double yHeight = currentShape.getMax(Direction.Axis.Y) - currentShape.getMin(Direction.Axis.Y);

                Direction.Axis directionAxis = direction.getAxis();
                double xOffset = directionAxis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getOffsetX() : (double)random.nextFloat();
                double yOffset = directionAxis == Direction.Axis.Y ? yHeight * (double)direction.getOffsetY() : (double)random.nextFloat() * yHeight;
                double zOffset = directionAxis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getOffsetZ() : (double)random.nextFloat();

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

    public static void spawnParticles(WorldAccess world, Vec3d location, Random random, double speedXZModifier, double speedYModifier, double initYSpeed) {
        double xOffset = (random.nextFloat() * 0.3) - 0.15;
        double yOffset = (random.nextFloat() * 0.3) - 0.15;
        double zOffset = (random.nextFloat() * 0.3) - 0.15;

        world.addParticle(
                BzParticles.POLLEN,
                location.getX() + xOffset,
                location.getY() + yOffset,
                location.getZ() + zOffset,
                random.nextGaussian() * speedXZModifier,
                (random.nextGaussian() * speedYModifier) + initYSpeed,
                random.nextGaussian() * speedXZModifier);
    }

    public static void spawnParticlesServer(WorldAccess world, Vec3d location, Random random, double speedXZModifier, double speedYModifier, double initYSpeed, int numberOfParticles) {
        if(world.isClient()) return;

        double xOffset = (random.nextFloat() * 0.3) - 0.15;
        double yOffset = (random.nextFloat() * 0.3) - 0.15;
        double zOffset = (random.nextFloat() * 0.3) - 0.15;

        ((ServerWorld)world).spawnParticles(
                BzParticles.POLLEN,
                location.getX() + xOffset,
                location.getY() + yOffset,
                location.getZ() + zOffset,
                numberOfParticles,
                random.nextGaussian() * speedXZModifier,
                (random.nextGaussian() * speedYModifier) + initYSpeed,
                random.nextGaussian() * speedXZModifier,
                0.02f
        );
    }
}
