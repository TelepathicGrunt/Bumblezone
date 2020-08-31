package com.telepathicgrunt.bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityPlayerPosAndDim {
		//the capability itself
		@CapabilityInject(IPlayerPosAndDim.class)
		public static Capability<IPlayerPosAndDim> PAST_POS_AND_DIM = null;

		//registers the capability and defines how it will read/write data from nbt
		public static void register() {
			CapabilityManager.INSTANCE.register(IPlayerPosAndDim.class, new Capability.IStorage<IPlayerPosAndDim>() 
			{
				@Override
				@Nullable
				public INBT writeNBT(Capability<IPlayerPosAndDim> capability, IPlayerPosAndDim instance, Direction side) 
				{
					return instance.saveNBTData();
				}

				@Override
				public void readNBT(Capability<IPlayerPosAndDim> capability, IPlayerPosAndDim instance, Direction side, INBT nbt) 
				{
					instance.loadNBTData((CompoundNBT) nbt);
				}
			}, PlayerPositionAndDimension::new);
		}
}
