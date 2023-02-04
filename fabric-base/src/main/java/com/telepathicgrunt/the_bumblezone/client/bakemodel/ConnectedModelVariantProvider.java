package com.telepathicgrunt.the_bumblezone.client.bakemodel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel.Texture;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Set;

public class ConnectedModelVariantProvider implements ModelVariantProvider {

    private static final ResourceLocation CONNECTED_BLOCK = new ResourceLocation(Bumblezone.MODID, "connected_block");

    @Override
    public @Nullable UnbakedModel loadModelVariant(ModelResourceLocation modelId, ModelProviderContext context) {
        if ("inventory".equals(modelId.getVariant())) return null;
        JsonObject data = LoaderModelManager.getData(CONNECTED_BLOCK, modelId);
        if (data == null) return null;
        EnumMap<Texture, Material> textures = getMaterials(data);
        Set<Block> blocks = getBlocks(data);
        return new UnbakedConnectedBlockModel(textures, state -> blocks.contains(state.getBlock()));
    }

    private static Set<Block> getBlocks(JsonObject json) {
        if (json.has("block")) {
            if (json.get("block") instanceof JsonArray array) {
                Set<Block> blocks = new ObjectOpenHashSet<>(array.size());
                for (int i = 0; i < array.size(); i++) {
                    blocks.add(BuiltInRegistries.BLOCK.get(new ResourceLocation(array.get(i).getAsString())));
                }
                return blocks;
            }
            return Set.of(BuiltInRegistries.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(json, "block"))));
        }
        throw new JsonParseException("Missing block, expected to find a string or a list of strings");
    }

    private static EnumMap<Texture, Material> getMaterials(JsonObject data) {
        JsonObject json = GsonHelper.getAsJsonObject(data, "textures");
        EnumMap<Texture, Material> map = new EnumMap<>(Texture.class);
        for (String key : json.keySet()) {
            Texture.tryParse(key)
                    .ifPresent(connection -> map.put(connection, create(json.get(key).getAsString())));
        }
        return map;
    }

    private static Material create(String id) {
        return new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(id));
    }
}
