package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.level.block.Block;

public record BzRegisterFlammabilityEvent(Registrar registrar) {

    public static final EventHandler<BzRegisterFlammabilityEvent> EVENT = new EventHandler<>();

    public void register(Block block, int igniteOdds, int burnOdds) {
        registrar.register(block, igniteOdds, burnOdds);
    }

    @FunctionalInterface
    public interface Registrar {
        void register(Block block, int igniteOdds, int burnOdds);
    }
}
