package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public record BzHookupItemTintSourcesEvent(BiConsumer<Identifier, MapCodec<? extends ItemTintSource>> registrator) {

    public static final EventHandler<BzHookupItemTintSourcesEvent> EVENT = new EventHandler<>();
}
