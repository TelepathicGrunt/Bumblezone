package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.stream.IntStream;

public class GlisteringHoneyCrystalModels {

    public static void setupModels() {
        ModelLoadingPlugin.register(pluginContext -> {
            List<Identifier> blockModelIds = IntStream.rangeClosed(1, 16).mapToObj(idx -> Identifier.fromNamespaceAndPath(Bumblezone.MODID, "block/glistering_honey_crystal/glistering_honey_crystal_" + idx)).toList();
            Identifier itemModelId = ModelLocationUtils.getModelLocation(BzItems.GLISTERING_HONEY_CRYSTAL.get());

            // tell Minecraft to load the additional models
            blockModelIds.forEach(id -> pluginContext.addModels(id.withSuffix("_inside"), id.withSuffix("_outside")));

            // swap base model for composite
            // also need to swap item model because it cannot resolve the parent otherwise
            pluginContext.modifyModelOnLoad().register(ModelModifier.OVERRIDE_PHASE, (model, context) -> {
                Identifier loadedModelId = context.resourceId();

                if (loadedModelId != null) {
                    if (itemModelId.equals(loadedModelId)) {
                        return context.getOrLoadModel(blockModelIds.getFirst());
                    }

                    if (blockModelIds.contains(loadedModelId)) {
                        Identifier insideModelId = loadedModelId.withSuffix("_inside");
                        Identifier outsideModelId = loadedModelId.withSuffix("_outside");
                        if (model instanceof BlockModel blockModel) {
                            return new GlisteringHoneyCrystalUnbakedModel(blockModel, insideModelId, outsideModelId);
                        } else {
                            Bumblezone.LOGGER.error("unable to bake model {}, expected BlockModel, got {}", loadedModelId, model.getClass());
                            return context.getOrLoadModel(outsideModelId); // fall back to full block model
                        }
                    }
                }

                return model;
            });
        });
    }
}
