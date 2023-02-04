package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {

    @Accessor("MAP")
    static java.util.Map<InputConstants.Key, KeyMapping> getMAP() {
        throw new AssertionError("Mixin did not apply");
    }

    @Accessor("ALL")
    static java.util.Map<String, KeyMapping> getALL() {
        throw new AssertionError("Mixin did not apply");
    }
}
