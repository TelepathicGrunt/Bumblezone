package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeeEntity.class)
public interface BeeEntityAccessor {

    @Accessor
    void setBeeFlag(int bit, boolean value);
}
