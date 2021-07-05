package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

public interface IEntityPosAndDim {

	//what methods the capability will have and what the capability is
	
	void setNonBZDim(ResourceLocation incomingDim);
	void setTeleporting(boolean teleporting);

	ResourceLocation getNonBZDim();
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
