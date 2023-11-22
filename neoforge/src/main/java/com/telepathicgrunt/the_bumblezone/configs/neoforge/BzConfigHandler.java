package com.telepathicgrunt.the_bumblezone.configs.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Files;

public class BzConfigHandler {

    public static void setup() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(BzConfigHandler::onConfigLoad);
        bus.addListener(BzConfigHandler::onConfigReload);

        try {
            Files.createDirectories(FMLPaths.CONFIGDIR.get().resolve("the_bumblezone"));
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BzClientConfig.GENERAL_SPEC, "the_bumblezone/client.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzGeneralConfig.GENERAL_SPEC, "the_bumblezone/general.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzWorldgenConfig.GENERAL_SPEC, "the_bumblezone/worldgen.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzDimensionConfig.GENERAL_SPEC, "the_bumblezone/dimension.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzBeeAggressionConfig.GENERAL_SPEC, "the_bumblezone/bee_aggression.toml");
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BzModCompatibilityConfig.GENERAL_SPEC, "the_bumblezone/mod_compatibility.toml");
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create Bumblezone config files: ", e);
        }
    }

    private static void onConfigLoad(ModConfigEvent.Loading event) {
        copyToCommon(event.getConfig().getSpec());
    }

    private static void onConfigReload(ModConfigEvent.Reloading event) {
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
