package com.telepathicgrunt.the_bumblezone.client.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel.Texture;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.EnumMap;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ForgeConnectedModelLoader implements IGeometryLoader<ForgeConnectedModelLoader.GeometryModel> {

    @Override
    public GeometryModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        Set<Block> blocks = getBlocks(json);
        return new GeometryModel(makeMaterials(GsonHelper.getAsJsonObject(json, "textures")), state -> blocks.contains(state.getBlock()));
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

    public record GeometryModel(EnumMap<Texture, Material> textures, Predicate<BlockState> predicate) implements IUnbakedGeometry<GeometryModel> {

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> function, ModelState state, ItemOverrides overrides, ResourceLocation id) {
            return new ForgeConnectedBlockModel(id, textures, predicate);
        }
    }

    private static EnumMap<Texture, Material> makeMaterials(JsonObject json) {
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
