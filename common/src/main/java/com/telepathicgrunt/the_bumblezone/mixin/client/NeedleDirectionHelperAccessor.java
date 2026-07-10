package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NeedleDirectionHelper.class)
public interface NeedleDirectionHelperAccessor {
    @Invoker("wobble")
    boolean bumblezone$callWobble();
}
