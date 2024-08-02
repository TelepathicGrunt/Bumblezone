package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;


public class LuminescentWaxChannel extends RotationAxisBlock implements LuminescentWaxBase {

    public static final MapCodec<LuminescentWaxChannel> CODEC = Block.simpleCodec(LuminescentWaxChannel::new);

    public LuminescentWaxChannel(MapColor mapColor, int light) {
        this(Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((blockState) -> light)
                .strength(3.0F, 19.0F));
    }

    public LuminescentWaxChannel(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends LuminescentWaxChannel> codec() {
        return CODEC;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        if (blockState.getBlock() instanceof LuminescentWaxChannel &&
            (PlatformHooks.isItemAbility(itemStack, ShearsItem.class, "shears_carve") ||
            PlatformHooks.isItemAbility(itemStack, SwordItem.class, "sword_dig")))
        {

            Direction.Axis newAxisProp = blockState.getValue(AXIS);
            int newRotateProperty = blockState.getValue(ROTATION) + 1;

            if (newRotateProperty > 1) {
                newAxisProp = Direction.Axis.values()[(newAxisProp.ordinal() + 1) % 3];
                newRotateProperty = 0;
            }

            level.setBlock(position,
                    blockState
                        .setValue(AXIS, newAxisProp)
                        .setValue(ROTATION, newRotateProperty),
                    3);

            this.spawnDestroyParticles(level, playerEntity, position, blockState);

            playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            if (playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.CARVE_WAX_TRIGGER.get().trigger(serverPlayer, position);

                if (!serverPlayer.getAbilities().instabuild) {
                    itemStack.hurtAndBreak(1, serverPlayer, LivingEntity.getSlotForHand(playerHand));
                }
            }

            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, blockState, level, position, playerEntity, playerHand, raytraceResult);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState state, Entity entity) {
        this.applyEntityEffects(state, entity);
        super.stepOn(level, blockPos, state, entity);
    }
}
