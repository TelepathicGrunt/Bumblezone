package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.ItemUseOnBlockEvent;
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
import com.telepathicgrunt.the_bumblezone.mixin.fabric.fabricapi.BiomeModificationContextImplMixin;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FabricEventManager {

    public static void init() {
        AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) -> {
            ModContainer container = getModPack(id);
            ResourceManagerHelper.registerBuiltinResourcePack(
                    new ResourceLocation(container.getMetadata().getId(), id.getPath()),
                    container,
                    displayName,
                    toType(mode)
            );
        }));

        FabricBaseEventManager.init();

        RegisterDataSerializersEvent.EVENT.invoke(new RegisterDataSerializersEvent((id, serializer) -> EntityDataSerializers.registerSerializer(serializer)));

        ServerTickEvents.START_WORLD_TICK.register(world -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, false)));
        ServerTickEvents.END_WORLD_TICK.register(world -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, true)));

        ServerLifecycleEvents.SERVER_STARTING.register((a) -> ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(a)));
        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE));

        ServerWorldEvents.LOAD.register((server, level) -> {
            setupWanderingTrades();
            setupVillagerTrades();
        });

        RegisterFlammabilityEvent.EVENT.invoke(new RegisterFlammabilityEvent(FlammableBlockRegistry.getDefaultInstance()::add));

        RegisterReloadListenerEvent.EVENT.invoke(new RegisterReloadListenerEvent((id, listener) ->
                ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricReloadListener(id, listener))));
        RegisterSpawnPlacementsEvent.EVENT.invoke(new RegisterSpawnPlacementsEvent(FabricEventManager::registerPlacement));
        CommonLifecycleEvents.TAGS_LOADED.register((registry, client) ->
                TagsUpdatedEvent.EVENT.invoke(new TagsUpdatedEvent(registry, client)));
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockentity) ->
                !BlockBreakEvent.EVENT_LOWEST.invoke(new BlockBreakEvent(player, state)));
        CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) ->
                RegisterCommandsEvent.EVENT.invoke(new RegisterCommandsEvent(dispatcher, environment, context)));

        UseBlockCallback.EVENT.register(FabricEventManager::onItemUseOnBlock);

    }

    public static void lateInit() {
        if (PlatformHooks.isModLoaded("resourcefulbees") && BzModCompatibilityConfigs.spawnResourcefulBeesHoneycombVeins) {
            BiomeModifications.create(new ResourceLocation(Bumblezone.MODID, "resourceful_bees_compat"))
                .add(ModificationPhase.ADDITIONS,
                    (context) -> context.hasTag(TagKey.create(Registries.BIOME, new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID))),
                    (context) -> {
                        for (Holder<PlacedFeature> placedFeatureHolder : getPlacedFeaturesByTag(context, BzTags.RESOURCEFUL_BEES_COMBS)) {
                            FeatureConfiguration featureConfiguration = placedFeatureHolder.value().feature().value().config();

                            if (featureConfiguration instanceof OreConfiguration oreConfiguration && oreConfiguration.targetStates.stream().noneMatch(e -> e.state.isAir())) {
                                context.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placedFeatureHolder.unwrapKey().get());
                            }
                        }
                    });
        }
    }

    private static ModContainer getModPack(ResourceLocation pack) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                if (mod.getMetadata().getId().startsWith("generated_") && mod.findPath("resourcepacks/" + pack.getPath()).isPresent()) {
                    return mod;
                }
            }
        }
        return FabricLoader.getInstance().getModContainer(pack.getNamespace()).orElseThrow();
    }

    private static <T extends Mob> void registerPlacement(EntityType<T> type, RegisterSpawnPlacementsEvent.Placement<T> placement) {
        SpawnPlacements.register(type, placement.spawn(), placement.height(), placement.predicate());
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

    private static Iterable<Holder<PlacedFeature>> getPlacedFeaturesByTag(BiomeModificationContext context, TagKey<PlacedFeature> placedFeatureTagKey) {
        RegistryAccess registryAccess = ((BiomeModificationContextImplMixin)context).getRegistries();
        Registry<PlacedFeature> placedFeatureRegistry = registryAccess.registryOrThrow(Registries.PLACED_FEATURE);
        return placedFeatureRegistry.getTagOrEmpty(placedFeatureTagKey);
    }

    public static InteractionResult onItemUseOnBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemUseOnBlockEvent event = new ItemUseOnBlockEvent(player, hitResult.getBlockPos(), world.getBlockState(hitResult.getBlockPos()), player.getItemInHand(hand));
        if (ItemUseOnBlockEvent.EVENT_HIGH.invoke(event)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
