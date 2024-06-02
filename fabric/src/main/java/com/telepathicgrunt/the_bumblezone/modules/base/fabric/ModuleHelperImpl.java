package com.telepathicgrunt.the_bumblezone.modules.base.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class ModuleHelperImpl {
    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> moduleHolder) {
        AttachmentType<T> attachmentType = (AttachmentType<T>) AttachmentRegistryImpl.get(moduleHolder.id());
        if (attachmentType != null) {
            if (!entity.hasAttached(attachmentType)) {
                entity.setAttached(attachmentType, moduleHolder.factory().get());
            }
            return Optional.ofNullable(entity.getAttached(attachmentType));
        }
        return Optional.empty();
    }
}
