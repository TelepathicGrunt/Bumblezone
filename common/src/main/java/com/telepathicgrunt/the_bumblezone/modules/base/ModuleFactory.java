package com.telepathicgrunt.the_bumblezone.modules.base;

public interface ModuleFactory<E, T extends Module<T>> {

    T create(E input);
}
