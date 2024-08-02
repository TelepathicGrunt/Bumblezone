package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;


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
        if (PlatformHooks.isItemAbility(itemStack, ShearsItem.class, "shears_carve") ||
            PlatformHooks.isItemAbility(itemStack, SwordItem.class, "sword_dig"))
        {

            ItemInteractionResult result = swapBlocks(level, blockState, position, BzTags.ANCIENT_WAX_SLABS);
            if (result.consumesAction()) {
                this.spawnDestroyParticles(level, playerEntity, position, blockState);

                playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                if (playerEntity instanceof ServerPlayer serverPlayer) {
                    BzCriterias.CARVE_WAX_TRIGGER.get().trigger(serverPlayer, position);

                    if (!serverPlayer.getAbilities().instabuild) {
                        itemStack.hurtAndBreak(1, serverPlayer, playerHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    }
                }

                return result;
            }
        }

        return super.useItemOn(itemStack, blockState, level, position, playerEntity, playerHand, raytraceResult);
    }
}
