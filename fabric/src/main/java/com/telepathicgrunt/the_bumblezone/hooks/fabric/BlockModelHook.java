package com.telepathicgrunt.the_bumblezone.hooks.fabric;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;

public interface BlockModelHook {

    void bz$setModelData(Identifier modelType, JsonObject data);

    Identifier bz$getModelType();

    JsonObject bz$getModelData();

    default boolean bz$hasModelData() {
        return bz$getModelData() != null && bz$getModelType() != null;
    }
}
