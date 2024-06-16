package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.events.AddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterBrewingRecipeEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterVillagerTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinDataPacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.DatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.FinalSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterDataSerializersEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterFlammabilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterReloadListenerEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.SetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.TagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemAttackBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.mixin.fabric.fabricapi.BiomeModificationContextImplMixin;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import com.telepathicgrunt.the_bumblezone.utils.fabric.PlatformHooksImpl;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FabricEventManager {

    private static final Map<ResourceKey<CreativeModeTab>, AddCreativeTabEntriesEvent.Type> TYPES = Util.make(new IdentityHashMap<>(), map -> {
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
        AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) -> {
            ModContainer container = getModPack(id);
            ResourceManagerHelper.registerBuiltinResourcePack(
                    ResourceLocation.fromNamespaceAndPath(container.getMetadata().getId(), id.getPath()),
                    container,
                    displayName,
                    toType(mode)
            );
        }));

        AddBuiltinDataPacks.EVENT.invoke(new AddBuiltinDataPacks((id, displayName, mode) -> {
            ModContainer container = getModPack(id);
            ResourceManagerHelperImpl.registerBuiltinResourcePack(
                    ResourceLocation.fromNamespaceAndPath(container.getMetadata().getId(), id.getPath()),
                    "datapacks/" + id.getPath(),
                    container,
                    displayName,
                    toType(mode)
            );
        }));

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) ->
                AddCreativeTabEntriesEvent.EVENT.invoke(new AddCreativeTabEntriesEvent(
                        TYPES.getOrDefault(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null), AddCreativeTabEntriesEvent.Type.CUSTOM),
                        tab,
                        entries.shouldShowOpRestrictedItems(),
                        entries::accept)));

        RegisterEntityAttributesEvent.EVENT.invoke(new RegisterEntityAttributesEvent(FabricDefaultAttributeRegistry::register));
        SetupEvent.EVENT.invoke(new SetupEvent(Runnable::run));
        FinalSetupEvent.EVENT.invoke(new FinalSetupEvent(Runnable::run));

        RegisterDataSerializersEvent.EVENT.invoke(new RegisterDataSerializersEvent((id, serializer) -> EntityDataSerializers.registerSerializer(serializer)));

        ServerTickEvents.START_WORLD_TICK.register(world -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, false)));
        ServerTickEvents.END_WORLD_TICK.register(world -> ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(world, true)));

        ServerLifecycleEvents.SERVER_STARTING.register((a) -> {
            ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(a));
            PlatformHooksImpl.currentRegistryAccess = a.registryAccess();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> {
            ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE);
            PlatformHooksImpl.currentRegistryAccess = null;
        });

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

        RegisterBrewingRecipeEvent.EVENT.invoke(new RegisterBrewingRecipeEvent((input, item, output) ->
            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> builder.registerPotionRecipe(input, Ingredient.of(item), output))));

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) ->
                DatapackSyncEvent.EVENT.invoke(new DatapackSyncEvent(player)));

        AttackBlockCallback.EVENT.register(FabricEventManager::onItemAttackBlock);
        UseBlockCallback.EVENT.register(FabricEventManager::onItemUseOnBlock);
        UseItemCallback.EVENT.register(FabricEventManager::onItemUse);
        ServerLivingEntityEvents.ALLOW_DEATH.register(FabricEventManager::allowLivingEntityDeath);

        FluidStorage.combinedItemApiProvider(BzItems.HONEY_BUCKET.get()).register(context ->
                new FullItemFluidStorage(context, bottle -> ItemVariant.of(Items.BUCKET), FluidVariant.of(BzFluids.HONEY_FLUID.get()), FluidConstants.BUCKET));

        FluidStorage.combinedItemApiProvider(BzItems.ROYAL_JELLY_BUCKET.get()).register(context ->
                new FullItemFluidStorage(context, bottle -> ItemVariant.of(Items.BUCKET), FluidVariant.of(BzFluids.ROYAL_JELLY_FLUID.get()), FluidConstants.BUCKET));

        FluidStorage.combinedItemApiProvider(BzItems.ROYAL_JELLY_BOTTLE.get()).register(context ->
                new FullItemFluidStorage(context, bottle -> ItemVariant.of(Items.GLASS_BOTTLE), FluidVariant.of(BzFluids.ROYAL_JELLY_FLUID.get()), FluidConstants.BUCKET / 4));

        FluidStorage.combinedItemApiProvider(Items.GLASS_BOTTLE).register(context ->
                new EmptyItemFluidStorage(context, bottle -> ItemVariant.of(BzItems.ROYAL_JELLY_BOTTLE.get()), BzFluids.ROYAL_JELLY_FLUID.get(), FluidConstants.BUCKET / 4));

        FluidStorage.combinedItemApiProvider(BzItems.SUGAR_WATER_BUCKET.get()).register(context ->
                new FullItemFluidStorage(context, bottle -> ItemVariant.of(Items.BUCKET), FluidVariant.of(BzFluids.SUGAR_WATER_FLUID.get()), FluidConstants.BUCKET));

        FluidStorage.combinedItemApiProvider(BzItems.SUGAR_WATER_BOTTLE.get()).register(context ->
                new FullItemFluidStorage(context, bottle -> ItemVariant.of(Items.GLASS_BOTTLE), FluidVariant.of(BzFluids.SUGAR_WATER_FLUID.get()), FluidConstants.BUCKET / 4));

        FluidStorage.combinedItemApiProvider(Items.GLASS_BOTTLE).register(context ->
                new EmptyItemFluidStorage(context, bottle -> ItemVariant.of(BzItems.SUGAR_WATER_BOTTLE.get()), BzFluids.SUGAR_WATER_FLUID.get(), FluidConstants.BUCKET / 4));
    }

    public static void lateInit() {
        if (PlatformHooks.isModLoaded("resourcefulbees") && BzModCompatibilityConfigs.spawnResourcefulBeesHoneycombVeins) {
            BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "resourceful_bees_compat"))
                .add(ModificationPhase.ADDITIONS,
                    (context) -> context.hasTag(TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, Bumblezone.MODID))),
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

    private static ResourcePackActivationType toType(AddBuiltinDataPacks.PackMode mode) {
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

    public static InteractionResult onItemAttackBlock(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction) {
        PlayerItemAttackBlockEvent event = new PlayerItemAttackBlockEvent(player, world, hand, player.getItemInHand(hand));
        InteractionResult result = PlayerItemAttackBlockEvent.EVENT_HIGH.invoke(event);
        return result != null ? result : InteractionResult.PASS;
    }

    public static InteractionResult onItemUseOnBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        PlayerItemUseOnBlockEvent event = new PlayerItemUseOnBlockEvent(player, world, hand, hitResult, player.getItemInHand(hand));
        InteractionResult result = PlayerItemUseOnBlockEvent.EVENT_HIGH.invoke(event);
        return result != null ? result : InteractionResult.PASS;
    }

    public static InteractionResultHolder<ItemStack> onItemUse(Player player, Level level, InteractionHand hand) {
        PlayerItemUseEvent event = new PlayerItemUseEvent(player, level, player.getItemInHand(hand));
        if (PlayerItemUseEvent.EVENT_HIGH.invoke(event)) {
            return InteractionResultHolder.success(event.usingStack());
        }
        return InteractionResultHolder.pass(event.usingStack());
    }

    private static boolean allowLivingEntityDeath(LivingEntity livingEntity, DamageSource damageSource, float damage) {
        if (EntityDeathEvent.EVENT.invoke(new EntityDeathEvent(livingEntity, damageSource))) {
            return false;
        }
        else if (EntityDeathEvent.EVENT_LOWEST.invoke(new EntityDeathEvent(livingEntity, damageSource))) {
            return false;
        }

        return true;
    }
}
