package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {

    @Shadow
    protected float oRainLevel;

    @Shadow
    protected float rainLevel;

    @Shadow
    protected float oThunderLevel;

    @Shadow
    protected float thunderLevel;

    // Fix Bumblezone dimension fog getting screwed by rain/thunder effects in Overworld.
    @Inject(method = "setRainLevel(F)V",
            at = @At(value = "TAIL"),
            require = 0)
    private void thebumblezone_noRainInDimension(float strength, CallbackInfo ci) {
        if (((Level)(Object)this).dimension().equals(BzDimension.BZ_WORLD_KEY)) {
            oRainLevel = 0;
            rainLevel = 0;
        }
    }

    // Fix Bumblezone dimension fog getting screwed by rain/thunder effects in Overworld.
    @Inject(method = "setThunderLevel(F)V",
            at = @At(value = "TAIL"),
            require = 0)
    private void thebumblezone_noThunderInDimension(float strength, CallbackInfo ci) {
        if (((Level)(Object)this).dimension().equals(BzDimension.BZ_WORLD_KEY)) {
            oThunderLevel = 0;
            thunderLevel = 0;
        }
    }
}