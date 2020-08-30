package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.items.ObtainSugarWaterBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GlassBottleItem.class)
public class GlassBottleUseMixin {
    //using glass bottle to get honey could anger bees
    @Inject(method = "Lnet/minecraft/item/GlassBottleItem;onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void bottleFluidInteract(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir, List<AreaEffectCloudEntity> list, ItemStack itemStack, RayTraceResult hitResult, BlockPos blockPos) {
        if (ObtainSugarWaterBottle.useBottleOnSugarWater(world, user, hand, blockPos))
            cir.setReturnValue(ActionResult.success(user.getHeldItem(hand)));
    }
}