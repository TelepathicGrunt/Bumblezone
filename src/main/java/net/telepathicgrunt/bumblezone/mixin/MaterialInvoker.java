package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Material.Builder.class)
public interface MaterialInvoker {

    @Invoker("notOpaque")
    Material.Builder getNotOpaque();

    @Invoker("pushDestroys")
    Material.Builder getPushDestroys();

}
