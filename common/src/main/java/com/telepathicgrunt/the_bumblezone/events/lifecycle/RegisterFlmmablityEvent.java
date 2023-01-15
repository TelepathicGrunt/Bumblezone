package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

public record RegisterFlmmablityEvent(TriConsumer<Block, Integer, Integer> registrar) {

    public static final EventHandler<RegisterFlmmablityEvent> EVENT = new EventHandler<>();

    public void register(Block block, int fireSpreadSpeed, int flammability) {
        registrar.accept(block, fireSpreadSpeed, flammability);
    }
}
