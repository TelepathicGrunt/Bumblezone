package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.InfinityBarrierBlockEntity;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzDamageSources;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;


public class InfinityBarrier extends BaseEntityBlock implements BlockExtension {
    public InfinityBarrier() {
        super(Properties.of()
                .mapColor(MapColor.NONE)
                .strength(0.1F, 3600000.8F)
                .lightLevel((blockState) -> 15)
                .noLootTable()
                .noOcclusion()
                .noParticlesOnBreak()
                .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                .pushReaction(PushReaction.BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BzBlockEntities.INFINITY_BARRIER.get().create(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState arg) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        if (level instanceof ServerLevel) {
            if (!player.isCreative()) {
                player.hurt(level.damageSources().source(BzDamageSources.ARCHITECTS_TYPE), Math.max(player.getHealth(), player.getMaxHealth()) / 2);
            }
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, BzBlocks.INFINITY_BARRIER.get().defaultBlockState(), 3);
        BlockEntity blockEntity2 = level.getBlockEntity(blockPos);
        if (blockEntity2 instanceof InfinityBarrierBlockEntity infinityBarrierBlockEntity) {
            infinityBarrierBlockEntity.setPrimaryColor(0x3D3D3D);
            infinityBarrierBlockEntity.setSecondaryColor(0x161616);
        }
    }

    @Override
    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        return 0;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (level instanceof ServerLevel serverLevel && player != null) {
            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    player.getX(),
                    player.getEyeY(),
                    player.getZ(),
                    25,
                    player.getRandom().nextGaussian() * 0.2D,
                    (player.getRandom().nextGaussian() * 0.25D) + 0.1,
                    player.getRandom().nextGaussian() * 0.2D,
                    0.2f);
        }
    }

    @Override
    public OptionalBoolean bz$shouldNotDisplayFluidOverlay() {
        return OptionalBoolean.TRUE;
    }

    public static InteractionResult onBlockInteractEvent(PlayerItemUseOnBlockEvent event) {
        Player player = event.user();
        InteractionHand interactionHand = event.hand();
        BlockState blockState = event.level().getBlockState(event.hitResult().getBlockPos());
        if (player != null && blockState.is(BzBlocks.INFINITY_BARRIER.get())) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            Item item = itemStack.getItem();
            if (item instanceof BlockItem) {
                return InteractionResult.FAIL;
            }
            else if (item instanceof BucketItem) {
                return InteractionResult.FAIL;
            }
            else if (item instanceof HangingEntityItem) {
                return InteractionResult.FAIL;
            }
        }
        return null;
    }
}
