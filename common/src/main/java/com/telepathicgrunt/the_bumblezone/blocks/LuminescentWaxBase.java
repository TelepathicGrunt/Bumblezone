package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface LuminescentWaxBase {

    default void applyEntityEffects(TagKey<Block> lightBlockTag, BlockState currentState, Entity collidingEntity) {
        if (collidingEntity instanceof LivingEntity livingEntity &&
            !collidingEntity.level().isClientSide() &&
            collidingEntity.tickCount % 10 == 0)
        {
            if (livingEntity instanceof Player player &&
                (!player.isCreative() || !player.isSpectator() || player.level().isClientSide()))
            {
                return;
            }

            if (livingEntity instanceof ServerPlayer player && EssenceOfTheBees.hasEssence(player)) {
                if (currentState.is(lightBlockTag)) {
                    livingEntity.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED,
                            50,
                            0,
                            false,
                            false,
                            true));

                    livingEntity.addEffect(new MobEffectInstance(
                            MobEffects.DAMAGE_RESISTANCE,
                            50,
                            0,
                            false,
                            false,
                            true));

                    livingEntity.addEffect(new MobEffectInstance(
                            BzEffects.BEENERGIZED.get(),
                            50,
                            0,
                            false,
                            false,
                            true));
                }
            }
            else {
                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        70,
                        0,
                        false,
                        false,
                        true));

                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN,
                        75,
                        2,
                        false,
                        false,
                        true));

                livingEntity.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS,
                        75,
                        2,
                        false,
                        false,
                        true));
            }
        }
    }
}
