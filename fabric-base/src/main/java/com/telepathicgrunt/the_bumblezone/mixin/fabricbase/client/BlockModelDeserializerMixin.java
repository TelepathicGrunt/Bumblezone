package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.client.bakemodel.LoaderModelManager;
import com.telepathicgrunt.the_bumblezone.hooks.fabricbase.BlockModelHook;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Type;

@Mixin(BlockModel.Deserializer.class)
public class BlockModelDeserializerMixin {

    @ModifyReturnValue(
        method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/BlockModel;",
        at = @At("RETURN")
    )
    private BlockModel bumblezone$onLoadModel(BlockModel model, JsonElement json, Type type, JsonDeserializationContext context) {
        if (model instanceof BlockModelHook hook && json instanceof JsonObject object && object.has("loader")) {
            JsonElement element = object.get("loader");
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                ResourceLocation id = new ResourceLocation(GsonHelper.convertToString(element, "loader"));
                if (LoaderModelManager.hasType(id)) {
                    hook.bz$setModelData(id, object);
                }
            }
        }
        return model;
    }
}
