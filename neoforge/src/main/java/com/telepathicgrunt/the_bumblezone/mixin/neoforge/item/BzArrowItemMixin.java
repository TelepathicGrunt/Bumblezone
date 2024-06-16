package com.telepathicgrunt.the_bumblezone.mixin.neoforge.item;

import com.telepathicgrunt.the_bumblezone.platform.BzArrowItem;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BzArrowItem.class)
public class BzArrowItemMixin extends ArrowItem {

    public BzArrowItemMixin(Properties arg) {
        super(arg);
    }

    @Shadow
    public OptionalBoolean bz$isInfinite(ItemStack stack, ItemStack bow, LivingEntity player) {
        throw new RuntimeException();
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        OptionalBoolean infiniteResult = bz$isInfinite(stack, bow, player);
        if (infiniteResult.isPresent()) {
            return infiniteResult.get();
        }
        return super.isInfinite(stack, bow, player);
    }
}
