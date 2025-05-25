package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerGrantAdvancementEvent;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {


    @Shadow private ServerPlayer player;

    @Inject(
            method = "award",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void bumblezone$onAward(Advancement advancement, String string, CallbackInfoReturnable<Boolean> cir) {
        PlayerGrantAdvancementEvent.EVENT.invoke(new PlayerGrantAdvancementEvent(advancement, this.player));
    }
}
