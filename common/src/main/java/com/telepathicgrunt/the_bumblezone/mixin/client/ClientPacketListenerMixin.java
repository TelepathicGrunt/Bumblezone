package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.screens.LevelLoadingScreenInterface;
import com.telepathicgrunt.the_bumblezone.packets.SyncHorseOwnerUUIDPacketToServer;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ClientPacketListener.class, priority = 1200)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleAddEntity(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;postAddEntitySoundInstance(Lnet/minecraft/world/entity/Entity;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            require = 0
    )
    private void bumblezone$syncHorseUUID1(
            ClientboundAddEntityPacket clientboundAddEntityPacket,
            CallbackInfo ci,
            Entity entity)
    {
        if (entity instanceof AbstractHorse && entity.level().isClientSide()) {
            SyncHorseOwnerUUIDPacketToServer.sendToServer(entity.getUUID());
        }
    }

    @ModifyArg(method = "startWaitingForNewLevel(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/gui/screens/LevelLoadingScreen$Reason;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"),
            require = 0)
    private Screen bumblezone$setLevelLoadScreenNewLevel(Screen screen, @Local(name = "level") ClientLevel level)
    {
        ((LevelLoadingScreenInterface)screen).theBumblezone$setNewLevel(level.dimension());
        return screen;
    }
}