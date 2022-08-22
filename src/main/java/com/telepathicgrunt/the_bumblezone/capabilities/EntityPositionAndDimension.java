package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.Level;


public class EntityPositionAndDimension implements INBTSerializable<CompoundTag> {
	private ResourceLocation nonBZDimension = new ResourceLocation("minecraft", "overworld");
	private Vec3 nonBZPosition = null;

	public void setNonBZDim(ResourceLocation incomingDim) {
		if (incomingDim.equals(Bumblezone.MOD_DIMENSION_ID)) {
			this.nonBZDimension = net.minecraft.world.level.Level.OVERWORLD.location();
			Bumblezone.LOGGER.log(Level.ERROR, "Error: The non-bz dimension passed in to be stored was bz dimension. Please contact mod creator to let them know of this issue.");
		}
		else {
			nonBZDimension = incomingDim;
		}
	}

	public ResourceLocation getNonBZDim() {
		return nonBZDimension;
	}

	public void setNonBZPos(Vec3 incomingPos) {
		nonBZPosition = incomingPos;
	}

	public Vec3 getNonBZPos() {
		return nonBZPosition;
	}


	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		if (this.getNonBZDim() != null) {
			nbt.putString("PreviousDimensionNamespace", this.getNonBZDim().getNamespace());
			nbt.putString("PreviousDimensionPath", this.getNonBZDim().getPath());

			if (this.getNonBZPos() != null) {
			    nbt.putDouble("NonBZ_X", this.getNonBZPos().x);
			    nbt.putDouble("NonBZ_Y", this.getNonBZPos().y);
			    nbt.putDouble("NonBZ_Z", this.getNonBZPos().z);
			}
		}

		return nbt;
	}


	@Override
	public void deserializeNBT(CompoundTag nbtTag) {
		//grabs past dimension resource location and tries to get that dimension from the registry
		String namespace = nbtTag.getString("PreviousDimensionNamespace");
		String path = nbtTag.getString("PreviousDimensionPath");
		ResourceLocation storedDimension;
		if(path.trim().isEmpty()) {
			storedDimension = new ResourceLocation("minecraft", "overworld");
		}
		else {
			storedDimension = new ResourceLocation(namespace, path);
		}

		Vec3 storedPositionNonBZ = null;
		//Need check for null so we can let rest for code know the entity has not exit the dimension yet for the first time.
		if (nbtTag.contains("NonBZ_X") && nbtTag.contains("NonBZ_Y") && nbtTag.contains("NonBZ_Z")) {
		    storedPositionNonBZ = new Vec3(nbtTag.getFloat("NonBZ_X"), nbtTag.getFloat("NonBZ_Y"), nbtTag.getFloat("NonBZ_Z"));
		}

		this.setNonBZDim(storedDimension.getPath().isEmpty() ? new ResourceLocation("minecraft", "overworld") : storedDimension);
		this.setNonBZPos(storedPositionNonBZ);
	}
}