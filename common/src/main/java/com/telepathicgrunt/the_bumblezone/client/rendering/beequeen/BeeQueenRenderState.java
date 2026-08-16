package com.telepathicgrunt.the_bumblezone.client.rendering.beequeen;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BeeQueenRenderState extends LivingEntityRenderState {
	public final AnimationState attackAnimationState = new AnimationState();
	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState itemThrownAnimationState = new AnimationState();
	public final AnimationState itemRejectAnimationState = new AnimationState();

	public boolean angry;
	public ItemStackRenderState wantItem = new ItemStackRenderState();
	public List<ItemStackRenderState> slicedRewardItems = new ArrayList<>();
	public int tradeHintTimeRemaining;
	public float animationProgress;
}
