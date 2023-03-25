package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class TrinketsCompat {
	public static void setupCompat() {

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.TrinketsPresent = true;
	}

	public static int getTrinketsBeeGearCount(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
			if (optionalTrinketComponent.isPresent()) {
				return optionalTrinketComponent.get().getEquipped((itemStack) -> itemStack.is(BzTags.BZ_ARMOR_ABILITY_ENHANCING_GEAR)).size();
			}
		}

		return 0;
	}
}
