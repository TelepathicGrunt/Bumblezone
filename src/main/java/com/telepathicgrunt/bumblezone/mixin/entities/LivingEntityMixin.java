package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.entities.EntityTeleportationHookup;
import com.telepathicgrunt.bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //bees become angrier when hit in bumblezone
    @Inject(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onEntityDamaged(DamageSource source, float amount, CallbackInfo ci) {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        BeeAggression.beeHitAndAngered(((LivingEntity)(Object)this), source.getAttacker());
    }

    //clear the wrath effect from all bees if they killed their target
    @Inject(method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDeath(DamageSource source, CallbackInfo ci) {
        WrathOfTheHiveEffect.calmTheBees(((LivingEntity)(Object)this).world, (LivingEntity)(Object)this);
    }

    // Handles teleportation
    @Inject(method = "tick",
            at = @At(value = "TAIL"))
    private void thebumblezone_onLivingEntityTick(CallbackInfo ci) {
        EntityTeleportationHookup.entityTick((LivingEntity) (Object) this);
    }


    //-----------------------------------------------------------//

    //deplete air supply
    @Inject(method = "baseTick()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_breathing(CallbackInfo ci) {
        HoneyFluid.breathing((LivingEntity)(Object)this);
    }

    // make jumping in honey weaker
    @ModifyVariable(method = "tickMovement()V", ordinal = 0,
            at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/entity/LivingEntity.getFluidHeight(Lnet/minecraft/tag/Tag;)D", ordinal = 1),
            slice = @Slice(
                    from = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/entity/LivingEntity.shouldSwimInFluids()Z"),
                    to = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.isTouchingWater()Z")
            ))
    private double thebumblezone_honeyFluidJump(double fluidHeight) {
        if(fluidHeight == 0) {
            return this.getFluidHeight(BzFluidTags.BZ_HONEY_FLUID);
        }
        return fluidHeight;
    }
}