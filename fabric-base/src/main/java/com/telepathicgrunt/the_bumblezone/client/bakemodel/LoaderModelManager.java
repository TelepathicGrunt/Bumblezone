package com.telepathicgrunt.the_bumblezone.client.bakemodel;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class LoaderModelManager {

    private static final Map<ResourceLocation, Map<ResourceLocation, JsonObject>> MODEL_RAW_DATA = new ConcurrentHashMap<>();
    private static Function<ResourceLocation, List<ModelBakery.LoadedJson>> getter;

    static {
        MODEL_RAW_DATA.put(new ResourceLocation(Bumblezone.MODID, "connected_block"), new ConcurrentHashMap<>());
    }

    public static boolean hasType(ResourceLocation type) {
        return MODEL_RAW_DATA.containsKey(type);
    }

    public static void addData(ResourceLocation type, ResourceLocation id, JsonObject data) {
        MODEL_RAW_DATA.computeIfPresent(type, (k, v) -> {
            v.put(id, data);
            return v;
        });
    }

    @Nullable
    public static JsonObject getData(ResourceLocation type, ResourceLocation id) {
        if (getter == null) return null;
        List<ModelBakery.LoadedJson> loadedJsons = getter.apply(new ResourceLocation(id.getNamespace(), "blockstates/" + id.getPath() + ".json"));
        if (loadedJsons == null || loadedJsons.size() != 1) return null;
        if (loadedJsons.get(0).data() instanceof JsonObject json) {
            if (json.get("variants") instanceof JsonObject variants) {
                if (variants.get("") instanceof JsonObject variant) {
                    String model = GsonHelper.getAsString(variant, "model", "");
                    return MODEL_RAW_DATA.get(type).get(ResourceLocation.tryParse(model));
                }
            }
        }
        return null;
    }

    @ApiStatus.Internal
    public static void clearStaleRawModelData() {
        MODEL_RAW_DATA.get(new ResourceLocation(Bumblezone.MODID, "connected_block")).clear();
    }

    @ApiStatus.Internal
    public static void setGetter(Function<ResourceLocation, List<ModelBakery.LoadedJson>> getter) {
        LoaderModelManager.getter = getter;
    }
}
