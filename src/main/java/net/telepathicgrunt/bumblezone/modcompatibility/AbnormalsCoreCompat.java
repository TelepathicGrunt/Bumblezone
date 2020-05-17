package net.telepathicgrunt.bumblezone.modcompatibility;

import com.bagel.buzzierbees.core.registry.BBItems;
import com.teamabnormals.abnormals_core.common.entity.AbnormalsBoatEntity;

import net.minecraft.entity.Entity;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionRegistration;

public class AbnormalsCoreCompat
{
	
	public static void setupBuzzierBees() 
	{
		ModChecking.AbnormalsCorePresent = true;
	}
	

	public static void speedUpHiveBoat(Entity entity) {
	    if (entity instanceof AbnormalsBoatEntity && entity.dimension == BzDimensionRegistration.bumblezone()) {
		AbnormalsBoatEntity boat = (AbnormalsBoatEntity) entity;
		if (boat.getBoat().getBoatItem() == BBItems.HIVE_BOAT.get()) {

		    double yMagnitude = Math.abs(entity.getMotion().y);
		    if (yMagnitude < 0.1D) {
			double speedFactor = 1.004D;
			entity.setMotion(entity.getMotion().mul(speedFactor, 1.0D, speedFactor));
		    }
		}
	    }
	}
    }
