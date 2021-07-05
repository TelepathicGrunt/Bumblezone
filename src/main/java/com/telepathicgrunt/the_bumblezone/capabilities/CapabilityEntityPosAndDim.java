package com.telepathicgrunt.the_bumblezone.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityEntityPosAndDim {
		//the capability itself
		@CapabilityInject(IEntityPosAndDim.class)
		public static Capability<IEntityPosAndDim> PAST_POS_AND_DIM = null;

		//registers the capability and defines how it will read/write data from nbt
		public static void register() {
			CapabilityManager.INSTANCE.register(IEntityPosAndDim.class, new Capability.IStorage<IEntityPosAndDim>()
			{
				@Override
				@Nullable
				public INBT writeNBT(Capability<IEntityPosAndDim> capability, IEntityPosAndDim instance, Direction side)
				{
					return instance.saveNBTData();
				}

				@Override
				public void readNBT(Capability<IEntityPosAndDim> capability, IEntityPosAndDim instance, Direction side, INBT nbt)
				{
					instance.loadNBTData((CompoundNBT) nbt);
				}
			}, EntityPositionAndDimension::new);
		}
}
