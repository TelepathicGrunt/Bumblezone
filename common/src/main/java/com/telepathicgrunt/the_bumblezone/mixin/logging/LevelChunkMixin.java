
package com.telepathicgrunt.the_bumblezone.mixin.logging;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import net.minecraft.world.level.chunk.LevelChunk;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LevelChunk.class, priority = 1200)
public class LevelChunkMixin {

    // Silence logspam that isn't an issue by lowering it from error to debug: https://bugs.mojang.com/browse/MC-278282
    @WrapOperation(method = "promotePendingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
            require = 0)
    private void bumblezone$lowerLoggingLevel1(Logger instance, String s, Object o1, Object o2, Operation<Void> original) {
        if (!PlatformHooks.isDevEnvironment()) {
            instance.debug(s, o1, o2);
        }
        else {
            instance.warn(s, o1, o2);
        }
    }
}