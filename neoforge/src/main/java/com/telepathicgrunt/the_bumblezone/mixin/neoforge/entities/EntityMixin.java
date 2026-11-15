package com.telepathicgrunt.the_bumblezone.mixin.neoforge.entities;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    protected boolean wasTouchingWater;

    @Shadow
    public abstract void resetFallDistance();

    @ModifyReturnValue(method = "updateFluidInteraction()Z",
            at = @At(value = "RETURN"))
    private boolean bumblezone$fluidDetectionForSwimming(boolean original) {
        boolean inHoney = PlatformService.INSTANCE.getFluidHeight(((Entity)(Object)this), BzTags.SPECIAL_HONEY_LIKE) > 0;
        if (inHoney) {
            this.resetFallDistance();
            this.wasTouchingWater = true;
        }
        return original || inHoney;
    }
}
