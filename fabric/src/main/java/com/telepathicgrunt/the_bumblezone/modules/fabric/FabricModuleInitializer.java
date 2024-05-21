package com.telepathicgrunt.the_bumblezone.modules.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistrar;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class FabricModuleInitializer {
    public static void init() {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerPlayerModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer) {
                AttachmentType<T> attachmentType = AttachmentRegistry.createPersistent(moduleHolder.id(), moduleHolder.codec());
                if (runDataCloneForPlayer) {
                    attachmentType.copyOnDeath();
                }
            }

            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerLivingEntityModule(ModuleHolder<T> moduleHolder, boolean runDataCloneForPlayer) {
                AttachmentType<T> attachmentType = AttachmentRegistry.createPersistent(moduleHolder.id(), moduleHolder.codec());
                if (runDataCloneForPlayer) {
                    attachmentType.copyOnDeath();
                }
            }
        });
    }
}
