package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;


public class NeurotoxinsMissCounter implements INBTSerializable<CompoundTag> {

	private int missedParalysis = 0;

	public void setMissedParalysis(int missedParalysis) {
		this.missedParalysis = missedParalysis;
	}

	public int getMissedParalysis() {
		return missedParalysis;
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putFloat("missed_paralysis", this.getMissedParalysis());
		return nbt;
	}

	public void deserializeNBT(CompoundTag nbtTag) {
		this.setMissedParalysis(nbtTag.getInt("missed_paralysis"));
	}
}