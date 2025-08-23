package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinDataPacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ForestryCompat implements ModCompat {

    public ForestryCompat() {

        Bumblezone.MOD_COMPAT_DATAPACKS.add(addBuiltinDataPacks ->
            addBuiltinDataPacks.add(
                ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "forestry_compat"),
                Component.literal("Bumblezone - Forestry Compat"),
                BzAddBuiltinDataPacks.PackMode.FORCE_ENABLED
            )
        );

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.forestryPresent = true;
    }
}
