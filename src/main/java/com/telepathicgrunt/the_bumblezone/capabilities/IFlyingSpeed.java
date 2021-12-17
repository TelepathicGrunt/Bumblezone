package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IFlyingSpeed extends INBTSerializable<CompoundTag> {

	//what methods the capability will have and what the capability is
	
	void setOriginalFlyingSpeed(float incomingDim);
	float getOriginalFlyingSpeed();
}
