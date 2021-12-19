package com.telepathicgrunt.the_bumblezone.mixin.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SimpleCookingSerializer.class)
public class SimpleCookingSerializerMixin<T extends AbstractCookingRecipe> {

    @Shadow @Final
    private int defaultCookingTime;

    @Shadow @Final
    private SimpleCookingSerializer.CookieBaker<T> factory;

    @Inject(method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/GsonHelper;getAsString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private void thebumblezone_checkForCountSize(ResourceLocation identifier, JsonObject jsonObject, CallbackInfoReturnable<T> cir, String string, JsonElement jsonElement, Ingredient ingredient) {
        if (!jsonObject.has("result")) {
            throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        }
        ItemStack itemstack;
        if (jsonObject.get("result").isJsonObject()) {
            JsonObject jsonObj2 = GsonHelper.getAsJsonObject(jsonObject, "result");
            itemstack = ShapedRecipe.itemStackFromJson(jsonObj2);
            float f = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
            int i = GsonHelper.getAsInt(jsonObject, "cookingtime", this.defaultCookingTime);
            cir.setReturnValue(this.factory.create(identifier, string, ingredient, itemstack, f, i));
        }
    }
}