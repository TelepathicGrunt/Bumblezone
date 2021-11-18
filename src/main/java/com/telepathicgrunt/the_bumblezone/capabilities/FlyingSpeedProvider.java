package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FlyingSpeedProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

	//the capability itself
	@CapabilityInject(IFlyingSpeed.class)
	public static Capability<IFlyingSpeed> ORIGINAL_FLYING_SPEED = null;

	private EntityFlyingSpeed instance = (EntityFlyingSpeed) ORIGINAL_FLYING_SPEED.getDefaultInstance();

	//returns the capability attached to the entity
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ORIGINAL_FLYING_SPEED) {
			if(instance == null) {
				instance = new EntityFlyingSpeed();
			}
			
			return LazyOptional.of(() -> instance).cast();
		}
		else {
			return LazyOptional.empty();
		}
	}


	@Override
	public CompoundNBT serializeNBT() {
		return instance.saveNBTData();
	}


	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		instance.loadNBTData(nbt);
	}
}
