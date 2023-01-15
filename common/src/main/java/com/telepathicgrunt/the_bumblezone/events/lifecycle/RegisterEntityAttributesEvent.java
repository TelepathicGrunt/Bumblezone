package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.Map;

public record RegisterEntityAttributesEvent(Map<EntityType<?>, AttributeSupplier.Builder> attributes) {

    public static final EventHandler<RegisterEntityAttributesEvent> EVENT = new EventHandler<>();

    public void register(EntityType<?> entityType, AttributeSupplier.Builder builder) {
        attributes.put(entityType, builder);
    }

}
