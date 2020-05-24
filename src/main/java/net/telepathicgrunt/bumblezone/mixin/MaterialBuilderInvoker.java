package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.block.Material;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Material.Builder.class)
public interface MaterialBuilderInvoker {

    @Invoker("destroyedByPiston")
    void callDestroyedByPiston();

    @Invoker("lightPassesThrough")
    void callLightPassesThrough();
}
