package com.telepathicgrunt.the_bumblezone.modcompat.neoforge.framedblocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import io.github.xfacthd.framedblocks.api.camo.CamoContainerFactory;
import io.github.xfacthd.framedblocks.api.util.FramedConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FramedBlocksCompat implements ModCompat {

    private static final DeferredRegister<CamoContainerFactory<?>> CAMO_FACTORIES = DeferredRegister.create(
            FramedConstants.Registries.CAMO_CONTAINER_FACTORY_REGISTRY_KEY,
            Bumblezone.MODID
    );
    static final DeferredHolder<CamoContainerFactory<?>, WaxBlockCamoContainerFactory> WAX_BLOCK_CAMO_FACTORY =
            CAMO_FACTORIES.register("carvable_wax", WaxBlockCamoContainerFactory::new);

    public FramedBlocksCompat(IEventBus modEventBus) {
        CAMO_FACTORIES.register(modEventBus);

        ModChecker.framedBlocksPresent = true;
    }
}
