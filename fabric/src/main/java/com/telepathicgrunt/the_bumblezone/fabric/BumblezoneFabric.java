package com.telepathicgrunt.the_bumblezone.fabric;

import com.google.common.collect.Lists;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.*;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BumblezoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Bumblezone.init();

        AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        id,
                        FabricLoader.getInstance().getModContainer(id.getNamespace()).orElseThrow(),
                        displayName,
                        toType(mode)
                ))
        );

        RegisterCreativeTabsEvent.EVENT.invoke(new RegisterCreativeTabsEvent((id, initializer, initialDisplayItems) -> {
            CreativeModeTab.Builder builder = FabricItemGroup.builder(id);
            initializer.accept(builder);
            builder.displayItems((flags, output, bl) -> {
                List<ItemStack> stacks = Lists.newArrayList();
                initialDisplayItems.accept(stacks);
                output.acceptAll(stacks);
            });
            builder.build();
        }));

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) ->
                AddCreativeTabEntriesEvent.EVENT.invoke(new AddCreativeTabEntriesEvent(toType(tab), tab, entries::accept)));

        ServerTickEvents.START_WORLD_TICK.register(world -> LevelTickEvent.EVENT.invoke(new LevelTickEvent(world, false)));
        ServerTickEvents.END_WORLD_TICK.register(world -> LevelTickEvent.EVENT.invoke(new LevelTickEvent(world, true)));

        ServerLifecycleEvents.SERVER_STARTING.register((a) -> ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(a)));
        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE));
        RegisterEntityAttributesEvent.EVENT.invoke(new RegisterEntityAttributesEvent(FabricDefaultAttributeRegistry::register));

        SetupEvent.EVENT.invoke(new SetupEvent(Runnable::run));
        FinalSetupEvent.EVENT.invoke(new FinalSetupEvent(Runnable::run));

        ServerWorldEvents.LOAD.register((server, level) -> {
            setupWanderingTrades();
            setupVillagerTrades();
        });

        RegisterFlammabilityEvent.EVENT.invoke(new RegisterFlammabilityEvent(FlammableBlockRegistry.getDefaultInstance()::add));

        RegisterReloadListenerEvent.EVENT.invoke(new RegisterReloadListenerEvent((id, listener) ->
                ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricReloadListener(id, listener))));
        RegisterSpawnPlacementsEvent.EVENT.invoke(new RegisterSpawnPlacementsEvent(BumblezoneFabric::registerPlacement));
        CommonLifecycleEvents.TAGS_LOADED.register((registry, client) ->
                TagsUpdatedEvent.EVENT.invoke(new TagsUpdatedEvent(registry, client)));
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockentity) ->
                !BlockBreakEvent.EVENT_LOWEST.invoke(new BlockBreakEvent(player, state)));
        CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) ->
                RegisterCommandsEvent.EVENT.invoke(new RegisterCommandsEvent(dispatcher, environment, context)));
    }

    private static <T extends Mob> void registerPlacement(EntityType<T> type, RegisterSpawnPlacementsEvent.Placement<T> placement) {
        SpawnPlacements.register(type, placement.spawn(), placement.height(), placement.predicate());
    }

    private static AddCreativeTabEntriesEvent.Type toType(CreativeModeTab tab) {
        if (CreativeModeTabs.BUILDING_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.BUILDING;
        else if (CreativeModeTabs.COLORED_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.COLORED;
        else if (CreativeModeTabs.NATURAL_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.NATURAL;
        else if (CreativeModeTabs.FUNCTIONAL_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.FUNCTIONAL;
        else if (CreativeModeTabs.REDSTONE_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.REDSTONE;
        else if (CreativeModeTabs.TOOLS_AND_UTILITIES.equals(tab)) return AddCreativeTabEntriesEvent.Type.TOOLS;
        else if (CreativeModeTabs.COMBAT.equals(tab)) return AddCreativeTabEntriesEvent.Type.COMBAT;
        else if (CreativeModeTabs.FOOD_AND_DRINKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.FOOD;
        else if (CreativeModeTabs.INGREDIENTS.equals(tab)) return AddCreativeTabEntriesEvent.Type.INGREDIENTS;
        else if (CreativeModeTabs.SPAWN_EGGS.equals(tab)) return AddCreativeTabEntriesEvent.Type.SPAWN_EGGS;
        else if (CreativeModeTabs.OP_BLOCKS.equals(tab)) return AddCreativeTabEntriesEvent.Type.OPERATOR;
        return AddCreativeTabEntriesEvent.Type.CUSTOM;
    }

    private static ResourcePackActivationType toType(AddBuiltinResourcePacks.PackMode mode) {
        return switch (mode) {
            case USER_CONTROLLED -> ResourcePackActivationType.NORMAL;
            case ENABLED_BY_DEFAULT -> ResourcePackActivationType.DEFAULT_ENABLED;
            case FORCE_ENABLED -> ResourcePackActivationType.ALWAYS_ENABLED;
        };
    }

    private static void setupWanderingTrades() {
        var trades = VillagerTrades.WANDERING_TRADER_TRADES;
        List<VillagerTrades.ItemListing> basic = Arrays.stream(trades.get(1)).collect(Collectors.toList());
        List<VillagerTrades.ItemListing> rare = Arrays.stream(trades.get(2)).collect(Collectors.toList());
        RegisterWanderingTradesEvent.EVENT.invoke(new RegisterWanderingTradesEvent(basic::add, rare::add));
        trades.put(1, basic.toArray(new VillagerTrades.ItemListing[0]));
        trades.put(2, rare.toArray(new VillagerTrades.ItemListing[0]));
    }

    private static void setupVillagerTrades() {
        var trades = VillagerTrades.TRADES;
        for (var profession : BuiltInRegistries.VILLAGER_PROFESSION) {
            if (profession == null) continue;
            Int2ObjectMap<VillagerTrades.ItemListing[]> profTrades = trades.computeIfAbsent(profession, key -> new Int2ObjectOpenHashMap<>());
            Int2ObjectMap<List<VillagerTrades.ItemListing>> listings = new Int2ObjectOpenHashMap<>();
            for (int i = 1; i <= 5; i++) {
                if (profTrades.containsKey(i)) {
                    List<VillagerTrades.ItemListing> list = Arrays.stream(profTrades.get(i)).collect(Collectors.toList());
                    listings.put(i, list);
                } else {
                    listings.put(i, new ArrayList<>());
                }
            }
            RegisterVillagerTradesEvent.EVENT.invoke(new RegisterVillagerTradesEvent(profession, (i, listing) -> listings.get(i.intValue()).add(listing)));
            for (int i = 1; i <= 5; i++) {
                profTrades.put(i, listings.get(i).toArray(new VillagerTrades.ItemListing[0]));
            }
        }
    }
}
