package com.telepathicgrunt.the_bumblezone.modules.base;

public interface ModuleHolder<T extends Module<T>> {

    ModuleSerializer<T> getSerializer();
}
