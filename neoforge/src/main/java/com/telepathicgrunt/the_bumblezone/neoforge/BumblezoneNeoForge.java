package com.telepathicgrunt.the_bumblezone.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.neoforge.BzConfigHandler;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzAttachmentTypes;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzBiomeModifiers;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzGlobalLootModifier;
import com.telepathicgrunt.the_bumblezone.modinit.registry.neoforge.ResourcefulRegistriesImpl;
import com.telepathicgrunt.the_bumblezone.modules.neoforge.NeoForgeModuleInitalizer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

@Mod(Bumblezone.MODID)
public class BumblezoneNeoForge {

    public BumblezoneNeoForge(IEventBus modEventBus) {
        BzConfigHandler.setup();
        loadBumblezoneClientConfigsEarly();

        NeoForgeModuleInitalizer.init();
        modEventBus.addListener(EventPriority.NORMAL, ResourcefulRegistriesImpl::onRegisterForgeRegistries);

        Bumblezone.init();

        BzGlobalLootModifier.GLM.register(modEventBus);
        BzBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);
        BzAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        IEventBus eventBus = NeoForge.EVENT_BUS;

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BumblezoneNeoForgeClient.init(modEventBus, eventBus);
        }

        NeoForgeEventManager.init(modEventBus, eventBus);
    }

    private static void loadBumblezoneClientConfigsEarly() {
        Optional<? extends ModContainer> modContainerById = ModList.get().getModContainerById(Bumblezone.MODID);
        modContainerById.ifPresent(container ->
            ConfigTracker.INSTANCE.configSets()
                .get(ModConfig.Type.CLIENT)
                .forEach(c -> {
                    if (c.getFileName().contains(Bumblezone.MODID)) {
                        try {
                            Method method = ConfigTracker.INSTANCE.getClass().getDeclaredMethod("openConfig", ModConfig.class, Path.class);
                            method.setAccessible(true);
                            method.invoke(ConfigTracker.INSTANCE, c, FMLPaths.CONFIGDIR.get());
                            method.setAccessible(false);
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
    }
}
