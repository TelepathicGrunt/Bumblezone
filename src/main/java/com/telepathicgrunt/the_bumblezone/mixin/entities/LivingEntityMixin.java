package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.google.common.base.Objects;
import com.telepathicgrunt.the_bumblezone.effects.WrathOfTheHiveEffect;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    //bees become angrier when hit in bumblezone
    @Inject(method = "actuallyHurt(Lnet/minecraft/util/DamageSource;F)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onEntityDamaged(DamageSource source, float amount, CallbackInfo ci) {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        BeeAggression.beeHitAndAngered(((LivingEntity)(Object)this), source.getEntity());
    }

    //clear the wrath effect from all bees if they killed their target
    @Inject(method = "die(Lnet/minecraft/util/DamageSource;)V",
            at = @At(value = "HEAD"))
    private void thebumblezone_onDeath(DamageSource source, CallbackInfo ci) {
        WrathOfTheHiveEffect.calmTheBees(this.level, (LivingEntity)(Object)this);
    }

    //-----------------------------------------------------------//

    //deplete air supply
    @Inject(method = "baseTick()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_breathing(CallbackInfo ci) {
        HoneyFluid.breathing((LivingEntity)(Object)this);
    }

    // make jumping in honey weaker
    @ModifyVariable(method = "aiStep()V",
            at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/entity/LivingEntity.getFluidHeight(Lnet/minecraft/tags/ITag;)D"), ordinal = 1)
    private double thebumblezone_honeyFluidJump1(double fluidHeight) {
        if(fluidHeight == 0) {
            return this.getFluidHeight(BzFluidTags.BZ_HONEY_FLUID);
        }
        return fluidHeight;
    }
}