package com.telepathicgrunt.the_bumblezone.mixins.neoforge.effect;

import com.telepathicgrunt.the_bumblezone.platform.EffectExtension;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(EffectExtension.class)
public interface EffectExtensionMixin extends IMobEffectExtension {

    @Shadow
    List<ItemStack> bz$getCurativeItems();

    @Override
    default List<ItemStack> getCurativeItems() {
        return this.bz$getCurativeItems();
    }

}
