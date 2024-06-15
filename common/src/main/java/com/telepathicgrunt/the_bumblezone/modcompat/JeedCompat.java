package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinDataPacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class JeedCompat implements ModCompat {

    public JeedCompat() {

        Bumblezone.MOD_COMPAT_DATAPACKS.add(addBuiltinDataPacks ->
                addBuiltinDataPacks.add(
                        ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "jeed_effects_compat"),
                        Component.literal("Bumblezone - JEED Compat"),
                        AddBuiltinDataPacks.PackMode.FORCE_ENABLED
                )
        );

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.jeedPresent = true;
    }
}
