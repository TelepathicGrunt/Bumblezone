package com.telepathicgrunt.the_bumblezone.mixin.neoforge.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.client.screens.LevelLoadingScreenInterface;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ClientPacketListener.class, priority = 1200)
public abstract class ClientPacketListenerMixin {

    @ModifyArg(method = "startWaitingForNewLevel(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/gui/screens/LevelLoadingScreen$Reason;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreenAndShow(Lnet/minecraft/client/gui/screens/Screen;)V"),
            require = 0)
    private Screen bumblezone$setLevelLoadScreenNewLevelNeo(Screen screen, @Local(name = "level", argsOnly = true) ClientLevel level)
    {
        ((LevelLoadingScreenInterface)screen).theBumblezone$setNewLevel(level.dimension());
        return screen;
    }
}