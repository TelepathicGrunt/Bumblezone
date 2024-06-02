package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.function.Predicate;

public class TrinketsCompat implements ModCompat {
	public TrinketsCompat() {
		TrinketsApi.registerTrinket(BzItems.FLOWER_HEADWEAR.get(), new FlowerHeadwearTrinkets());
		ModChecker.trinketsPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.CUSTOM_EQUIPMENT_SLOTS);
	}

	@Override
	public int getNumberOfMatchingEquippedItemsInCustomSlots(Entity entity, Predicate<ItemStack> itemStackPredicate) {
		if (entity instanceof LivingEntity livingEntity) {
//			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
//			if (optionalTrinketComponent.isPresent()) {
//				return optionalTrinketComponent.get().getEquipped(itemStackPredicate).size();
//			}
		}

		return 0;
	}

	public static class FlowerHeadwearTrinkets implements Trinket {
		@Override
		public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
			if (entity instanceof Player player &&
				stack.getItem() instanceof FlowerHeadwearHelmet flowerHeadwearHelmet &&
				!player.getItemBySlot(EquipmentSlot.HEAD).is(BzItems.FLOWER_HEADWEAR.get()))
			{
				flowerHeadwearHelmet.bz$onArmorTick(stack, player.level(), player);
			}
		}
	}
}
