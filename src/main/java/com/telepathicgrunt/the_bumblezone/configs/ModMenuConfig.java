package com.telepathicgrunt.the_bumblezone.configs;


import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import eu.midnightdust.lib.config.MidnightConfig;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class ModMenuConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, Bumblezone.MODID);
    }
}