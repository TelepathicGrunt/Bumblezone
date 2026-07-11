package com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class BeehemothRenderState extends LivingEntityRenderState {

	public boolean shouldRenderFriendshipProgress;
	public int friendship;

	public boolean saddled;
	public boolean queen;
	public boolean onGround;
	public boolean sitting;
	public double xzSpeed;

	public float[] kneeOffsets = new float[6];
}
