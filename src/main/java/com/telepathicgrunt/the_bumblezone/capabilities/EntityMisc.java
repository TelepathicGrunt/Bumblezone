package com.telepathicgrunt.the_bumblezone.capabilities;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;


public class EntityMisc implements INBTSerializable<CompoundTag> {

	public boolean isBeeEssenced = false;
	public boolean receivedEssencePrize = false;
	public long tradeResetPrimedTime = -1000;
	public int craftedBeehives = 0;
	public int beesBred = 0;
	public int flowersSpawned = 0;
	public int honeyBottleDrank = 0;
	public int beeStingersFired = 0;
	public int beeSaved = 0;
	public int pollenPuffHits = 0;
	public int honeySlimeBred = 0;
	public int beesFed = 0;
	public int queenBeeTrade = 0;
	public Map<ResourceLocation, Integer> mobsKilledTracker = new Object2IntOpenHashMap<>();

	public void resetAllTrackerStats() {
		receivedEssencePrize = false;
		tradeResetPrimedTime = -1000;
		craftedBeehives = 0;
		beesBred = 0;
		flowersSpawned = 0;
		honeyBottleDrank = 0;
		beeStingersFired = 0;
		beeSaved = 0;
		pollenPuffHits = 0;
		honeySlimeBred = 0;
		beesFed = 0;
		queenBeeTrade = 0;
		mobsKilledTracker.clear();
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("is_bee_essenced", this.isBeeEssenced);
		nbt.putBoolean("received_essence_prize", this.receivedEssencePrize);
		nbt.putLong("trade_reset_primed_time", this.tradeResetPrimedTime);
		nbt.putInt("crafted_beehives", this.craftedBeehives);
		nbt.putInt("bees_bred", this.beesBred);
		nbt.putInt("flowers_spawned", this.flowersSpawned);
		nbt.putInt("honey_bottle_drank", this.honeyBottleDrank);
		nbt.putInt("bee_stingers_fired", this.beeStingersFired);
		nbt.putInt("bee_saved", this.beeSaved);
		nbt.putInt("pollen_puff_hits", this.pollenPuffHits);
		nbt.putInt("honey_slime_bred", this.honeySlimeBred);
		nbt.putInt("bees_fed", this.beesFed);
		nbt.putInt("queen_bee_trade", this.queenBeeTrade);

		ListTag mapList = new ListTag();
		for (Map.Entry<ResourceLocation, Integer> entry : this.mobsKilledTracker.entrySet()) {
			CompoundTag compoundTag = new CompoundTag();
			compoundTag.putString("id", entry.getKey().toString());
			compoundTag.putInt("count", entry.getValue());
			mapList.add(compoundTag);
		}
		nbt.put("mobs_killed_tracker", mapList);

		return nbt;
	}

	public void deserializeNBT(CompoundTag nbtTag) {
		this.isBeeEssenced = nbtTag.getBoolean("is_bee_essenced");
		this.receivedEssencePrize = nbtTag.getBoolean("received_essence_prize");
		this.tradeResetPrimedTime = nbtTag.getLong("trade_reset_primed_time");
		this.craftedBeehives = nbtTag.getInt("crafted_beehives");
		this.beesBred = nbtTag.getInt("bees_bred");
		this.flowersSpawned = nbtTag.getInt("flowers_spawned");
		this.honeyBottleDrank = nbtTag.getInt("honey_bottle_drank");
		this.beeStingersFired = nbtTag.getInt("bee_stingers_fired");
		this.beeSaved = nbtTag.getInt("bee_saved");
		this.pollenPuffHits = nbtTag.getInt("pollen_puff_hits");
		this.honeySlimeBred = nbtTag.getInt("honey_slime_bred");
		this.beesFed = nbtTag.getInt("bees_fed");
		this.queenBeeTrade = nbtTag.getInt("queen_bee_trade");

		ListTag mapList = nbtTag.getList("mobs_killed_tracker", Tag.TAG_COMPOUND);
		for (int i = 0; i < mapList.size(); i++) {
			CompoundTag compoundTag = mapList.getCompound(i);
			this.mobsKilledTracker.put(
				new ResourceLocation(compoundTag.getString("id")),
				compoundTag.getInt("count")
			);
		}
	}

