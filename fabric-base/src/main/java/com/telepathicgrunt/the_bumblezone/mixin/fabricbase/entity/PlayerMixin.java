package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.google.common.util.concurrent.AtomicDouble;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerEntityInteractEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.items.BzShieldItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {

    @Shadow protected boolean wasUnderwater;

    @Shadow @Final private Inventory inventory;

    @Shadow public abstract ItemCooldowns getCooldowns();

    public PlayerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(
            method = "getDestroySpeed",
            at = @At("RETURN")
    )
    private float bumblezone$onGetDigSpeed(float speed, BlockState state) {
        AtomicDouble eventSpeed = new AtomicDouble(speed);
        PlayerBreakSpeedEvent.EVENT.invoke(new PlayerBreakSpeedEvent((Player) ((Object)this), state, eventSpeed));
        return eventSpeed.floatValue();
    }

    @Inject(
            method = "interactOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void bumblezone$onInteractOn(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = PlayerEntityInteractEvent.EVENT.invoke(new PlayerEntityInteractEvent((Player) ((Object)this), entity, interactionHand));
        if (result != null) {
            cir.setReturnValue(result);
        }
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void bumblezone$onTickPre(CallbackInfo ci) {
        PlayerTickEvent.EVENT.invoke(new PlayerTickEvent((Player) ((Object)this), false));
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void bumblezone$onTickPost(CallbackInfo ci) {
        PlayerTickEvent.EVENT.invoke(new PlayerTickEvent((Player) ((Object)this), true));
    }

    @Inject(method = "updateIsUnderwater()Z",
            at = @At(value = "RETURN"), cancellable = true)
    private void thebumblezone$setUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue()) {
            this.wasUnderwater = this.isEyeInFluid(BzTags.SPECIAL_HONEY_LIKE);
            if(this.wasUnderwater) {
                cir.setReturnValue(true);
            }
        }
    }

    @WrapOperation(method = "hurtCurrentlyUsedShield",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private boolean thebumblezone$damageHoneyCrystalShield(ItemStack callingItem, Item vanillaShield, Operation<Boolean> originalCall) {
        if(callingItem.is(BzItems.HONEY_CRYSTAL_SHIELD.get())) {
            return true;
        }
        return originalCall.call(callingItem, vanillaShield);
    }

    @Inject(method = "disableShield",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"))
    private void thebumblezone$applyCooldownForShield(boolean sprinting, CallbackInfo ci) {
        inventory.items.forEach(item -> {
            if (item.getItem() instanceof BzShieldItem) {
                getCooldowns().addCooldown(item.getItem(), 100);
            }
        });
    }
}
