package com.telepathicgrunt.the_bumblezone.client.rendering.rootmin;

import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class RootminRenderState extends LivingEntityRenderState {

	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState angryAnimationState = new AnimationState();
	public final AnimationState curiousAnimationState = new AnimationState();
	public final AnimationState curseAnimationState = new AnimationState();
	public final AnimationState embarassedAnimationState = new AnimationState();
	public final AnimationState shockAnimationState = new AnimationState();
	public final AnimationState shootAnimationState = new AnimationState();
	public final AnimationState runAnimationState = new AnimationState();
	public final AnimationState walkAnimationState = new AnimationState();
	public final AnimationState blockToEntityAnimationState = new AnimationState();
	public final AnimationState entityToBlockAnimationState = new AnimationState();

	public final BlockModelRenderState flower = new BlockModelRenderState();
	public int flowerLightCoords;
	public final BlockModelRenderState tallFlowerHalf = new BlockModelRenderState();
	public int doubleFlowerLightCoords;

	public RootminState pose = RootminState.NONE;
	public float hideTransitionPercentage;
	public boolean isJebRootmin;
	public int grassColor;
	public boolean shielded;

	public int getGrassColor() {
		return this.isJebRootmin ? ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, this.ageInTicks) : this.grassColor;
	}
}
