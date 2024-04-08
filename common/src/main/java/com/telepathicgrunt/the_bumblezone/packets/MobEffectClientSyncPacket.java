package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketContext;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record MobEffectClientSyncPacket(int entityId, ResourceLocation effectRl, byte effectAmplifier, int effectDurationTicks, byte flags) implements Packet<MobEffectClientSyncPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "mob_effect_client_sync");
    static final Handler HANDLER = new Handler();

    private static final int FLAG_AMBIENT = 1;
    private static final int FLAG_VISIBLE = 2;
    private static final int FLAG_SHOW_ICON = 4;

    public MobEffectClientSyncPacket(int mobId, MobEffectInstance mobEffectInstance) {
        this(
            mobId,
            BuiltInRegistries.MOB_EFFECT.getKey(mobEffectInstance.getEffect()),
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
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<MobEffectClientSyncPacket> getHandler() {
        return HANDLER;
    }

    private static final class Handler implements PacketHandler<MobEffectClientSyncPacket> {

        @Override
        public void encode(MobEffectClientSyncPacket message, FriendlyByteBuf buffer) {
            buffer.writeVarInt(message.entityId);
            buffer.writeResourceLocation(message.effectRl);
            buffer.writeByte(message.effectAmplifier);
            buffer.writeVarInt(message.effectDurationTicks);
            buffer.writeByte(message.flags);
        }

        @Override
        public MobEffectClientSyncPacket decode(FriendlyByteBuf buffer) {
            return new MobEffectClientSyncPacket(
                    buffer.readVarInt(),
                    buffer.readResourceLocation(),
                    buffer.readByte(),
                    buffer.readVarInt(),
                    buffer.readByte()
            );
        }

        @Override
        public PacketContext handle(MobEffectClientSyncPacket message) {
            return (player, level) -> {
                Entity entity = level.getEntity(message.entityId());
                if (entity instanceof LivingEntity) {
                    MobEffect mobeffect = BuiltInRegistries.MOB_EFFECT.get(message.effectRl());
                    if (mobeffect != null) {
                        if (message.effectDurationTicks() == 0) {
                            ((LivingEntity)entity).removeEffect(mobeffect);
                        }
                        else {
                            MobEffectInstance mobeffectinstance = new MobEffectInstance(mobeffect, message.effectDurationTicks(), message.effectAmplifier(), message.isEffectAmbient(), message.isEffectVisible(), message.effectShowsIcon());
                            ((LivingEntity)entity).forceAddEffect(mobeffectinstance, null);
                        }
                    }
                }
            };
        }
    }
}
