package com.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

public interface IPlayerPosAndDim {

	//what methods the capability will have and what the capability is
	
	void setNonBZDim(ResourceLocation incomingDim);
	void setDestDim(ResourceLocation incomingDim);
	void setTeleporting(boolean teleporting);

	ResourceLocation getNonBZDim();
	ResourceLocation getDestDim();
	boolean getTeleporting();

	void setNonBZPos(Vector3d incomingPos);
	Vector3d getNonBZPos();
	void setNonBZPitch(float incomingPitch);
	float getNonBZPitch();
	void setNonBZYaw(float incomingYaw);
	float getNonBZYaw();

	CompoundNBT saveNBTData();
	void loadNBTData(CompoundNBT nbtTag);
}
