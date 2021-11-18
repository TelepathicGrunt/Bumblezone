package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

public class CapabilityEntityPosAndDim {
	//legacy capability name to prevent breaking player's data when updating
	public static final ResourceLocation ENTITY_PAST_POS_AND_DIM = new ResourceLocation(Bumblezone.MODID, "player_past_pos_and_dim");

	//registers the capability and defines how it will read/write data from nbt
	public static void register() {
		CapabilityManager.INSTANCE.register(IEntityPosAndDim.class, new Capability.IStorage<IEntityPosAndDim>() {
			@Override
			@Nullable
			public INBT writeNBT(Capability<IEntityPosAndDim> capability, IEntityPosAndDim instance, Direction side) {
				return instance.saveNBTData();
			}
			@Override
			public void readNBT(Capability<IEntityPosAndDim> capability, IEntityPosAndDim instance, Direction side, INBT nbt) {
				instance.loadNBTData((CompoundNBT) nbt);
			}
		}, EntityPositionAndDimension::new);
	}

	public static void onAttachCapabilitiesToEntities(AttachCapabilitiesEvent<Entity> e) {
		Entity ent = e.getObject();
		if (ent instanceof LivingEntity) {
			e.addCapability(ENTITY_PAST_POS_AND_DIM, new PastPosAndDimProvider());
		}
	}
}
