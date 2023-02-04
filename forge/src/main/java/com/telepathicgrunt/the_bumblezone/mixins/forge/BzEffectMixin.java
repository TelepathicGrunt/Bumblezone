package com.telepathicgrunt.the_bumblezone.mixins.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.client.rendering.MobEffectRenderer;
import com.telepathicgrunt.the_bumblezone.effects.BzEffect;
import com.telepathicgrunt.the_bumblezone.utils.LazySupplier;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(BzEffect.class)
public class BzEffectMixin extends MobEffect {

    protected BzEffectMixin(MobEffectCategory arg, int i) {
        super(arg, i);
    }

    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        final LazySupplier<MobEffectRenderer> renderer = LazySupplier.of(() -> MobEffectRenderer.RENDERERS.get(this));
        consumer.accept(new IClientMobEffectExtensions() {

            @Override
            public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, PoseStack poseStack, int x, int y, float z, float alpha) {
                return renderer.getOptional().map(r -> r.renderGuiIcon(instance, gui, poseStack, x, y, z, alpha)).orElse(false);
            }
        });
    }
}
