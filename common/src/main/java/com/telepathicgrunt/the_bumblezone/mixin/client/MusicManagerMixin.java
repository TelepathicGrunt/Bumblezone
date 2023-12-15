package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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