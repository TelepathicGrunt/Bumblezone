package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.Optional;

public class CuriosCompat {
	public static void setupCompat() {

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.curiosPresent = true;
	}

	public static int getCuriosBeeGearCount(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			return CuriosApi.getCuriosHelper().findCurios(livingEntity, (itemStack) -> itemStack.is(BzTags.BZ_ARMOR_ABILITY_ENHANCING_GEAR)).size();
		}

		return 0;
	}
}
