package com.telepathicgrunt.the_bumblezone.neoforge;

import com.google.common.util.concurrent.AtomicDouble;
import com.telepathicgrunt.the_bumblezone.configs.neoforge.BzGeneralConfig;
import com.telepathicgrunt.the_bumblezone.entities.neoforge.DisableFlightAttribute;
import com.telepathicgrunt.the_bumblezone.events.block.BzBlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzBabySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityTravelingToDimensionEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzEntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzFinishUseItemEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzRegisterVillagerTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BzRegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.item.BzRegisterBrewingRecipeEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinDataPacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzAddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzDatapackSyncEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzFinalSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterDataSerializersEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterEntityAttributesEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterFlammabilityEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterReloadListenerEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzRegisterSpawnPlacementsEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzServerGoingToStartEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzServerGoingToStopEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzSetupEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzTagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerEntityInteractEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemAttackBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerPickupItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.BzPlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.BzFluidBottlesWrapper;
import com.telepathicgrunt.the_bumblezone.fluids.neoforge.BzFluidBucketWrapper;
import com.telepathicgrunt.the_bumblezone.mixin.neoforge.block.FireBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.neoforge.NeoForgeModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

public class NeoForgeEventManager {
    public static void init(IEventBus modEventBus, IEventBus eventBus) {
        modEventBus.addListener(NeoForgeEventManager::onRegistryEvent);
        modEventBus.addListener(NeoForgeEventManager::onRegisterPackFinder);
        modEventBus.addListener(NeoForgeEventManager::onRegisterAttributes);
        modEventBus.addListener(NeoForgeEventManager::onSetup);
        modEventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onFinalSetup);
        modEventBus.addListener(NeoForgeEventManager::onAddTabContents);
        modEventBus.addListener(NeoForgeEventManager::onSpawnPlacements);
        modEventBus.addListener(NeoForgeEventManager::registerBumblezoneCapProviders);

