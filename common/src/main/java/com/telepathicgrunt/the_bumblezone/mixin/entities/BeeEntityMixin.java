package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

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
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/animal/Bee;isInWaterOrBubble()Z", shift = At.Shift.AFTER),
            require = 0)
    private void bumblezone$honeyFluidNotDrown(CallbackInfo ci) {
        if(this.underWaterTicks >= 10 && this.fluidHeight.getOrDefault(BzTags.SPECIAL_HONEY_LIKE, 0) > 0)
        {
            this.underWaterTicks = 9;
        }
    }

    //spawns bees with chance to bee full of pollen
    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V",
            at = @At(value = "TAIL"))
    private void bumblezone$pollinateSpawnedBee(EntityType<? extends Bee> entityType, Level world, CallbackInfo ci) {
        if (!world.isClientSide() && world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            Bee beeEntity = (Bee)(Object)this;

            //20% chance of being full of pollen
            if ((new Random()).nextFloat() < 0.2f) {
                ((BeeEntityInvoker) beeEntity).callSetHasNectar(true);
            }
        }
    }
}