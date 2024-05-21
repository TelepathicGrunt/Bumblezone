package com.telepathicgrunt.the_bumblezone.modules.base;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public record ModuleHolder<T extends Module<?>>(ResourceLocation id, Codec<T> codec, Supplier<T> factory) {}
