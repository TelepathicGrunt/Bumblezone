
package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.world.entity.decoration.HangingEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HangingEntity.class)
public class HangingEntityMixin {

    // Silence logspam that isn't an issue by lowering it from error to debug: https://bugs.mojang.com/browse/MC-252934
    @Redirect(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
            require = 0)
    private void thebumblezone_lowerLoggingLevel(Logger instance, String s, Object o) {
        instance.debug(s, o);
    }
}