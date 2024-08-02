package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface LuminescentWaxBase {

    default void applyEntityEffects(BlockState currentState, Entity collidingEntity) {
        if (collidingEntity instanceof LivingEntity livingEntity &&
            !collidingEntity.level().isClientSide() &&
            collidingEntity.tickCount % 10 == 0)
        {
            if (livingEntity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                return;
            }

            if (livingEntity.getType().is(BzTags.LUMINESCENT_WAX_IMMUNE_TO_EFFECTS)) {
                return;
            }

            if (livingEntity instanceof ServerPlayer player && EssenceOfTheBees.hasEssence(player)) {

                boolean isLuminescent = currentState.is(BzTags.LUMINESCENT_WAX_LIGHT_CHANNELS) ||
                        currentState.is(BzTags.LUMINESCENT_WAX_LIGHT_CORNERS) ||
                        currentState.is(BzTags.LUMINESCENT_WAX_LIGHT_NODES);

                if (isLuminescent) {
                    livingEntity.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED,
                            110,
                            0,
                            true,
                            false,
                            true));

                    livingEntity.addEffect(new MobEffectInstance(
                            MobEffects.DAMAGE_RESISTANCE,
                            110,
                            0,
                            true,
                            false,
                            true));

                    Registry<MobEffect> mobEffects = livingEntity.level().registryAccess().registryOrThrow(Registries.MOB_EFFECT);
                    livingEntity.addEffect(new MobEffectInstance(
                            mobEffects.getHolder(BzEffects.BEENERGIZED.getId()).get(),
                            110,
                            0,
                            true,
                            false,
                            true));
                }
            }
            else {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        95,
                        0,
                        false,
                        false,
                        true));

                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        100,
                        2,
                        false,
                        false,
                        true));

                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        100,
                        2,
                        false,
                        false,
                        true));
            }
        }
    }

    @Nullable
    BlockState tryRotate(ItemStack itemStack, BlockState blockState, Level level, BlockPos position, Player playerEntity, InteractionHand playerHand);
}
