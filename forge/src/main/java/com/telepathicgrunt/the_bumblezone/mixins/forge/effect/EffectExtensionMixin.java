package com.telepathicgrunt.the_bumblezone.mixins.forge.effect;

import com.telepathicgrunt.the_bumblezone.platform.EffectExtension;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import org.jetbrains.annotations.Nullable;
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
