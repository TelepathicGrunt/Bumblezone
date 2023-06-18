package com.telepathicgrunt.the_bumblezone.client.bakedmodel;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class EmptyHoneycombBroodLoader implements IGeometryLoader<EmptyHoneycombBroodLoader.EmptyHoneycombBroodModel>
{
    @Override
    public EmptyHoneycombBroodModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException
    {
        final JsonObject tex = json.getAsJsonObject("textures");
        return new EmptyHoneycombBroodModel(getID(tex, "bottom_left"), getID(tex, "bottom_right"), getID(tex, "top_left"), getID(tex, "top_right"), getID(tex, "particle"), getID(tex, "side"), getID(tex, "front"));
    }

    public record EmptyHoneycombBroodModel(ResourceLocation botLeft, ResourceLocation botRight, ResourceLocation topLeft, ResourceLocation topRight, ResourceLocation particle, ResourceLocation side, ResourceLocation front) implements IUnbakedGeometry<EmptyHoneycombBroodModel>
    {
        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation)
        {
            return new EmptyHoneycombBroodBlockModel(modelLocation, botLeft, botRight, topLeft, topRight, particle, side, front, spriteGetter);
        }

        @Override
        public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
        {
            return makeMaterials(botLeft, botRight, topLeft, topRight, particle, side, front);
        }
    }

    private static Collection<Material> makeMaterials(ResourceLocation... textures)
    {
        return Arrays.stream(textures).map(texture -> new Material(PorousHoneycombBlockModel.BLOCK_ATLAS, texture)).toList();
    }

    public static ResourceLocation getID(JsonObject json, String member)
    {
        return new ResourceLocation(GsonHelper.getAsString(json, member));
    }
}
