package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.world.structures.SempiternalSanctumBehavior;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "doTick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/PlayerTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void bumblezone$checkIfInSpecialStructures(CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer)(Object)this;
        if (!EssenceOfTheBees.hasEssence(serverPlayer)) {
            BeeAggression.applyAngerIfInTaggedStructures(serverPlayer);
            BeeQueenEntity.applyMiningFatigueInStructures(serverPlayer);
            SempiternalSanctumBehavior.applyFatigueIfInTaggedStructures(serverPlayer);
        }
    }
}