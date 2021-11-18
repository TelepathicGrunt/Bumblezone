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

public class CapabilityFlyingSpeed {
	//legacy capability name to prevent breaking player's data when updating
	public static final ResourceLocation ORIGINAL_FLYING_SPEED = new ResourceLocation(Bumblezone.MODID, "original_flying_speed");

	//registers the capability and defines how it will read/write data from nbt
	public static void register() {
		CapabilityManager.INSTANCE.register(IFlyingSpeed.class, new Capability.IStorage<IFlyingSpeed>() {
			@Override
			@Nullable
			public INBT writeNBT(Capability<IFlyingSpeed> capability, IFlyingSpeed instance, Direction side) {
				return instance.saveNBTData();
			}
			@Override
			public void readNBT(Capability<IFlyingSpeed> capability, IFlyingSpeed instance, Direction side, INBT nbt) {
				instance.loadNBTData((CompoundNBT) nbt);
			}
		}, EntityFlyingSpeed::new);
	}

	public static void onAttachCapabilitiesToEntities(AttachCapabilitiesEvent<Entity> e) {
		Entity ent = e.getObject();
		if (ent instanceof LivingEntity) {
			e.addCapability(ORIGINAL_FLYING_SPEED, new FlyingSpeedProvider());
		}
	}
}
