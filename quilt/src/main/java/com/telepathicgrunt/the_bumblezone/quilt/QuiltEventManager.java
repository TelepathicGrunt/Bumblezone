package com.telepathicgrunt.the_bumblezone.quilt;

import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterVillagerTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterDataSerializersEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterFlammabilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterReloadListenerEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.TagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.fabricbase.FabricBaseEventManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.block.content.registry.api.BlockContentRegistries;
import org.quiltmc.qsl.block.content.registry.api.FlammableBlockEntry;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.entity.networking.api.tracked_data.QuiltTrackedDataHandlerRegistry;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldLoadEvents;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldTickEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;
import org.quiltmc.qsl.villager.api.TradeOfferHelper;

public class QuiltEventManager {

    public static void init() {
        FabricBaseEventManager.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> RegisterCommandsEvent.EVENT.invoke(new RegisterCommandsEvent(dispatcher, selection, context)));
        ServerWorldTickEvents.START.register((server, world) -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, false)));
        ServerWorldTickEvents.END.register((server, world) -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, true)));
        ServerLifecycleEvents.STARTING.register(server -> ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(server)));
        ServerLifecycleEvents.STOPPING.register(server -> ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE));

        ServerWorldLoadEvents.LOAD.register((server, level) -> registerTrades());

        RegisterFlammabilityEvent.EVENT.invoke(new RegisterFlammabilityEvent((block, igniteOdds, burnOdds) -> {
            BlockContentRegistries.FLAMMABLE.put(block, new FlammableBlockEntry(igniteOdds, burnOdds));
        }));
        RegisterReloadListenerEvent.EVENT.invoke(new RegisterReloadListenerEvent((id, listener) ->
                ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new QuiltReloadListener(id, listener))));
        RegisterSpawnPlacementsEvent.EVENT.invoke(new RegisterSpawnPlacementsEvent(QuiltEventManager::registerPlacement));
        CommonLifecycleEvents.TAGS_LOADED.register((registry, client) ->
                TagsUpdatedEvent.EVENT.invoke(new TagsUpdatedEvent(registry, client)));
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockentity) ->
                !BlockBreakEvent.EVENT_LOWEST.invoke(new BlockBreakEvent(player, state)));

        AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) ->
                ResourceLoader.registerBuiltinResourcePack(
                        id,
                        QuiltLoader.getModContainer(id.getNamespace()).orElseThrow(),
                        switch (mode) {
                            case USER_CONTROLLED -> ResourcePackActivationType.NORMAL;
                            case ENABLED_BY_DEFAULT -> ResourcePackActivationType.DEFAULT_ENABLED;
                            case FORCE_ENABLED -> ResourcePackActivationType.ALWAYS_ENABLED;
                        },
                        displayName
                )
        ));

        RegisterDataSerializersEvent.EVENT.invoke(new RegisterDataSerializersEvent(QuiltTrackedDataHandlerRegistry::register));
    }

    private static <T extends Mob> void registerPlacement(EntityType<T> type, RegisterSpawnPlacementsEvent.Placement<T> placement) {
        SpawnPlacements.register(type, placement.spawn(), placement.height(), placement.predicate());
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

}
