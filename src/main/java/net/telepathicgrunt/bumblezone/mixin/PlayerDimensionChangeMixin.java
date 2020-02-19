package net.telepathicgrunt.bumblezone.mixin;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.dimension.BzPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(Entity.class)
public class PlayerDimensionChangeMixin
{
    // Handles storing of past non-bumblezone dimension the player is leaving
    @Inject(at = @At(value="INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", ordinal = 0),
            method = "changeDimension")
    private void onDimensionChange(DimensionType newDimension, CallbackInfoReturnable<Entity> ci) {
        if(((Entity)(Object)this) instanceof PlayerEntity)
        {
            //Updates the non-BZ dimension that the player is leaving if going to BZ
            PlayerEntity playerEntity = (PlayerEntity)(Object)this;

            if(playerEntity.dimension != BzDimensionType.BUMBLEZONE_TYPE)
            {
                Bumblezone.PLAYER_COMPONENT.get(playerEntity).setNonBZDimension(playerEntity.dimension);
            }
        }
    }
}