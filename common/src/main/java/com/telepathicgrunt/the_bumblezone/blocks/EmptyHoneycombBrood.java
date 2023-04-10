package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.mixin.entities.BeeEntityInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;


public class EmptyHoneycombBrood extends ProperFacingBlock {

    public EmptyHoneycombBrood() {
        super(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }


    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        beeHoneyFill(state, level, blockPos, entity);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState state, Entity entity) {
        beeHoneyFill(state, level, blockPos, entity);
    }

    public static void beeHoneyFill(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if(entity instanceof Bee beeEntity &&
            beeEntity.hasNectar() &&
            state.is(BzBlocks.EMPTY_HONEYCOMB_BROOD.get()))
        {
            ((BeeEntityInvoker) entity).callSetHasNectar(false);
            level.setBlock(blockPos,
                    BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                    .setValue(HoneycombBrood.STAGE, 0)
                    .setValue(HoneycombBrood.FACING, state.getValue(HoneycombBrood.FACING)),
                    3);

            Vec3 centerOfBee = beeEntity.getBoundingBox().getCenter();
            PileOfPollen.spawnParticlesServer(
                    level,
                    centerOfBee,
                    beeEntity.getRandom(),
                    0.05D,
                    0.05D,
                    -0.001D,
                    55);
        }
    }

    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult HitResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        for (ModCompat compat : ModChecker.BROOD_EMPTY_COMPATS) {
            InteractionResult compatResult = compat.onEmptyBroodInteract(itemstack, playerEntity, playerHand);
            if (compatResult == InteractionResult.SUCCESS || compatResult == InteractionResult.CONSUME_PARTIAL) {
                playerEntity.swing(playerHand);
                level.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                level.setBlock(position, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                                .setValue(HoneycombBrood.STAGE, compatResult == InteractionResult.SUCCESS ? 3 : 2)
                                .setValue(BlockStateProperties.FACING, blockState.getValue(BlockStateProperties.FACING)),
                        3);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(blockState, level, position, playerEntity, playerHand, HitResult);
    }
}
