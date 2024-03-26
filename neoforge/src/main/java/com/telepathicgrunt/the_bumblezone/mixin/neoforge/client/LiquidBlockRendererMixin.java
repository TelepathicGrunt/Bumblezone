package com.telepathicgrunt.the_bumblezone.mixin.neoforge.client;

import com.telepathicgrunt.the_bumblezone.utils.neoforge.BzFluidSpriteCache;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {

    @Inject(method = "setupSprites()V", at = @At(value = "RETURN"))
    public void bumblezone_resetFluidTextureCache(CallbackInfo ci) {
        BzFluidSpriteCache.reload();
    }
}
