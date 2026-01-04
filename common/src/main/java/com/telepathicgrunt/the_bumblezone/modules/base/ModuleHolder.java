package com.telepathicgrunt.the_bumblezone.modules.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public record ModuleHolder<T extends Module<?>>(Identifier id, MapCodec<T> codec, Supplier<T> factory) {}
