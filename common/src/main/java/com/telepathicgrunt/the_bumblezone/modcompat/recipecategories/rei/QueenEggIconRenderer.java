package com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics;
import net.minecraft.resources.Identifier;

public class QueenEggIconRenderer extends DisplayRenderer {

	private final Identifier texture;

	public QueenEggIconRenderer(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
		graphics.pose().pushMatrix();
		graphics.blit(this.texture,
				bounds.getCenterX() - 8,
				bounds.getCenterY() - 8,
				bounds.getCenterX() + 8,
				bounds.getCenterY() + 8,
				0,
				0,
				16,
				16);
		graphics.pose().popMatrix();
	}

	@Override
	public int getHeight() {
		return 16;
	}
}
