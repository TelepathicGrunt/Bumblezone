package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class AncientWaxStairs extends StairBlock implements AncientWaxBase {

    protected final BlockState baseState;

    public static final MapCodec<AncientWaxStairs> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockState.CODEC.fieldOf("base_state").forGetter(stairBlock -> stairBlock.baseState),
            StairBlock.propertiesCodec()
    ).apply(instance, AncientWaxStairs::new));

    public AncientWaxStairs(BlockState state) {
        this(state, Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F, 19.0F));
    }

    public AncientWaxStairs(BlockState state, Properties properties) {
        super(state, properties);
        this.baseState = state;
    }

    @Override
    public MapCodec<? extends AncientWaxStairs> codec() {
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
        BlockState swappedState = trySwap(itemStack, currentState, blockPos, playerEntity, playerHand, BzTags.ANCIENT_WAX_STAIRS);
        if (swappedState != null) {
            this.spawnDestroyParticles(level, playerEntity, blockPos, currentState);
            return swappedState;
        }

        return null;
    }
}
