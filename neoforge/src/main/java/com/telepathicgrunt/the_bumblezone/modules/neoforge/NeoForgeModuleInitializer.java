package com.telepathicgrunt.the_bumblezone.modules.neoforge;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzAttachmentTypes;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistrar;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NeoForgeModuleInitializer {

    private static final List<ModuleRegistryValue<?>> LIVING_ENTITY_MODULES = new ArrayList<>();
    private static final List<ModuleRegistryValue<?>> PLAYER_ENTITY_MODULES = new ArrayList<>();
    private static final List<ModuleRegistryValue<?>> PERSIST_PLAYER_DATA_SAVE_MODULES = new ArrayList<>();

    public static void init() {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerPlayerModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer) {
                ModuleRegistryValue<?> moduleRegistryValue = new ModuleRegistryValue<>(moduleHolder.id(), moduleHolder.codec(), moduleHolder.factory());
                PLAYER_ENTITY_MODULES.add(moduleRegistryValue);
                if (runDataCloneForPlayer) {
                    PERSIST_PLAYER_DATA_SAVE_MODULES.add(moduleRegistryValue);
                }
            }

            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerLivingEntityModule(ModuleHolder<T> moduleHolder,  boolean runDataCloneForPlayer) {
                ModuleRegistryValue<?> moduleRegistryValue = new ModuleRegistryValue<>(moduleHolder.id(), moduleHolder.codec(), moduleHolder.factory());
                LIVING_ENTITY_MODULES.add(moduleRegistryValue);
                if (runDataCloneForPlayer) {
                    PERSIST_PLAYER_DATA_SAVE_MODULES.add(moduleRegistryValue);
                }
            }
        });

        for (var value : LIVING_ENTITY_MODULES) {
            AttachmentType.Builder<?> builder = getAttachmentTypeBuilder(value);
            BzAttachmentTypes.ATTACHMENT_TYPES.register(
                    value.factory.get().id().getPath(),
                    builder::build
            );
        }
        for (var value : PLAYER_ENTITY_MODULES) {
            BzAttachmentTypes.ATTACHMENT_TYPES.register(
                    value.factory.get().id().getPath(),
                    () ->  {
                        AttachmentType.Builder<?> builder = getAttachmentTypeBuilder(value);

                        if (PERSIST_PLAYER_DATA_SAVE_MODULES.contains(value)) {
                            builder.copyOnDeath();
                        }

                        return builder.build();
                    }
            );
        }
    }

    private static <T extends Module<T>> AttachmentType.Builder<T> getAttachmentTypeBuilder(ModuleRegistryValue<T> value) {
        return AttachmentType.builder(value.factory()).serialize(value.codec());
    }

    private record ModuleRegistryValue<T extends Module<T>>(ResourceLocation id, Codec<T> codec, Supplier<T> factory) { }
}