        eventBus.addListener(NeoForgeEventManager::onBabySpawn);
        eventBus.addListener(NeoForgeEventManager::onServerStarting);
        eventBus.addListener(NeoForgeEventManager::onServerStopping);
        eventBus.addListener(NeoForgeEventManager::onAddVillagerTrades);
        eventBus.addListener(NeoForgeEventManager::onWanderingTrades);
        eventBus.addListener(NeoForgeEventManager::onRegisterCommand);
        eventBus.addListener(NeoForgeEventManager::onRegisterBrewingRecipies);
        eventBus.addListener(NeoForgeEventManager::onProjectileHit);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onItemAttackBlock);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onItemUseOnBlock);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onItemUse);
        eventBus.addListener(EventPriority.HIGH, NeoForgeEventManager::onProjectileHitHighPriority);
        eventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onBlockBreak);
        eventBus.addListener(NeoForgeEventManager::onPlayerTickPre);
        eventBus.addListener(NeoForgeEventManager::onPlayerTickPost);
        eventBus.addListener(NeoForgeEventManager::onPickupItem);
        eventBus.addListener(NeoForgeEventManager::onGrantAdvancement);
        eventBus.addListener(NeoForgeEventManager::onInteractEntity);
        eventBus.addListener(NeoForgeEventManager::onBreakSpeed);
        eventBus.addListener(NeoForgeEventManager::onTagsUpdate);
        eventBus.addListener(NeoForgeEventManager::onLevelTickPre);
        eventBus.addListener(NeoForgeEventManager::onLevelTickPost);
        eventBus.addListener(NeoForgeEventManager::onAddReloadListeners);
        eventBus.addListener(NeoForgeEventManager::onDatapackSync);
        eventBus.addListener(NeoForgeEventManager::onEntityAttacked);
        eventBus.addListener(NeoForgeEventManager::onEntityDeath);
        eventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onEntityDeathLowest);
        eventBus.addListener(NeoForgeEventManager::onEntitySpawn);
        eventBus.addListener(NeoForgeEventManager::onEntityTick);
        eventBus.addListener(NeoForgeEventManager::onEntityDimensionTravel);
        eventBus.addListener(NeoForgeEventManager::onEntityVisibility);
        eventBus.addListener(NeoForgeEventManager::onFinishUseItem);
        eventBus.addListener(EventPriority.LOWEST, NeoForgeEventManager::onEntityHurtLowest);
    }

    private static void onAddTabContents(BuildCreativeModeTabContentsEvent event) {
        BzAddCreativeTabEntriesEvent.EVENT.invoke(new BzAddCreativeTabEntriesEvent(toType(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(event.getTab()).orElse(null)), event.getTab(), event.hasPermissions(), event::accept));
    }

    private static BzAddCreativeTabEntriesEvent.Type toType(ResourceKey<CreativeModeTab> tab) {
        if (CreativeModeTabs.BUILDING_BLOCKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.BUILDING;
        else if (CreativeModeTabs.COLORED_BLOCKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.COLORED;
        else if (CreativeModeTabs.NATURAL_BLOCKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.NATURAL;
        else if (CreativeModeTabs.FUNCTIONAL_BLOCKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.FUNCTIONAL;
        else if (CreativeModeTabs.REDSTONE_BLOCKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.REDSTONE;
        else if (CreativeModeTabs.TOOLS_AND_UTILITIES.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.TOOLS;
        else if (CreativeModeTabs.COMBAT.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.COMBAT;
        else if (CreativeModeTabs.FOOD_AND_DRINKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.FOOD;
        else if (CreativeModeTabs.INGREDIENTS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.INGREDIENTS;
        else if (CreativeModeTabs.SPAWN_EGGS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.SPAWN_EGGS;
        else if (CreativeModeTabs.OP_BLOCKS.equals(tab)) return BzAddCreativeTabEntriesEvent.Type.OPERATOR;
        return BzAddCreativeTabEntriesEvent.Type.CUSTOM;
    }

    private static void onSetup(FMLCommonSetupEvent event) {
        BzSetupEvent.EVENT.invoke(new BzSetupEvent(event::enqueueWork));

        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                BzFluids.SUGAR_WATER_FLUID_TYPE.get().flowing().get().getFluidType(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState()
        ));

        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                BzFluids.SUGAR_WATER_FLUID_TYPE.get().still().get().getFluidType(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState()
        ));

        event.enqueueWork(() ->
                BzRegisterFlammabilityEvent.EVENT.invoke(new BzRegisterFlammabilityEvent((item, igniteOdds, burnOdds) ->
                        ((FireBlockInvoker) Blocks.FIRE).callSetFlammable(item, igniteOdds, burnOdds)))
        );
    }

    private static void onFinalSetup(FMLCommonSetupEvent event) {
        BzFinalSetupEvent.EVENT.invoke(new BzFinalSetupEvent(event::enqueueWork));
        event.enqueueWork(NeoForgeModChecker::setupModCompat);
    }

    private static void onServerStarting(ServerAboutToStartEvent event) {
        BzServerGoingToStartEvent.EVENT.invoke(new BzServerGoingToStartEvent(event.getServer()));
    }

    private static void onServerStopping(ServerStoppingEvent event) {
        BzServerGoingToStopEvent.EVENT.invoke(BzServerGoingToStopEvent.INSTANCE);
    }

    private static void onRegistryEvent(RegisterEvent event) {
        if (event.getRegistryKey() == NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS) {
            BzRegisterDataSerializersEvent.EVENT.invoke(new BzRegisterDataSerializersEvent(
                    (id, serializer) -> event.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, id, () -> serializer)));
        }
    }

    private static void onRegisterPackFinder(AddPackFindersEvent event) {

        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            BzAddBuiltinResourcePacks.EVENT.invoke(new BzAddBuiltinResourcePacks((id, displayName, mode) -> {
                event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "resourcepacks/" + id.getPath()),
                    PackType.CLIENT_RESOURCES,
                    displayName,
                    PackSource.BUILT_IN,
                    mode == BzAddBuiltinResourcePacks.PackMode.FORCE_ENABLED,
                    Pack.Position.BOTTOM
                );
            }));
        }

        if (event.getPackType() == PackType.SERVER_DATA) {
            BzAddBuiltinDataPacks.EVENT.invoke(new BzAddBuiltinDataPacks((id, displayName, mode) -> {
                event.addPackFinders(
                        ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "datapacks/" + id.getPath()),
                        PackType.SERVER_DATA,
                        displayName,
                        PackSource.BUILT_IN,
                        mode == BzAddBuiltinDataPacks.PackMode.FORCE_ENABLED,
                        Pack.Position.BOTTOM
                );
            }));
        }
    }

    private static void onBabySpawn(BabyEntitySpawnEvent event) {
        boolean cancel = BzBabySpawnEvent.EVENT.invoke(new BzBabySpawnEvent(event.getParentA(), event.getParentB(), event.getCausedByPlayer(), event.getChild()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        BzRegisterEntityAttributesEvent.EVENT.invoke(new BzRegisterEntityAttributesEvent((entity, builder) -> event.put(entity, builder.build())));
    }

    private static void onAddVillagerTrades(VillagerTradesEvent event) {
        BzRegisterVillagerTradesEvent.EVENT.invoke(new BzRegisterVillagerTradesEvent(event.getType(), (i, listing) -> event.getTrades().get(i.intValue()).add(listing)));
    }

    private static void onWanderingTrades(WandererTradesEvent event) {
        BzRegisterWanderingTradesEvent.EVENT.invoke(new BzRegisterWanderingTradesEvent(event.getGenericTrades()::add, event.getRareTrades()::add));
    }

    private static void onRegisterCommand(RegisterCommandsEvent event) {
        BzRegisterCommandsEvent.EVENT.invoke(new BzRegisterCommandsEvent(event.getDispatcher(), event.getCommandSelection(), event.getBuildContext()));
    }

    private static void onRegisterBrewingRecipies(RegisterBrewingRecipesEvent event) {
        BzRegisterBrewingRecipeEvent.EVENT.invoke(new BzRegisterBrewingRecipeEvent(event.getBuilder()::addMix));
    }

    private static void onProjectileHit(ProjectileImpactEvent event) {
        boolean cancel = BzProjectileHitEvent.EVENT.invoke(new BzProjectileHitEvent(event.getProjectile(), event.getRayTraceResult()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onProjectileHitHighPriority(ProjectileImpactEvent event) {
        boolean cancel = BzProjectileHitEvent.EVENT_HIGH.invoke(new BzProjectileHitEvent(event.getProjectile(), event.getRayTraceResult()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onBlockBreak(BlockEvent.BreakEvent event) {
        boolean cancel = BzBlockBreakEvent.EVENT_LOWEST.invoke(new BzBlockBreakEvent(event.getPlayer(), event.getState()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onPlayerTickPre(PlayerTickEvent.Pre event) {
        BzPlayerTickEvent eventObject = new BzPlayerTickEvent(event.getEntity(), false);

        BzPlayerTickEvent.EVENT.invoke(eventObject);
        if (event.getEntity().level().isClientSide()) {
            BzPlayerTickEvent.CLIENT_EVENT.invoke(eventObject);
        }

        DisableFlightAttribute.onPlayerTickToRemoveDisabledFlight(event);
    }

    private static void onPlayerTickPost(PlayerTickEvent.Post event) {
        BzPlayerTickEvent eventObject = new BzPlayerTickEvent(event.getEntity(), true);

        BzPlayerTickEvent.EVENT.invoke(eventObject);
        if (event.getEntity().level().isClientSide()) {
            BzPlayerTickEvent.CLIENT_EVENT.invoke(eventObject);
        }
    }

    private static void onPickupItem(ItemEntityPickupEvent.Post event) {
        BzPlayerPickupItemEvent.EVENT.invoke(new BzPlayerPickupItemEvent(event.getPlayer(), event.getItemEntity().getItem()));
    }

    private static void onGrantAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        BzPlayerGrantAdvancementEvent.EVENT.invoke(new BzPlayerGrantAdvancementEvent(event.getAdvancement().value(), event.getEntity()));
    }

    private static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        InteractionResult result = BzPlayerEntityInteractEvent.EVENT.invoke(new BzPlayerEntityInteractEvent(event.getEntity(), event.getTarget(), event.getHand()));
        if (result != null) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        AtomicDouble speed = new AtomicDouble(event.getNewSpeed());
        BzPlayerBreakSpeedEvent.EVENT.invoke(new BzPlayerBreakSpeedEvent(event.getEntity(), event.getState(), speed));
        event.setNewSpeed(speed.floatValue());
    }

    private static void onTagsUpdate(TagsUpdatedEvent event) {
        BzTagsUpdatedEvent.EVENT.invoke(new BzTagsUpdatedEvent(event.getRegistryAccess(), event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED));
    }

    private static void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        BzRegisterSpawnPlacementsEvent.EVENT.invoke(new BzRegisterSpawnPlacementsEvent(NeoForgeEventManager.registerPlacement(event)));
    }

    private static BzRegisterSpawnPlacementsEvent.Registrar registerPlacement(RegisterSpawnPlacementsEvent event) {
        return new BzRegisterSpawnPlacementsEvent.Registrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, BzRegisterSpawnPlacementsEvent.Placement<T> place) {
                event.register(type, place.spawn(), place.height(), place.predicate(), RegisterSpawnPlacementsEvent.Operation.AND);
            }
        };
    }

    private static void onLevelTickPre(LevelTickEvent.Pre event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        BzServerLevelTickEvent.EVENT.invoke(new BzServerLevelTickEvent(event.getLevel(), false));
    }

    private static void onLevelTickPost(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        BzServerLevelTickEvent.EVENT.invoke(new BzServerLevelTickEvent(event.getLevel(), true));
    }

    private static void onAddReloadListeners(AddReloadListenerEvent event) {
        BzRegisterReloadListenerEvent.EVENT.invoke(new BzRegisterReloadListenerEvent((id, listener) -> event.addListener(listener)));
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            if (event.getPlayer() != null) {
                BzDatapackSyncEvent.EVENT.invoke(new BzDatapackSyncEvent(event.getPlayer()));
            }
            else {
                event.getPlayerList().getPlayers().forEach(player -> BzDatapackSyncEvent.EVENT.invoke(new BzDatapackSyncEvent(player)));
            }
        }
    }

    private static void onFinishUseItem(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = BzFinishUseItemEvent.EVENT.invoke(new BzFinishUseItemEvent(event.getEntity(), event.getItem(), event.getDuration()));
        if (stack != null) {
            event.setResultStack(stack);
        }
    }

    private static void onEntityVisibility(LivingEvent.LivingVisibilityEvent event) {
        BzEntityVisibilityEvent visibilityEvent = new BzEntityVisibilityEvent(event.getVisibilityModifier(), event.getEntity(), event.getLookingEntity());
        BzEntityVisibilityEvent.EVENT.invoke(visibilityEvent);
        event.modifyVisibility(visibilityEvent.visibility() / event.getVisibilityModifier());
    }

    private static void onEntityDimensionTravel(EntityTravelToDimensionEvent event) {
        boolean cancel = BzEntityTravelingToDimensionEvent.EVENT.invoke(new BzEntityTravelingToDimensionEvent(event.getDimension(), event.getEntity()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityTick(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            BzEntityTickEvent.EVENT.invoke(new BzEntityTickEvent(livingEntity));
        }
    }

    private static void onEntitySpawn(FinalizeSpawnEvent event) {
        BzEntitySpawnEvent.EVENT.invoke(new BzEntitySpawnEvent(event.getEntity(), event.getLevel(), event.getEntity().isBaby(), event.getEntity().getSpawnType()), event.isCanceled());
    }

    private static void onEntityHurtLowest(LivingDamageEvent.Post event) {
        BzEntityHurtEvent.EVENT_LOWEST.invoke(new BzEntityHurtEvent(event.getEntity(), event.getSource(), event.getNewDamage()));
    }

    private static void onEntityDeath(LivingDeathEvent event) {
        boolean cancel = BzEntityDeathEvent.EVENT.invoke(new BzEntityDeathEvent(event.getEntity(), event.getSource()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityDeathLowest(LivingDeathEvent event) {
        boolean cancel = BzEntityDeathEvent.EVENT_LOWEST.invoke(new BzEntityDeathEvent(event.getEntity(), event.getSource()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }


    private static void onEntityAttacked(LivingIncomingDamageEvent event) {
        boolean cancel = BzEntityAttackedEvent.EVENT.invoke(new BzEntityAttackedEvent(event.getEntity(), event.getSource(), event.getAmount()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    public static void onItemAttackBlock(PlayerInteractEvent.LeftClickBlock event) {
        BzPlayerItemAttackBlockEvent eventBz = new BzPlayerItemAttackBlockEvent(event.getEntity(), event.getLevel(), event.getHand(), event.getItemStack());
        InteractionResult result = BzPlayerItemAttackBlockEvent.EVENT_HIGH.invoke(eventBz);
        if (result != null) {
            event.setCanceled(true);
        }
    }

    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        BzPlayerItemUseOnBlockEvent eventBz = new BzPlayerItemUseOnBlockEvent(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec(), event.getItemStack());
        InteractionResult result = BzPlayerItemUseOnBlockEvent.EVENT_HIGH.invoke(eventBz);
        if (result != null) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        BzPlayerItemUseEvent eventBz = new BzPlayerItemUseEvent(event.getEntity(), event.getLevel(), event.getItemStack());
        if (BzPlayerItemUseEvent.EVENT_HIGH.invoke(eventBz)) {
            event.setCanceled(true);
        }
    }

    public static void registerBumblezoneCapProviders(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BzBlockEntities.HONEY_COCOON.get(),
                (honeyCocoon, side) -> new SidedInvWrapper(honeyCocoon, Direction.UP));

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBucketWrapper(stack),
                BzItems.HONEY_BUCKET.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBucketWrapper(stack),
                BzItems.ROYAL_JELLY_BUCKET.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBucketWrapper(stack),
                BzItems.SUGAR_WATER_BUCKET.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBottlesWrapper(stack, BzFluids.ROYAL_JELLY_FLUID.get()),
                BzItems.ROYAL_JELLY_BOTTLE.get());

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new BzFluidBottlesWrapper(stack, BzFluids.SUGAR_WATER_FLUID.get()),
                BzItems.SUGAR_WATER_BOTTLE.get());

        if (BzGeneralConfig.bzHoneyFluidFromHoneyBottles.get()) {
            event.registerItem(
                    Capabilities.FluidHandler.ITEM,
                    (stack, ctx) -> new BzFluidBottlesWrapper(stack, BzFluids.HONEY_FLUID.get()),
                    Items.HONEY_BOTTLE);
        }
    }
}
