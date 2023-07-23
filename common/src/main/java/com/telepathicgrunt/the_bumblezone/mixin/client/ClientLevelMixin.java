package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "getMarkerParticleTarget()Lnet/minecraft/world/level/block/Block;",
            at = @At(value = "RETURN"),
            cancellable = true)
    private void bumblezone$showWindyAirInSurvival(CallbackInfoReturnable<Block> cir) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Item item = player.getMainHandItem().getItem();
            if (item instanceof BlockItem blockItem && item == BzItems.WINDY_AIR.get()) {
                cir.setReturnValue(blockItem.getBlock());
            }
        }
    }
}