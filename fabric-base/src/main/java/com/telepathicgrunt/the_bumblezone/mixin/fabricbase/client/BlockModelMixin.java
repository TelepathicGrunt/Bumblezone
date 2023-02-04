package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.hooks.fabricbase.BlockModelHook;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockModel.class)
public class BlockModelMixin implements BlockModelHook {

    @Unique
    private ResourceLocation bz$modelType;
    @Unique
    private JsonObject bz$modelData;

    @Override
    public void bz$setModelData(ResourceLocation modelType, JsonObject data) {
        this.bz$modelType = modelType;
        this.bz$modelData = data;
    }

    @Override
    public ResourceLocation bz$getModelType() {
        return this.bz$modelType;
    }

    @Override
    public JsonObject bz$getModelData() {
        return this.bz$modelData;
    }
}
