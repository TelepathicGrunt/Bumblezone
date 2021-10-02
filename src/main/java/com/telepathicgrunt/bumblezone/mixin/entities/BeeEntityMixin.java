package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends Entity {

    @Shadow
    private int ticksInsideWater;

    public BeeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author TelepathicGrunt
     * @reason make bees not drown in honey fluid
     */
    @Inject(method = "mobTick()V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/passive/BeeEntity;isInsideWaterOrBubbleColumn()Z", shift = At.Shift.AFTER))
    private void thebumblezone_honeyFluidNotDrown(CallbackInfo ci){
        if(this.ticksInsideWater >= 10 && this.fluidHeight.get(BzFluidTags.BZ_HONEY_FLUID) > 0){
            this.ticksInsideWater = 9;
        }
    }
}