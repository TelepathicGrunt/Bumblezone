package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    public LocalPlayer player;

    @ModifyReturnValue(method = "shouldEntityAppearGlowing(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(value = "RETURN"))
    private boolean thebumblezone_glowNearbyBeesForPlayer(boolean isGlowing, Entity entity) {
        if (!isGlowing && StinglessBeeHelmet.HELMET_EFFECT_COUNTER_CLIENTSIDE > 0 && player != null && StinglessBeeHelmet.shouldEntityGlow(player, entity)) {
            StinglessBeeHelmet.BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.add(entity);
            return true;
        }
        return isGlowing;
    }
}