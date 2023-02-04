package com.telepathicgrunt.the_bumblezone.modules;

import com.telepathicgrunt.the_bumblezone.entities.mobs.HoneySlimeEntity;
import com.telepathicgrunt.the_bumblezone.events.entity.BabySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.FinishUseItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerCraftedItemEvent;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BeehiveBlock;

public class EntityMiscHandler {

    public static void initEvents() {
        PlayerCraftedItemEvent.EVENT.addListener(EntityMiscHandler::onItemCrafted);
        BabySpawnEvent.EVENT.addListener(EntityMiscHandler::onBeeBreed);
        BabySpawnEvent.EVENT.addListener(EntityMiscHandler::onHoneySlimeBred);
        EntityDeathEvent.EVENT_LOWEST.addListener(EntityMiscHandler::onEntityKilled);
        FinishUseItemEvent.EVENT.addListener(EntityMiscHandler::onHoneyBottleDrank);
    }

    public static void onItemCrafted(PlayerCraftedItemEvent event) {
        ItemStack createdItem = event.item();
        if (event.player() instanceof ServerPlayer serverPlayer &&
                createdItem.getItem() instanceof BlockItem blockItem &&
                blockItem.getBlock() instanceof BeehiveBlock &&
                rootAdvancementDone(serverPlayer))
        {
            ModuleHelper.getModule(event.player(), ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.craftedBeehives++;
                BzCriterias.BEEHIVE_CRAFTED_TRIGGER.trigger(serverPlayer, module.craftedBeehives);
            });
        }
    }

    public static void onBeeBreed(boolean cancelled, BabySpawnEvent event) {
        if (cancelled) return;
        if (!(event.child() instanceof Bee)) return;
        if (event.player() instanceof ServerPlayer player && rootAdvancementDone(player)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.beesBred++;
                BzCriterias.BEE_BREEDING_TRIGGER.trigger(player, module.beesBred);
            });
        }
    }

    public static void onFlowerSpawned(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.flowersSpawned++;
                BzCriterias.POLLEN_PUFF_SPAWN_FLOWERS_TRIGGER.trigger(serverPlayer, module.flowersSpawned);
            });
        }
    }

    public static void onEntityKilled(boolean cancelled, EntityDeathEvent event) {
        if (cancelled) return;
        if (event.entity() == null) return;
        if (event.source() == null) return;
        if (event.source().getEntity() instanceof ServerPlayer player && rootAdvancementDone(player)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                ResourceLocation id = EntityType.getKey(event.entity().getType());
                if (id != null) {
                    module.mobsKilledTracker.merge(id, 1, Integer::sum);
                    BzCriterias.KILLED_COUNTER_TRIGGER.trigger(player, id, module.mobsKilledTracker.get(id));
                }

            });
        }
    }

    public static void onHoneyBottleDrank(ItemStack result, FinishUseItemEvent event) {
        if (!event.item().is(Items.HONEY_BOTTLE)) return;

        if (event.user() instanceof ServerPlayer player && rootAdvancementDone(player)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.honeyBottleDrank++;
                BzCriterias.HONEY_BOTTLE_DRANK_TRIGGER.trigger(player, module.honeyBottleDrank);
            });
        }
    }

    public static void onBeeStingerFired(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.beeStingersFired++;
                BzCriterias.BEE_STINGER_SHOOTER_TRIGGER.trigger(serverPlayer, module.beeStingersFired);
            });
        }
    }

    public static void onBeesSaved(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.beeSaved++;
                BzCriterias.BEE_SAVED_BY_STINGER_TRIGGER.trigger(serverPlayer, module.beeSaved);
            });
        }
    }

    public static void onPollenHit(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.pollenPuffHits++;
                BzCriterias.POLLEN_PUFF_HIT_TRIGGER.trigger(serverPlayer, module.pollenPuffHits);
            });
        }
    }

    public static void onHoneySlimeBred(boolean cancelled, BabySpawnEvent event) {
        if (cancelled) return;
        if (!(event.child() instanceof HoneySlimeEntity)) return;
        if (event.player() instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(serverPlayer, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.honeySlimeBred++;
                BzCriterias.HONEY_SLIME_BRED_TRIGGER.trigger(serverPlayer, module.honeySlimeBred);
            });
        }
    }

    public static void onBeesFed(Player player) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.beesFed++;
                BzCriterias.BEE_FED_TRIGGER.trigger(serverPlayer, module.beesFed);
            });
        }
    }

    public static void onQueenBeeTrade(Player player, int tradedItems) {
        if (player instanceof ServerPlayer serverPlayer && rootAdvancementDone(serverPlayer)) {
            ModuleHelper.getModule(player, ModuleRegistry.ENTITY_MISC).ifPresent(module -> {
                module.queenBeeTrade += tradedItems;
                BzCriterias.BEE_QUEEN_TRADING_TRIGGER.trigger(serverPlayer, module.queenBeeTrade);
            });
        }
    }

    public static void onQueenBeeTrade(Player player) {
        onQueenBeeTrade(player, 1);
    }

    public static boolean rootAdvancementDone(ServerPlayer serverPlayer) {
        Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(BzCriterias.QUEENS_DESIRE_ROOT_ADVANCEMENT);
        var progress = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
        return advancement != null &&
                progress.containsKey(advancement) &&
                progress.get(advancement).isDone();
    }
}
