package net.telepathicgrunt.bumblezone.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.items.HoneyCrystalShieldBehavior;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CookingRecipeSerializer.class)
public class CookingRecipeSerializerMixin<T extends AbstractCookingRecipe> {

    @Shadow @Final private int cookingTime;
    @Shadow @Final private CookingRecipeSerializer.RecipeFactory<T> recipeFactory;

    @Inject(method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/JsonHelper;getString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void checkForCountSize(Identifier identifier, JsonObject jsonObject, CallbackInfoReturnable<T> cir, String string, JsonElement jsonElement, Ingredient ingredient) {
        if (!jsonObject.has("result")) {
            throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        }
        ItemStack itemstack;
        if (jsonObject.get("result").isJsonObject()) {
            JsonObject jsonObj2 = JsonHelper.getObject(jsonObject, "result");
            itemstack = ShapedRecipe.getItemStack(jsonObj2);
            float f = JsonHelper.getFloat(jsonObject, "experience", 0.0F);
            int i = JsonHelper.getInt(jsonObject, "cookingtime", this.cookingTime);
            cir.setReturnValue(this.recipeFactory.create(identifier, string, ingredient, itemstack, f, i));
        }
    }

}