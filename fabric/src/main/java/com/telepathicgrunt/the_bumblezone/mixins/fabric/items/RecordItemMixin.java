package com.telepathicgrunt.the_bumblezone.mixins.fabric.items;

import com.telepathicgrunt.the_bumblezone.items.BzMusicDiscs;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecordItem.class)
public class RecordItemMixin {

    @Inject(method = "getBySound", at = @At("HEAD"), cancellable = true)
    private static void bumblezone$onGetBySound(SoundEvent soundEvent, CallbackInfoReturnable<RecordItem> cir) {
        RecordItem record = BzMusicDiscs.get(soundEvent);
        if (record != null) {
            cir.setReturnValue(record);
        }
    }
}
