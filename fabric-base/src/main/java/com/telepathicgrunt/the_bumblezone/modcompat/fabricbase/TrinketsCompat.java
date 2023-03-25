package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;

public class TrinketsCompat implements ModCompat {
	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.CUSTOM_EQUIPMENT_SLOTS);
	}

	public int getNumberOfMatchingEquippedItemsInCustomSlots(Entity entity, Predicate<ItemStack> itemStackPredicate) {
		if (entity instanceof LivingEntity livingEntity) {
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
			if (optionalTrinketComponent.isPresent()) {
				return optionalTrinketComponent.get().getEquipped(itemStackPredicate).size();
			}
		}

		return 0;
	}
}
