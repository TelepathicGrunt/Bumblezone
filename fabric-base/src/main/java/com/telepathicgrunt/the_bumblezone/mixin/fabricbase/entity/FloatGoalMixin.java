package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FloatGoal.class)
public abstract class FloatGoalMixin {

    @Final
    @Shadow
    private Mob mob;

    // let Bumblezone fluids have entities float in its honey
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getFluidHeight(Lnet/minecraft/tags/TagKey;)D"))
    private double thebumblezone_fluidFloating(double originalFluidHeight) {
        double newFluidHeight = mob.getFluidHeight(BzTags.SPECIAL_HONEY_LIKE);
        if (newFluidHeight > originalFluidHeight) {
            return newFluidHeight;
        }
        return originalFluidHeight;
    }
}