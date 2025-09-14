package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.entities.teleportation.EntityTeleportationHookup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import mekanism.api.MekanismAPI;
import mekanism.api.event.MekanismTeleportEvent;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.ModuleData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
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
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.EnumSet;
import java.util.Optional;

public class MekanismCompat <MODULE extends ICustomModule<MODULE>> implements ModCompat {
	final DeferredHolder<ModuleData<?>, ModuleData<MODULE>> JETPACK_UNIT = DeferredHolder.create(MekanismAPI.MODULE_REGISTRY_NAME, ResourceLocation.fromNamespaceAndPath("mekanism", "jetpack_unit"));
	final DeferredHolder<ModuleData<?>, ModuleData<MODULE>> GRAV_UNIT = DeferredHolder.create(MekanismAPI.MODULE_REGISTRY_NAME, ResourceLocation.fromNamespaceAndPath("mekanism", "gravitational_modulating_unit"));
	public static Optional<Holder.Reference<Item>> JETPACK;
	public static Optional<Holder.Reference<Item>> JETPACK_ARMORED;

	public MekanismCompat() {
		JETPACK = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("mekanism", "jetpack"));
		JETPACK_ARMORED = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("mekanism", "jetpack_armored"));

		IEventBus forgeBus = NeoForge.EVENT_BUS;
		forgeBus.addListener(MekanismCompat.Events::isMekaToolTeleporting);

		ModChecker.mekanismPresent = true;
	}

	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
	}

	@Override
	public void restrictFlight(Entity entity, double extraGravity) {
		if (entity instanceof Player player) {
			ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);

			if ((JETPACK_ARMORED.isPresent() && chestplate.is(JETPACK_ARMORED.get())) ||
				(JETPACK.isPresent() && chestplate.is(JETPACK.get())))
			{
				if (!player.getCooldowns().isOnCooldown(chestplate)) {
					if (player instanceof ServerPlayer serverPlayer) {
						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_jetpack")
								.withStyle(ChatFormatting.ITALIC)
								.withStyle(ChatFormatting.RED), true);
					}
				}

				player.getCooldowns().addCooldown(chestplate, 40);
			}

			if (player instanceof ServerPlayer) {
				IModule<?> jetpackUnit = IModuleHelper.INSTANCE.getModule(chestplate, JETPACK_UNIT);
				if (jetpackUnit != null && jetpackUnit.isEnabled()) {
					jetpackUnit.toggleEnabled(
							IModuleHelper.INSTANCE.getModuleContainer(chestplate),
							chestplate,
							player,
							Component.translatable("system.the_bumblezone.denied_mek_jetpack_module").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED));
				}

				IModule<?> gravUnit = IModuleHelper.INSTANCE.getModule(chestplate, GRAV_UNIT);
				if (gravUnit != null && gravUnit.isEnabled()) {
					gravUnit.toggleEnabled(
							IModuleHelper.INSTANCE.getModuleContainer(chestplate),
							chestplate,
							player,
							Component.translatable("system.the_bumblezone.denied_mek_grav_module").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED));
				}
			}
		}
	}

	public static class Events {
		private static void isMekaToolTeleporting(MekanismTeleportEvent.MekaTool event) {
			Player player = event.getEntity();
			BlockHitResult blockHitResult = event.getTargetBlock();
			if (blockHitResult != null && EntityTeleportationHookup.runGenericTeleport(player, blockHitResult.getBlockPos())) {
				event.setCanceled(true);
			}
		}
	}
}
