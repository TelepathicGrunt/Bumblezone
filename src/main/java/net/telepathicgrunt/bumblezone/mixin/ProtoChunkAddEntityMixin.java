package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.ChunkPrimer;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkPrimer.class)
public class ProtoChunkAddEntityMixin {
    //bees attacks bear or non-bee insects mobs that is in the dimension
    @Inject(method = "addEntity(Lnet/minecraft/entity/Entity;)V",
            at = @At(value = "RETURN"))
    private void onAddedEntity(Entity entity, CallbackInfo ci) {
        BeeAggression.entityTypeBeeAnger(entity);
    }
}