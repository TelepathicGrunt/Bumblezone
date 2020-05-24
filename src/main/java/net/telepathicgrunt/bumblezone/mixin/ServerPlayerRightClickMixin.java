package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.items.ObtainSugarWaterBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerRightClickMixin
{
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "interactItem",
            at = @At(value="INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void clientRightClick(PlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(ObtainSugarWaterBottle.useBottleOnSugarWater(world, player, player.getActiveHand()))
            cir.cancel();
    }

}