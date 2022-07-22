package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @ModifyArgs(method = "burn(Lnet/minecraft/world/item/crafting/Recipe;Lnet/minecraft/core/NonNullList;I)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V", ordinal = 0))
    private static void thebumblezone_fillWithStackCount(Args args, Recipe<?> recipe, NonNullList<ItemStack> slots, int count) {
        args.set(0, recipe.getResultItem().getCount());
    }
}