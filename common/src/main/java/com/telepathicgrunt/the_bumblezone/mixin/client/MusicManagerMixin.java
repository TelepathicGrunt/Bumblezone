package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {

    @Inject(method = "tick()V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone$stopTickingMusicManager(CallbackInfo ci) {
        if (MusicHandler.BUMBLEZONE_MUSIC_PLAYING) {
            ci.cancel();
        }
    }
}