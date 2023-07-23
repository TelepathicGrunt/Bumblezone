package com.telepathicgrunt.the_bumblezone.mixins.forge.effect;

import com.telepathicgrunt.the_bumblezone.platform.EffectExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EffectExtension.class)
public interface EffectExtensionMixin extends IForgeMobEffect {

    @Shadow
    List<ItemStack> bz$getCurativeItems();

    @Override
    default List<ItemStack> getCurativeItems() {
        return this.bz$getCurativeItems();
    }

}
