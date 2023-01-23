package com.telepathicgrunt.the_bumblezone.quilt;

import com.google.common.collect.Lists;
import com.telepathicgrunt.the_bumblezone.events.*;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerLevelTickEvent;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldTickEvents;
import org.quiltmc.qsl.villager.api.TradeOfferHelper;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class BumblezoneQuiltEventManager {

    private static final Map<CreativeModeTab, AddCreativeTabEntriesEvent.Type> TYPES = Util.make(new IdentityHashMap<>(), map -> {
        map.put(CreativeModeTabs.BUILDING_BLOCKS, AddCreativeTabEntriesEvent.Type.BUILDING);
        map.put(CreativeModeTabs.COLORED_BLOCKS, AddCreativeTabEntriesEvent.Type.COLORED);
        map.put(CreativeModeTabs.NATURAL_BLOCKS, AddCreativeTabEntriesEvent.Type.NATURAL);
        map.put(CreativeModeTabs.FUNCTIONAL_BLOCKS, AddCreativeTabEntriesEvent.Type.FUNCTIONAL);
        map.put(CreativeModeTabs.REDSTONE_BLOCKS, AddCreativeTabEntriesEvent.Type.REDSTONE);
        map.put(CreativeModeTabs.TOOLS_AND_UTILITIES, AddCreativeTabEntriesEvent.Type.TOOLS);
        map.put(CreativeModeTabs.COMBAT, AddCreativeTabEntriesEvent.Type.COMBAT);
        map.put(CreativeModeTabs.FOOD_AND_DRINKS, AddCreativeTabEntriesEvent.Type.FOOD);
        map.put(CreativeModeTabs.INGREDIENTS, AddCreativeTabEntriesEvent.Type.INGREDIENTS);
        map.put(CreativeModeTabs.SPAWN_EGGS, AddCreativeTabEntriesEvent.Type.SPAWN_EGGS);
        map.put(CreativeModeTabs.OP_BLOCKS, AddCreativeTabEntriesEvent.Type.OPERATOR);
    });

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> RegisterCommandsEvent.EVENT.invoke(new RegisterCommandsEvent(dispatcher, selection, context)));
        ServerWorldTickEvents.START.register((server, world) -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, false)));
        ServerWorldTickEvents.END.register((server, world) -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, true)));
        ServerLifecycleEvents.STARTING.register(server -> ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(server)));
        ServerLifecycleEvents.STOPPING.register(server -> ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE));
    }

    public static void registerTrades() {

        for (VillagerProfession profession : BuiltInRegistries.VILLAGER_PROFESSION) {
            RegisterVillagerTradesEvent.EVENT.invoke(new RegisterVillagerTradesEvent(profession,
                    (i, listing) -> TradeOfferHelper.registerVillagerOffers(profession, i, fill -> fill.add(listing))));
        }

        RegisterWanderingTradesEvent.EVENT.invoke(new RegisterWanderingTradesEvent(
                trade -> TradeOfferHelper.registerWanderingTraderOffers(1, fill -> fill.add(trade)),
                trade -> TradeOfferHelper.registerWanderingTraderOffers(2, fill -> fill.add(trade))
        ));
    }

    public static void registerCreativeTabs() {
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
                AddCreativeTabEntriesEvent.EVENT.invoke(new AddCreativeTabEntriesEvent(TYPES.getOrDefault(tab, AddCreativeTabEntriesEvent.Type.CUSTOM), tab, entries::accept)));

    }

}
