package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


public interface AncientWaxBase {

    default void applyEntityEffects(BlockState currentState, Entity collidingEntity) {
        if (collidingEntity instanceof LivingEntity livingEntity &&
            !collidingEntity.level().isClientSide() &&
            collidingEntity.tickCount % 10 == 2)
        {
            if (livingEntity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                return;
            }

            if (livingEntity.getType().is(BzTags.ANCIENT_WAX_IMMUNE_TO_EFFECTS)) {
                return;
            }

            if (!(livingEntity instanceof ServerPlayer player && EssenceOfTheBees.hasEssence(player))) {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        95,
                        0,
                        true,
                        false,
                        true));

                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        100,
                        2,
                        true,
                        false,
                        true));

                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        100,
                        2,
                        true,
                        false,
                        true));
            }
        }
    }

    @Nullable
    BlockState trySwap(ItemStack itemStack, BlockState currentState, Level level, BlockPos blockPos, Player playerEntity, InteractionHand playerHand);

    @Nullable
    default BlockState trySwap(ItemStack itemStack, BlockState currentState, BlockPos blockPos, Player playerEntity, InteractionHand playerHand, TagKey<Block> blockIterationTag) {
        if (!PlatformHooks.isItemAbility(itemStack, ShearsItem.class, "shears_carve") &&
            !PlatformHooks.isItemAbility(itemStack, SwordItem.class, "sword_dig"))
        {
            return null;
        }

        Optional<HolderSet.Named<Block>> tagEntries = BuiltInRegistries.BLOCK.getTag(blockIterationTag);
        if (tagEntries.isPresent() && tagEntries.get().size() > 1) {

            List<Block> blockList = tagEntries.get().stream().map(Holder::value).toList();
            int indexOfCurrentBlock = blockList.indexOf(currentState.getBlock());
            if (indexOfCurrentBlock != -1) {
                Block newBlock = (indexOfCurrentBlock + 1 == blockList.size()) ?
                        blockList.get(0) : blockList.get(indexOfCurrentBlock + 1);

                BlockState newState = newBlock.defaultBlockState();
                for(Property<?> property : currentState.getProperties()) {
                    if(newState.hasProperty(property)) {
                        newState = GeneralUtils.getStateWithProperty(newState, currentState, property);
                    }
                }

                playerEntity.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                if (playerEntity instanceof ServerPlayer serverPlayer) {
                    BzCriterias.CARVE_WAX_TRIGGER.get().trigger(serverPlayer, blockPos);

                    if (!serverPlayer.getAbilities().instabuild) {
                        itemStack.hurtAndBreak(1, serverPlayer, LivingEntity.getSlotForHand(playerHand));
                    }
                }

                return newState;
            }

        }

        return null;
    }
}
