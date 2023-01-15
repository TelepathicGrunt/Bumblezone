package com.telepathicgrunt.the_bumblezone.modules.forge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.forge.ForgeModuleHolder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModuleCapabilityProvider<M extends Module<M>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final ForgeModuleHolder<M> holder;
    private final M backend;
    private final LazyOptional<M> optionalData;

    public ModuleCapabilityProvider(ForgeModuleHolder<M> holder, M backend) {
        this.holder = holder;
        this.backend = backend;
        this.optionalData = LazyOptional.of(() -> backend);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return holder.capability().orEmpty(capability, optionalData);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        holder.getSerializer().write(tag, backend);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        holder.getSerializer().read(backend, arg);
    }
}
