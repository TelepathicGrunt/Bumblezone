package com.telepathicgrunt.the_bumblezone.blocks;

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
import org.checkerframework.checker.units.qual.A;


public class LuminescentWaxChannel extends AxisFacingBlock implements LuminescentWaxBase{
    public LuminescentWaxChannel(MapColor mapColor) {
        super(Properties.of()
                .mapColor(mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((blockState) -> blockState.is(BzTags.LUMINESCENT_WAX_LIGHT_CHANNELS) ? 14 : 0)
                .strength(3.0F, 19.0F));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        if (blockState.getBlock() instanceof LuminescentWaxChannel && (itemstack.getItem() instanceof ShearsItem || itemstack.getItem() instanceof SwordItem)) {

            Direction.Axis newAxisProperty = blockState.getValue(AXIS);
            Direction newDirectProperty = blockState.getValue(FACING);
            if (newDirectProperty.get3DDataValue() == 5) {
                newAxisProperty = Direction.Axis.VALUES[(newAxisProperty.ordinal() + 1) % 3];
                newDirectProperty = Direction.from3DDataValue(0);
            }
            else {
                newDirectProperty = Direction.from3DDataValue(newDirectProperty.get3DDataValue() + 1);
            }

            world.setBlock(position,
                    blockState
                            .setValue(FACING, newDirectProperty)
                            .setValue(AXIS, newAxisProperty),
                    3);

            this.spawnDestroyParticles(world, playerEntity, position, blockState);

            playerEntity.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
            if (playerEntity instanceof ServerPlayer serverPlayer) {
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
        this.applyEntityEffects(BzTags.LUMINESCENT_WAX_LIGHT_CHANNELS, state, entity);
        super.stepOn(level, blockPos, state, entity);
    }
}
