package com.telepathicgrunt.the_bumblezone.effects;

import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.packets.MobEffectClientSyncPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
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
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    /**
     * sync paralysis to client so they can shake on client side
     */
    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        MobEffectInstance effect = entity.getEffect(BzEffects.PARALYZED.get());
        if(!entity.isRemoved() && effect != null && entity.level instanceof ServerLevel) {
            MobEffectClientSyncPacket.sendToClient(entity, effect);
        }
        super.addAttributeModifiers(entity, attributes, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        MobEffectInstance effect = entity.getEffect(BzEffects.PARALYZED.get());
        if(!entity.isRemoved() && effect != null && entity.level instanceof ServerLevel) {
            MobEffectClientSyncPacket.sendToClient(
                entity,
                new MobEffectInstance(
                    BzEffects.PARALYZED.get(),
                    0,
                    effect.getAmplifier() + 1,
                    false,
                    true,
                    true)
            );
        }
        super.removeAttributeModifiers(entity, attributes, amplifier);
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
