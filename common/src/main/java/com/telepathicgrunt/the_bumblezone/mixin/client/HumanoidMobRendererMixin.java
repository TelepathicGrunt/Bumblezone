package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.armor.HumanoidRenderStateInterface;
import com.telepathicgrunt.the_bumblezone.items.BumbleBeeChestplate;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin {

    @Inject(method = "extractHumanoidRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FLnet/minecraft/client/renderer/item/ItemModelResolver;)V",
            at = @At(value = "RETURN"),
            require = 0 // Not important. No crashy
    )
    private static void bumblezone$armorStateForRendering(LivingEntity entity, HumanoidRenderState state, float partialTicks, ItemModelResolver itemModelResolver, CallbackInfo ci) {
        ((HumanoidRenderStateInterface) state).theBumblezone$setChestplateVariant(BumbleBeeChestplate.getVariant(state.chestEquipment));
        ((HumanoidRenderStateInterface) state).theBumblezone$setIsChestplateFlying(BumbleBeeChestplate.isFlying(state.chestEquipment));
        ((HumanoidRenderStateInterface) state).theBumblezone$setIsLeggingsPollinated(HoneyBeeLeggings.isPollinated(state.legsEquipment));
    }
}
