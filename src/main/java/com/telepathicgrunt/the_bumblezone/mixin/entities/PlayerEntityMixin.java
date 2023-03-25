package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends Entity {

    @Shadow
    protected boolean wasUnderwater;

    public PlayerEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    // Handles where wrath of the hive can be on,
    // change player fog color when effect is active
    @Inject(method = "tick",
            at = @At(value = "TAIL"))
    private void thebumblezone_onPlayerTick(CallbackInfo ci) {
        BeeAggression.playerTick((Player) (Object) this);
    }

    //allow underwater stuff to run properly when inside honey fluid
    @Inject(method = "updateIsUnderwater()Z",
            at = @At(value = "RETURN"), cancellable = true)
    private void thebumblezone_honeyUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue()) {
            this.wasUnderwater = this.isEyeInFluid(BzTags.SPECIAL_HONEY_LIKE);
            if(this.wasUnderwater) cir.setReturnValue(true);
        }
    }
}