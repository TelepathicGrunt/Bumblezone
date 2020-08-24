package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DynamicRegistryManager.class)
public interface DynamicRegistryManagerAccessor {

    @Accessor("field_26733")
    static DynamicRegistryManager.Impl getBUILTIN() {
        throw new UnsupportedOperationException();
    }
}