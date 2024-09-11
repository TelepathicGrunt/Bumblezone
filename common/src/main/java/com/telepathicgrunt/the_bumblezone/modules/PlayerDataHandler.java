package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.events.entity.BzBabySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzFinishUseItemEvent;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;

public class PlayerDataHandler {

    public static void initEvents() {
        BzBabySpawnEvent.EVENT.addListener(PlayerDataHandler::onBeeBreed);
        BzBabySpawnEvent.EVENT.addListener(PlayerDataHandler::onHoneySlimeBred);
        BzEntityDeathEvent.EVENT_LOWEST.addListener(PlayerDataHandler::onEntityKilled);
        BzFinishUseItemEvent.EVENT.addListener(PlayerDataHandler::onHoneyBottleDrank);
    }

    public static void onBeeBreed(boolean cancelled, BzBabySpawnEvent event) {
        if (cancelled) return;
        if (!(event.child() instanceof Bee)) return;
        if (event.player() instanceof ServerPlayer player && rootAdvancementDone(player)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.beesBred++;
                BzCriterias.BEE_BREEDING_TRIGGER.get().trigger(player, module.beesBred);
            });
        }
    }

    public static void onFlowerSpawned(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.flowersSpawned++;
                BzCriterias.POLLEN_PUFF_SPAWN_FLOWERS_TRIGGER.get().trigger(serverPlayer, module.flowersSpawned);
            });
        }
    }

    public static void onEntityKilled(boolean cancelled, BzEntityDeathEvent event) {
        if (cancelled) return;
        if (event.entity() == null) return;
        if (event.source() == null) return;
        if (event.source().getEntity() instanceof ServerPlayer player && rootAdvancementDone(player)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                ResourceLocation id = EntityType.getKey(event.entity().getType());
                if (id != null) {
                    BzCriterias.KILLED_COUNTER_TRIGGER.get().trigger(player, event.entity(), module);
                }
            });
        }
    }

    public static void onHoneyBottleDrank(BzFinishUseItemEvent event) {
        if (!event.item().is(BzTags.HONEY_DRUNK_TRIGGER_ITEMS)) return;

        if (event.user() instanceof ServerPlayer player && rootAdvancementDone(player)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.honeyBottleDrank++;
                BzCriterias.HONEY_BOTTLE_DRANK_TRIGGER.get().trigger(player, module.honeyBottleDrank);
            });
        }
    }

    public static void onBeeStingerFired(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.beeStingersFired++;
                BzCriterias.BEE_STINGER_SHOOTER_TRIGGER.get().trigger(serverPlayer, module.beeStingersFired);
            });
        }
    }

    public static void onBeesSaved(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.beeSaved++;
                BzCriterias.BEE_SAVED_BY_STINGER_TRIGGER.get().trigger(serverPlayer, module.beeSaved);
            });
        }
    }

    public static void onPollenHit(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.pollenPuffHits++;
                BzCriterias.POLLEN_PUFF_HIT_TRIGGER.get().trigger(serverPlayer, module.pollenPuffHits);
            });
        }
    }

    public static void onHoneySlimeBred(boolean cancelled, BzBabySpawnEvent event) {
        if (cancelled) return;
        if (!(event.child() instanceof HoneySlimeEntity)) return;
        if (event.player() instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(serverPlayer, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.honeySlimeBred++;
                BzCriterias.HONEY_SLIME_BRED_TRIGGER.get().trigger(serverPlayer, module.honeySlimeBred);
            });
        }
    }

    public static void onBeesFed(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.beesFed++;
                BzCriterias.BEE_FED_TRIGGER.get().trigger(serverPlayer, module.beesFed);
            });
        }
    }

    public static void onQueenBeeTrade(Player player, int tradedItems) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.PLAYER_DATA).ifPresent(module -> {
                module.queenBeeTrade += tradedItems;
                BzCriterias.BEE_QUEEN_TRADING_TRIGGER.get().trigger(serverPlayer, module.queenBeeTrade);
            });
        }
    }

    public static void onQueenBeeTrade(Player player) {
        onQueenBeeTrade(player, 1);
    }

    public static boolean rootAdvancementDone(ServerPlayer serverPlayer) {
        AdvancementHolder advancementHolder = serverPlayer.server.getAdvancements().get(BzCriterias.QUEENS_DESIRE_ROOT_ADVANCEMENT);
        if (advancementHolder == null) {
            return false;
        }

        var progress = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getProgress();
        return progress.containsKey(advancementHolder) && progress.get(advancementHolder).isDone();
    }
}
