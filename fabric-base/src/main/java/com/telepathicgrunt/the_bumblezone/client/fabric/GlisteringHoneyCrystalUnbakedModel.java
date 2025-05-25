package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.google.common.collect.ImmutableSet;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;

public record GlisteringHoneyCrystalUnbakedModel(BlockModel mainModel, ResourceLocation insideModelId, ResourceLocation outsideModelId) implements UnbakedModel {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.addAll(mainModel.getDependencies());
        builder.add(insideModelId);
        builder.add(outsideModelId);
        return builder.build();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
        mainModel.resolveParents(resolver);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        BakedModel bakedMainModel = mainModel.bake(modelBaker, function, modelState, resourceLocation);

        BakedModel insideModel = modelBaker.bake(insideModelId, modelState);
        BakedModel outsideModel = modelBaker.bake(outsideModelId, modelState);

        if(bakedMainModel instanceof SimpleBakedModel simpleBakedModel) {
            return new GlisteringHoneyCrystalBakedModel(simpleBakedModel, insideModel, outsideModel);
        }

        Bumblezone.LOGGER.error("unable to bake model! expected SimpleBakedModel, got {}", bakedMainModel.getClass());
        return outsideModel;
    }
}
