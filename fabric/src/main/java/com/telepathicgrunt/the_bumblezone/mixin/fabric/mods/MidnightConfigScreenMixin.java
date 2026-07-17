package com.telepathicgrunt.the_bumblezone.mixin.fabric.mods;

import com.telepathicgrunt.the_bumblezone.configs.fabric.BzConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MidnightConfig.MidnightConfigScreen.class)
public class MidnightConfigScreenMixin {

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void bumblezone$onTick(CallbackInfo ci) {
        BzConfig.copyConfigsToCommon();
    }
}
