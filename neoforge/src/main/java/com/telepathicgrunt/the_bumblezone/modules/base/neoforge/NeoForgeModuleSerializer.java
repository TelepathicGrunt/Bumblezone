package com.telepathicgrunt.the_bumblezone.modules.base.neoforge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class NeoForgeModuleSerializer<T extends Module<T>> implements INBTSerializable {
    private final T module;

    public NeoForgeModuleSerializer(T module) {
        this.module = module;
    }

    public T getModule() {
        return module;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        module.serializer().write(compoundTag, module);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag tag) {
        if (tag instanceof CompoundTag compoundTag) {
            module.serializer().read(module, compoundTag);
        }
    }
}
