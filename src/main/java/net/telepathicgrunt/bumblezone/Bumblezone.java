package net.telepathicgrunt.bumblezone;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import nerdhub.cardinal.components.api.util.EntityComponents;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.dblsaiko.qcommon.cfg.core.api.ConfigApi;
import net.dblsaiko.qcommon.cfg.core.api.cvar.IntConVar;
import net.dblsaiko.qcommon.cfg.core.api.cvar.StringConVar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.telepathicgrunt.bumblezone.configs.BzConfig;
import net.telepathicgrunt.bumblezone.entities.IPlayerComponent;
import net.telepathicgrunt.bumblezone.entities.PlayerComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.telepathicgrunt.bumblezone.biome.BzBiomesInit;
import net.telepathicgrunt.bumblezone.blocks.BzBlocksInit;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.effects.BzEffectsInit;


public class Bumblezone implements ModInitializer
{
	public static final String MODID = "the_bumblezone";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final ComponentType<IPlayerComponent> PLAYER_COMPONENT =
			ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID,"player_component"), IPlayerComponent.class)
					.attach(EntityComponentCallback.event(PlayerEntity.class), zombie -> new PlayerComponent());


	@Override
	public void onInitialize()
	{
		BzBlocksInit.registerBlocks();
		BzBlocksInit.registerItems();
		BzEffectsInit.registerEffects();
		BzBiomesInit.registerBiomes();
		BzDimensionType.registerChunkGenerator();
		BzDimensionType.registerDimension();

		//attach component to player
		EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> components.put(PLAYER_COMPONENT, new PlayerComponent()));
		EntityComponents.setRespawnCopyStrategy(PLAYER_COMPONENT, RespawnCopyStrategy.INVENTORY);

		BzConfig.initalizeConfigs();;
	}

}
