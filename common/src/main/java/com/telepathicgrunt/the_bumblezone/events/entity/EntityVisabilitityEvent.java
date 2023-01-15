package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityVisabilitityEvent {

    public static final EventHandler<EntityVisabilitityEvent> EVENT = new EventHandler<>();

    private double visibility;
    @Nullable
    private final Entity watcher;
    private final LivingEntity entity;

    public EntityVisabilitityEvent(double visibility, @Nullable Entity watcher, LivingEntity entity) {
        this.visibility = visibility;
        this.watcher = watcher;
        this.entity = entity;
    }

    public double visibility() {
        return visibility;
    }

    public void modify(double visibility) {
        this.visibility *= visibility;
    }

    @Nullable
    public Entity watcher() {
        return watcher;
    }

    public LivingEntity entity() {
        return entity;
    }


}
