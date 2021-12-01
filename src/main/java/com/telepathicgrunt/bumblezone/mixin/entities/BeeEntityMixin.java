package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeEntityMixin extends Entity {

    @Shadow
    private int underWaterTicks;

    public BeeEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * @author TelepathicGrunt
     * @reason make bees not drown in honey fluid
     */
    @Inject(method = "customServerAiStep()V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/animal/Bee;isInWaterOrBubble()Z", shift = At.Shift.AFTER))
    private void thebumblezone_honeyFluidNotDrown(CallbackInfo ci) {
        if(this.underWaterTicks >= 10 && this.fluidHeight.get(BzFluidTags.BZ_HONEY_FLUID) > 0) {
            this.underWaterTicks = 9;
        }
    }
}