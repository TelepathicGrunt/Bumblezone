package com.telepathicgrunt.bumblezone;

import com.telepathicgrunt.bumblezone.configs.BzConfig;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.entities.EntityComponent;
import com.telepathicgrunt.bumblezone.entities.IEntityComponent;
import com.telepathicgrunt.bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.bumblezone.items.dispenserbehavior.DispenserItemSetup;
import com.telepathicgrunt.bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzConfiguredFeatures;
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
import com.telepathicgrunt.bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.bumblezone.tags.BzEntityTags;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import com.telepathicgrunt.bumblezone.tags.BzItemTags;
import com.telepathicgrunt.bumblezone.world.dimension.BzDimension;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bumblezone implements ModInitializer, EntityComponentInitializer {

    public static final String MODID = "the_bumblezone";
    public static final Identifier MOD_DIMENSION_ID = new Identifier(Bumblezone.MODID, Bumblezone.MODID);

    public static BzConfig BZ_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    // legacy name to prevent breaking player's data even though this is for entities now
    public static final ComponentKey<IEntityComponent> ENTITY_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(MODID, "player_component"), IEntityComponent.class);

    @Override
    public void onInitialize() {
        //Set up config
        AutoConfig.register(BzConfig.class, JanksonConfigSerializer::new);
        BZ_CONFIG = AutoConfig.getConfigHolder(BzConfig.class).getConfig();

        BzBlockTags.initTags();
        BzItemTags.initTags();
        BzFluidTags.tagInit();
        BzEntityTags.tagInit();

        // Must be before items so that items like music disc can get sounds
        BzSounds.registerSounds();

        BzBlocks.registerBlocks();
        BzFluids.registerFluids();
        BzEntities.registerEntities();
        BzItems.registerItems();
        BzRecipes.registerRecipes();
        BzEffects.registerEffects();
        BzEnchantments.registerEnchantment();

        BzPOI.registerPOIs();
        BzProcessors.registerProcessors();
        BzPlacements.registerPlacements();
        BzFeatures.registerFeatures();
        BzConfiguredFeatures.registerConfiguredFeatures();
        BzStructures.registerStructures();
        BzDimension.setupDimension();
        WanderingTrades.addWanderingTrades();

        BeeAggression.setupEvents();
        DispenserItemSetup.setupDispenserBehaviors();
        ModChecker.setupModCompat();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        //attach component to living entities
        registry.registerFor(LivingEntity.class, ENTITY_COMPONENT, p -> new EntityComponent());
    }
}
