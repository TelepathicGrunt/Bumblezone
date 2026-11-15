package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.rei.REICompat;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.forge.REIPluginClient;

@REIPluginClient
public class REIIntegration implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        REICompat.registerDisplays(registry);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        REICompat.registerCategories(registry);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        REICompat.registerScreens(registry);
    }
}