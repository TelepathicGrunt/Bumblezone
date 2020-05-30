package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.items.ObtainSugarWaterBottle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GlassBottleItem.class)
public class GlassBottleUseMixin {
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "use",
            at = @At(value = "HEAD", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void bottleFluidInteract(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (ObtainSugarWaterBottle.useBottleOnSugarWater(world, user, hand))
            cir.setReturnValue(TypedActionResult.success(new ItemStack(BzItems.SUGAR_WATER_BOTTLE)));
    }
}