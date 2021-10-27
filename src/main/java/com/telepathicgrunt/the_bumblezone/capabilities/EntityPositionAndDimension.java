package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;


public class EntityPositionAndDimension implements IEntityPosAndDim
{
	private ResourceLocation nonBZDimension = new ResourceLocation("minecraft", "overworld");
	private Vector3d nonBZPosition = null;
	private float nonBZPitch = 0;
	private float nonBZYaw = 0;


	@Override
	public void setNonBZDim(ResourceLocation incomingDim)
	{
		nonBZDimension = incomingDim;
	}

	@Override
	public ResourceLocation getNonBZDim()
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
	public void setNonBZPos(Vector3d incomingPos)
	{
		nonBZPosition = incomingPos;
	}

	@Override
	public Vector3d getNonBZPos()
	{
		return nonBZPosition;
	}


	@Override
	public CompoundNBT saveNBTData()
	{
		CompoundNBT nbt = new CompoundNBT();

		if (this.getNonBZDim() != null)
		{
			nbt.putString("PreviousDimensionNamespace", this.getNonBZDim().getNamespace());
			nbt.putString("PreviousDimensionPath", this.getNonBZDim().getPath());

			if (this.getNonBZPos() != null)
			{
			    nbt.putDouble("NonBZ_X", this.getNonBZPos().x());
			    nbt.putDouble("NonBZ_Y", this.getNonBZPos().y());
			    nbt.putDouble("NonBZ_Z", this.getNonBZPos().z());
			}
			nbt.putFloat("NonBZPitch", nonBZPitch);
			nbt.putFloat("NonBZYaw", nonBZYaw);
		}

		return nbt;
	}


	@Override
	public void loadNBTData(CompoundNBT nbtTag)
	{
		//grabs past dimension resource location and tries to get that dimension from the registry
		ResourceLocation storedDimension = new ResourceLocation(nbtTag.getString("PreviousDimensionNamespace"), nbtTag.getString("PreviousDimensionPath"));
		Vector3d storedPositionNonBZ = null;
		float storedNonBZPitch;
		float storedNonBZYaw;
		//Need check for null so we can let rest for code know the entity has not exit the dimension yet for the first time.
		if (nbtTag.contains("NonBZ_X") && nbtTag.contains("NonBZ_Y") && nbtTag.contains("NonBZ_Z")) {
		    storedPositionNonBZ = new Vector3d(nbtTag.getFloat("NonBZ_X"), nbtTag.getFloat("NonBZ_Y"), nbtTag.getFloat("NonBZ_Z"));
		}
		storedNonBZPitch = nbtTag.getFloat("NonBZPitch");
		storedNonBZYaw = nbtTag.getFloat("NonBZYaw");

		boolean isteleporting = nbtTag.getBoolean("isTeleporting");

		this.setNonBZDim(storedDimension.getPath().isEmpty() ? new ResourceLocation("minecraft", "overworld") : storedDimension);
		this.setNonBZPitch(storedNonBZPitch);
		this.setNonBZYaw(storedNonBZYaw);
		this.setNonBZPos(storedPositionNonBZ);
	}
}