package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.BzMusicDiscsDownloadLinkTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = JukeboxPlayable.class, priority = 1200)
public class JukeboxPlayableMixin {

    /**
     * Using Glass Bottle to get honey could anger bees
     */
    @Inject(method = "addToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At(value = "RETURN"))
    private void bumblezone$showDownloadLinkForrMusicDisc(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo ci) {
        BzMusicDiscsDownloadLinkTooltip.appendDownloadLinkText((JukeboxPlayable) (Object)this, tooltipContext, consumer, tooltipFlag);
    }
}