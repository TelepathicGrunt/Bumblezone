package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.level.Level;

public record ServerLevelTickEvent(Level level, boolean end) {

    public static final EventHandler<ServerLevelTickEvent> EVENT = new EventHandler<>();

    public Level getLevel() {
        return level;
    }
}