	public static void resetValueOnRespawn(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayerNew && event.getOriginal() instanceof ServerPlayer serverPlayerOld) {
			serverPlayerOld.reviveCaps();
			
			serverPlayerNew.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capabilityNew ->
					serverPlayerOld.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capabilityOld ->
							capabilityNew.deserializeNBT(capabilityOld.serializeNBT())));

			if (!event.isWasDeath()) {
				serverPlayerNew.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).ifPresent(capabilityNew ->
						serverPlayerOld.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).ifPresent(capabilityOld ->
								capabilityNew.deserializeNBT(capabilityOld.serializeNBT())));

				serverPlayerNew.getCapability(BzCapabilities.NEUROTOXINS_MISS_COUNTER_CAPABILITY).ifPresent(capabilityNew ->
						serverPlayerOld.getCapability(BzCapabilities.NEUROTOXINS_MISS_COUNTER_CAPABILITY).ifPresent(capabilityOld ->
								capabilityNew.deserializeNBT(capabilityOld.serializeNBT())));

				serverPlayerNew.getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).ifPresent(capabilityNew ->
						serverPlayerOld.getCapability(BzCapabilities.ORIGINAL_FLYING_SPEED_CAPABILITY).ifPresent(capabilityOld ->
								capabilityNew.deserializeNBT(capabilityOld.serializeNBT())));
			}

			if (BzGeneralConfigs.keepEssenceOfTheBeesOnRespawning.get() || !event.isWasDeath()) {
				EssenceOfTheBees.setEssence(serverPlayerNew, EssenceOfTheBees.hasEssence(serverPlayerOld));
			}
			else {
				EssenceOfTheBees.setEssence(serverPlayerNew, false);

				Component message = Component.translatable("system.the_bumblezone.lost_bee_essence").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
				serverPlayerNew.displayClientMessage(message, true);
			}

			serverPlayerOld.invalidateCaps();
		}
	}

	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		ItemStack createdItem = event.getCrafting();
		if (event.getEntity() instanceof ServerPlayer serverPlayer &&
			createdItem.getItem() instanceof BlockItem blockItem &&
			blockItem.getBlock() instanceof BeehiveBlock &&
			rootAdvancementDone(serverPlayer))
		{
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentlyCraftedCount = capability.craftedBeehives + 1;
				BzCriterias.BEEHIVE_CRAFTED_TRIGGER.trigger(serverPlayer, currentlyCraftedCount);
				capability.craftedBeehives = currentlyCraftedCount;
			});
		}
	}

	public static void onBeeBreed(BabyEntitySpawnEvent event) {
		if (!event.isCanceled() &&
			event.getChild() instanceof Bee &&
			event.getCausedByPlayer() instanceof ServerPlayer serverPlayer &&
			rootAdvancementDone(serverPlayer))
		{
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentlyBredBees = capability.beesBred + 1;
				BzCriterias.BEE_BREEDING_TRIGGER.trigger(serverPlayer, currentlyBredBees);
				capability.beesBred = currentlyBredBees;
			});
		}
	}

	public static void onFlowerSpawned(Player player) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentlySpawnedFlowers = capability.flowersSpawned + 1;
				BzCriterias.POLLEN_PUFF_SPAWN_FLOWERS_TRIGGER.trigger(serverPlayer, currentlySpawnedFlowers);
				capability.flowersSpawned = currentlySpawnedFlowers;
			});
		}
	}

	public static void onEntityKilled(LivingDeathEvent event) {
		DamageSource damageSource = event.getSource();
		if (!event.isCanceled() &&
			event.getEntity() != null &&
			damageSource != null &&
			damageSource.getEntity() instanceof ServerPlayer serverPlayer &&
			rootAdvancementDone(serverPlayer))
		{
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				ResourceLocation killedEntity = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
				int killedCount = capability.mobsKilledTracker.getOrDefault(killedEntity, 0);
				killedCount += 1;

				BzCriterias.KILLED_COUNTER_TRIGGER.trigger(serverPlayer, killedEntity, killedCount);
				capability.mobsKilledTracker.put(killedEntity, killedCount);
			});
		}
	}

	public static void onHoneyBottleDrank(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer &&
			event.getItem().is(BzTags.HONEY_DRUNK_TRIGGER_ITEMS)
			&& rootAdvancementDone(serverPlayer))
		{
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentlyDrankHoneyBottles = capability.honeyBottleDrank + 1;
				BzCriterias.HONEY_BOTTLE_DRANK_TRIGGER.trigger(serverPlayer, currentlyDrankHoneyBottles);
				capability.honeyBottleDrank = currentlyDrankHoneyBottles;
			});
		}
	}

	public static void onBeeStingerFired(Player player) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentBeeStingersFired = capability.beeStingersFired + 1;
				BzCriterias.BEE_STINGER_SHOOTER_TRIGGER.trigger(serverPlayer, currentBeeStingersFired);
				capability.beeStingersFired = currentBeeStingersFired;
			});
		}
	}

	public static void onBeesSaved(Player player) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentBeesSaved = capability.beeSaved + 1;
				BzCriterias.BEE_SAVED_BY_STINGER_TRIGGER.trigger(serverPlayer, currentBeesSaved);
				capability.beeSaved = currentBeesSaved;
			});
		}
	}

	public static void onPollenHit(Player player) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentPollenPuffHits = capability.pollenPuffHits + 1;
				BzCriterias.POLLEN_PUFF_HIT_TRIGGER.trigger(serverPlayer, currentPollenPuffHits);
				capability.pollenPuffHits = currentPollenPuffHits;
			});
		}
	}

	public static void onHoneySlimeBred(BabyEntitySpawnEvent event) {
		if (!event.isCanceled() &&
			event.getChild() instanceof HoneySlimeEntity &&
			event.getCausedByPlayer() instanceof ServerPlayer serverPlayer &&
			rootAdvancementDone(serverPlayer))
		{
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentlyHoneySlimeBred = capability.honeySlimeBred + 1;
				BzCriterias.HONEY_SLIME_BRED_TRIGGER.trigger(serverPlayer, currentlyHoneySlimeBred);
				capability.honeySlimeBred = currentlyHoneySlimeBred;
			});
		}
	}

	public static void onBeesFed(Player player) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentBeesFed = capability.beesFed + 1;
				BzCriterias.BEE_FED_TRIGGER.trigger(serverPlayer, currentBeesFed);
				capability.beesFed = currentBeesFed;
			});
		}
	}

	public static void onQueenBeeTrade(Player player) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentTrades = capability.queenBeeTrade + 1;
				BzCriterias.BEE_QUEEN_TRADING_TRIGGER.trigger(serverPlayer, currentTrades);
				capability.queenBeeTrade = currentTrades;
			});
		}
	}

	public static void onQueenBeeTrade(Player player, int tradedItems) {
		if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
			serverPlayer.getCapability(BzCapabilities.ENTITY_MISC).ifPresent(capability -> {
				int currentTrades = capability.queenBeeTrade + tradedItems;
				BzCriterias.BEE_QUEEN_TRADING_TRIGGER.trigger(serverPlayer, currentTrades);
				capability.queenBeeTrade = currentTrades;
			});
		}
	}

	public static boolean rootAdvancementDone(ServerPlayer serverPlayer) {
		Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(BzCriterias.QUEENS_DESIRE_ROOT_ADVANCEMENT);
		Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
		return advancement != null &&
				advancementsProgressMap.containsKey(advancement) &&
				advancementsProgressMap.get(advancement).isDone();
	}
}