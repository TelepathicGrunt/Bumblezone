package com.telepathicgrunt.the_bumblezone.mixin.fabricbase;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SimpleCookingSerializer.class)
public class SimpleCookingSerializerMixin {

    @WrapOperation(
            method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/GsonHelper;getAsString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;"
            )
    )
    private String bumblezone$getItemId(JsonObject json, String string, Operation<String> operation) {
        if (json.get(string).isJsonObject()) {
            return GsonHelper.getAsString(json.get(string).getAsJsonObject(), "item");
        }
        return operation.call(json, string);
    }

    @ModifyVariable(
            method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lnet/minecraft/world/item/crafting/AbstractCookingRecipe;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/GsonHelper;getAsFloat(Lcom/google/gson/JsonObject;Ljava/lang/String;F)F"
            )
    )
    private ItemStack bumblezone$createItemStack(ItemStack stack, ResourceLocation id, JsonObject json) {
        if (json.get("result").isJsonObject()) {
            JsonObject result = json.get("result").getAsJsonObject();
            stack.setCount(GsonHelper.getAsInt(result, "count", 1));
        }
        return stack;
    }
}
