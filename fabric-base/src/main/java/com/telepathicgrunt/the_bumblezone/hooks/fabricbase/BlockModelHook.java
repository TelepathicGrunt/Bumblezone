package com.telepathicgrunt.the_bumblezone.hooks.fabricbase;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface BlockModelHook {

    void bz$setModelData(ResourceLocation modelType, JsonObject data);

    ResourceLocation bz$getModelType();

    JsonObject bz$getModelData();

    default boolean bz$hasModelData() {
        return bz$getModelData() != null && bz$getModelType() != null;
    }
}
