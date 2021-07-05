package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class CapabilityEventHandler
{
	//legacy capability name to prevent breaking player's data when updating
	public static final ResourceLocation ENTITY_PAST_POS_AND_DIM = new ResourceLocation(Bumblezone.MODID, "player_past_pos_and_dim");

	public static void onAttachCapabilitiesToEntities(AttachCapabilitiesEvent<Entity> e)
	{
		Entity ent = e.getObject();
		if (ent instanceof LivingEntity)
		{
			e.addCapability(ENTITY_PAST_POS_AND_DIM, new PastPosAndDimProvider());
		}
	}
}