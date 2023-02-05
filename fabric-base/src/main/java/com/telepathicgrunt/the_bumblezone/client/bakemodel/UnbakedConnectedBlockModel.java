package com.telepathicgrunt.the_bumblezone.client.bakemodel;

import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel.Texture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
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

public class UnbakedConnectedBlockModel implements UnbakedModel {

    private final Predicate<BlockState> predicate;
    private final Map<Texture, Material> textures;

    public UnbakedConnectedBlockModel(Map<Texture, Material> textures, Predicate<BlockState> predicate) {
        this.predicate = predicate;
        this.textures = textures;
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
        Map<Texture, TextureAtlasSprite> bakedTextures = new HashMap<>();
        textures.forEach((key, value) -> bakedTextures.put(key, function.apply(value)));
        return new BakedConnectedBlockModel(bakedTextures, predicate);
    }
}
