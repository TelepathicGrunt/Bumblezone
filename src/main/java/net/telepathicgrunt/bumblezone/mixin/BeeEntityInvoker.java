package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.class)
public interface BeeEntityInvoker {

    @Invoker("setBeeFlag")
    void bz_callSetBeeFlag(int bit, boolean value);
}
