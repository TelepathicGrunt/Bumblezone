package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CuriosCompat implements ModCompat {
	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.CUSTOM_EQUIPMENT_SLOTS);
	}

	public int getNumberOfMatchingEquippedItemsInCustomSlots(Entity entity, Predicate<ItemStack> itemStackPredicate) {
		if (entity instanceof LivingEntity livingEntity) {
			return CuriosApi.getCuriosHelper().findCurios(livingEntity, itemStackPredicate).size();
		}

		return 0;
	}
}
