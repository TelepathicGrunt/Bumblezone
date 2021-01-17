package net.telepathicgrunt.bumblezone.mixin.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.entities.BeeAI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public class BeePathfindingMixin {

    @Unique
    private BeeEntity beeEntity;

    @Unique
    private BeeAI.CachedPathHolder cachedPathHolder;

    @Inject(method = "<init>()V", at = @At(value = "RETURN"))
    private void init(BeeEntity beeEntity, CallbackInfo ci){
        this.beeEntity = beeEntity;
    }

    /**
     * @author TelepathicGrunt
     * @reason Make bees not get stuck on ceiling anymore and lag people as a result. (Only applies in Bumblezone dimension)
     */
    @Inject(method = "start()V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void newWander(CallbackInfo ci){
        // Do our own bee AI in the Bumblezone. Makes bees wander more and should be slightly better performance. Maybe...
        if(beeEntity.world.getRegistryKey().equals(BzDimension.BZ_WORLD_KEY)){
            cachedPathHolder = BeeAI.smartBeesTM(beeEntity, cachedPathHolder);
            ci.cancel();
        }
    }
}