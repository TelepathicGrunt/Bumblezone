package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;


public class EntityFlyingSpeed implements INBTSerializable<CompoundTag> {

	private float originalFlyingSpeed = 0.02f;

	public void setOriginalFlyingSpeed(float originalFlyingSpeed) {
		this.originalFlyingSpeed = originalFlyingSpeed;
	}

	public float getOriginalFlyingSpeed() {
		return originalFlyingSpeed;
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat("original_flying_speed", this.getOriginalFlyingSpeed());
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbtTag) {
		this.setOriginalFlyingSpeed(nbtTag.getFloat("original_flying_speed"));
	}
}