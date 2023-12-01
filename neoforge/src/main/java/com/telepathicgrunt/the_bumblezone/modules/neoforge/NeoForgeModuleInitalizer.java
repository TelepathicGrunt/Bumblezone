package com.telepathicgrunt.the_bumblezone.modules.neoforge;

import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzAttachmentTypes;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.neoforge.NeoForgeModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.neoforge.NeoForgeModuleSerializer;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistrar;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NeoForgeModuleInitalizer {

    private static final List<ModuleRegistryValue<?>> LIVING_ENTITY_MODULES = new ArrayList<>();
    private static final List<ModuleRegistryValue<?>> PLAYER_ENTITY_MODULES = new ArrayList<>();
    private static final List<ModuleRegistryValue<?>> PERSIST_PLAYER_DATA_SAVE_MODULES = new ArrayList<>();

    public static void init() {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerPlayerModule(ModuleHolder<T> holder, Supplier<T> factory, boolean runDataCloneForPlayer) {
                if (holder instanceof NeoForgeModuleHolder<T> forgeHolder) {
                    ModuleRegistryValue<?> moduleRegistryValue = new ModuleRegistryValue<>(forgeHolder, factory);
                    PLAYER_ENTITY_MODULES.add(moduleRegistryValue);
                    if (runDataCloneForPlayer) {
                        PERSIST_PLAYER_DATA_SAVE_MODULES.add(moduleRegistryValue);
                    }
                }
            }

            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerLivingEntityModule(ModuleHolder<T> holder, Supplier<T> factory, boolean runDataCloneForPlayer) {
                if (holder instanceof NeoForgeModuleHolder<T> neoforgeHolder) {
                    ModuleRegistryValue<?> moduleRegistryValue = new ModuleRegistryValue<>(neoforgeHolder, factory);
                    LIVING_ENTITY_MODULES.add(new ModuleRegistryValue<>(neoforgeHolder, factory));
                    if (runDataCloneForPlayer) {
                        PERSIST_PLAYER_DATA_SAVE_MODULES.add(moduleRegistryValue);
                    }
                }
            }
        });

        for (var value : LIVING_ENTITY_MODULES) {
            BzAttachmentTypes.ATTACHMENT_TYPES.register(
                    value.factory.get().serializer().id().getPath(),
                    () -> AttachmentType.serializable(() -> new NeoForgeModuleSerializer(value.factory.get())).build()
            );
        }
        for (var value : PLAYER_ENTITY_MODULES) {
            BzAttachmentTypes.ATTACHMENT_TYPES.register(
                    value.factory.get().serializer().id().getPath(),
                    () ->  {
                        AttachmentType.Builder<NeoForgeModuleSerializer> builder =
                                AttachmentType.serializable(() -> new NeoForgeModuleSerializer(value.factory.get()));

                        if (PERSIST_PLAYER_DATA_SAVE_MODULES.contains(value)) {
                            builder.copyOnDeath();
                        }

                        return builder.build();
                    }
            );
        }
    }

    private record ModuleRegistryValue<T extends Module<T>>(NeoForgeModuleHolder<T> holder, Supplier<T> factory) { }
}
