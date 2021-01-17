package net.telepathicgrunt.bumblezone.mixin.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.entities.BeeAI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public class BeePathfindingMixin {

    @Unique
    private static Field FIELD;

    @Unique
    private BeeAI.CachedPathHolder cachedPathHolder;

    /**
     * @author TelepathicGrunt
     * @reason Make bees not get stuck on ceiling anymore and lag people as a result. (Only for Forge version of Bumblezone)
     */
    @Inject(method = "start()V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void newWander(CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException {

        // Reflection needed to get BeeEntity outer class of WanderGoal as BeeEntity.this does not work inside mixins.
        BeeEntity.BeeWanderAroundGoal wanderGoal = ((BeeEntity.BeeWanderAroundGoal)(Object)this);
        if(FIELD == null){
            // Cache the field for better performance and set it accessible.
            FIELD = BeeEntity.BeeWanderAroundGoal.class.getDeclaredField("this$0");
            FIELD.setAccessible(true);
        }
        BeeEntity beeEntity = (BeeEntity)FIELD.get(wanderGoal);

        // Do our own bee AI in the Bumblezone. Makes bees wander more and should be slightly better performance. Maybe...
        if(beeEntity.world.getRegistryKey().equals(BzDimension.BZ_WORLD_KEY)){
            cachedPathHolder = BeeAI.smartBeesTM(beeEntity, cachedPathHolder);
            ci.cancel();
        }
    }
}