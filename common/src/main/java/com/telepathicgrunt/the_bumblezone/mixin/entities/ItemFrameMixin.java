package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.SwapItemFrameContents;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin {

    @Inject(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "RETURN"), require = 0)
    private void thebumblezone_swapItemFrameBucket(ItemStack itemStack, boolean updateNeighbor, CallbackInfo ci) {
        SwapItemFrameContents.onItemFrameSpawn((ItemFrame)(Object)this);
    }
}