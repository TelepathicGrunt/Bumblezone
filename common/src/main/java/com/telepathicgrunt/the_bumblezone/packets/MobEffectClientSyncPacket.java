package com.telepathicgrunt.the_bumblezone.packets;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record MobEffectClientSyncPacket(int entityId, ResourceLocation effectRl, byte effectAmplifier, int effectDurationTicks, byte flags) implements Packet<MobEffectClientSyncPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "mob_effect_client_sync");
    public static final ClientboundPacketType<MobEffectClientSyncPacket> TYPE = new MobEffectClientSyncPacket.Handler();

    private static final int FLAG_AMBIENT = 1;
    private static final int FLAG_VISIBLE = 2;
    private static final int FLAG_SHOW_ICON = 4;

    public MobEffectClientSyncPacket(int mobId, MobEffectInstance mobEffectInstance) {
        this(
            mobId,
            BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect().value()),
            (byte) (mobEffectInstance.getAmplifier() & 255),
            Math.min(mobEffectInstance.getDuration(), 32767),
            getFlags(mobEffectInstance)
        );
    }

    private static byte getFlags(MobEffectInstance mobEffectInstance) {
        byte flags = 0;
        if (mobEffectInstance.isAmbient()) {
            flags = (byte)(flags | FLAG_AMBIENT);
        }

        if (mobEffectInstance.isVisible()) {
            flags = (byte)(flags | FLAG_VISIBLE);
        }

        if (mobEffectInstance.showIcon()) {
            flags = (byte)(flags | FLAG_SHOW_ICON);
        }

        return flags;
    }

    public boolean isEffectVisible() {
        return (this.flags & 2) == 2;
    }

    public boolean isEffectAmbient() {
        return (this.flags & 1) == 1;
    }

    public boolean effectShowsIcon() {
        return (this.flags & 4) == 4;
    }

    public static void sendToClient(Entity entity, MobEffectInstance mobEffectInstance) {
        MessageHandler.DEFAULT_CHANNEL.sendToPlayersInLevel(new MobEffectClientSyncPacket(entity.getId(), mobEffectInstance), entity.level());
    }

    @Override
    public PacketType<MobEffectClientSyncPacket> type() {
        return TYPE;
    }

    private static final class Handler implements ClientboundPacketType<MobEffectClientSyncPacket> {

        @Override
        public void encode(MobEffectClientSyncPacket message, RegistryFriendlyByteBuf buffer) {
            buffer.writeVarInt(message.entityId);
            buffer.writeResourceLocation(message.effectRl);
            buffer.writeByte(message.effectAmplifier);
            buffer.writeVarInt(message.effectDurationTicks);
            buffer.writeByte(message.flags);
        }

        @Override
        public MobEffectClientSyncPacket decode(RegistryFriendlyByteBuf buffer) {
            return new MobEffectClientSyncPacket(
                    buffer.readVarInt(),
                    buffer.readResourceLocation(),
                    buffer.readByte(),
                    buffer.readVarInt(),
                    buffer.readByte()
            );
        }

        @Override
        public Runnable handle(MobEffectClientSyncPacket message) {
            return () -> {
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
            };
        }

        @Override
        public Class<MobEffectClientSyncPacket> type() {
            return MobEffectClientSyncPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
