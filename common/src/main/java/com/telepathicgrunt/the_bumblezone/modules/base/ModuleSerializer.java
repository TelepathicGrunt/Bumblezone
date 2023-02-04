package com.telepathicgrunt.the_bumblezone.modules.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface ModuleSerializer<T extends Module<T>> {

    Class<T> moduleClass();

    ResourceLocation id();

    void read(T module, CompoundTag tag);

    void write(CompoundTag tag, T module);

    default void onPlayerCopy(T oldModule, T thisModule, ServerPlayer player, boolean isPersistent) {
        if (isPersistent) {
            CompoundTag tag = new CompoundTag();
            oldModule.serializer().write(tag, oldModule);
            read(thisModule, tag);
        }
    }
}
