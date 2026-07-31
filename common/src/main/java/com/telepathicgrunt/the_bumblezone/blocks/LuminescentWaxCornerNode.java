package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class LuminescentWaxCornerNode extends RotationFacingBlock implements LuminescentWaxBase {

    public static final MapCodec<LuminescentWaxCornerNode> CODEC = Block.simpleCodec(LuminescentWaxCornerNode::new);

    public LuminescentWaxCornerNode(Properties properties) {
        super(properties);
    }

    public LuminescentWaxCornerNode(MapColor mapColor, int light, Properties properties) {
        super(properties
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((blockState) -> light)
                .strength(3.0F, 19.0F));
    }

    @Override
    public MapCodec<? extends LuminescentWaxCornerNode> codec() {
        return CODEC;
    }


    @Override
    public InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        BlockState rotatedState = tryRotate(itemStack, blockState, level, position, playerEntity, playerHand);
        if (rotatedState != null) {
            level.setBlock(position, rotatedState, 3);
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, blockState, level, position, playerEntity, playerHand, raytraceResult);
    }

    @Override
    @Nullable
    public BlockState tryRotate(ItemStack itemStack, BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand) {
        if (blockState.getBlock() instanceof LuminescentWaxCornerNode &&
            (PlatformService.INSTANCE.isItemAbility(itemStack, BzTags.WAX_CARVING_ITEMS, "shears_carve") ||
            PlatformService.INSTANCE.isItemAbility(itemStack, BzTags.WAX_CARVING_ITEMS, "sword_dig")))
        {

            Direction newDirectProperty = blockState.getValue(FACING);
            int newRotateProperty = blockState.getValue(ROTATION) + 1;
            if (newRotateProperty > 3) {
                newDirectProperty = Direction.from3DDataValue((newDirectProperty.get3DDataValue() + 1) % 6);
                newRotateProperty = 0;
            }

            this.spawnDestroyParticles(level, playerEntity, position, blockState);

            playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            if (playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.CARVE_WAX_TRIGGER.get().trigger(serverPlayer, position);

                if (!serverPlayer.getAbilities().instabuild) {
                    itemStack.hurtAndBreak(1, serverPlayer, playerHand.asEquipmentSlot());
                }
            }

            return blockState.setValue(FACING, newDirectProperty).setValue(ROTATION, newRotateProperty);
        }

        return null;
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState state, Entity entity) {
        this.applyEntityEffects(state, entity);
        super.stepOn(level, blockPos, state, entity);
    }
}
