package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public interface IEntityPosAndDim {

	//what methods the capability will have and what the capability is
	
	void setNonBZDim(ResourceLocation incomingDim);
	ResourceLocation getNonBZDim();
	void setNonBZPos(Vec3 incomingPos);
	Vec3 getNonBZPos();

	CompoundTag saveNBTData();
	void loadNBTData(CompoundTag nbtTag);
}
