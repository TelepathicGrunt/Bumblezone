package com.telepathicgrunt.the_bumblezone.events.entity;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityVisibilityEvent {

    public static final EventHandler<EntityVisibilityEvent> EVENT = new EventHandler<>();

    private double visibility;
    @Nullable
    private final Entity watcher;
    private final LivingEntity entity;

    public EntityVisibilityEvent(double visibility, LivingEntity entity, @Nullable Entity watcher) {
        this.visibility = visibility;
        this.entity = entity;
        this.watcher = watcher;
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
