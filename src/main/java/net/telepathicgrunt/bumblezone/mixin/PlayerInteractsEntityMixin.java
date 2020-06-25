package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.telepathicgrunt.bumblezone.entities.BeeInteractivity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;


@Mixin(PlayerEntity.class)
public class PlayerInteractsEntityMixin {
    //bees attack player that drinks honey bottles
    @Inject(method = "interact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"))
    private void onHoneyDrink(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        BeeInteractivity.beeFeeding(entity.world, ((PlayerEntity)(Object)this), hand, entity);
    }
}