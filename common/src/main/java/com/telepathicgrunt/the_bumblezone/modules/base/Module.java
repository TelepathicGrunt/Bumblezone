package com.telepathicgrunt.the_bumblezone.modules.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public interface Module<T extends Module<T>> {

    MapCodec<T> codec();

    ResourceLocation id();
}
