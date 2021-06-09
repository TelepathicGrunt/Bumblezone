package com.telepathicgrunt.bumblezone;

import com.telepathicgrunt.bumblezone.configs.BzConfig;
import com.telepathicgrunt.bumblezone.entities.BeeAggression;
import com.telepathicgrunt.bumblezone.entities.IPlayerComponent;
import com.telepathicgrunt.bumblezone.entities.PlayerComponent;
import com.telepathicgrunt.bumblezone.entities.WanderingTrades;
import com.telepathicgrunt.bumblezone.items.DispenserItemSetup;
import com.telepathicgrunt.bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.bumblezone.modinit.*;
import com.telepathicgrunt.bumblezone.tags.BZBlockTags;
import com.telepathicgrunt.bumblezone.tags.BZItemTags;
import com.telepathicgrunt.bumblezone.world.dimension.BzDimension;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.DefaultBiomeCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bumblezone implements ModInitializer, EntityComponentInitializer {

    public static final String MODID = "the_bumblezone";
    public static final Identifier MOD_DIMENSION_ID = new Identifier(Bumblezone.MODID, Bumblezone.MODID);

    public static BzConfig BZ_CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ComponentKey<IPlayerComponent> PLAYER_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier(MODID, "player_component"), IPlayerComponent.class);

    @Override
    public void onInitialize() {
        //Set up config
        AutoConfig.register(BzConfig.class, JanksonConfigSerializer::new);
        BZ_CONFIG = AutoConfig.getConfigHolder(BzConfig.class).getConfig();

        BZBlockTags.initTags();
        BZItemTags.initTags();

        // Must be before items so that items like music disc can get sounds
        BzSounds.registerSounds();

        BzBlocks.registerBlocks();
        BzEntities.registerEntities();
        BzItems.registerItems();
        BzEffects.registerEffects();
        BzEnchantments.registerEnchantment();

        BzProcessors.registerProcessors();
        BzPlacements.registerPlacements();
        BzFeatures.registerFeatures();
        BzConfiguredFeatures.registerConfiguredFeatures();
        BzDimension.setupDimension();
        WanderingTrades.addWanderingTrades();

        BeeAggression.setupEvents();
        DispenserItemSetup.setupDispenserBehaviors();
        ModChecker.setupModCompat();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        //attach component to player
        registry.registerForPlayers(PLAYER_COMPONENT, p -> new PlayerComponent(), RespawnCopyStrategy.INVENTORY);
    }

    public static void reserveBiomeIDs() {
        //Reserve Bumblezone biome IDs for the json version to replace
        Registry.register(BuiltinRegistries.BIOME, new Identifier(Bumblezone.MODID, "hive_wall"), DefaultBiomeCreator.createTheVoid());
        Registry.register(BuiltinRegistries.BIOME, new Identifier(Bumblezone.MODID, "hive_pillar"), DefaultBiomeCreator.createTheVoid());
        Registry.register(BuiltinRegistries.BIOME, new Identifier(Bumblezone.MODID, "sugar_water_floor"), DefaultBiomeCreator.createTheVoid());
    }
}
