package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.client;

import com.telepathicgrunt.the_bumblezone.client.bakemodel.LoaderModelManager;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {

    @Shadow @Final public static ModelResourceLocation MISSING_MODEL_LOCATION;

    @Shadow @Final private Map<ResourceLocation, List<ModelBakery.LoadedJson>> blockStateResources;

    @Inject(method = "loadTopLevel", at = @At("HEAD"))
    private void bumblezone$onStart(ModelResourceLocation id, CallbackInfo ci) {
        if (id.equals(MISSING_MODEL_LOCATION)) { //Means start because this is the first thing loaded after the lists and maps are set.
            LoaderModelManager.setGetter(model -> this.blockStateResources.get(model));
        }
    }

}
