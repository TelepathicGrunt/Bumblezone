package com.telepathicgrunt.the_bumblezone.client.bakemodel;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FabricModelProvider implements ModelVariantProvider {

    private static final ResourceLocation POROUS = new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block");
    private static final ResourceLocation BROOD = new ResourceLocation(Bumblezone.MODID, "empty_honeycomb_brood_block");

    @Override
    public @Nullable UnbakedModel loadModelVariant(ModelResourceLocation modelId, ModelProviderContext context) {
        if (POROUS.equals(modelId)) {
            return new FabricUnbakedConnectedModel(Map.of(
                "base", "the_bumblezone:block/porous_honeycomb_block",
                "particle", "the_bumblezone:block/porous_honeycomb_block",
                "bottom_left", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/bottom_left",
                "bottom_right", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/bottom_right",
                "top_left", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/top_left",
                "top_right", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/top_right"
            ), state -> state.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get()));
        } else if (BROOD.equals(modelId)) {
            return new FabricUnbakedConnectedModel(Map.of(
                    "base", "the_bumblezone:block/honeycomb_brood_block/empty_honeycomb_brood_block_side",
                    "front", "the_bumblezone:block/honeycomb_brood_block/empty_honeycomb_brood_block",
                    "particle", "the_bumblezone:block/porous_honeycomb_block",
                    "bottom_left", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/bottom_left",
                    "bottom_right", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/bottom_right",
                    "top_left", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/top_left",
                    "top_right", "the_bumblezone:block/porous_honeycomb_block_corner_overlays/top_right"
            ), state -> state.is(BzBlocks.HONEYCOMB_BROOD.get()));
        }
        return null;
    }
}
