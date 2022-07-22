package com.telepathicgrunt.the_bumblezone;

import com.telepathicgrunt.the_bumblezone.components.EntityComponent;
import com.telepathicgrunt.the_bumblezone.components.EssenceComponent;
import com.telepathicgrunt.the_bumblezone.components.FlyingSpeedComponent;
import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
import com.telepathicgrunt.the_bumblezone.components.NeurotoxinsMissedCounterComponent;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.entities.BeeAggression;
import com.telepathicgrunt.the_bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers.PollenPuffEntityPollinateManager;
import com.telepathicgrunt.the_bumblezone.entities.queentrades.QueensTradeManager;
import com.telepathicgrunt.the_bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.*;
import com.telepathicgrunt.the_bumblezone.packets.BeehemothControlsPacket;
import com.telepathicgrunt.the_bumblezone.packets.BumbleBeeChestplateFlyingPacket;
import com.telepathicgrunt.the_bumblezone.packets.StinglessBeeHelmetSightPacket;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bumblezone implements ModInitializer, EntityComponentInitializer {

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final ComponentKey<EntityComponent> ENTITY_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "entity_component"), EntityComponent.class);
    public static final ComponentKey<FlyingSpeedComponent> FLYING_SPEED_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "original_flying_speed"), FlyingSpeedComponent.class);
    public static final ComponentKey<NeurotoxinsMissedCounterComponent> NEUROTOXINS_MISSED_COUNTER_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "neurotoxins_missed_counter"), NeurotoxinsMissedCounterComponent.class);
    public static final ComponentKey<EssenceComponent> ESSENCE_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "essence_component"), EssenceComponent.class);
    public static final ComponentKey<MiscComponent> MISC_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "misc_component"), MiscComponent.class);

    @Override
    public void onInitialize() {
        //Set up config
        MidnightConfig.init(MODID, BzConfig.class);

        BzTags.initTags();
        BzBiomeHeightRegistry.initBiomeHeightRegistry();

        // Must be before items so that items like music disc can get sounds
        BzSounds.registerSounds();

        BzBlocks.registerBlocks();
        BzBlockEntities.registerBlockEntities();
        BzFluids.registerFluids();
        BzEntities.registerEntities();
        BzItems.registerItems();
        BzRecipes.registerRecipes();
        BzEffects.registerEffects();
        BzEnchantments.registerEnchantment();
        BzCriterias.registerCriteriaTriggers();
        BzPOI.registerPOIs();
        BzMenuTypes.registerMenu();
        BzParticles.registerParticles();
        BzStats.registerStats();

        BzPredicates.registerPredicates();
        BzLootFunctionTypes.registerContainerLootFunctions();
        BzProcessors.registerProcessors();
        BzPlacements.registerPlacements();
        BzFeatures.registerFeatures();
        BzSurfaceRules.registerSurfaceRules();
        BzStructures.registerStructures();
        BzDimension.registerDimensionParts();

        WanderingTrades.addWanderingTrades();
        DispenserItemSetup.setupDispenserBehaviors();

        BeeAggression.setupEvents();
        ModChecker.setupModCompat();
        ServerTickEvents.END_WORLD_TICK.register(BzWorldSavedData::tick);

        EntityDataSerializers.registerSerializer(BeeQueenEntity.QUEEN_POSE_SERIALIZER);
        BeehemothControlsPacket.registerPacket();
        BumbleBeeChestplateFlyingPacket.registerPacket();
        StinglessBeeHelmetSightPacket.registerPacket();

        MiscComponent.onEntityKilled();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(PollenPuffEntityPollinateManager.POLLEN_PUFF_ENTITY_POLLINATE_MANAGER);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(QueensTradeManager.QUEENS_TRADE_MANAGER);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((a, b, c) -> QueensTradeManager.QUEENS_TRADE_MANAGER.resolveQueenTrades());
        ServerLifecycleEvents.SERVER_STARTED.register((a) -> QueensTradeManager.QUEENS_TRADE_MANAGER.resolveQueenTrades());
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        //attach component to living entities
        registry.registerFor(LivingEntity.class, ENTITY_COMPONENT, p -> new EntityComponent());
        registry.registerFor(LivingEntity.class, FLYING_SPEED_COMPONENT, p -> new FlyingSpeedComponent());
        registry.registerFor(LivingEntity.class, NEUROTOXINS_MISSED_COUNTER_COMPONENT, p -> new NeurotoxinsMissedCounterComponent());
        registry.registerForPlayers(ESSENCE_COMPONENT, p -> new EssenceComponent(), RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(MISC_COMPONENT, p -> new MiscComponent(), RespawnCopyStrategy.ALWAYS_COPY);

        PlayerCopyCallback.EVENT.register((serverPlayerOriginal, serverPlayerNew, lossless) -> {
            if (BzConfig.keepBeeEssenceOnRespawning || lossless) {
                EssenceComponent capabilityOld = Bumblezone.ESSENCE_COMPONENT.get(serverPlayerOriginal);
                EssenceComponent capability = Bumblezone.ESSENCE_COMPONENT.get(serverPlayerNew);
                capability.isBeeEssenced = capabilityOld.isBeeEssenced;
            }
            else {
                EssenceComponent capability = Bumblezone.ESSENCE_COMPONENT.get(serverPlayerNew);
                capability.isBeeEssenced = false;
            }
        });
    }
}
