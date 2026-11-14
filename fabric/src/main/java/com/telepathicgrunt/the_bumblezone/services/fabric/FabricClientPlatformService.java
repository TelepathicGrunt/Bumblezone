package com.telepathicgrunt.the_bumblezone.services.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import com.telepathicgrunt.the_bumblezone.services.ClientPlatformService;
import net.minecraft.client.KeyMapping;

public class FabricClientPlatformService implements ClientPlatformService {
    @Override
    public KeyMapping createKey(String display, BeehemothControls.KeyConflict conflict, InputConstants.Key key, KeyMapping.Category category) {
        return new KeyMapping(display, key.getValue(), category);
    }
}
