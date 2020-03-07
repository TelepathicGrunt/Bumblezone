package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;


public class PlayerPositionAndDimension implements IPlayerPosAndDim
{

	public DimensionType nonBZDimension = null;
	public DimensionType nextDimension = null;
	public boolean isTeleporting = false;


	@Override
	public void setNonBZDim(DimensionType incomingDim)
	{
		nonBZDimension = incomingDim;
	}

	@Override
	public DimensionType getNonBZDim()
	{
		return nonBZDimension;
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

		if (this.getNonBZDim() != null)
		{
			nbt.putString("PreviousDimensionNamespace", this.getNonBZDim().getRegistryName().getNamespace());
			nbt.putString("PreviousDimensionPath", this.getNonBZDim().getRegistryName().getPath());
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
		CompoundNBT cnbt = nbtTag;

		//grabs past dimension resource location and tries to get that dimension from the registry
		DimensionType storedDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("PreviousDimensionNamespace"), cnbt.getString("PreviousDimensionPath")));
		DimensionType storedDestDimension = DimensionType.byName(new ResourceLocation(cnbt.getString("NextDimensionNamespace"), cnbt.getString("NextDimensionPath")));

		boolean isteleporting = cnbt.getBoolean("isTeleporting");

		this.setNonBZDim(storedDimension);
		this.setDestDim(storedDestDimension);
		this.setTeleporting(isteleporting);
	}
}