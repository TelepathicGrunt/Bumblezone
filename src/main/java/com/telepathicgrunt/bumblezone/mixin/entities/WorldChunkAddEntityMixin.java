package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
public class WorldChunkAddEntityMixin {
    //bees attacks bear or non-bee insects mobs that is in the dimension
    @Inject(method = "addEntity",
            at = @At(value = "RETURN"))
    private void thebumblezone_onAddedEntity(Entity entity, CallbackInfo ci) {
        BeeAggression.entityTypeBeeAnger(entity);
    }
}