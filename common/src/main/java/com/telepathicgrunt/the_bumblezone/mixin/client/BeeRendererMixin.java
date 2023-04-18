package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeeVariantRenderer;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeeRenderer.class)
public class BeeRendererMixin {

    @ModifyReturnValue(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Bee;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "RETURN"))
    private ResourceLocation bumblezone$alternativeBeeSkins(ResourceLocation currentSkin, Bee bee) {
        ResourceLocation newSkin = BeeVariantRenderer.getTextureLocation(bee);
        if (newSkin != null) {
            return newSkin;
        }
        return currentSkin;
    }
}