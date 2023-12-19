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
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Bumblezone.MODID)
public class BumblezoneNeoForge {

    public BumblezoneNeoForge(IEventBus modEventBus) {
        BzConfigHandler.setup();
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
}
