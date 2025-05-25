package com.telepathicgrunt.the_bumblezone.services.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import com.telepathicgrunt.the_bumblezone.mixin.fabric.client.KeyMappingAccessor;
import com.telepathicgrunt.the_bumblezone.services.KeyMappingService;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.client.KeyMapping;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Contract;

public class FabricKeyMappingService implements KeyMappingService {

    @Override
    public KeyMapping createBeehemothKey(String display, BeehemothControls.KeyConflict conflict, InputConstants.Key key, String category) {
        KeyMapping oldMapping = KeyMappingAccessor.bumblezone$getMAP().get(key);
        KeyMapping keyMapping = new KeyMapping(display, key.getValue(), category);
        KeyMappingAccessor.bumblezone$getMAP().put(key, oldMapping);
        KeyMappingAccessor.bumblezone$getALL().remove(display);
        return keyMapping;
    }
}
