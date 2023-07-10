package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ClientLevel.class)
public interface ClientLevelAccessor {
    @Accessor("MARKER_PARTICLE_ITEMS")
    static Set<Item> getMARKER_PARTICLE_ITEMS() {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor("MARKER_PARTICLE_ITEMS")
    static void setMARKER_PARTICLE_ITEMS(Set<Item> MARKER_PARTICLE_ITEMS) {
        throw new UnsupportedOperationException();
    }
}
