package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NameTagItem.class, priority = 1200)
public class NameTagItemMixin {

    /**
     * Set rootmin owner upon naming
     */
    @Inject(method = "interactLivingEntity(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setCustomName(Lnet/minecraft/network/chat/Component;)V", ordinal = 0))
    private void bumblezone$setRootminOwnerOnNaming(ItemStack itemStack,
                                                    Player player,
                                                    LivingEntity livingEntity,
                                                    InteractionHand interactionHand,
                                                    CallbackInfoReturnable<InteractionResult> cir)
    {
        if (livingEntity instanceof RootminEntity rootminEntity && player != null) {
            rootminEntity.setOwnerUUID(player.getUUID());
        }
    }
}