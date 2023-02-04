package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public record RegisterCreativeTabsEvent(TabCreator creator) {

    public static final EventHandler<RegisterCreativeTabsEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation id, Consumer<CreativeModeTab.Builder> builder, Consumer<List<ItemStack>> displayStacks) {
        creator.create(id, builder, displayStacks);
    }


    @FunctionalInterface
    public interface TabCreator {

        void create(ResourceLocation id, Consumer<CreativeModeTab.Builder> builder, Consumer<List<ItemStack>> displayStacks);
    }
}
