package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;

public interface IFlyingSpeed {

	//what methods the capability will have and what the capability is
	
	void setOriginalFlyingSpeed(float incomingDim);
	float getOriginalFlyingSpeed();

	CompoundNBT saveNBTData();
	void loadNBTData(CompoundNBT nbtTag);
}
