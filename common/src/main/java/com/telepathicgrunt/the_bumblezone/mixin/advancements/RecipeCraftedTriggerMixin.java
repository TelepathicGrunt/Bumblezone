package com.telepathicgrunt.the_bumblezone.mixin.advancements;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(RecipeCraftedTrigger.class)
public class RecipeCraftedTriggerMixin {

    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;)V",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void bumblezone$hookToRecipeCrafting(ServerPlayer serverPlayer, ResourceLocation resourceLocation, List<ItemStack> list, CallbackInfo ci) {
        ModuleHelper.getModule(serverPlayer, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
            module.craftedBeehives++;
            BzCriterias.BEEHIVE_CRAFTED_TRIGGER.trigger(serverPlayer, module.craftedBeehives);
        });
    }
}