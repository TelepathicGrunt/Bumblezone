package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Bee.BeeGoToKnownFlowerGoal.class)
public class BeeGoToKnownFlowerGoalMixin {

    @Final
    @Shadow(aliases = {"field_226508_a_", "field_20372"})
    private Bee this$0;

    /**
     * @author TelepathicGrunt
     * @reason Always use the entity's own randomSource instead of world's when creating/initing entities or else you risk a crash from threaded worldgen entity spawning. Fixed this bug with vanilla bees.
     */
    @ModifyReceiver(method = "<init>(Lnet/minecraft/world/entity/animal/Bee;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private RandomSource thebumblezone_fixGoalRandomSourceUsage2(RandomSource randomSource, int range) {
        return this$0.getRandom();
    }
}