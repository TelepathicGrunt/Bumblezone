package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.effects.MobEffectInstanceAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.packets.MobEffectClientSyncPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;

public class ParalyzedEffect extends MobEffect {
    public ParalyzedEffect(MobEffectCategory type, int potionColor) {
        super(type, potionColor);
    }

    /**
     * Returns true if the potion has an instant effect instead of a continuous one (eg Harming)
     */
    @Override
    public boolean isInstantenous() {
        return false;
    }

    /**
     * checks if Potion effect is ready to be applied this tick.
     */
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration >= 1;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getType().is(EntityTypeTags.UNDEAD) || livingEntity.getType().is(BzTags.PARALYZED_IMMUNE)) {
            return false;
        }

        return true;
    }

    /**
     * sync paralyzed to client so they can shake on client side
     */
    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        MobEffectInstance effect = livingEntity.getEffect(BzEffects.PARALYZED.get());
        if (effect == null) {
            return;
        }

        if (effect.getDuration() > BzGeneralConfigs.paralyzedMaxTickDuration) {
            ((MobEffectInstanceAccessor)effect).setDuration(BzGeneralConfigs.paralyzedMaxTickDuration);
        }

        if(!livingEntity.isRemoved() && livingEntity.level() instanceof ServerLevel) {
            MobEffectClientSyncPacket.sendToClient(livingEntity, effect);
        }

        super.onEffectStarted(livingEntity, amplifier);
    }

    // Make sure client removes effect when done.
    public static void effectRemoval(LivingEntity entity, MobEffectInstance mobEffectInstance) {
        if (entity.level().isClientSide || mobEffectInstance.getEffect() != BzEffects.PARALYZED.get()) {
            return;
        }

        MobEffectClientSyncPacket.sendToClient(
            entity,
            new MobEffectInstance(
                BzEffects.PARALYZED.get(),
                0,
                mobEffectInstance.getAmplifier(),
                false,
                true,
                true)
        );
    }

    public static boolean isParalyzed(LivingEntity livingEntity) {
        if(livingEntity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }

        return livingEntity.hasEffect(BzEffects.PARALYZED.get());
    }

    public static boolean isParalyzedClient(LivingEntity livingEntity) {
        if(livingEntity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }

        MobEffectInstance effect = livingEntity.getEffect(BzEffects.PARALYZED.get());
        if(effect != null) {
            if(effect.getDuration() <= 0) {
                livingEntity.removeEffect(BzEffects.PARALYZED.get());
            }

            return true;
        }

        return false;
    }
}
