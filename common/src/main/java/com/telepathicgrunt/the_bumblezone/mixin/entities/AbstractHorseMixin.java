package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketFromServer;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AbstractHorse.class)
public class AbstractHorseMixin {

    @Inject(method = "setOwnerUUID(Ljava/util/UUID;)V",
            at = @At(value = "TAIL"))
    private void bumblezone$syncHorseUUID2(UUID uUID, CallbackInfo ci) {
        AbstractHorse abstractHorse = ((AbstractHorse)(Object)this);
        if (!abstractHorse.level().isClientSide()) {
            SyncHorseOwnerUUIDPacketFromServer.sendToClient(abstractHorse, abstractHorse.getId(), abstractHorse.getOwnerUUID());
        }
    }
}