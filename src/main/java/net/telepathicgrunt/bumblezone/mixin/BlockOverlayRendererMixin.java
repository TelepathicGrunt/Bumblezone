package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.fluids.SugarWaterClientOverlay;
import net.telepathicgrunt.bumblezone.items.ObtainSugarWaterBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameOverlayRenderer.class)
public class BlockOverlayRendererMixin
{
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "renderOverlays",
            at = @At(value="INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameOverlayRenderer;renderInWallOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/texture/Sprite;Lnet/minecraft/client/util/math/MatrixStack;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void clientRightClick(MinecraftClient minecraftClient, MatrixStack matrixStack, CallbackInfo ci, PlayerEntity playerEntity) {
        if(SugarWaterClientOverlay.sugarWaterOverlay(playerEntity, new BlockPos(playerEntity.getPos()), matrixStack))
            ci.cancel();
    }

}