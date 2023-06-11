package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.jei;

import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.client.gui.GuiGraphics;

public class DrawableAnimated implements IDrawableAnimated {
    private final IDrawableStatic drawable;

    public DrawableAnimated(IDrawableStatic drawable) {
        this.drawable = drawable;
    }

    @Override
    public int getWidth() {
        return drawable.getWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getHeight();
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        int maskLeft = 0;
        int maskRight = 0;
        int maskTop = 0;
        int maskBottom = 0;

        drawable.draw(guiGraphics, xOffset, yOffset, maskTop, maskBottom, maskLeft, maskRight);
    }
}
