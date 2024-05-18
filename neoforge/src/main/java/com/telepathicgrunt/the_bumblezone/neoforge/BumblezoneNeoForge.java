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
        BzConfigHandler.setup(modEventBus);

        NeoForgeModuleInitalizer.init();
        modEventBus.addListener(EventPriority.NORMAL, ResourcefulRegistriesImpl::onRegisterForgeRegistries);

        Bumblezone.init();

        BzGlobalLootModifier.GLM.register(modEventBus);
        BzBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);
        BzAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        IEventBus eventBus = NeoForge.EVENT_BUS;
        NeoForgeEventManager.init(modEventBus, eventBus);
    }
}
