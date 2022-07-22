package com.telepathicgrunt.the_bumblezone.mixin.items;

import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Material.Builder.class)
public interface MaterialInvoker {

    @Invoker("notSolidBlocking")
    Material.Builder thebumblezone_getNotSolidBlocking();

    @Invoker("destroyOnPush")
    Material.Builder thebumblezone_getDestroyOnPush();
}
