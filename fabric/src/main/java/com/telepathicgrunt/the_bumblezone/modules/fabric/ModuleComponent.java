package com.telepathicgrunt.the_bumblezone.modules.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record ModuleComponent<T extends Module<T>>(T module) implements Component {

    public static <E, T extends Module<T>> ModuleComponent<T> create(Supplier<T> factory) {
        return new ModuleComponent<>(factory.get());
    }

    @Override
    public void readFromNbt(@NotNull CompoundTag tag) {
        module.serializer().read(module, tag);
    }

    @Override
    public void writeToNbt(@NotNull CompoundTag tag) {
        module.serializer().write(tag, module);
    }
}
