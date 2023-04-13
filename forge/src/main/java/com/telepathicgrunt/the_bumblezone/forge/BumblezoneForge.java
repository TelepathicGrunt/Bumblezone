package com.telepathicgrunt.the_bumblezone.forge;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.forge.BzConfigHandler;
import com.telepathicgrunt.the_bumblezone.events.AddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCreativeTabsEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterVillagerTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterWanderingTradesEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.BabySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityAttackedEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityDeathEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityHurtEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTickEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityTravelingToDimensionEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.EntityVisibilityEvent;
import com.telepathicgrunt.the_bumblezone.events.entity.FinishUseItemEvent;
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
import com.telepathicgrunt.the_bumblezone.events.player.PlayerBreakSpeedEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerCraftedItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerEntityInteractEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerPickupItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.fluids.forge.BzFluidType;
import com.telepathicgrunt.the_bumblezone.mixins.forge.block.FireBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.forge.ForgeModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.forge.BzBiomeModifiers;
import com.telepathicgrunt.the_bumblezone.modinit.forge.BzGlobalLootModifier;
import com.telepathicgrunt.the_bumblezone.modinit.registry.forge.ResourcefulRegistriesImpl;
import com.telepathicgrunt.the_bumblezone.modules.forge.ForgeModuleInitalizer;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Mod(Bumblezone.MODID)
public class BumblezoneForge {

    public BumblezoneForge() {
        BzConfigHandler.setup();
        ForgeModuleInitalizer.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, ResourcefulRegistriesImpl::onRegisterForgeRegistries);

