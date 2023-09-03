package com.telepathicgrunt.the_bumblezone.mixins.forge.item;

import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmetMixinTarget;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.forge.CuriosCompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlowerHeadwearHelmetMixinTarget.class)
public interface FlowerHeadwearHelmetMixin extends IForgeItem {

    @Override
    default ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        if (ModChecker.curiosPresent) {
            return CuriosCompat.getCurioCapForFlowerHeadwear(stack);
        }
        return null;
    }
}
