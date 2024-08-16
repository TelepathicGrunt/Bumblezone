package com.telepathicgrunt.the_bumblezone.modules.fabric;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistrar;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class FabricModuleInitializer {
    public static void init() {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerPlayerModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer) {
                createPersistent(moduleHolder.id(), moduleHolder.codec(), runDataCloneForPlayer);
            }

            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerLivingEntityModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer) {
                createPersistent(moduleHolder.id(), moduleHolder.codec(), runDataCloneForPlayer);
            }
        });
    }

    public static <A> void createPersistent(ResourceLocation id, Codec<A> codec, boolean copyOnDeath) {
        Objects.requireNonNull(id, "identifier cannot be null");
        Objects.requireNonNull(codec, "codec cannot be null");

        AttachmentRegistry.Builder<A> builder = AttachmentRegistry.<A>builder().persistent(codec);
        if (copyOnDeath) {
            builder = builder.copyOnDeath();
        }
        builder.buildAndRegister(id);
    }
}
