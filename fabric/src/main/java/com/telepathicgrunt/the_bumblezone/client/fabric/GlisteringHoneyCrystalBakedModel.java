package com.telepathicgrunt.the_bumblezone.client.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.impl.renderer.VanillaModelEncoder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public record GlisteringHoneyCrystalBakedModel(BakedModel wrapped, BakedModel insideModel, BakedModel outsideModel) implements BakedModel, FabricBakedModel {

    private static final MaterialFinder materialFinder = Optional.ofNullable(RendererAccess.INSTANCE.getRenderer()).map(Renderer::materialFinder).orElse(null);
    static {
        if(materialFinder == null) {
            Bumblezone.LOGGER.error("No FRAPI Renderer found! GlisteringHoneyCrystalBakedModel will not work correctly!");
            if(FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("indium")) {
                Bumblezone.LOGGER.error("Found Sodium but not Indium! please try installing Indium before reporting this as a bug!");
            }
        }
    }

    @Override
    public boolean isVanillaAdapter() {
        return materialFinder == null;
    }

    /**
     * custom impl of {@link VanillaModelEncoder#emitBlockQuads(BakedModel, BlockState, Supplier, RenderContext)} that supports multiple render materials
     */
    private void emitBlockModelQuads(RenderContext context, BlockState state, Supplier<RandomSource> randomSupplier, BakedModel model, RenderMaterial renderMaterial) {
        QuadEmitter emitter = context.getEmitter();

        for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
            @Nullable Direction cullFace = ModelHelper.faceFromIndex(i);

            if (!context.hasTransform() && context.isFaceCulled(cullFace)) {
                // Skip entire quad list if possible.
                continue;
            }

            for (BakedQuad q : model.getQuads(state, cullFace, randomSupplier.get())) {
                emitter.fromVanilla(q, renderMaterial, cullFace);
                emitter.emit();
            }
        }
    }

    /**
     * custom impl of {@link VanillaModelEncoder#emitItemQuads(BakedModel, BlockState, Supplier, RenderContext)} that supports multiple render materials
     */
    private void emitItemModelQuads(RenderContext context, @Nullable BlockState state, Supplier<RandomSource> randomSupplier, BakedModel model, RenderMaterial renderMaterial) {
        QuadEmitter emitter = context.getEmitter();

        for (int i = 0; i <= ModelHelper.NULL_FACE_ID; i++) {
            @Nullable Direction cullFace = ModelHelper.faceFromIndex(i);

            for (final BakedQuad q : model.getQuads(state, cullFace, randomSupplier.get())) {
                emitter.fromVanilla(q, renderMaterial, cullFace);
                emitter.emit();
            }
        }
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        emitBlockModelQuads(context, state, randomSupplier, insideModel, materialFinder.blendMode(BlendMode.CUTOUT).find());
        emitBlockModelQuads(context, state, randomSupplier, outsideModel, materialFinder.blendMode(BlendMode.TRANSLUCENT).find());
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        emitItemModelQuads(context, null, randomSupplier, insideModel, materialFinder.blendMode(BlendMode.CUTOUT).find());
        emitItemModelQuads(context, null, randomSupplier, outsideModel, materialFinder.blendMode(BlendMode.TRANSLUCENT).find());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        // fallback to be used if no FRAPI implementation is found
        return outsideModel.getQuads(blockState, direction, randomSource);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return wrapped().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return wrapped().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return wrapped().usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return wrapped().isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return wrapped().getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return wrapped().getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return wrapped().getOverrides();
    }
}
