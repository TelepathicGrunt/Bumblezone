package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobTickMixin {
    //bees attacks bear mobs that is in the dimension
    @Inject(method = "tick",
            at = @At(value = "HEAD"))
    private void onEntityTick(CallbackInfo ci) {
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        BeeAggression.entityTypeBeeAnger(((Entity) (Object) this));
    }

}