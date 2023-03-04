package com.telepathicgrunt.the_bumblezone.mixin.containers;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {

    @Shadow
    @Final
    private ResultContainer resultSlots;

    @Shadow
    @Final
    private Player player;

    // makes RecipeDiscoveredTrigger work.
    @Inject(method = "slotsChanged(Lnet/minecraft/world/Container;)V",
            at = @At(value = "TAIL"),
            require = 0)
    private void thebumblezone_recipeDiscoveredTrigger(Container container, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && this.resultSlots.getRecipeUsed() != null) {
            BzCriterias.RECIPE_DISCOVERED_TRIGGER.trigger(serverPlayer, resultSlots.getRecipeUsed().getId());
        }
    }
}