package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.BeeAI;
import com.telepathicgrunt.bumblezone.world.dimension.BzDimension;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public class BeePathfindingMixin {

    @Unique
    private BeeEntity thebumblezone_beeEntity;

    @Unique
    private BeeAI.CachedPathHolder thebumblezone_cachedPathHolder;

    // This target does exist in bytecode and is neccessary for mixin to work
    // DO NOT REMOVE
    @Inject(method = "<init>(Lnet/minecraft/entity/passive/BeeEntity;)V", at = @At(value = "RETURN"))
    private void thebumblezone_init(BeeEntity beeEntity, CallbackInfo ci){
        this.thebumblezone_beeEntity = beeEntity;
    }

    /**
     * @author TelepathicGrunt
     * @reason Make bees not get stuck on ceiling anymore and lag people as a result. (Only applies in Bumblezone dimension)
     */
    @Inject(method = "start()V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_newWander(CallbackInfo ci){
        // Do our own bee AI in the Bumblezone. Makes bees wander more and should be slightly better performance. Maybe...
        if(thebumblezone_beeEntity.world.getRegistryKey().equals(BzDimension.BZ_WORLD_KEY)){
            thebumblezone_cachedPathHolder = BeeAI.smartBeesTM(thebumblezone_beeEntity, thebumblezone_cachedPathHolder);
            ci.cancel();
        }
    }
}