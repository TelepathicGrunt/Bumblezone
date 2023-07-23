package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.TemporaryPlayerData;
import com.telepathicgrunt.the_bumblezone.items.essence.CalmingEssence;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerTimeInAirMixin extends LivingEntity implements TemporaryPlayerData {

    @Shadow public abstract boolean isCreative();

    @Shadow public abstract boolean isSpectator();

    @Unique
    private int bumblezone$ticksOffGround = 0;

    protected PlayerTimeInAirMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    private void bumblezone$recordTicksOffGround(CallbackInfo ci) {
        if (this.onGround()) {
            this.bumblezone$ticksOffGround = 0;
        }
        else if (!this.isCreative() && !this.isSpectator()) {
            this.bumblezone$ticksOffGround++;
        }
    }

    @Override
    public int playTickOffGround() { return bumblezone$ticksOffGround; }
}