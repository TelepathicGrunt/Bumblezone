package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.teleportation.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import mekanism.api.MekanismAPI;
import mekanism.api.event.MekanismTeleportEvent;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.ModuleData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import java.util.EnumSet;

public class MekanismCompat implements ModCompat {
//	final RegistryObject<ModuleData<?>> JETPACK_UNIT = RegistryObject.create(new ResourceLocation("mekanism", "jetpack_unit"), MekanismAPI.MODULE_REGISTRY_NAME, Bumblezone.MODID);
//	final RegistryObject<ModuleData<?>> GRAV_UNIT = RegistryObject.create(new ResourceLocation("mekanism", "gravitational_modulating_unit"), MekanismAPI.MODULE_REGISTRY_NAME, Bumblezone.MODID);
//	public static Item JETPACK;
//	public static Item JETPACK_ARMORED;
//
//	public MekanismCompat() {
//		JETPACK = BuiltInRegistries.ITEM.get(new ResourceLocation("mekanism", "jetpack"));
//		JETPACK_ARMORED = BuiltInRegistries.ITEM.get(new ResourceLocation("mekanism", "jetpack_armored"));
//
//		IEventBus forgeBus = NeoForge.EVENT_BUS;
//		forgeBus.addListener(MekanismCompat::isMekaToolTeleporting);
//
//		ModChecker.mekanismPresent = true;
//	}
//
//	@Override
//	public EnumSet<Type> compatTypes() {
//		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
//	}
//
//	@Override
//	public void restrictFlight(Entity entity, double extraGravity) {
//		if (entity instanceof Player player) {
//			ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
//
//			if ((JETPACK_ARMORED != null && chestplate.is(JETPACK_ARMORED)) ||
//				(JETPACK != null && chestplate.is(JETPACK)))
//			{
//				if (!player.getCooldowns().isOnCooldown(chestplate.getItem())) {
//					if (player instanceof ServerPlayer serverPlayer) {
//						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_jetpack")
//								.withStyle(ChatFormatting.ITALIC)
//								.withStyle(ChatFormatting.RED), true);
//					}
//				}
//
//				player.getCooldowns().addCooldown(chestplate.getItem(), 40);
//			}
//
//			if (player instanceof ServerPlayer) {
//				IModule<?> jetpackUnit = IModuleHelper.INSTANCE.load(chestplate, JETPACK_UNIT.get());
//				if (jetpackUnit != null && jetpackUnit.isEnabled()) {
//					jetpackUnit.toggleEnabled(player, Component.translatable("system.the_bumblezone.denied_mek_jetpack_module")
//							.withStyle(ChatFormatting.ITALIC)
//							.withStyle(ChatFormatting.RED));
//				}
//
//				IModule<?> gravUnit = IModuleHelper.INSTANCE.load(chestplate, GRAV_UNIT.get());
//				if (gravUnit != null && gravUnit.isEnabled()) {
//					gravUnit.toggleEnabled(player, Component.translatable("system.the_bumblezone.denied_mek_grav_module")
//							.withStyle(ChatFormatting.ITALIC)
//							.withStyle(ChatFormatting.RED));
//				}
//			}
//		}
//	}
//
//	private static void isMekaToolTeleporting(MekanismTeleportEvent.MekaTool event) {
//		Player player = event.getEntity();
//		BlockHitResult blockHitResult = event.getTargetBlock();
//		if (blockHitResult != null && EntityTeleportationHookup.runGenericTeleport(player, blockHitResult.getBlockPos())) {
//			event.setCanceled(true);
//		}
//	}
}
