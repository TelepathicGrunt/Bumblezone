package com.telepathicgrunt.the_bumblezone.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class MobEffectClientSyncPacket {
    private static final int FLAG_AMBIENT = 1;
    private static final int FLAG_VISIBLE = 2;
    private static final int FLAG_SHOW_ICON = 4;
    private final int entityId;
    private final byte effectId;
    private final byte effectAmplifier;
    private final int effectDurationTicks;
    private final byte flags;

    public MobEffectClientSyncPacket(int mobId, MobEffectInstance mobEffectInstance) {
        this.entityId = mobId;
        this.effectId = (byte)(MobEffect.getId(mobEffectInstance.getEffect()) & 255);
        this.effectAmplifier = (byte)(mobEffectInstance.getAmplifier() & 255);
        this.effectDurationTicks = Math.min(mobEffectInstance.getDuration(), 32767);

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

        this.flags = flags;
    }

    private MobEffectClientSyncPacket(int entityId, byte effectId, byte effectAmplifier, int effectDurationTicks, byte flags) {
        this.entityId = entityId;
        this.effectId = effectId;
        this.effectAmplifier = effectAmplifier;
        this.effectDurationTicks = effectDurationTicks;
        this.flags = flags;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public byte getEffectId() {
        return this.effectId;
    }

    public byte getEffectAmplifier() {
        return this.effectAmplifier;
    }

    public int getEffectDurationTicks() {
        return this.effectDurationTicks;
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

    public boolean isSuperLongDuration() {
        return this.effectDurationTicks == 32767;
    }

    public static void sendToClient(Entity entity, MobEffectInstance mobEffectInstance) {
        MessageHandler.DEFAULT_CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                new MobEffectClientSyncPacket(entity.getId(), mobEffectInstance));
    }

    /*
     * How the client will read the packet.
     */
    public static MobEffectClientSyncPacket parse(final FriendlyByteBuf buf) {
        return new MobEffectClientSyncPacket(
                buf.readVarInt(),
                buf.readByte(),
                buf.readByte(),
                buf.readVarInt(),
                buf.readByte()
        );
    }

    /*
     * creates the packet buffer and sets its values
     */
    public static void compose(final MobEffectClientSyncPacket pkt, final FriendlyByteBuf buf) {
        buf.writeVarInt(pkt.entityId);
        buf.writeByte(pkt.effectId);
        buf.writeByte(pkt.effectAmplifier);
        buf.writeVarInt(pkt.effectDurationTicks);
        buf.writeByte(pkt.flags);
    }

    /*
     * What the client will do with the packet
     */
    public static class Handler {
        //this is what gets run on the client
        public static void handle(final MobEffectClientSyncPacket pkt, final Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if(Minecraft.getInstance().level == null) {
                    return;
                }

                Entity entity = Minecraft.getInstance().level.getEntity(pkt.getEntityId());
                if (entity instanceof LivingEntity) {
                    MobEffect mobeffect = MobEffect.byId(pkt.getEffectId() & 0xFF);
                    if (mobeffect != null) {
                        MobEffectInstance mobeffectinstance = new MobEffectInstance(mobeffect, pkt.getEffectDurationTicks(), pkt.getEffectAmplifier(), pkt.isEffectAmbient(), pkt.isEffectVisible(), pkt.effectShowsIcon());
                        mobeffectinstance.setNoCounter(pkt.isSuperLongDuration());
                        ((LivingEntity)entity).forceAddEffect(mobeffectinstance, null);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
