package com.telepathicgrunt.the_bumblezone.services;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.KeyMapping;

public interface ClientPlatformService {
    ClientPlatformService INSTANCE = GeneralUtils.loadService(ClientPlatformService.class);

    KeyMapping createKey(String display, BeehemothControls.KeyConflict conflict, InputConstants.Key key, String category);
}
