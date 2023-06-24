package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ClientEntityMixin {

    @Inject(method = "getTeamColor()I",
            at = @At(value = "RETURN"),
            cancellable = true)
    private void bumblezone$knowingEssenceGlowColoring(CallbackInfoReturnable<Integer> cir) {
        Player player = GeneralUtilsClient.getClientPlayer();
        if (KnowingEssence.IsKnowingEssenceActive(player)) {
            int teamColor = KnowingEssence.GetTeamColor((Entity)(Object)this, player);
            if (teamColor != -1) {
                cir.setReturnValue(teamColor);
            }
        }
        else if (RagingEssence.IsRagingEssenceActive(player)) {
            int teamColor = RagingEssence.GetTeamColor((Entity)(Object)this, player);
            if (teamColor != -1) {
                cir.setReturnValue(teamColor);
            }
        }
    }
}