package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BeeQueenRenderState extends LivingEntityRenderState {
	public final AnimationState attackAnimationState = new AnimationState();
	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState itemThrownAnimationState = new AnimationState();
	public final AnimationState itemRejectAnimationState = new AnimationState();

	public boolean angry;
}
