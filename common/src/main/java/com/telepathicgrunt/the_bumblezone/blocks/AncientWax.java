package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class AncientWax extends Block implements AncientWaxBase {

    public static final MapCodec<AncientWax> CODEC = Block.simpleCodec(AncientWax::new);

    public AncientWax() {
        this(Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F, 19.0F));
    }

    public AncientWax(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends AncientWax> codec() {
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
        BlockState swappedState = trySwap(itemStack, currentState, blockPos, playerEntity, playerHand, BzTags.ANCIENT_WAX_FULL_BLOCKS);
        if (swappedState != null) {
            this.spawnDestroyParticles(level, playerEntity, blockPos, currentState);
            return swappedState;
        }

        return null;
    }
}
