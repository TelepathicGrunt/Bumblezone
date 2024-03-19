package com.telepathicgrunt.the_bumblezone.mixin.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AgeableMob.class)
public interface AgeableMobAccessor {
    @Accessor("DATA_BABY_ID")
    static EntityDataAccessor<Boolean> getDATA_BABY_ID() {
        throw new UnsupportedOperationException();
    }
}
