package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;


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
    public InteractionResult use(BlockState blockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        if (PlatformHooks.isToolAction(itemstack, ShearsItem.class, "shears_carve") ||
            PlatformHooks.isToolAction(itemstack, SwordItem.class, "sword_dig"))
        {

            InteractionResult result = swapBlocks(world, blockState, position, BzTags.ANCIENT_WAX_STAIRS);
            if (result == InteractionResult.SUCCESS) {
                this.spawnDestroyParticles(world, playerEntity, position, blockState);

                playerEntity.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                if (playerEntity instanceof ServerPlayer serverPlayer) {
                    BzCriterias.CARVE_WAX_TRIGGER.get().trigger(serverPlayer, position);

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
