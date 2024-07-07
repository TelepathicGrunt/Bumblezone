package com.telepathicgrunt.the_bumblezone.mixin.fabric.client;

import com.telepathicgrunt.the_bumblezone.events.client.BzKeyInputEvent;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress",
            at = @At(value = "HEAD"))
    private void bumblezone$keyPressHandling(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        BzKeyInputEvent.EVENT.invoke(new BzKeyInputEvent(key, scancode, action));
    }
}