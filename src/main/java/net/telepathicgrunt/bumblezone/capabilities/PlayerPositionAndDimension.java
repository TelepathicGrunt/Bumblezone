package net.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;


public class PlayerPositionAndDimension implements IPlayerPosAndDim
{

	public DimensionType nonBZDimension = null;
	public DimensionType nextDimension = null;
	public boolean isTeleporting = false;
	public Vec3d nonBZPosition = null;
	public float nonBZPitch = 0;
	public float nonBZYaw = 0;


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
	public void setNonBZPitch(float incomingPitch)
	{
		nonBZPitch = incomingPitch;
	}

	@Override
	public float getNonBZPitch()
	{
		return nonBZPitch;
	}


	@Override
	public void setNonBZYaw(float incomingYaw)
	{
		nonBZYaw = incomingYaw;
	}
	
	@Override
	public float getNonBZYaw()
	{
		return nonBZYaw;
	}

	
	@Override
	public void setNonBZPos(Vec3d incomingPos)
	{
		nonBZPosition = incomingPos;
	}

	@Override
	public Vec3d getNonBZPos()
	{
		return nonBZPosition;
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

			if (this.getNonBZPos() != null)
			{
			    nbt.putDouble("NonBZ_X", this.getNonBZPos().getX());
			    nbt.putDouble("NonBZ_Y", this.getNonBZPos().getY());
			    nbt.putDouble("NonBZ_Z", this.getNonBZPos().getZ());
			}
			nbt.putFloat("NonBZPitch", nonBZPitch);
			nbt.putFloat("NonBZYaw", nonBZYaw);
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
		Vec3d storedPositionNonBZ = null;
		float storedNonBZPitch = 3.75F;
		float storedNonBZYaw = 0F;
		//Need check for null so we can let rest for code know the player has not exit the dimension yet for the first time.
		if (cnbt.contains("NonBZ_X") && cnbt.contains("NonBZ_Y") && cnbt.contains("NonBZ_Z")) {
		    storedPositionNonBZ = new Vec3d(cnbt.getFloat("NonBZ_X"), cnbt.getFloat("NonBZ_Y"), cnbt.getFloat("NonBZ_Z"));
		}
		storedNonBZPitch = cnbt.getFloat("NonBZPitch");
		storedNonBZYaw = cnbt.getFloat("NonBZYaw");

		boolean isteleporting = cnbt.getBoolean("isTeleporting");

		this.setNonBZDim(storedDimension);
		this.setNonBZPitch(storedNonBZPitch);
		this.setNonBZYaw(storedNonBZYaw);
		this.setNonBZPos(storedPositionNonBZ);
		this.setDestDim(storedDestDimension);
		this.setTeleporting(isteleporting);
	}
}