package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class AncientWaxSlab extends SlabBlock implements AncientWaxBase {

    public static final MapCodec<AncientWaxSlab> CODEC = Block.simpleCodec(AncientWaxSlab::new);

    public AncientWaxSlab() {
        this(Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F, 19.0F));
    }

    public AncientWaxSlab(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends AncientWaxSlab> codec() {
        return CODEC;
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState state, Entity entity) {
        this.applyEntityEffects(state, entity);
        super.stepOn(level, blockPos, state, entity);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        BlockState swappedState = trySwap(itemStack, blockState, level, position, playerEntity, playerHand);
        if (swappedState != null) {
            level.setBlock(position, swappedState, 3);
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.useItemOn(itemStack, blockState, level, position, playerEntity, playerHand, raytraceResult);
    }

    @Override
    @Nullable
    public BlockState trySwap(ItemStack itemStack, BlockState currentState, Level level, BlockPos blockPos, Player playerEntity, InteractionHand playerHand) {
        BlockState swappedState = trySwap(itemStack, currentState, blockPos, playerEntity, playerHand, BzTags.ANCIENT_WAX_SLABS);
        if (swappedState != null) {
            this.spawnDestroyParticles(level, playerEntity, blockPos, currentState);
            return swappedState;
        }

        return null;
    }
}
