package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;


public class InfinityBarrier extends BaseEntityBlock {
    public InfinityBarrier() {
        super(Properties.of()
                .mapColor(MapColor.NONE)
                .strength(-1.0F, 3600000.8F)
                .lightLevel((blockState) -> 15)
                .noLootTable()
                .noOcclusion()
                .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                .pushReaction(PushReaction.BLOCK));
    }

    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.hurt(level.damageSources().source(BzDamageSources.ARCHITECTS_TYPE), Float.MAX_VALUE);
        level.setBlock(blockPos, BzBlocks.INFINITY_BARRIER.get().defaultBlockState(), 3);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BzBlockEntities.INFINITY_BARRIER.get().create(blockPos, blockState);
    }
}
