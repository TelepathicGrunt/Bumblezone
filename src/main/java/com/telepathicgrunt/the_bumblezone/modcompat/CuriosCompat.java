package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
	public static void setupCompat() {

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.curiosPresent = true;
	}

	public static int getCuriosBeeGearCount(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			return CuriosApi.getCuriosHelper().findCurios(livingEntity, (itemStack) ->  {
				if (itemStack.is(BzTags.BZ_ARMOR_ABILITY_ENHANCING_GEAR)) {
					return !ModChecker.backpackedPresent || BackpackedCompat.isBackpackedHoneyThemedOrOtherItem(itemStack);
				}
				return false;
			}).size();
		}

		return 0;
	}
}
