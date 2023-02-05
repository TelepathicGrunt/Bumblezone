package com.telepathicgrunt.the_bumblezone.mixin.quilt.recipes;

import com.telepathicgrunt.the_bumblezone.items.recipes.BzRecipeSerializer;
import net.minecraft.world.item.crafting.Recipe;
import org.quiltmc.qsl.recipe.api.serializer.QuiltRecipeSerializer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BzRecipeSerializer.class)
public interface BzRecipeSerializerMixin<T extends Recipe<?>> extends QuiltRecipeSerializer<T> { }
