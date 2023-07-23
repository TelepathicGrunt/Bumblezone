package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketToServer;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleAddEntity(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;getId()I"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            require = 0
    )
    private void bumblezone$syncHorseUUID1(
            ClientboundAddEntityPacket clientboundAddEntityPacket,
            CallbackInfo ci,
            EntityType<?> entitytype,
            Entity entity)
    {
        if (entity instanceof AbstractHorse && entity.level().isClientSide()) {
            SyncHorseOwnerUUIDPacketToServer.sendToServer(entity.getUUID());
        }
    }
}