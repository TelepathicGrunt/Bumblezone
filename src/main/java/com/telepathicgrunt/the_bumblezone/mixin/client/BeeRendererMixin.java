package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.BeeVariantRenderer;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeRenderer.class)
public class BeeRendererMixin {

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Bee;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "HEAD"), cancellable = true, require = 0)
    private void thebumblezone_alternativeBeeSkins(Bee bee, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation newSkin = BeeVariantRenderer.getTextureLocation(bee);
        if (newSkin != null) {
            cir.setReturnValue(newSkin);
        }
    }
}