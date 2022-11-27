package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Mixin(Gui.class)
public class GuiMixin {

    @Unique
    private static MobEffectInstance thebumblezone_mobEffectInstance = null;

    @Inject(method = "renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getEffect()Lnet/minecraft/world/effect/MobEffect;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            require = 0)
    private void thebumblezone_noFlashingHiddenEffectIcon1(PoseStack matrices,
                                                         CallbackInfo ci,
                                                         Collection<MobEffectInstance> collection,
                                                         int i,
                                                         int j,
                                                         MobEffectTextureManager mobEffectTextureManager,
                                                         List<Runnable> list,
                                                         Iterator<MobEffectInstance> var7,
                                                         MobEffectInstance mobEffectInstance)
    {
        thebumblezone_mobEffectInstance = mobEffectInstance;
    }

    @ModifyVariable(method = "renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isAmbient()Z", ordinal = 0),
            ordinal = 0, require = 0)
    private float thebumblezone_noFlashingHiddenEffectIcon2(float alpha) {
        if (thebumblezone_mobEffectInstance != null && thebumblezone_mobEffectInstance.getEffect().equals(BzEffects.HIDDEN)) {
            return thebumblezone_mobEffectInstance.getAmplifier() == 0 ? 0.5f : 1f;
        }
        return alpha;
    }
}