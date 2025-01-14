package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public class GlisteringHoneyCrystalCompositeModel {

    public static void setupCompositeMode() {

        ModelLoadingPlugin.register(pluginContext -> {

            ResourceLocation model1RL = ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "block/glistering_honey_crystal/glistering_honey_crystal_1");
            pluginContext.modifyModelOnLoad().register(ModelModifier.OVERRIDE_PHASE, (model, context) -> {
                ResourceLocation id = context.resourceId();

                if (id != null && id.equals(model1RL)) {
                    return crystalModel(context, model1RL);
                }

                return model;
            });
        });
    }

    private static UnbakedModel crystalModel(ModelModifier.OnLoad.Context context, ResourceLocation model) {
        UnbakedModel insideModel = context.getOrLoadModel(ResourceLocation.fromNamespaceAndPath(model.getNamespace(), model.getPath() + "_inside"));
        UnbakedModel outsideModel = context.getOrLoadModel(ResourceLocation.fromNamespaceAndPath(model.getNamespace(), model.getPath() + "_outside"));

        return new Something();
    }
}
