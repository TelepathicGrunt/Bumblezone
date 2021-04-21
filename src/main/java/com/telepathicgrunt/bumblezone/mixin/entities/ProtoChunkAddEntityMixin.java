package com.telepathicgrunt.bumblezone.mixin.entities;

import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProtoChunk.class)
public class ProtoChunkAddEntityMixin {
    //bees attacks bear or non-bee insects mobs that is in the dimension
    @Inject(method = "Lnet/minecraft/world/chunk/ProtoChunk;addEntity(Lnet/minecraft/entity/Entity;)V",
            at = @At(value = "RETURN"))
    private void onAddedEntity(Entity entity, CallbackInfo ci) {
        BeeAggression.entityTypeBeeAnger(entity);
    }
}