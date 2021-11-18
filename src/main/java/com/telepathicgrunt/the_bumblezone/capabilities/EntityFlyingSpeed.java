package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;


public class EntityFlyingSpeed implements IFlyingSpeed {
	private float originalFlyingSpeed = 0.02f;

	@Override
	public void setOriginalFlyingSpeed(float originalFlyingSpeed) {
		this.originalFlyingSpeed = originalFlyingSpeed;
	}

	@Override
	public float getOriginalFlyingSpeed() {
		return originalFlyingSpeed;
	}

	@Override
	public CompoundNBT saveNBTData() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("original_flying_speed", this.getOriginalFlyingSpeed());
		return nbt;
	}


	@Override
	public void loadNBTData(CompoundNBT nbtTag) {
		this.setOriginalFlyingSpeed(nbtTag.getFloat("original_flying_speed"));
	}
}