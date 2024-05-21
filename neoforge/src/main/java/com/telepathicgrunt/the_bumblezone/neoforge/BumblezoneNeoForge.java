package com.telepathicgrunt.the_bumblezone.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.neoforge.BzConfigHandler;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzAttachmentTypes;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzBiomeModifiers;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzGlobalLootModifier;
import com.telepathicgrunt.the_bumblezone.modules.neoforge.NeoForgeModuleInitializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Bumblezone.MODID)
public class BumblezoneNeoForge {

    public BumblezoneNeoForge(IEventBus modEventBus) {
        BzConfigHandler.setup(modEventBus);

        NeoForgeModuleInitializer.init();

        Bumblezone.init();

        BzGlobalLootModifier.GLM.register(modEventBus);
        BzBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);
        BzAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        IEventBus eventBus = NeoForge.EVENT_BUS;
        NeoForgeEventManager.init(modEventBus, eventBus);
    }
}
