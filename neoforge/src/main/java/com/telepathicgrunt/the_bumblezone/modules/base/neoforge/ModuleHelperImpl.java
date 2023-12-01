package com.telepathicgrunt.the_bumblezone.modules.base.neoforge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;

public class ModuleHelperImpl {
    public static <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        return NeoForgeModuleHolder.of(serializer);
    }

    public static <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        if (holder instanceof NeoForgeModuleHolder<T> neoforgeHolder) {
            AttachmentType<NeoForgeModuleSerializer<T>> attachmentType = (AttachmentType<NeoForgeModuleSerializer<T>>) NeoForgeRegistries.ATTACHMENT_TYPES.get(neoforgeHolder.id());
            if (attachmentType != null) {
                return Optional.of(entity.getData(attachmentType).getModule());
            }
        }
        return Optional.empty();
    }
}
