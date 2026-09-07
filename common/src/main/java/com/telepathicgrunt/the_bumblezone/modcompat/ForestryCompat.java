package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinDataPacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ForestryCompat implements ModCompat {

    public ForestryCompat() {

        if (BzModCompatibilityConfigs.allowForestryCompat) {
            Bumblezone.MOD_COMPAT_DATAPACKS.add(addBuiltinDataPacks ->
                    addBuiltinDataPacks.add(
                            Identifier.fromNamespaceAndPath(Bumblezone.MODID, "forestry_compat"),
                            Component.literal("Bumblezone - Forestry Compat"),
                            BzAddBuiltinDataPacks.PackMode.ENABLED_BY_DEFAULT
                    )
            );
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.forestryPresent = true;
    }
}
