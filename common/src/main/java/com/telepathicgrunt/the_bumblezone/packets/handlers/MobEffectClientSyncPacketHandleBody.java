package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.packets.MobEffectClientSyncPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MobEffectClientSyncPacketHandleBody {
    public static void handle(MobEffectClientSyncPacket message) {
        Entity entity = GeneralUtilsClient.getClientLevel().getEntity(message.entityId());
        if (entity instanceof LivingEntity) {
            BuiltInRegistries.MOB_EFFECT.getHolder(message.effectRl()).ifPresent(mobEffect -> {
                if (message.effectDurationTicks() == 0) {
                    ((LivingEntity) entity).removeEffect(mobEffect);
                }
                else {
                    MobEffectInstance mobeffectinstance = new MobEffectInstance(mobEffect, message.effectDurationTicks(), message.effectAmplifier(), message.isEffectAmbient(), message.isEffectVisible(), message.effectShowsIcon());
                    ((LivingEntity) entity).forceAddEffect(mobeffectinstance, null);
                }
            });
        }
    }
}