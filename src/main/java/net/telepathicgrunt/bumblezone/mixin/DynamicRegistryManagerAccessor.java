package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.util.registry.DynamicRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DynamicRegistries.class)
public interface DynamicRegistryManagerAccessor {

    @Accessor("BUILTIN")
    static DynamicRegistries.Impl getBUILTIN() {
        throw new UnsupportedOperationException();
    }
}