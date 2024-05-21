package com.telepathicgrunt.the_bumblezone.modules.base;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public interface Module<T extends Module<T>> {

    Codec<T> codec();

    ResourceLocation id();
}
