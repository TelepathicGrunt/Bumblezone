package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;


public class PlayerPositionAndDimension implements IPlayerPosAndDim
{

	public DimensionType prevDimension = null;
	public DimensionType nextDimension = null;
	public BlockPos prevBlockPos = new BlockPos(0, 0, 0);
	public boolean isTeleporting = false;


	@Override
	public void setPrevDim(DimensionType incomingDim)
	{
		prevDimension = incomingDim;
	}

	@Override
	public DimensionType getPrevDim()
	{
		return prevDimension;
	}


	@Override
	public void setPos(BlockPos incomingPos)
	{
		prevBlockPos = incomingPos;
	}
	
	@Override
	public BlockPos getPos()
	{
		return prevBlockPos;
	}


	@Override
	public void setDestDim(DimensionType incomingDim)
	{
		nextDimension = incomingDim;
	}

	@Override
	public DimensionType getDestDim()
	{
		return nextDimension;
	}


	@Override
	public void setTeleporting(boolean teleporting)
	{
		this.isTeleporting = teleporting;
	}

	@Override
	public boolean getTeleporting()
	{
		return this.isTeleporting;
	}


	@Override
	public CompoundNBT saveNBTData()
	{
		CompoundNBT nbt = new CompoundNBT();

		nbt.putInt("PrevX", this.getPos().getX());
		nbt.putInt("PrevY", this.getPos().getY());
		nbt.putInt("PrevZ", this.getPos().getZ());

		if (this.getPrevDim() != null)
		{
			nbt.putString("PreviousDimensionNamespace", this.getPrevDim().getRegistryName().getNamespace());
			nbt.putString("PreviousDimensionPath", this.getPrevDim().getRegistryName().getPath());
		}
		
		if (this.getDestDim() != null)
		{
			nbt.putString("NextDimensionNamespace", this.getDestDim().getRegistryName().getNamespace());
			nbt.putString("NextDimensionPath", this.getDestDim().getRegistryName().getPath());
		}

		nbt.putBoolean("isTeleporting", this.getTeleporting());

		return nbt;
	}


	@Override
	public void loadNBTData(CompoundNBT nbtTag)
	{
		CompoundNBT cnbt = (CompoundNBT) nbtTag;
		BlockPos storedBlockPos = new BlockPos(cnbt.getInt("PrevX"), cnbt.getInt("PrevY"), cnbt.getInt("PrevZ"));

		//grabs past dimension resource location and tries to get that dimension from the registry
		DimensionType storedDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("PreviousDimensionNamespace"), cnbt.getString("PreviousDimensionPath")));
		DimensionType storedDestDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("NextDimensionNamespace"), cnbt.getString("NextDimensionPath")));

		boolean isteleporting = cnbt.getBoolean("isTeleporting");

		this.setPrevDim(storedDimension);
		this.setDestDim(storedDestDimension);
		this.setPos(storedBlockPos);
		this.setTeleporting(isteleporting);
	}
}