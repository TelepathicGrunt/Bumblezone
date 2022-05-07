package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {
    @Accessor("MAP")
    static Map<InputConstants.Key, KeyMapping> getMAP() {
        throw new UnsupportedOperationException();
    }

    @Accessor("ALL")
    static Map<String, KeyMapping> getALL() {
        throw new UnsupportedOperationException();
    }
}
