package com.telepathicgrunt.the_bumblezone.modules.base;

public interface Module<T extends Module<T>> {

    ModuleSerializer<T> serializer();
}
