package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

    @Shadow
    protected boolean isSubmergedInWater;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Handles where wrath of the hive can be on,
    // change player fog color when effect is active
    @Inject(method = "tick",
            at = @At(value = "TAIL"))
    private void thebumblezone_onPlayerTick(CallbackInfo ci) {
        BeeAggression.playerTick((PlayerEntity) (Object) this);
    }

    //allow underwater stuff to run properly when inside honey fluid
    @Inject(method = "updateWaterSubmersionState()Z",
            at = @At(value = "RETURN"), cancellable = true)
    private void thebumblezone_honeyUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue()) {
            this.isSubmergedInWater = this.isSubmergedIn(BzFluidTags.BZ_HONEY_FLUID);
            if(this.isSubmergedInWater) cir.setReturnValue(true);
        }
    }
}