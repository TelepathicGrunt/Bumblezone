package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record RegisterMenuScreenEvent(Registrar registrar) {

    public static final EventHandler<RegisterMenuScreenEvent> EVENT = new EventHandler<>();

    public <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void register(MenuType<T> type, ScreenConstructor<T, U> provider) {
        registrar.register(type, provider);
    }


    public interface Registrar {
        <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<M> menuType, ScreenConstructor<M, U> screenConstructor);
    }

    @FunctionalInterface
    public interface ScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T abstractContainerMenu, Inventory inventory, Component component);
    }
}
