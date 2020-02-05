package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class PlayerPositionAndDimension implements IPlayerPosAndDim{


	public DimensionType prevDimension = null;
	public BlockPos prevBlockPos = new BlockPos(0,0,0);
	
	@Override
	public void setDim(DimensionType incomingDim) {
		prevDimension = incomingDim;
	}

	@Override
	public void setPos(BlockPos incomingPos) {
		prevBlockPos = incomingPos;
	}

	@Override
	public DimensionType getDim() {
		return prevDimension;
	}

	@Override
	public BlockPos getPos() {
		return prevBlockPos;
	}

	@Override
	public CompoundNBT saveNBTData() {
		CompoundNBT nbt = new CompoundNBT();

		nbt.putInt("PrevX", this.getPos().getX());
		nbt.putInt("PrevY", this.getPos().getY());
		nbt.putInt("PrevZ", this.getPos().getZ());
		
		if(this.getDim() != null) {
			nbt.putString("PreviousDimensionNamespace", this.getDim().getRegistryName().getNamespace());
			nbt.putString("PreviousDimensionPath", this.getDim().getRegistryName().getPath());
		}

		return nbt;
	}

	@Override
	public void loadNBTData(CompoundNBT nbtTag) {
		CompoundNBT cnbt = (CompoundNBT) nbtTag;
		BlockPos storedBlockPos = new BlockPos(cnbt.getInt("PrevX"), cnbt.getInt("PrevY"), cnbt.getInt("PrevZ"));
		
		//grabs past dimension resource location and tries to get that dimension from the registry
		DimensionType storedDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("PreviousDimensionNamespace"), 
																			      cnbt.getString("PreviousDimensionPath")));
		
		this.setDim(storedDimension);
		this.setPos(storedBlockPos);
	}
		
}