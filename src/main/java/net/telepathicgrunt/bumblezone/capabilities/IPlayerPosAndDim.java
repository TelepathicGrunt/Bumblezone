package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public interface IPlayerPosAndDim {

	//what methods the capability will have and what the capability is
	
	void setDim(DimensionType incomingDim);
	void setPos(BlockPos incomingPos);

	DimensionType getDim();
	BlockPos getPos();

	CompoundNBT saveNBTData();
	void loadNBTData(CompoundNBT nbtTag);
}
