package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.telepathicgrunt.the_bumblezone.items.potions.ConfigSafePotion;
import com.telepathicgrunt.the_bumblezone.modinit.BzPotions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Files;

public class BzConfigHandler {

    public static void setup(ModContainer modContainer, IEventBus modEventBus) {
        modEventBus.addListener(BzConfigHandler::onConfigLoad);
        modEventBus.addListener(BzConfigHandler::onConfigReload);

        try {
            Files.createDirectories(FMLPaths.CONFIGDIR.get().resolve("the_bumblezone"));
            createAndLoadConfigs(modContainer, ModConfig.Type.CLIENT, BzClientConfig.GENERAL_SPEC, "the_bumblezone/client.toml");
            createAndLoadConfigs(modContainer, ModConfig.Type.COMMON, BzGeneralConfig.GENERAL_SPEC, "the_bumblezone/general.toml");
            createAndLoadConfigs(modContainer, ModConfig.Type.COMMON, BzWorldgenConfig.GENERAL_SPEC, "the_bumblezone/worldgen.toml");
            createAndLoadConfigs(modContainer, ModConfig.Type.COMMON, BzDimensionConfig.GENERAL_SPEC, "the_bumblezone/dimension.toml");
            createAndLoadConfigs(modContainer, ModConfig.Type.COMMON, BzBeeAggressionConfig.GENERAL_SPEC, "the_bumblezone/bee_aggression.toml");
            createAndLoadConfigs(modContainer, ModConfig.Type.COMMON, BzModCompatibilityConfig.GENERAL_SPEC, "the_bumblezone/mod_compatibility.toml");
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

    private static void createAndLoadConfigs(ModContainer modContainer, ModConfig.Type type, ModConfigSpec spec, String path) {
        modContainer.registerConfig(type, spec, path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve(path))
                                                .preserveInsertionOrder()
                                                .autoreload()
                                                .writingMode(WritingMode.REPLACE)
                                                .sync()
                                                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
