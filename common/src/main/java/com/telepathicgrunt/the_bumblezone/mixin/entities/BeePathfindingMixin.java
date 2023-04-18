package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeAI;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.BeeWanderGoal.class)
public class BeePathfindingMixin {

    @Final
    @Shadow(aliases = {"field_226508_a_", "field_20380", "f_28091_"})
    private Bee this$0;

    @Unique
    private BeeAI.CachedPathHolder bumblezone$cachedPathHolder;

    /**
     * @author TelepathicGrunt
     * @reason Make bees not get stuck on ceiling anymore and lag people as a result. (Only applies in Bumblezone dimension)
     */
    @Inject(method = "start()V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone$newWander(CallbackInfo ci){
        // Do our own bee AI in the Bumblezone. Makes bees wander more and should be slightly better performance. Maybe...
        if(this$0.level.dimension().equals(BzDimension.BZ_WORLD_KEY)){
            bumblezone$cachedPathHolder = BeeAI.smartBeesTM(this$0, bumblezone$cachedPathHolder);
            ci.cancel();
        }
    }
}