package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;


public class AncientWaxStairs extends StairBlock implements AncientWaxBase {

    public AncientWaxStairs(BlockState state) {
        super(state, Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F, 19.0F));
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState state, Entity entity) {
        this.applyEntityEffects(state, entity);
        super.stepOn(level, blockPos, state, entity);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (itemstack.getItem() instanceof ShearsItem || itemstack.getItem() instanceof SwordItem) {

            InteractionResult result = swapBlocks(world, blockState, position, BzTags.ANCIENT_WAX_STAIRS);
            if (result == InteractionResult.SUCCESS) {
                this.spawnDestroyParticles(world, playerEntity, position, blockState);

                playerEntity.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                if (playerEntity instanceof ServerPlayer serverPlayer) {
                    BzCriterias.CARVE_WAX_TRIGGER.trigger(serverPlayer, position);

                    if (!serverPlayer.getAbilities().instabuild) {
                        itemstack.hurt(1, playerEntity.getRandom(), serverPlayer);
                    }
                }

                return result;
            }
        }

        return super.use(blockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
