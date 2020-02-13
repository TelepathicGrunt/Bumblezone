package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;


public class PlayerPositionAndDimension implements IPlayerPosAndDim
{

	public DimensionType prevDimension = null;
	public DimensionType nextDimension = null;
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

		//grabs past dimension resource location and tries to get that dimension from the registry
		DimensionType storedDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("PreviousDimensionNamespace"), cnbt.getString("PreviousDimensionPath")));
		DimensionType storedDestDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("NextDimensionNamespace"), cnbt.getString("NextDimensionPath")));

		boolean isteleporting = cnbt.getBoolean("isTeleporting");

		this.setPrevDim(storedDimension);
		this.setDestDim(storedDestDimension);
		this.setTeleporting(isteleporting);
	}
}