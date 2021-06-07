package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.ChunkPrimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkPrimer.class)
public class ProtoChunkAddEntityMixin {
    //bees attacks bear or non-bee insects mobs that is in the dimension
    @Inject(method = "addEntity(Lnet/minecraft/entity/Entity;)V",
            at = @At(value = "RETURN"))
    private void thebumblezone_onAddedEntity(Entity entity, CallbackInfo ci) {
        BeeAggression.entityTypeBeeAnger(entity);
    }
}