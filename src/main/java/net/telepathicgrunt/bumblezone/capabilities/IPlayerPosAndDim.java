package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.dimension.DimensionType;

public interface IPlayerPosAndDim {

	//what methods the capability will have and what the capability is
	
	void setPrevDim(DimensionType incomingDim);
	void setDestDim(DimensionType incomingDim);
	void setTeleporting(boolean teleporting);

	DimensionType getPrevDim();
	DimensionType getDestDim();
	boolean getTeleporting();

	CompoundNBT saveNBTData();
	void loadNBTData(CompoundNBT nbtTag);
}
