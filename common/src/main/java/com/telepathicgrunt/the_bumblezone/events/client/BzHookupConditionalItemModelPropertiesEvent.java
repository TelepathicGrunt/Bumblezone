package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public record BzHookupConditionalItemModelPropertiesEvent(BiConsumer<Identifier, MapCodec<? extends ConditionalItemModelProperty>> registrator) {

    public static final EventHandler<BzHookupConditionalItemModelPropertiesEvent> EVENT = new EventHandler<>();
}
