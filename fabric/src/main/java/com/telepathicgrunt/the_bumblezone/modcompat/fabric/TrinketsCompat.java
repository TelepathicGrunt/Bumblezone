package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

//public class TrinketsCompat implements ModCompat {
//	public TrinketsCompat() {
//		TrinketsApi.registerTrinket(BzItems.FLOWER_HEADWEAR.get(), new FlowerHeadwearTrinkets());
//		ModChecker.trinketsPresent = true;
//	}
//
//	@Override
//	public EnumSet<Type> compatTypes() {
//		return EnumSet.of(Type.CUSTOM_EQUIPMENT_SLOTS);
//	}
//
//	@Override
//	public int getNumberOfMatchingEquippedItemsInCustomSlots(Entity entity, Predicate<ItemStack> itemStackPredicate) {
//		if (entity instanceof LivingEntity livingEntity) {
////			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
////			if (optionalTrinketComponent.isPresent()) {
////				return optionalTrinketComponent.get().getEquipped(itemStackPredicate).size();
////			}
//		}
//
//		return 0;
//	}
//
//	public static class FlowerHeadwearTrinkets implements Trinket {
//		@Override
//		public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
//			if (entity instanceof Player player &&
//				stack.getItem() instanceof FlowerHeadwearHelmet flowerHeadwearHelmet &&
//				!player.getItemBySlot(EquipmentSlot.HEAD).is(BzItems.FLOWER_HEADWEAR.get()))
//			{
//				flowerHeadwearHelmet.bz$onArmorTick(stack, player.level(), player);
//			}
//		}
//	}
//}
