package com.telepathicgrunt.bumblezone;

import com.telepathicgrunt.bumblezone.components.EntityComponent;
import com.telepathicgrunt.bumblezone.components.FlyingSpeedComponent;
import com.telepathicgrunt.bumblezone.components.IEntityComponent;
import com.telepathicgrunt.bumblezone.components.IFlyingSpeedComponent;
import com.telepathicgrunt.bumblezone.configs.BzConfig;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.bumblezone.modinit.BzBiomeHeightRegistry;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.bumblezone.modinit.BzEffects;
import com.telepathicgrunt.bumblezone.modinit.BzEnchantments;
import com.telepathicgrunt.bumblezone.modinit.BzEntities;
import com.telepathicgrunt.bumblezone.modinit.BzFeatures;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.modinit.BzPOI;
import com.telepathicgrunt.bumblezone.modinit.BzPlacements;
import com.telepathicgrunt.bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.bumblezone.modinit.BzRecipes;
import com.telepathicgrunt.bumblezone.modinit.BzSounds;
import com.telepathicgrunt.bumblezone.modinit.BzStructures;
import com.telepathicgrunt.bumblezone.modinit.BzSurfaceRules;
import com.telepathicgrunt.bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.bumblezone.tags.BzEntityTags;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.bumblezone.tags.BzItemTags;
import com.telepathicgrunt.bumblezone.world.dimension.BzDimension;
import com.telepathicgrunt.bumblezone.world.dimension.BzWorldSavedData;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bumblezone implements ModInitializer, EntityComponentInitializer {

    public static final String MODID = "the_bumblezone";
    public static final ResourceLocation MOD_DIMENSION_ID = new ResourceLocation(Bumblezone.MODID, Bumblezone.MODID);

    public static BzConfig BZ_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final ComponentKey<IEntityComponent> ENTITY_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "entity_component"), IEntityComponent.class);
    public static final ComponentKey<IFlyingSpeedComponent> FLYING_SPEED_COMPONENT = ComponentRegistry.getOrCreate(new ResourceLocation(MODID, "original_flying_speed"), IFlyingSpeedComponent.class);

    @Override
    public void onInitialize() {
        //Set up config
        AutoConfig.register(BzConfig.class, JanksonConfigSerializer::new);
        BZ_CONFIG = AutoConfig.getConfigHolder(BzConfig.class).getConfig();

        BzBlockTags.initTags();
        BzItemTags.initTags();
        BzFluidTags.tagInit();
        BzEntityTags.tagInit();
        BzBiomeHeightRegistry.initBiomeHeightRegistry();

        // Must be before items so that items like music disc can get sounds
        BzSounds.registerSounds();

        BzBlocks.registerBlocks();
        BzFluids.registerFluids();
        BzEntities.registerEntities();
        BzItems.registerItems();
        BzRecipes.registerRecipes();
        BzEffects.registerEffects();
        BzEnchantments.registerEnchantment();
        BzCriterias.registerCriteriaTriggers();
        BzPOI.registerPOIs();

        BzProcessors.registerProcessors();
        BzPlacements.registerPlacements();
        BzFeatures.registerFeatures();
        BzSurfaceRules.registerSurfaceRules();
        BzStructures.registerStructures();
        BzDimension.setupDimension();

        WanderingTrades.addWanderingTrades();
        DispenserItemSetup.setupDispenserBehaviors();

        BeeAggression.setupEvents();
        ModChecker.setupModCompat();
        ServerTickEvents.END_WORLD_TICK.register(BzWorldSavedData::tick);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        //attach component to living entities
        registry.registerFor(LivingEntity.class, ENTITY_COMPONENT, p -> new EntityComponent());
        registry.registerFor(LivingEntity.class, FLYING_SPEED_COMPONENT, p -> new FlyingSpeedComponent());
    }
}
