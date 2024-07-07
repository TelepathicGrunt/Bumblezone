package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.telepathicgrunt.the_bumblezone.items.potions.ConfigSafePotion;
import com.telepathicgrunt.the_bumblezone.modinit.BzPotions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Files;

public class BzConfigHandler {

    public static void setup(ModContainer modContainer, IEventBus modEventBus) {
        modEventBus.addListener(BzConfigHandler::onConfigLoad);
        modEventBus.addListener(BzConfigHandler::onConfigReload);

        try {
            Files.createDirectories(FMLPaths.CONFIGDIR.get().resolve("the_bumblezone"));
            modContainer.registerConfig(ModConfig.Type.STARTUP, BzClientConfig.GENERAL_SPEC, "the_bumblezone/client.toml");
            modContainer.registerConfig(ModConfig.Type.STARTUP, BzGeneralConfig.GENERAL_SPEC, "the_bumblezone/general.toml");
            modContainer.registerConfig(ModConfig.Type.STARTUP, BzWorldgenConfig.GENERAL_SPEC, "the_bumblezone/worldgen.toml");
            modContainer.registerConfig(ModConfig.Type.STARTUP, BzDimensionConfig.GENERAL_SPEC, "the_bumblezone/dimension.toml");
            modContainer.registerConfig(ModConfig.Type.STARTUP, BzBeeAggressionConfig.GENERAL_SPEC, "the_bumblezone/bee_aggression.toml");
            modContainer.registerConfig(ModConfig.Type.STARTUP, BzModCompatibilityConfig.GENERAL_SPEC, "the_bumblezone/mod_compatibility.toml");
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create Bumblezone config files: ", e);
        }
    }

    private static void onConfigLoad(ModConfigEvent.Loading event) {
        copyToCommon(event.getConfig().getSpec());
    }

    private static void onConfigReload(ModConfigEvent.Reloading event) {
        BzPotions.POTIONS.getEntries().forEach(p -> {
            if (p instanceof ConfigSafePotion configSafePotion) {
                configSafePotion.clearCachedEffectList();
            }
        });
        copyToCommon(event.getConfig().getSpec());
    }

    private static void copyToCommon(IConfigSpec<?> spec) {
        if (spec == BzClientConfig.GENERAL_SPEC) BzClientConfig.copyToCommon();
        if (spec == BzGeneralConfig.GENERAL_SPEC) BzGeneralConfig.copyToCommon();
        if (spec == BzWorldgenConfig.GENERAL_SPEC) BzWorldgenConfig.copyToCommon();
        if (spec == BzDimensionConfig.GENERAL_SPEC) BzDimensionConfig.copyToCommon();
        if (spec == BzBeeAggressionConfig.GENERAL_SPEC) BzBeeAggressionConfig.copyToCommon();
        if (spec == BzModCompatibilityConfig.GENERAL_SPEC) BzModCompatibilityConfig.copyToCommon();
    }
}
