package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.items.ObtainSugarWaterBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerRightClickMixin {
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "breakBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void clientRightClick(BlockPos pos, CallbackInfoReturnable<Boolean> cir, World world) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (ObtainSugarWaterBottle.useBottleOnSugarWater(world, player, player.getActiveHand()))
            cir.cancel();
    }

}