package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public record BzHookupRangeSelectItemModelPropertiesEvent(BiConsumer<Identifier, MapCodec<? extends RangeSelectItemModelProperty>> registrator) {

    public static final EventHandler<BzHookupRangeSelectItemModelPropertiesEvent> EVENT = new EventHandler<>();
}
