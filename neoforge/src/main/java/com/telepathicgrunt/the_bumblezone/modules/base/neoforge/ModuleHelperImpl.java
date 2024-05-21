package com.telepathicgrunt.the_bumblezone.modules.base.neoforge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

public class ModuleHelperImpl {
    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> moduleHolder) {
        AttachmentType<T> attachmentType = (AttachmentType<T>) NeoForgeRegistries.ATTACHMENT_TYPES.get(moduleHolder.id());
        if (attachmentType != null) {
            return Optional.of(entity.getData(attachmentType));
        }
        return Optional.empty();
    }
}
