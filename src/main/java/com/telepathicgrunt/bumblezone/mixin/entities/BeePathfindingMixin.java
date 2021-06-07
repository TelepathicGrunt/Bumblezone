package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.BeeAI;
import com.telepathicgrunt.bumblezone.world.dimension.BzDimension;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public class BeePathfindingMixin {

    @Final
    @Shadow(aliases = "field_20380")
    private BeeEntity field_20380;

    @Unique
    private BeeAI.CachedPathHolder thebumblezone_cachedPathHolder;

    /**
     * @author TelepathicGrunt
     * @reason Make bees not get stuck on ceiling anymore and lag people as a result. (Only applies in Bumblezone dimension)
     */
    @Inject(method = "start()V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void thebumblezone_newWander(CallbackInfo ci){
        // Do our own bee AI in the Bumblezone. Makes bees wander more and should be slightly better performance. Maybe...
        if(field_20380.world.getRegistryKey().equals(BzDimension.BZ_WORLD_KEY)){
            thebumblezone_cachedPathHolder = BeeAI.smartBeesTM(field_20380, thebumblezone_cachedPathHolder);
            ci.cancel();
        }
    }
}