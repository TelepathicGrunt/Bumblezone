package com.telepathicgrunt.the_bumblezone.client.rendering.transparentitem;

import com.mojang.blaze3d.vertex.VertexConsumer;

// Special thanks to source: https://github.com/SlimeKnights/TinkersConstruct/blob/86c1af90cfa79c37e7992b08aafefcd3f4afac41/src/main/java/slimeknights/tconstruct/smeltery/client/util/TintedVertexBuilder.java#L10
public class TintedVertexBuilder implements VertexConsumer {
    /** Base vertex builder */
    private final VertexConsumer inner;
    /** Tint color from 0-255 */
    private final int tintRed, tintGreen, tintBlue, tintAlpha;

    public TintedVertexBuilder(VertexConsumer inner, int tintRed, int tintGreen, int tintBlue, int tintAlpha) {
        this.inner = inner;
        this.tintRed = tintRed;
        this.tintGreen = tintGreen;
        this.tintBlue = tintBlue;
        this.tintAlpha = tintAlpha;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return inner.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return inner.color((red * tintRed) / 0xFF, (green * tintGreen) / 0xFF, (blue * tintBlue) / 0xFF, (alpha * tintAlpha) / 0xFF);
    }

    @Override
    public void defaultColor(int red, int green, int blue, int alpha) {
        inner.defaultColor((red * tintRed) / 0xFF, (green * tintGreen) / 0xFF, (blue * tintBlue) / 0xFF, (alpha * tintAlpha) / 0xFF);
    }

    @Override
    public void unsetDefaultColor() {
        inner.unsetDefaultColor();
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return inner.uv(u, v);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return inner.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        return inner.uv2(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return inner.normal(x, y, z);
    }

    @Override
    public void endVertex() {
        inner.endVertex();
    }
}