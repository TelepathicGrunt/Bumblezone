package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobRenderer.class)
public interface MobRendererAccessor {
    @Invoker("shouldShowName")
    <T extends Mob, M extends EntityModel<T>> boolean callShouldShowName(T entity);
}
