package com.telepathicgrunt.the_bumblezone.client.shaders;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.client.RenderPipelinesAccessor;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public class EssenceBlockShader {
    public static void initShaderSetup(){}

    public static final Identifier BASE_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/block/essence/base_background.png");
    public static final Identifier BEE_TEXTURE = Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/block/essence/bee_icon_background.png");

    public static final RenderPipeline.Snippet BUMBLEZONE_ESSENCE_SNIPPET =
            RenderPipeline.builder(
                RenderPipeline.builder().withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER).withUniform("Projection", UniformType.UNIFORM_BUFFER).buildSnippet(),
                RenderPipeline.builder().withUniform("Fog", UniformType.UNIFORM_BUFFER).buildSnippet(),
                RenderPipeline.builder().withUniform("Globals", UniformType.UNIFORM_BUFFER).buildSnippet()
            )
            .withVertexShader(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "core/rendertype_bumblezone_essence"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "core/rendertype_bumblezone_essence"))
            .withSampler("Sampler0")
            .withSampler("Sampler1")
            .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .buildSnippet();

    public static final RenderPipeline BUMBLEZONE_ESSENCE_PIPELINE = registerPipeline(
            RenderPipeline.builder(BUMBLEZONE_ESSENCE_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "pipeline/bumblezone_essence"))
                    .withShaderDefine("LAYERS", 5).build()
    );

    public static final RenderType BUMBLEZONE_ESSENCE_RENDERTYPE = RenderType.create(
            "bumblezone_essence",
            RenderSetup.builder(BUMBLEZONE_ESSENCE_PIPELINE)
                    .withTexture("Sampler0", BASE_TEXTURE)
                    .withTexture("Sampler1", BEE_TEXTURE)
                    .createRenderSetup()
    );

    private static RenderPipeline registerPipeline(RenderPipeline pipeline) {
        RenderPipelinesAccessor.bumblezone$getPIPELINES_BY_LOCATION().put(pipeline.getLocation(), pipeline);
        return pipeline;
    }
}
