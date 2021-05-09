package com.telepathicgrunt.the_bumblezone.mixin.items;

import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Material.Builder.class)
public interface MaterialInvoker {

    @Invoker("notSolidBlocking")
    Material.Builder bz_getNotSolidBlocking();

    @Invoker("destroyOnPush")
    Material.Builder bz_getDestroyOnPush();

}
