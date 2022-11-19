package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress",
            at = @At(value = "TAIL"))
    private void thebumblezone_keyPressHandling(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        BeehemothControls.keyInput(key, scancode, action);
    }
}