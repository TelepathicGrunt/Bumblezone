package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinDataPacks;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class TokenEnchanterCompat implements ModCompat {

    public TokenEnchanterCompat() {

        Bumblezone.MOD_COMPAT_DATAPACKS.add(addBuiltinDataPacks ->
                addBuiltinDataPacks.add(
                        Identifier.fromNamespaceAndPath(Bumblezone.MODID, "enchanted_token_compat"),
                        Component.literal("Bumblezone - Token Enchanter Compat"),
                        BzAddBuiltinDataPacks.PackMode.FORCE_ENABLED
                )
        );

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.tokenEnchanterPresent = true;
    }
}
