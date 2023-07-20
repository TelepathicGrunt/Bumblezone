package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;


public class LuminescentWaxChannel extends RotationAxisBlock implements LuminescentWaxBase{
    public LuminescentWaxChannel(MapColor mapColor, int light) {
        super(Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((blockState) -> light)
                .strength(3.0F, 19.0F));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if (blockState.getBlock() instanceof LuminescentWaxChannel && (itemstack.getItem() instanceof ShearsItem || itemstack.getItem() instanceof SwordItem)) {

            Direction.Axis newAxisProp = blockState.getValue(AXIS);
            int newRotateProperty = blockState.getValue(ROTATION) + 2;

            boolean isLuminescent = blockState.is(BzTags.LUMINESCENT_WAX_LIGHT_CHANNELS);

            if (newRotateProperty > (isLuminescent ? 3 : 2)) {
                newAxisProp = Direction.Axis.values()[(newAxisProp.ordinal() + 1) % 3];
                newRotateProperty = 0;
            }

            world.setBlock(position,
                    blockState
                        .setValue(AXIS, newAxisProp)
                        .setValue(ROTATION, newRotateProperty),
                    3);

            this.spawnDestroyParticles(world, playerEntity, position, blockState);

            playerEntity.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
            if (playerEntity instanceof ServerPlayer serverPlayer) {
                BzCriterias.CARVE_WAX_TRIGGER.trigger(serverPlayer, position);

                if (!serverPlayer.getAbilities().instabuild) {
                    itemstack.hurt(1, playerEntity.getRandom(), serverPlayer);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, world, position, playerEntity, playerHand, raytraceResult);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState state, Entity entity) {
        this.applyEntityEffects(entity);
        super.stepOn(level, blockPos, state, entity);
    }
}
