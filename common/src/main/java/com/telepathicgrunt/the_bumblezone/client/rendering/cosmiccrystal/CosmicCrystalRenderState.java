package com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;

public class CosmicCrystalRenderState extends LivingEntityRenderState {

	public final AnimationState idleAnimationState = new AnimationState();
	public CosmicCrystalState crystalState = CosmicCrystalState.NORMAL;
	public boolean firing;
	public boolean shielded;
	public int currentStateTimeTick;
	public int laserStartDelay;
	public int laserFireStartTime;

	public float health;
	public boolean onFire;
	public boolean freezing;
	public float currentHealthState;
	public HashSet<Holder<MobEffect>> activeEffects;
	public Vec3 pos;

	public LaserInfo laser = new LaserInfo();

	public static class LaserInfo {
		public long uniqueValue;
		public Vec3 lookAngle;
		public float laserLength;
		public Vec3 vecToTarget;
		public Pair<Vec3, Vec3> positions;
	}
}