        Bumblezone.init();

        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BzBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);
        BzGlobalLootModifier.GLM.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BumblezoneForgeClient.init();
        }

        modEventBus.addListener(BumblezoneForge::onRegisterPackFinder);
        modEventBus.addListener(BumblezoneForge::onRegisterAttributes);
        modEventBus.addListener(BumblezoneForge::onSetup);
        modEventBus.addListener(EventPriority.LOWEST, BumblezoneForge::onFinalSetup);
        modEventBus.addListener(BumblezoneForge::onRegisterCreativeTabs);
        modEventBus.addListener(BumblezoneForge::onAddTabContents);
        modEventBus.addListener(BumblezoneForge::onSpawnPlacements);

        eventBus.addListener(BumblezoneForge::onBabySpawn);
        eventBus.addListener(BumblezoneForge::onServerStarting);
        eventBus.addListener(BumblezoneForge::onServerStopping);
        eventBus.addListener(BumblezoneForge::onAddVillagerTrades);
        eventBus.addListener(BumblezoneForge::onWanderingTrades);
        eventBus.addListener(BumblezoneForge::onRegisterCommand);
        eventBus.addListener(BumblezoneForge::onProjectileHit);
        eventBus.addListener(EventPriority.HIGH, BumblezoneForge::onItemUseOnBlock);
        eventBus.addListener(EventPriority.HIGH, BumblezoneForge::onItemUse);
        eventBus.addListener(EventPriority.HIGH, BumblezoneForge::onProjectileHitHigh);
        eventBus.addListener(EventPriority.LOWEST, BumblezoneForge::onBlockBreak);
        eventBus.addListener(BumblezoneForge::onPlayerTick);
        eventBus.addListener(BumblezoneForge::onPickupItem);
        eventBus.addListener(BumblezoneForge::onGrantAdvancement);
        eventBus.addListener(BumblezoneForge::onInteractEntity);
        eventBus.addListener(BumblezoneForge::onItemCrafted);
        eventBus.addListener(BumblezoneForge::onBreakSpeed);
        eventBus.addListener(BumblezoneForge::onTagsUpdate);
        eventBus.addListener(BumblezoneForge::onLevelTick);
        eventBus.addListener(BumblezoneForge::onAddReloadListeners);
        eventBus.addListener(BumblezoneForge::onDatapackSync);
        eventBus.addListener(BumblezoneForge::onEntityAttacked);
        eventBus.addListener(BumblezoneForge::onEntityDeath);
        eventBus.addListener(BumblezoneForge::onEntitySpawn);
        eventBus.addListener(BumblezoneForge::onEntityTick);
        eventBus.addListener(BumblezoneForge::onEntityDimensionTravel);
        eventBus.addListener(BumblezoneForge::onEntityVisibility);
        eventBus.addListener(BumblezoneForge::onFinishUseItem);
        eventBus.addListener(EventPriority.LOWEST, BumblezoneForge::onEntityHurtLowest);
    }

    private static void onAddTabContents(CreativeModeTabEvent.BuildContents event) {
        AddCreativeTabEntriesEvent.EVENT.invoke(new AddCreativeTabEntriesEvent(toType(event.getTab()), event.getTab(), event::accept));
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

    private static void onRegisterCreativeTabs(CreativeModeTabEvent.Register event) {
        RegisterCreativeTabsEvent.EVENT.invoke(new RegisterCreativeTabsEvent((id, operator, initialDisplayItems) ->
                event.registerCreativeModeTab(id, builder -> {
                    operator.accept(builder);
                    builder.displayItems((flag, output) -> {
                        List<ItemStack> stacks = Lists.newArrayList();
                        initialDisplayItems.accept(stacks);
                        output.acceptAll(stacks);
                    });
                })
        ));
    }

    private static void onSetup(FMLCommonSetupEvent event) {
        SetupEvent.EVENT.invoke(new SetupEvent(event::enqueueWork));

        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                BzFluids.SUGAR_WATER_FLUID_TYPE.get().flowing().getFluidType(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState()
        ));

        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                BzFluids.SUGAR_WATER_FLUID_TYPE.get().source().getFluidType(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : BzBlocks.SUGAR_INFUSED_COBBLESTONE.get().defaultBlockState()
        ));

        event.enqueueWork(() ->
                RegisterFlammabilityEvent.EVENT.invoke(new RegisterFlammabilityEvent((item, igniteOdds, burnOdds) ->
                ((FireBlockInvoker) Blocks.FIRE).callSetFlammable(item, igniteOdds, burnOdds)))
        );
    }

    private static void onFinalSetup(FMLCommonSetupEvent event) {
        FinalSetupEvent.EVENT.invoke(new FinalSetupEvent(event::enqueueWork));
        event.enqueueWork(ForgeModChecker::setupModCompat);
        event.enqueueWork(() ->
                RegisterDataSerializersEvent.EVENT.invoke(new RegisterDataSerializersEvent(
                        (id, serializer) -> EntityDataSerializers.registerSerializer(serializer))));
    }

    private static void onServerStarting(ServerStartingEvent event) {
        ServerGoingToStartEvent.EVENT.invoke(new ServerGoingToStartEvent(event.getServer()));
    }

    private static void onServerStopping(ServerStoppingEvent event) {
        ServerGoingToStopEvent.EVENT.invoke(ServerGoingToStopEvent.INSTANCE);
    }

    private static void onRegisterPackFinder(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) -> {
                IModFileInfo info = getPackInfo(id);
                Path resourcePath = info.getFile().findResource("resourcepacks/" + id.getPath());

                final Pack.Info packInfo = createInfoForLatest(displayName, mode == AddBuiltinResourcePacks.PackMode.FORCE_ENABLED);
                final Pack pack = Pack.create(
                    "builtin/add_pack_finders_test", displayName,
                    mode == AddBuiltinResourcePacks.PackMode.FORCE_ENABLED,
                    (path) -> new PathPackResources(path, true, resourcePath),
                    packInfo, PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, false, createSource(mode)
                );
                event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
            }));
        }
    }

    private static IModFileInfo getPackInfo(ResourceLocation pack) {
        if (!FMLLoader.isProduction()) {
            for (IModInfo mod : ModList.get().getMods()) {
                if (mod.getModId().startsWith("generated_") && fileExists(mod, "resourcepacks/" + pack.getPath())) {
                    return mod.getOwningFile();
                }
            }
        }
        return ModList.get().getModFileById(pack.getNamespace());
    }

    private static boolean fileExists(IModInfo info, String path) {
        return Files.exists(info.getOwningFile().getFile().findResource(path.split("/")));
    }

    private static Pack.Info createInfoForLatest(Component description, boolean hidden) {
        return new Pack.Info(
                description,
                SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA),
                SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES),
                FeatureFlagSet.of(),
                hidden
        );
    }

    private static PackSource createSource(AddBuiltinResourcePacks.PackMode mode) {
        final Component text = Component.translatable("pack.source.builtin");
        return PackSource.create(
                component -> Component.translatable("pack.nameAndSource", component, text).withStyle(ChatFormatting.GRAY),
                mode != AddBuiltinResourcePacks.PackMode.USER_CONTROLLED
        );
    }

    private static void onBabySpawn(BabyEntitySpawnEvent event) {
        boolean cancel = BabySpawnEvent.EVENT.invoke(new BabySpawnEvent(event.getParentA(), event.getParentB(), event.getCausedByPlayer(), event.getChild()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        RegisterEntityAttributesEvent.EVENT.invoke(new RegisterEntityAttributesEvent((entity, builder) -> event.put(entity, builder.build())));
    }

    private static void onAddVillagerTrades(VillagerTradesEvent event) {
        RegisterVillagerTradesEvent.EVENT.invoke(new RegisterVillagerTradesEvent(event.getType(), (i, listing) -> event.getTrades().get(i.intValue()).add(listing)));
    }

    private static void onWanderingTrades(WandererTradesEvent event) {
        RegisterWanderingTradesEvent.EVENT.invoke(new RegisterWanderingTradesEvent(event.getGenericTrades()::add, event.getRareTrades()::add));
    }

    private static void onRegisterCommand(net.minecraftforge.event.RegisterCommandsEvent event) {
        RegisterCommandsEvent.EVENT.invoke(new RegisterCommandsEvent(event.getDispatcher(), event.getCommandSelection(), event.getBuildContext()));
    }

    private static void onProjectileHit(ProjectileImpactEvent event) {
        boolean cancel = ProjectileHitEvent.EVENT.invoke(new ProjectileHitEvent(event.getProjectile(), event.getRayTraceResult()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onProjectileHitHigh(ProjectileImpactEvent event) {
        boolean cancel = ProjectileHitEvent.EVENT_HIGH.invoke(new ProjectileHitEvent(event.getProjectile(), event.getRayTraceResult()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onBlockBreak(BlockEvent.BreakEvent event) {
        boolean cancel = BlockBreakEvent.EVENT_LOWEST.invoke(new BlockBreakEvent(event.getPlayer(), event.getState()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerTickEvent.EVENT.invoke(new PlayerTickEvent(event.player, event.phase == TickEvent.Phase.END));
    }

    private static void onPickupItem(PlayerEvent.ItemPickupEvent event) {
        PlayerPickupItemEvent.EVENT.invoke(new PlayerPickupItemEvent(event.getEntity(), event.getStack()));
    }

    private static void onGrantAdvancement(AdvancementEvent event) {
        PlayerGrantAdvancementEvent.EVENT.invoke(new PlayerGrantAdvancementEvent(event.getAdvancement(), event.getEntity()));
    }

    private static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        InteractionResult result = PlayerEntityInteractEvent.EVENT.invoke(new PlayerEntityInteractEvent(event.getEntity(), event.getTarget(), event.getHand()));
        if (result != null) {
            event.setCancellationResult(result);
            event.setCanceled(true);
        }
    }

    private static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerCraftedItemEvent.EVENT.invoke(new PlayerCraftedItemEvent(event.getEntity(), event.getCrafting(), event.getInventory()));
    }

    private static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        AtomicDouble speed = new AtomicDouble(event.getNewSpeed());
        PlayerBreakSpeedEvent.EVENT.invoke(new PlayerBreakSpeedEvent(event.getEntity(), event.getState(), speed));
        event.setNewSpeed(speed.floatValue());
    }

    private static void onTagsUpdate(net.minecraftforge.event.TagsUpdatedEvent event) {
        TagsUpdatedEvent.EVENT.invoke(new TagsUpdatedEvent(event.getRegistryAccess(), event.getUpdateCause() == net.minecraftforge.event.TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED));
    }

    private static void onSpawnPlacements(SpawnPlacementRegisterEvent event) {
        RegisterSpawnPlacementsEvent.EVENT.invoke(new RegisterSpawnPlacementsEvent(BumblezoneForge.registerPlacement(event)));
    }

    private static RegisterSpawnPlacementsEvent.Registrar registerPlacement(SpawnPlacementRegisterEvent event) {
        return new RegisterSpawnPlacementsEvent.Registrar() {
            @Override
            public <T extends Mob> void register(EntityType<T> type, RegisterSpawnPlacementsEvent.Placement<T> place) {
                event.register(type, place.spawn(), place.height(), place.predicate(), SpawnPlacementRegisterEvent.Operation.AND);
            }
        };
    }

    private static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) return;
        ServerLevelTickEvent.EVENT.invoke(new ServerLevelTickEvent(event.level, event.phase == TickEvent.Phase.END));
    }

    private static void onAddReloadListeners(AddReloadListenerEvent event) {
        RegisterReloadListenerEvent.EVENT.invoke(new RegisterReloadListenerEvent((id, listener) -> event.addListener(listener)));
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            if (event.getPlayer() != null) {
                DatapackSyncEvent.EVENT.invoke(new DatapackSyncEvent(event.getPlayer()));
            }
            else {
                event.getPlayerList().getPlayers().forEach(player -> DatapackSyncEvent.EVENT.invoke(new DatapackSyncEvent(player)));
            }
        }
    }

    private static void onFinishUseItem(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = FinishUseItemEvent.EVENT.invoke(new FinishUseItemEvent(event.getEntity(), event.getItem(), event.getDuration()));
        if (stack != null) {
            event.setResultStack(stack);
        }
    }

    private static void onEntityVisibility(LivingEvent.LivingVisibilityEvent event) {
        EntityVisibilityEvent visibilityEvent = new EntityVisibilityEvent(event.getVisibilityModifier(), event.getEntity(), event.getLookingEntity());
        EntityVisibilityEvent.EVENT.invoke(visibilityEvent);
        event.modifyVisibility(visibilityEvent.visibility() / event.getVisibilityModifier());
    }

    private static void onEntityDimensionTravel(EntityTravelToDimensionEvent event) {
        boolean cancel = EntityTravelingToDimensionEvent.EVENT.invoke(new EntityTravelingToDimensionEvent(event.getDimension(), event.getEntity()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityTick(LivingEvent.LivingTickEvent event) {
        EntityTickEvent.EVENT.invoke(new EntityTickEvent(event.getEntity()));
    }

    private static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        boolean cancel = EntitySpawnEvent.EVENT.invoke(new EntitySpawnEvent(event.getEntity(), event.getLevel(), event.getEntity().isBaby(), event.getSpawnType()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityHurtLowest(LivingHurtEvent event) {
        boolean cancel = EntityHurtEvent.EVENT_LOWEST.invoke(new EntityHurtEvent(event.getEntity(), event.getSource(), event.getAmount()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityDeath(LivingDeathEvent event) {
        boolean cancel = EntityDeathEvent.EVENT.invoke(new EntityDeathEvent(event.getEntity(), event.getSource()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static void onEntityAttacked(LivingAttackEvent event) {
        boolean cancel = EntityAttackedEvent.EVENT.invoke(new EntityAttackedEvent(event.getEntity(), event.getSource(), event.getAmount()), event.isCanceled());
        if (cancel) {
            event.setCanceled(true);
        }
    }

    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerItemUseOnBlockEvent eventBz = new PlayerItemUseOnBlockEvent(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec(), event.getItemStack());
        InteractionResult result = PlayerItemUseOnBlockEvent.EVENT_HIGH.invoke(eventBz);
        if (result != null) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        PlayerItemUseEvent eventBz = new PlayerItemUseEvent(event.getEntity(), event.getLevel(), event.getItemStack());
        if (PlayerItemUseEvent.EVENT_HIGH.invoke(eventBz)) {
            event.setCanceled(true);
        }
    }
}
