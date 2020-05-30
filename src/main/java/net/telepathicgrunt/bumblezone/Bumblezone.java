package net.telepathicgrunt.bumblezone;

import io.github.cottonmc.cotton.config.ConfigManager;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.util.EntityComponents;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.biome.BzBiomes;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.configs.BzConfig;
import net.telepathicgrunt.bumblezone.configs.FileWatcher;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.entities.BeeAggression;
import net.telepathicgrunt.bumblezone.entities.IPlayerComponent;
import net.telepathicgrunt.bumblezone.entities.PlayerComponent;
import net.telepathicgrunt.bumblezone.items.BzItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Bumblezone implements ModInitializer {
    public static final String MODID = "the_bumblezone";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ComponentType<IPlayerComponent> PLAYER_COMPONENT =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "player_component"), IPlayerComponent.class)
                    .attach(EntityComponentCallback.event(PlayerEntity.class), zombie -> new PlayerComponent());
    public static BzConfig BZ_CONFIG;


    @Override
    public void onInitialize() {
        BzBlocks.registerBlocks();
        BzItems.registerItems();
        BzEffects.registerEffects();
        BzBiomes.registerBiomes();
        BzDimensionType.registerChunkGenerator();
        BzDimensionType.registerDimension();

        //attach component to player
        EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> components.put(PLAYER_COMPONENT, new PlayerComponent()));
        EntityComponents.setRespawnCopyStrategy(PLAYER_COMPONENT, RespawnCopyStrategy.INVENTORY);


        //Set up config
        BZ_CONFIG = ConfigManager.loadConfig(BzConfig.class);

        // monitor the config file
        TimerTask task = new FileWatcher(FabricLoader.getInstance().getConfigDirectory().listFiles(new MyFileNameFilter("TheBumblezoneConfig.json5"))[0]) {
            protected void onChange(File file) {
                // Config was changed! Update the ingame values based on new config
                //System.out.println( "File "+ file.getName() +" have change !" );
                BZ_CONFIG = ConfigManager.loadConfig(BzConfig.class);
            }
        };


        ServerStartCallback.EVENT.register((MinecraftServer world) -> {
            BeeAggression.setupBeeHatingList(world.getWorld(DimensionType.OVERWORLD));
        });

        Timer timer = new Timer();
        // repeat the check for a changed config every second
        timer.schedule(task, new Date(), 1000);
    }

    // FileNameFilter implementation
    // Is used to find and return only our config file
    public static class MyFileNameFilter implements FilenameFilter {

        private String configName;

        public MyFileNameFilter(String extension) {
            this.configName = extension.toLowerCase();
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().equals(configName);
        }

    }

}
