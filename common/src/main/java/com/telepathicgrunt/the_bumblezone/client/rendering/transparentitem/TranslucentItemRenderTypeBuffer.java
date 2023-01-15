package com.telepathicgrunt.the_bumblezone.client.rendering.transparentitem;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.mixin.client.RenderStateShardAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.client.TextureStateShardAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

// Credit to:https://github.com/SlimeKnights/TinkersConstruct/blob/1.18.2/src/main/java/slimeknights/tconstruct/smeltery/client/util/CastingItemRenderTypeBuffer.java
public class TranslucentItemRenderTypeBuffer implements MultiBufferSource {
    private static final Set<String> MAKE_TRANSPARENT = ImmutableSet.of("entity_solid", "entity_cutout", "entity_cutout_no_cull", "entity_translucent", "entity_no_outline");

    /** Base render type buffer */
    private final MultiBufferSource inner;
    private final int alpha;

    /**
     * Creates a new instance of this class
     * @param inner        Base render type buffer
     * @param alpha        Opacity of the item from 0 to 255. 255 is the end of the animation.
     */
    public TranslucentItemRenderTypeBuffer(MultiBufferSource inner, int alpha) {
        this.inner = inner;
        // alpha is a direct fade from 0 to 255
        this.alpha = Mth.clamp(alpha, 0, 0xFF);
    }

    @Override
    public VertexConsumer getBuffer(@NotNull RenderType type) {
        if (MAKE_TRANSPARENT.contains(((RenderStateShardAccessor)type).getName())) {
            //noinspection ConstantConditions For some reason intellij thinks this is not possible.
            RenderType.CompositeRenderType composite = type instanceof RenderType.CompositeRenderType comp ? comp : null;
            //noinspection ConstantConditions Same above.
            if (composite != null && composite.state().textureState instanceof RenderStateShard.TextureStateShard textureState) {
                ResourceLocation texture = ((TextureStateShardAccessor)textureState).getTexture().orElse(InventoryMenu.BLOCK_ATLAS);
                type = RenderType.entityTranslucentCull(texture);
            }
        }

        return new TintedVertexBuilder(inner.getBuffer(type), 0xFF, 0xFF, 0xFF, alpha);
    }
}