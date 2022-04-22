package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class MobEffectClientSyncPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "mob_effect_sync_packet");
    private static final int FLAG_AMBIENT = 1;
    private static final int FLAG_VISIBLE = 2;
    private static final int FLAG_SHOW_ICON = 4;

    public static void registerPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID,
            (client, handler, buf, responseSender) -> {
                int entityId = buf.readVarInt();
                byte effectId = buf.readByte();
                byte effectAmplifier = buf.readByte();
                int effectDurationTicks = buf.readVarInt();
                byte flags = buf.readByte();

                client.execute(() -> {
                    if(Minecraft.getInstance().level == null) {
                        return;
                    }

                    Entity entity = Minecraft.getInstance().level.getEntity(entityId);
                    if (entity instanceof LivingEntity) {
                        MobEffect mobeffect = MobEffect.byId(effectId & 0xFF);
                        if (mobeffect != null) {
                            MobEffectInstance mobeffectinstance = new MobEffectInstance(mobeffect, effectDurationTicks, effectAmplifier, (flags & 1) == 1, (flags & 2) == 2, (flags & 4) == 4);
                            mobeffectinstance.setNoCounter(effectDurationTicks == 32767);
                            ((LivingEntity)entity).forceAddEffect(mobeffectinstance, null);
                        }
                    }
                });
            });
    }

    public static void sendMobEffectSyncPacket(Entity entity, MobEffectInstance mobEffectInstance) {
        if(entity.level.isClientSide()) return;

        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        passedData.writeVarInt(entity.getId());
        passedData.writeByte((byte)(MobEffect.getId(mobEffectInstance.getEffect()) & 255));
        passedData.writeByte((byte)(mobEffectInstance.getAmplifier() & 255));
        passedData.writeVarInt(Math.min(mobEffectInstance.getDuration(), 32767));

        byte flags = 0;
        if (mobEffectInstance.isAmbient()) flags = (byte)(flags | FLAG_AMBIENT);
        if (mobEffectInstance.isVisible()) flags = (byte)(flags | FLAG_VISIBLE);
        if (mobEffectInstance.showIcon()) flags = (byte)(flags | FLAG_SHOW_ICON);
        passedData.writeByte(flags);

        PlayerLookup.world((ServerLevel) entity.level).forEach(player ->
                ServerPlayNetworking.send(player, PACKET_ID, passedData));
    }
}
