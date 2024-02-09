package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    public LocalPlayer player;

    @ModifyReturnValue(method = "shouldEntityAppearGlowing(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(value = "RETURN"))
    private boolean bumblezone$glowNearbyEntitiesForPlayer(boolean isGlowing, Entity entity) {
        if (!isGlowing && player != null) {
            if (StinglessBeeHelmet.HELMET_EFFECT_COUNTER_CLIENTSIDE > 0 && StinglessBeeHelmet.shouldEntityGlow(player, entity)) {
                StinglessBeeHelmet.BEE_HIGHLIGHTED_COUNTER_CLIENTSIDE.add(entity);
                return true;
            }
            else if (KnowingEssence.IsKnowingEssenceActive(player) && KnowingEssence.IsValidEntityToGlow(entity, player)) {
                return true;
            }
            else if (RagingEssence.IsRagingEssenceActive(player) && RagingEssence.IsValidEntityToGlow(entity, player)) {
                return true;
            }
        }
        return isGlowing;
    }

    @WrapOperation(method = "startAttack()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy4(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }

    @WrapOperation(method = "pickBlock()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy5(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }

    @WrapOperation(method = "continueAttack(Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"),
            require = 0)
    private boolean bumblezone$allowSpecialAirDestroy6(BlockState blockState, Operation<Boolean> operation) {
        boolean isAir = operation.call(blockState);

        if (isAir && blockState.is(BzTags.AIR_LIKE)) {
            return false;
        }

        return isAir;
    }
}