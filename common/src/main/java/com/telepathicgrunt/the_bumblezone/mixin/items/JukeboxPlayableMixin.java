package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.telepathicgrunt.the_bumblezone.items.BzMusicDiscsDownloadLinkTooltip;
import net.minecraft.core.component.DataComponentGetter;
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
    @Inject(method = "addToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/core/component/DataComponentGetter;)V",
            at = @At(value = "RETURN"))
    private void bumblezone$showDownloadLinkForrMusicDisc(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter, CallbackInfo ci) {
        BzMusicDiscsDownloadLinkTooltip.appendDownloadLinkText((JukeboxPlayable) (Object)this, context, tooltipAdder, tooltipFlag);
    }
}