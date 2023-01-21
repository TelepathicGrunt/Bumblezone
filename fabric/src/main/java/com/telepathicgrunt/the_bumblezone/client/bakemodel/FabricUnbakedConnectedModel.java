package com.telepathicgrunt.the_bumblezone.client.bakemodel;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class FabricUnbakedConnectedModel implements UnbakedModel {

    private final Predicate<BlockState> predicate;
    private final Map<String, Material> textures;

    public FabricUnbakedConnectedModel(Map<String, String> textures, Predicate<BlockState> predicate) {
        this.predicate = predicate;
        this.textures = new HashMap<>();
        textures.forEach((key, value) -> this.textures.put(key, new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(value))));
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of();
    }

    @Override
    public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> function) {

    }

    @Nullable
    @Override
    public BakedModel bake(@NotNull ModelBaker baker, @NotNull Function<Material, TextureAtlasSprite> function, @NotNull ModelState modelState, @NotNull ResourceLocation resourceLocation) {
        Map<String, TextureAtlasSprite> bakedTextures = new HashMap<>();
        textures.forEach((key, value) -> bakedTextures.put(key, function.apply(value)));
        return new FabricConnectedBlockModel(bakedTextures, predicate);
    }
}
