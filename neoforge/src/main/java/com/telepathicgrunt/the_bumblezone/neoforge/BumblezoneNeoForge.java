package com.telepathicgrunt.the_bumblezone.neoforge;

import com.google.common.util.concurrent.AtomicDouble;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.neoforge.BzConfigHandler;
import com.telepathicgrunt.the_bumblezone.events.AddCreativeTabEntriesEvent;
import com.telepathicgrunt.the_bumblezone.events.BlockBreakEvent;
import com.telepathicgrunt.the_bumblezone.events.ProjectileHitEvent;
import com.telepathicgrunt.the_bumblezone.events.RegisterCommandsEvent;
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
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemAttackBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerItemUseOnBlockEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerPickupItemEvent;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerTickEvent;
import com.telepathicgrunt.the_bumblezone.mixins.neoforge.block.FireBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modcompat.neoforge.NeoForgeModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzAttachmentTypes;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzBiomeModifiers;
import com.telepathicgrunt.the_bumblezone.modinit.neoforge.BzGlobalLootModifier;
import com.telepathicgrunt.the_bumblezone.modinit.registry.neoforge.ResourcefulRegistriesImpl;
import com.telepathicgrunt.the_bumblezone.modules.neoforge.NeoForgeModuleInitalizer;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Mod(Bumblezone.MODID)
public class BumblezoneNeoForge {

    public BumblezoneNeoForge(IEventBus modEventBus) {
        BzConfigHandler.setup();
        NeoForgeModuleInitalizer.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, ResourcefulRegistriesImpl::onRegisterForgeRegistries);

        Bumblezone.init();

        BzGlobalLootModifier.GLM.register(modEventBus);
        BzBiomeModifiers.BIOME_MODIFIERS.register(modEventBus);
        BzAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        IEventBus eventBus = NeoForge.EVENT_BUS;

        if (FMLEnvironment.dist == Dist.CLIENT) {
            BumblezoneNeoForgeClient.init(modEventBus, eventBus);
        }

        NeoForgeEventManager.init(modEventBus, eventBus);
    }
}
