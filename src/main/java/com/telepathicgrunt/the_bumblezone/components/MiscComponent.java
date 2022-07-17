package com.telepathicgrunt.the_bumblezone.components;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.items.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import dev.architectury.event.events.common.PlayerEvent;
import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;

import java.util.Map;

public class MiscComponent implements Component {

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

    @Override
    public void writeToNbt(CompoundTag nbt) {
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
    }

    @Override
    public void readFromNbt(CompoundTag nbtTag) {
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
        if (BzConfig.keepBeeEssenceOnRespawning && event.isWasDeath()) {
            if (event.getEntity() instanceof ServerPlayer serverPlayerNew && event.getOriginal() instanceof ServerPlayer serverPlayerOld) {
                serverPlayerOld.reviveCaps();
                EssenceOfTheBees.setEssence(serverPlayerNew, EssenceOfTheBees.hasEssence(serverPlayerOld));
                serverPlayerOld.invalidateCaps();
            }
        }
    }

    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack createdItem = event.getCrafting();
        if (event.getEntity() instanceof ServerPlayer serverPlayer &&
                createdItem.getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof BeehiveBlock &&
                rootAdvancementDone(serverPlayer))
        {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentlyCraftedCount = capability.craftedBeehives + 1;
            BzCriterias.BEEHIVE_CRAFTED_TRIGGER.trigger(serverPlayer, currentlyCraftedCount);
            capability.craftedBeehives = currentlyCraftedCount;
        }
    }

    public static void onBeeBreed(BabyEntitySpawnEvent event) {
        if (!event.isCanceled() &&
                event.getChild() instanceof Bee &&
                event.getCausedByPlayer() instanceof ServerPlayer serverPlayer &&
                rootAdvancementDone(serverPlayer))
        {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentlyBredBees = capability.beesBred + 1;
            BzCriterias.BEE_BREEDING_TRIGGER.trigger(serverPlayer, currentlyBredBees);
            capability.beesBred = currentlyBredBees;
        }
    }

    public static void onFlowerSpawned(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentlySpawnedFlowers = capability.flowersSpawned + 1;
            BzCriterias.POLLEN_PUFF_SPAWN_FLOWERS_TRIGGER.trigger(serverPlayer, currentlySpawnedFlowers);
            capability.flowersSpawned = currentlySpawnedFlowers;
        }
    }

    public static void onEntityKilled(LivingDeathEvent event) {
        DamageSource damageSource = event.getSource();
        if (!event.isCanceled() &&
                event.getEntity() instanceof LivingEntity &&
                damageSource != null &&
                damageSource.getEntity() instanceof ServerPlayer serverPlayer &&
                rootAdvancementDone(serverPlayer))
        {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            ResourceLocation killedEntity = Registry.ENTITY_TYPE.getKey(event.getEntity().getType());
            int killedCount = capability.mobsKilledTracker.getOrDefault(killedEntity, 0);
            killedCount += 1;

            BzCriterias.KILLED_COUNTER_TRIGGER.trigger(serverPlayer, killedEntity, killedCount);
            capability.mobsKilledTracker.put(killedEntity, killedCount);
        }
    }

    public static void onHoneyBottleDrank(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer &&
                event.getItem().is(Items.HONEY_BOTTLE)
                && rootAdvancementDone(serverPlayer))
        {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentlyDrankHoneyBottles = capability.honeyBottleDrank + 1;
            BzCriterias.HONEY_BOTTLE_DRANK_TRIGGER.trigger(serverPlayer, currentlyDrankHoneyBottles);
            capability.honeyBottleDrank = currentlyDrankHoneyBottles;
        }
    }

    public static void onBeeStingerFired(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentBeeStingersFired = capability.beeStingersFired + 1;
            BzCriterias.BEE_STINGER_SHOOTER_TRIGGER.trigger(serverPlayer, currentBeeStingersFired);
            capability.beeStingersFired = currentBeeStingersFired;
        }
    }

    public static void onBeesSaved(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentBeesSaved = capability.beeSaved + 1;
            BzCriterias.BEE_SAVED_BY_STINGER_TRIGGER.trigger(serverPlayer, currentBeesSaved);
            capability.beeSaved = currentBeesSaved;
        }
    }

    public static void onPollenHit(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentPollenPuffHits = capability.pollenPuffHits + 1;
            BzCriterias.POLLEN_PUFF_HIT_TRIGGER.trigger(serverPlayer, currentPollenPuffHits);
            capability.pollenPuffHits = currentPollenPuffHits;
        }
    }

    public static void onHoneySlimeBred(BabyEntitySpawnEvent event) {
        if (!event.isCanceled() &&
                event.getChild() instanceof HoneySlimeEntity &&
                event.getCausedByPlayer() instanceof ServerPlayer serverPlayer &&
                rootAdvancementDone(serverPlayer))
        {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentlyHoneySlimeBred = capability.honeySlimeBred + 1;
            BzCriterias.HONEY_SLIME_BRED_TRIGGER.trigger(serverPlayer, currentlyHoneySlimeBred);
            capability.honeySlimeBred = currentlyHoneySlimeBred;
        }
    }

    public static void onBeesFed(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentBeesFed = capability.beesFed + 1;
            BzCriterias.BEE_FED_TRIGGER.trigger(serverPlayer, currentBeesFed);
            capability.beesFed = currentBeesFed;
        }
    }

    public static void onQueenBeeTrade(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            MiscComponent capability = Bumblezone.MISC_COMPONENT.get(serverPlayer);
            int currentTrades = capability.queenBeeTrade + 1;
            BzCriterias.BEE_QUEEN_TRADING_TRIGGER.trigger(serverPlayer, currentTrades);
            capability.queenBeeTrade = currentTrades;
        }
    }

    private static boolean rootAdvancementDone(ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(BzCriterias.QUEENS_DESIRE_ROOT_ADVANCEMENT);
        Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
        return advancement != null &&
                advancementsProgressMap.containsKey(advancement) &&
                advancementsProgressMap.get(advancement).isDone();
    }
}