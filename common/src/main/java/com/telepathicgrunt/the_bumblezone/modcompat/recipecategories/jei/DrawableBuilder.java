package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.resources.ResourceLocation;

public class DrawableBuilder implements IDrawableBuilder {
    private final ResourceLocation resourceLocation;
    private int u;
    private int v;
    private int width;
    private int height;
    private int textureWidth = 256;
    private int textureHeight = 256;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;

    public DrawableBuilder(ResourceLocation resourceLocation, int u, int v, int width, int height) {
        assert resourceLocation != null;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.resourceLocation = resourceLocation;
    }

    @Override
    public IDrawableBuilder setTextureSize(int width, int height) {
        this.textureWidth = width;
        this.textureHeight = height;
        return this;
    }

    @Override
    public IDrawableBuilder addPadding(int paddingTop, int paddingBottom, int paddingLeft, int paddingRight) {
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        return this;
    }

    @Override
    public IDrawableBuilder trim(int trimTop, int trimBottom, int trimLeft, int trimRight) {
        this.u += trimLeft;
        this.v += trimTop;
        this.width -= trimLeft + trimRight;
        this.height -= trimTop + trimBottom;
        return this;
    }

    @Override
    public IDrawableStatic build() {
        return new DrawableResource(resourceLocation, u, v, width, height, paddingTop, paddingBottom, paddingLeft, paddingRight, textureWidth, textureHeight);
    }

    @Override
    public IDrawableAnimated buildAnimated(int ticksPerCycle, IDrawableAnimated.StartDirection startDirection, boolean inverted) {
        IDrawableStatic drawable = build();
        return new DrawableAnimated(drawable);
    }

    @Override
    public IDrawableAnimated buildAnimated(ITickTimer tickTimer, IDrawableAnimated.StartDirection startDirection) {
        IDrawableStatic drawable = build();
        return new DrawableAnimated(drawable);
    }
}

