package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.telepathicgrunt.the_bumblezone.client.BeehemothControls;
import com.telepathicgrunt.the_bumblezone.mixins.fabric.client.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

public class BeehemothControlsImpl {
    public static KeyMapping createKey(String display, BeehemothControls.KeyConflict conflict, InputConstants.Key key, String category) {
        KeyMapping oldMapping = KeyMappingAccessor.getMAP().get(key);
        KeyMapping keyMapping = new KeyMapping(display, key.getValue(), category);
        KeyMappingAccessor.getMAP().put(key, oldMapping);
        KeyMappingAccessor.getALL().remove(display);
        return keyMapping;
    }
}
