package com.telepathicgrunt.the_bumblezone.modcompat.forge;

import com.telepathicgrunt.the_bumblezone.items.FlowerHeadwearHelmet;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CuriosCompat implements ModCompat {
	public CuriosCompat() {
		ModChecker.curiosPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.CUSTOM_EQUIPMENT_SLOTS);
	}

	public int getNumberOfMatchingEquippedItemsInCustomSlots(Entity entity, Predicate<ItemStack> itemStackPredicate) {
		if (entity instanceof LivingEntity livingEntity) {
			return CuriosApi.getCuriosInventory(livingEntity).map(i -> i.findCurios(itemStackPredicate).size()).orElse(0);
		}

		return 0;
	}

	public static ICapabilityProvider getCurioCapForFlowerHeadwear(ItemStack stack) {
		return CurioItemCapability.createProvider(new ICurio() {
			@Override
			public ItemStack getStack() {
				return stack;
			}

			@Override
			public void curioTick(SlotContext slotContext) {
				if (slotContext.entity() instanceof Player player &&
					stack.getItem() instanceof FlowerHeadwearHelmet flowerHeadwearHelmet &&
					!player.getItemBySlot(EquipmentSlot.HEAD).is(BzItems.FLOWER_HEADWEAR.get()))
				{
					flowerHeadwearHelmet.bz$onArmorTick(stack, player.level(), player);
				}
			}
		});
	}
}
